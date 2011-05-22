// по чату и т.п.
//* при открытии новой вкладки автоматически перемещать курсор на чат
//* возможность вести логи чатов
//* возможность менять ширину колонки с приватным чатом
//* строчки через один слегка затемнять.
//* возможность отключаться от общего канала, оставаясь в игре
//* смайлики (ещё под вопросом, нужно ли..)
//* подключение через proxy
//* возможность сворачиваться в трей
//* всплывающая подсказка в списке игр (как открыть игру?..)
//* открывать окно приватного чата и игры через правую кнопку мыши
//* крестик для закрытия вкладки появляется только при выделении этой вкладки (защита от случаиного нажатия).
//
//по игровой части:
//
//* сохранение игр на компьютер
//* счетчики времени
//* соединение точек палочками, отдельная раскраска мини-треугольников.
//* возможность прокрутки партии по ходам.
//* красивые объёмные точки вместо плоских
//
// opros 29 marta
//
// pont - соединялки
// romati - регулировка цвета и размера точек
// huligan - более красивые точки
// qmesis - настройки времени
// agent47 - настройки времени
package ru.narod.vn91.pointsop.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jibble.pircbot.User;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.SurroundingAbstract;
import ru.narod.vn91.pointsop.gui.GuiController;
import ru.narod.vn91.pointsop.sounds.Sounds;

public class ServerPointsxt extends PircBot implements ServerInterface {

	GuiController gui;
	String myNickOnServ, myNick_Originally;
	private String defaultServ;
	private String defaultChannel;
	private String defaultPass;
	static String pointsxtTail_RegExp = "_X[0-9]{12,12}\\[....\\]";
	static String gamePrefix = "#pxt";
	static String commandCommonPrefix = "OpCmd ";
	static String commandIWantJoinGame = "I want to join this game.";
	static String commandAcceptOpponent = "I accept opponent ";
	Date lastSpectrTime = new Date(0);
	HashMap<String, String> spectrGameData = new HashMap<String, String>();
	IrcNicknameManager nicknameManager = new IrcNicknameManager();
	String mainGameRoomName = "";
	String mainGameOpponent = "";
	boolean mainGameAmIRed;
	SingleGameEngineInterface mainGameEngine;
	ArrayList<SimpleMove> mainGameMoveList = new ArrayList<SimpleMove>();

	public void connect() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (defaultServ.equals("ircworld.ru")) {
						gui.receiveRawServerInfo(ServerPointsxt.this,
								"Cоединение с сервером "
								+ defaultServ + ". Пожалуйста, подождите... (примерно 30 секунд)");
					} else {
						gui.receiveRawServerInfo(ServerPointsxt.this, "Cоединение с сервером "
								+ defaultServ + ". Пожалуйста, подождите...");
					}
					connect(defaultServ);
					myNickOnServ = getNick();
					ServerPointsxt.super.sendMessage("podbot",
							"!opConnect0423");
//					subscribeRoom(defaultChannel);
				} catch (NickAlreadyInUseException e) {
				} catch (IOException e) {
				} catch (IrcException e) {
				}
			}
		});
		thread.start();
	}

	public void disconnecttt() {
		super.disconnect();
		super.dispose();
	}

	public ServerPointsxt(String server,
			GuiController gui,
			String myName,
			String password) {
		super();
		this.gui = gui;
		this.defaultServ = server;
//		defaultChannel = (defaultServ.equals("tochki.org")) ? "#pointsxt" : "#nobot";
//		defaultPass = (defaultChannel.equals("#pointsxt")) ? "1ppass1" : "#nobot";
		defaultChannel = "#pointsxt";
		defaultPass = defaultServ.equals("ircworld.ru") ? "201120" : "1ppass1";

		String login = "";
		try {
			login = InetAddress.getLocalHost().getHostName();
			login = login.replaceAll("[^a-zA-Z0-9-]", "");
		} catch (Exception e) {
		}
		super.setLogin(login);
//		setVerbose(true);

		myName = getAllowedNick(myName);
		myNick_Originally = myName;
		myName = "^" + myName;
		myName = myName + "_X091000000000[free]";
		super.setName(myName);
		this.myNickOnServ = myName;

		super.setVersion("PointsOp (a client from vn91)");
		try {
			super.setEncoding("CP1251");
		} catch (UnsupportedEncodingException e1) {
		}
	}

	public synchronized void searchOpponent() {
		if (mainGameRoomName != null) {
			clearMainGameVariables();
			super.partChannel(mainGameRoomName);
		}
		int roomNumber;
		String roomAsString;
		boolean unoccupiedFound = false;
		do {
			roomNumber = (int)(Math.random() * 99999);
			roomAsString = gamePrefix + String.format("%05d", roomNumber);
			unoccupiedFound = true;
			User[] userList = getUsers(defaultChannel);
			for (int i = 0; i < userList.length; i++) {
				String user = userList[i].getNick();
				if (roomAsString.equals(getPlayerRoom(user))) {
					// equal found
					unoccupiedFound = false;
				}
			}
		} while (unoccupiedFound == false);
		mainGameRoomName = roomAsString;

		mainGameEngine = new SingleGameEngine(39, 32);
		mainGameOpponent = "";
		super.joinChannel(roomAsString);
		super.changeNick(String.format("%s_X0910000%05d[g100]", getMyName(),
				roomNumber));
//		super.changeNick(String.format("%s_X0910000%05d[free]", getMyName(), roomNumber));
	}

	public void acceptOpponent(String roomName,
			String name) {
		if (!roomName.equals(mainGameRoomName)) {
			return;
		}
//		if (mainGameOpponent.equals("") == false) {
//			return;
//		} this protection is already done earlier
		mainGameOpponent = nicknameManager.getIrcNick(name);
		if (mainGameOpponent == null) {
			mainGameOpponent = "";
		} else {
			mainGameAmIRed = true;
			super.sendMessage(roomName,
					commandCommonPrefix + commandAcceptOpponent + mainGameOpponent);
			super.changeNick(String.format("%s_X0910000%s[g100]", getMyName(), roomName.substring(
					4)));
			gui.subscribedGame(roomName, this, getMyName(), name, 0, 0,
					"999мин/ход", false, "", true, true/*i am the player*/);
			for (User user : super.getUsers(roomName)) {
				String ircNick = user.getNick();
				gui.userJoinedGameRoom(
						this,
						roomName,
						nicknameManager.getOrCreateShortNick(ircNick),
						true,
						getPlayerRank(ircNick),
						extractUserStatus(ircNick));
			}
		}
	}

	private void clearMainGameVariables() {
		mainGameRoomName = "";
		mainGameMoveList.clear();
		mainGameOpponent = "";
		mainGameEngine = null;
		super.changeNick(String.format("%s_X091000000000[free]", getMyName()));
	}

	public void stopSearchingOpponent() {
		super.partChannel(mainGameRoomName);
		clearMainGameVariables();
	}

	public void requestJoinGame(String gameRoomName) {
		super.joinChannel(gameRoomName);
		super.sendMessage(gameRoomName,
				commandCommonPrefix + commandIWantJoinGame);
	}

	public static String getAllowedNick(String inputNick) {
		inputNick = inputNick.replaceAll("[^a-zA-Z0-9а-яА-Я]", "");
		if (inputNick.matches(".*[a-zA-Z].*")) {
			inputNick = inputNick.replaceAll("[а-яА-Я]", "");
			// delete all russian letters in case of a mixed nickname
		}
		if (inputNick.length() > 9) {
			inputNick = inputNick.substring(0, 9);
		}
		return inputNick;
	}

	public void subscribeRoom(String roomName) {
		if (roomName.equals(defaultChannel)) {
			boolean isTochkiOrg = defaultServ.equals("tochki.org");
			String guiName = isTochkiOrg ? "основная комната" : defaultServ;
			gui.subscribedLangRoom(roomName, this, guiName, defaultChannel.equals(
					roomName));
		} else {
			GameInfoAbstract gameInfoAbstract = getGameInfoFromRoomName(roomName);
			if (gameInfoAbstract != null) {
				gui.subscribedGame(roomName, this,
						gameInfoAbstract.userFirst, gameInfoAbstract.userSecond,
						gameInfoAbstract.rank1, gameInfoAbstract.rank2,
						gameInfoAbstract.timeLimits, gameInfoAbstract.isRated,
						"",
						true /* chat is read-only */, false/*I'm a spectator*/);
			}
		}

		if (roomName.equals("#pointsxt")) {
			super.joinChannel(roomName, defaultPass);
		} else {
			super.joinChannel(roomName);
		}
	}

	public void unsubscribeRoom(String roomName) {
		if ((new Date()).getTime() - lastSpectrTime.getTime() >= 0) {
			// limit the users from joining-leaving too fast
			// because it causes a bug in pointsxt
			lastSpectrTime = new Date();
			if (roomName.equals(mainGameRoomName)) {
				clearMainGameVariables();
			}
			super.partChannel(roomName);
			gui.unsubsribedRoom(this, roomName);
		}
	}

	@Override
	protected void onJoin(String channel,
			String sender,
			String login,
			String hostname) {
		userConnected_PointsxtStyle(channel, sender, /*not silent*/ false);
		if ((channel.equals(defaultChannel)) && (sender.equals(myNickOnServ))) {
			gui.receiveRawServerInfo(this,
					"Успешно подключился к основной комнате.");
		}
		if (channel.equals(mainGameRoomName) && mainGameAmIRed) {
			super.sendMessage(sender, getSpectrGame_PointsxtStyle());
		}
	}

	@Override
	protected void onConnect() {
		if (super.isConnected()) {
			gui.receiveRawServerInfo(this,
					"Удалось соединиться с " + defaultServ + ", пытаюсь подключиться к основной комнате...");
		}
	}

	@Override
	protected void onUserList(String channel,
			User[] users) {
		for (int index = 0; index < users.length; index++) {
			User user = users[index];
			userConnected_PointsxtStyle(channel, user.getNick(),/*silent*/ true);
		}
	}

	@Override
	protected void onTopic(String channel,
			String topic,
			String setBy,
			long date,
			boolean changed) {
		gui.serverNoticeReceived(this, channel, "Topic: " + topic);
	}

	@Override
	protected void onKick(String channel,
			String kickerNick,
			String kickerLogin,
			String kickerHostname,
			String recipientNick,
			String reason) {
		userDisconnected_PointsxtStyle(channel, recipientNick);
	}

	@Override
	protected void onPart(String channel,
			String sender,
			String login,
			String hostname) {
		userDisconnected_PointsxtStyle(channel, sender);
		if (sender.equals(mainGameOpponent) && channel.equals(mainGameRoomName)) {
//			System.out.println("opponent exited channel " + channel.replaceAll(
//					"#pxt", ""));
			clearMainGameVariables();
			super.partChannel(mainGameRoomName);
//			gui.chatReceived(this, channel, sender, "Ваш оппонент закрыл игру.");
		}
	}

	@Override
	protected void onQuit(String sourceNick,
			String sourceLogin,
			String sourceHostname,
			String reason) {
		String[] channels = getChannels();
		for (int channelIndex = 0; channelIndex < channels.length; channelIndex++) {
			String channelName = channels[channelIndex];
			userDisconnected_PointsxtStyle(channelName, sourceNick);
		}
		gui.userDisconnected(this, nicknameManager.getOrCreateShortNick(
				sourceNick));
	}

	@Override
	protected void onNickChange(String oldNick,
			String login,
			String hostname,
			String newNick) {
		nicknameManager.changeIrcNick(oldNick, newNick);
		clearCreatedGames_PointsxtStyle(oldNick);
		if (nicknameManager.getOrCreateShortNick(newNick).
				equals(nicknameManager.getOrCreateShortNick(oldNick)) == false) {
			userDisconnected_PointsxtStyle(defaultChannel, oldNick);
		}
		userConnected_PointsxtStyle(defaultChannel, newNick, /*silent*/ true);

		int rankOld = getPlayerRank(oldNick), rankNew = getPlayerRank(newNick);
		if ((rankNew != rankOld) && (rankOld != 0) && (rankNew != 0)) {
			gui.serverNoticeReceived(this, defaultChannel,
					"" + nicknameManager.getOrCreateShortNick(newNick)
					+ " " + rankOld + " -> " + rankNew);
		}
	}

	@Override
	protected void onMessage(String channel,
			String sender,
			String login,
			String hostname,
			String message) {
		String nick = nicknameManager.getOrCreateShortNick(sender);
		int playerNumb = getPlayerIngameNumber(sender);
		if (message.startsWith(commandCommonPrefix)) {
			if ((message.startsWith(commandCommonPrefix + commandIWantJoinGame))
					&& (channel.equals(mainGameRoomName))
					&& (mainGameOpponent.equals(""))) {
				String opponentNick = nicknameManager.getOrCreateShortNick(
						sender);
				this.acceptOpponent(channel, opponentNick);
			} else if (message.startsWith(
					commandCommonPrefix + commandAcceptOpponent + myNickOnServ)) {
				super.changeNick(String.format("%s_X0910000%s[g200]",
						getMyName(), channel.substring(4)));
				mainGameAmIRed = false;
				mainGameEngine = new SingleGameEngine(39, 32);
				mainGameMoveList = new ArrayList<SimpleMove>();
				mainGameOpponent = nick;
				mainGameRoomName = channel;
				gui.subscribedGame(channel, this, nick, getMyName(), 0, 0,
						"999мин/ход", false, "", true, true/*i am the player*/);
				for (User user : super.getUsers(channel)) {
					String ircNick = user.getNick();
					gui.userJoinedGameRoom(
							this,
							channel,
							nicknameManager.getOrCreateShortNick(ircNick),
							true,
							getPlayerRank(ircNick),
							extractUserStatus(ircNick));
				}
			}
			return; /* "if I won't take it - no one will."
			if we had a pointsOp message and failed to handle it - do nothing.
			Because this may be a message from higher versions of the protocol.
			And this message shouldn't be visible as text in this case */
		} else if (channel.equals(defaultChannel)) {
			if (message.startsWith("ACTION")) {
				gui.chatReceived(this, channel, "***" + nick, message.substring(
						7).replaceAll(pointsxtTail_RegExp, ""));
			} else {
				gui.chatReceived(this, channel, nick, message.replaceAll(
						pointsxtTail_RegExp, ""));
			}
		} else {
			// game channel
			if ((playerNumb != -1) && (message.matches("..[0-9]{3,3}"))) {
				// new move
				int x = getCoordinate(message.charAt(0));
				int y = getCoordinate(message.charAt(1));
				x = x + 1; // pointsOp version of coordinates
				y = 31 - y + 1; // pointsOp version of coordinates
				makedMove_PointsxtStyle(channel, false /*not silent*/,
						x,
						y,
						(playerNumb == 1));
			} else if (message.equalsIgnoreCase("/ImLost")) {
				User[] users = getUsers(channel);
				boolean biggerFishFound = false;
				for (int userIndex = 0; userIndex < users.length; userIndex++) {
					String user = users[userIndex].getNick();
					if ((user.compareTo(getNick()) > 0) && (user.startsWith("^"))) {
						biggerFishFound = true;
					}
				}
				gui.gameLost(this, getPlayerRoom(sender), playerNumb == 1,
						biggerFishFound == false);
			} else if (message.equalsIgnoreCase("/StopTheGame")) {
				gui.gameStop(this, getPlayerRoom(sender), playerNumb == 1);
			}
		}
	}

	@Override
	protected void onPrivateMessage(String sender,
			String login,
			String hostname,
			String message) {

		if (message.equals("/SendSOUND")) {
			new Sounds().playAlarmSignal();
			gui.serverNoticeReceived(this, defaultChannel, nicknameManager.getOrCreateShortNick(
					sender) + " sends you a sound");
		} else if (message.startsWith("/PASSOK ")
				&& (sender.toLowerCase().equals("podbot"))) {
			subscribeRoom("#pointsxt");
		} else if (message.startsWith("/PASSOK ")) {
			// someone else sent "passok"
		} else if (message.startsWith("/SpectrGame")) {
			String targetRoom = getPlayerRoom(sender);
			int headerEndsPosition;
			headerEndsPosition = message.lastIndexOf("00000000") + 8;
			int tailStartsPosition;
			tailStartsPosition = message.lastIndexOf(" ");
			if (tailStartsPosition < headerEndsPosition) {
				tailStartsPosition = message.length();
			} else {
			}

			String suffPartAsString = message.substring(headerEndsPosition,
					tailStartsPosition);
//			System.out.printf("%s\n", suffPartAsString);
//			System.out.printf("full: %s\nsuff: %s\n", message, suffPartAsString);
			String previousData = spectrGameData.get(targetRoom);
			if (previousData == null) {
				previousData = "";
			}
			spectrGameData.put(targetRoom, previousData + suffPartAsString);

			if (message.endsWith("-")) {
				byte[] sufficiendPart = spectrGameData.get(targetRoom).getBytes();

				for (int moveN = 0; ((moveN * 2 + 1) < sufficiendPart.length); moveN++) {
					int y = sufficiendPart[moveN * 2 + 1];
					int x = sufficiendPart[moveN * 2];
					boolean isRed = (x >= 88);
					x = (x >= 88) ? x - 88 : x - 48;
					y = (y >= 88) ? y - 88 : y - 48;

					x = x + 1; // pointsOp version of coordinates
					y = 31 - y + 1; // pointsOp version of coordinates
					makedMove_PointsxtStyle(targetRoom, true /*silent*/, x, y,
							isRed);
				}
				makedMove_PointsxtStyle(targetRoom, false, -2, -2, true); // update the paper of the user
			}

		} else if (message.equals("/Ping")) {
			super.sendMessage(sender, "/Pong");
			// pointsxt-style ping-ing
		} else {
			String nick = nicknameManager.getOrCreateShortNick(sender);
			gui.privateMessageReceived(this, nick, message.replaceAll(
					pointsxtTail_RegExp, ""));
		}
	}

	@Override
	protected void onUnknown(String line) {
		gui.receiveRawServerInfo(this, line.replaceAll(pointsxtTail_RegExp, ""));
	}

	private boolean hasPointsxtNickname(String fullNick) {
		return fullNick.matches(".*" + pointsxtTail_RegExp);
	}

	private String getPlayerRoom(String nick) {
		if (hasPointsxtNickname(nick) == false) {
			return "";
		} else {
			String roomSuffix =
					nick.substring(nick.length() - 11, nick.length() - 6);
			if (roomSuffix.equals("00000")) {
				return "";
			} else {
				return "#pxt" + roomSuffix;
			}
		}
	}

	GameInfoAbstract getGameInfoFromIrcNick(String ircNick) {
		return getGameInfoFromRoomName(getPlayerRoom(ircNick));
	}

	GameInfoAbstract getGameInfoFromRoomName(String roomName) {
		GameInfoAbstract gameInfoAbstract = new GameInfoAbstract();
		User[] users = super.getUsers(defaultChannel);
		for (int userNumber = 0; userNumber < users.length; userNumber++) {
			User user = users[userNumber];
			String userIrcName = user.getNick();
			if (getPlayerRoom(userIrcName).equals(roomName)) {
				String pxtGameInfo = userIrcName.substring(
						userIrcName.length() - 5, userIrcName.length() - 1);
				gameInfoAbstract.isRated = pxtGameInfo.startsWith("1", 2);
				gameInfoAbstract.timeLimits = pxtGameInfo.startsWith("1", 3) ? "5sec/turn" : "180sec/5turns";
				if (pxtGameInfo.startsWith("1", 1)) {
					gameInfoAbstract.userFirst = nicknameManager.getOrCreateShortNick(
							userIrcName);
					gameInfoAbstract.rank1 = getPlayerRank(userIrcName);
				} else {
					gameInfoAbstract.userSecond = nicknameManager.getOrCreateShortNick(
							userIrcName);
					gameInfoAbstract.rank2 = getPlayerRank(userIrcName);
				}
				gameInfoAbstract.startingPosition = "";
//				with this code you can connect even if 1 player disconnects from the public chat.
//				if (gameInfoAbstract.userFirst==null) {
//					gameInfoAbstract.userFirst="";
//				}
//				if (gameInfoAbstract.userSecond==null) {
//					gameInfoAbstract.userSecond="";
//				}
			}
		}
		if ((gameInfoAbstract.userFirst != null) && (gameInfoAbstract.userSecond != null)) {
			return gameInfoAbstract;
		} else {
			return null;
		}
	}

	private int getPlayerRank(String ircNick) {
		if (hasPointsxtNickname(ircNick)) {
			String rankAsString = ircNick.substring(ircNick.length() - 15,
					ircNick.length() - 11);
			return Integer.parseInt(rankAsString);
		} else {
			return 0;
		}
	}

	private String extractUserStatus(String nick) {
		if (hasPointsxtNickname(nick)) {
			String stateType = nick.substring(nick.length() - 5,
					nick.length() - 1);
			if (stateType.equals("free")) {
				return " ";
			} else if (stateType.equals("away")) {
				return "*";
			} else {
				return "!";
			}
		} else {
			return "";
		}
	}

	private String getPlayerGameType(String nick) {
		if (hasPointsxtNickname(nick)) {
			return getGameInfoFromIrcNick(nick).getTimeAndIsRated();
		} else {
			return "";
		}
	}

	private int getPlayerIngameNumber(String fullNick) {
		if (hasPointsxtNickname(fullNick)) {
			String letter = fullNick.substring(fullNick.length() - 4,
					fullNick.length() - 3);
			if (letter.equals("1")) {
				return 1;
			} else if (letter.equals("2")) {
				return 2;
			} else {
				return -1;
			}
		} else {
			return -1;
		}

	}

	private int getCoordinate(char charr) {
		return charr - "0".charAt(0);
	}

	private boolean isPointsop(String ircNick) {
		return ircNick.startsWith("^");
	}

	public void userConnected_PointsxtStyle(String room,
			String fullNickname,
			boolean silent) {
		String pointsxtNick = nicknameManager.getOrCreateShortNick(fullNickname);
		if (room.equals(defaultChannel)) {
			// join Lang room
			gui.userJoinedLangRoom(this, room, pointsxtNick, silent,
					getPlayerRank(fullNickname), extractUserStatus(fullNickname));
		} else {
			// join Game room
			gui.userJoinedGameRoom(this, room, pointsxtNick, silent,
					getPlayerRank(fullNickname), extractUserStatus(fullNickname));
		}
		if (room.equals(defaultChannel)) {
			String newRoom = getPlayerRoom(fullNickname);
			if (newRoom.length() > 0) {
				User[] users = getUsers(defaultChannel);
				String opponent = "";
				for (int i = 0; i < users.length; i++) {
					String possibleOpponent = users[i].getNick();
					if ((getPlayerRoom(possibleOpponent).equals(newRoom))
							&& (!nicknameManager.getOrCreateShortNick(
							possibleOpponent).
							equals(pointsxtNick))) {
						opponent = nicknameManager.getOrCreateShortNick(
								possibleOpponent);
					}
				}
				if (opponent.length() > 0) {
					if (getPlayerIngameNumber(fullNickname) == 1) {
						gui.gameCreated(this, defaultChannel, newRoom,
								pointsxtNick, opponent, getPlayerGameType(
								fullNickname));
					} else if (getPlayerIngameNumber(fullNickname) == 2) {
						gui.gameCreated(this, defaultChannel, newRoom,
								opponent, pointsxtNick, getPlayerGameType(
								fullNickname));
					} else {
						throw new UnsupportedOperationException(
								"error creating a game");
					}
				} else if (isPointsop(fullNickname)) {
					// no opponent found of a pointsOp player
					gui.gameVacancyCreated(this, defaultChannel, newRoom,
							pointsxtNick, "999sec/ turn");
				}
			}
		}
	}

//room leaving - fix.  Исправил очистку списка в случае одновременной игры и просмотров через Op
	public void userDisconnected_PointsxtStyle(String room,
			String user) {
//		shortNick2FullNick.remove(user);
		if (room.equals(defaultChannel)) {
			nicknameManager.removeIrcNick(user);
			gui.userLeavedRoom(this, room,
					nicknameManager.getOrCreateShortNick(user));
			clearCreatedGames_PointsxtStyle(user);
		}
	}

	public void clearCreatedGames_PointsxtStyle(String user) {
//		System.out.println("serv.clearCreatedGames, user=" + user);
		if (getPlayerRoom(user).length() > 0) {
			gui.gameDestroyed(this, defaultChannel, getPlayerRoom(user));
		}
	}

	public void makedMove_PointsxtStyle(String room,
			boolean silent,
			int x,
			int y,
			boolean isRed) {
		if (room.equals(mainGameRoomName)) {
			MoveResult moveResult = mainGameEngine.makeMove(x, y, isRed);
			if (moveResult != MoveResult.ERROR) {
				mainGameMoveList.add(new SimpleMove(x, y, isRed));
			}
		}
		gui.makedMove(this, room, silent, x, y, isRed);
	}

	boolean isMainGameMyMoveNow() {
		boolean firstMove = mainGameAmIRed && mainGameMoveList.isEmpty();
		boolean myTurnNow = (mainGameMoveList.isEmpty() == false)
				&& (mainGameMoveList.get(mainGameMoveList.size() - 1).isRed
				^ mainGameAmIRed);
		return firstMove || myTurnNow;
	}

	boolean isMainGameRedMoveNow() {
		boolean firstMove = mainGameAmIRed && mainGameMoveList.isEmpty();
		boolean previousWasBlue = (mainGameMoveList.isEmpty() == false)
				&& (mainGameMoveList.get(mainGameMoveList.size() - 1).isRed == false);
		return firstMove || previousWasBlue;
	}

	public void makeMove(String roomName,
			int x,
			int y) {
		if (roomName.equals(mainGameRoomName)) {
			boolean myMoveNow = isMainGameMyMoveNow();
			boolean isFirstMoveAllowed = (mainGameMoveList.size() >= 2)
					|| ((x - 1 >= 12) && (x - 1 <= 19)
					&& (32 - y >= 12) && (32 - y <= 26)); // 12<=x<=19, 12<=y<=26
			if (myMoveNow && isFirstMoveAllowed) {
				makedMove_PointsxtStyle(roomName, false, x, y, mainGameAmIRed);
				super.sendMessage(roomName,
						"" + (char)('0' + x - 1)
						+ (char)('0' + 32 - y)
						+ "999");
			}
		}
	}

	public void surrender(String roomName) {
		if (roomName.equals(mainGameRoomName) && isMainGameMyMoveNow()) {
			super.sendMessage(roomName, "/ImLost");
		}
	}

	public void sendChat(String room,
			String message) {
		super.sendMessage(room, message);
		gui.chatReceived(this, room, getMyName(), message.replaceAll("ACTION",
				"***"));
	}

	public void sendPrivateMsg(String target,
			String message) {
		String fullTargetName = nicknameManager.getIrcNick(target);
		if (fullTargetName != null) {
			super.sendMessage(fullTargetName, message);
		}
	}

	/**
	 * @return gui name
	 */
	public String getMyName() {
		return myNickOnServ.replaceAll(pointsxtTail_RegExp, "");
	}

	public String getMainRoom() {
		return defaultChannel;
	}

	public String getServerName() {
		return defaultServ;
	}

	public String int2characterString(int i) {
		return Character.toString((char)(i));
	}

	public String int2SpectrGameCharacter(int i) {
		return Character.toString((char)(i + '0'));
	}

	public String getSpectrGame_PointsxtStyle() {
		StringBuilder result = new StringBuilder();
		result.append("/SpectrGame ");
		String numberOfTurnsAsString =
				mainGameMoveList.isEmpty() ? "-001"
				: String.format("%03d", mainGameMoveList.size() - 1);
		result.append(numberOfTurnsAsString);
		result.append(String.format("%03d",
				mainGameEngine.getSurroundings().size()));
		result.append(isMainGameRedMoveNow() ? "2" : "1");
		result.append("00000000");
		for (int moveIndex = 0; moveIndex < mainGameMoveList.size(); moveIndex++) {
			SimpleMove simpleMove = mainGameMoveList.get(moveIndex);
			int x = simpleMove.x - 1;
			int y = 32 - simpleMove.y;
			if (simpleMove.isRed) {
				result.append(int2SpectrGameCharacter(x + 40));
			} else {
				result.append(int2SpectrGameCharacter(x));
			}
			result.append(int2SpectrGameCharacter(y));
		}
		result.append(" ");
		List<SurroundingAbstract> surroundingsList = mainGameEngine.getSurroundings();
		for (int surrIndex = 0; surrIndex < surroundingsList.size(); surrIndex++) {
			SurroundingAbstract surrounding = surroundingsList.get(surrIndex);
			int x = surrounding.firstCapturedEnemy.x - 1;
			int y = 32 - surrounding.firstCapturedEnemy.y;
			if ((mainGameEngine.getDotType(x, y)
					== SingleGameEngineInterface.DotType.RED_EATED_BLUE)
					|| (mainGameEngine.getDotType(x, y)
					== SingleGameEngineInterface.DotType.RED_TIRED)) {
				// damn this pointsxt!!!!!!!!!!!
				result.append(int2SpectrGameCharacter(x + 40));
			} else {
				result.append(int2SpectrGameCharacter(x));
			}
			result.append(int2SpectrGameCharacter(y));
		}
		result.append("-");
		return result.toString();
	}
}

class IrcNicknameManager {

	Map<String, String> ShortNick2IrcNick = new HashMap<String, String>();
	Map<String, String> IrcNick2ShortNick = new HashMap<String, String>();

	String getOrCreateShortNick(String ircNick) {
		// inDI_X220111123511[g101]
		if (IrcNick2ShortNick.get(ircNick) != null) {
			// we already had him
			return IrcNick2ShortNick.get(ircNick);
		} else {
			String shortBasic = ircNick.replaceAll(
					ServerPointsxt.pointsxtTail_RegExp, "");
			String shortResult;
			if (ShortNick2IrcNick.containsKey(shortBasic)) {
				int i = 2;
				while (ShortNick2IrcNick.containsKey(shortBasic + "(" + i + ")")) {
					i += 1;
				}
				shortResult = shortBasic + "(" + i + ")";
			} else {
				shortResult = shortBasic;
			}
			ShortNick2IrcNick.put(shortResult, ircNick);
			IrcNick2ShortNick.put(ircNick, shortResult);
			return shortResult;
		}
	}

	String getIrcNick(String s) {
		String result = ShortNick2IrcNick.get(s);
		return (result == null) ? "" : result;
	}

	void changeIrcNick(String oldIrcNick,
			String newIrcNick) {
		String shortNick = IrcNick2ShortNick.get(oldIrcNick);
		IrcNick2ShortNick.put(newIrcNick, shortNick);
//		IrcNick2ShortNick.remove(oldIrcNick);

		ShortNick2IrcNick.put(shortNick, newIrcNick); // overwrite the old
	}

	void removeIrcNick(String ircNick) {
		String shortNick = IrcNick2ShortNick.get(ircNick);
		ShortNick2IrcNick.remove(shortNick);
		IrcNick2ShortNick.remove(ircNick);
	}
}

class SimpleMove {

	int x, y;
	boolean isRed;

	public SimpleMove() {
	}

	public SimpleMove(int x,
			int y,
			boolean isRed) {
		this.x = x;
		this.y = y;
		this.isRed = isRed;
	}
}
//	full: /SpectrGame 046000200000000F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9 -
//	suff: F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9
//	full: /SpectrGame 076000200000000F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8 -
//	suff: F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8
//	full: /SpectrGame 129001100000000F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKE H;-
//	suff: F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKE
//	full: /SpectrGame 154001200000000F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d> H;-
//	suff: F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d>
//	full: /SpectrGame 185001100000000F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d>=>i><?j==BhEACkCBDgB?CfC@
//	suff: F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d>=>i><?j==BhEACkCBDgB?CfC@
//	full: /SpectrGame 000000000000000BhA>BgABEf@=?c?<AeC;Bc@<Bd@=Ac::<b=9< H;-
//	suff: BhA>BgABEf@=?c?<AeC;Bc@<Bd@=Ac::<b=9<
//	full: /SpectrGame 273005100000000F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d>=>i><?j==BhEACkCBDgB?CfC@
//	suff: F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d>=>i><?j==BhEACkCBDgB?CfC@
//	full: /SpectrGame 000000000000000BhA>BgABEf@=?c?<AeC;Bc@<Bd@=Ac::<b=9<dF:C`A8C_<8;b;;<b>7:pHGEqEIDpDHEqFJDpCFFkFCElEDFlDBFmFDGmGEHnHFIoIDIqGHLnJDLmIBKlHBInLFMqLIMoMGNmMFNmLCNkMDMkLDKkKDJkHCJjHAHiIENhHAJiG@JcH>HdD?FgD9GcJ9LaI8IdM;LdL<KcK=KbL;MbM;N H;FBFIEHAH-
//	suff: BhA>BgABEf@=?c?<AeC;Bc@<Bd@=Ac::<b=9<dF:C`A8C_<8;b;;<b>7:pHGEqEIDpDHEqFJDpCFFkFCElEDFlDBFmFDGmGEHnHFIoIDIqGHLnJDLmIBKlHBInLFMqLIMoMGNmMFNmLCNkMDMkLDKkKDJkHCJjHAHiIENhHAJiG@JcH>HdD?FgD9GcJ9LaI8IdM;LdL<KcK=KbL;MbM;N
//	full: /SpectrGame 426013200000000F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d>=>i><?j==BhEACkCBDgB?CfC@
//	suff: F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d>=>i><?j==BhEACkCBDgB?CfC@
//	full: /SpectrGame 000000000000000BhA>BgABEf@=?c?<AeC;Bc@<Bd@=Ac::<b=9<dF:C`A8C_<8;b;;<b>7:pHGEqEIDpDHEqFJDpCFFkFCElEDFlDBFmFDGmGEHnHFIoIDIqGHLnJDLmIBKlHBInLFMqLIMoMGNmMFNmLCNkMDMkLDKkKDJkHCJjHAHiIENhHAJiG@JcH>HdD?FgD9GcJ9LaI8IdM;LdL<KcK=KbL;MbM;NbN<GcG;FcE:FeG<HdI=HgI2J[J=FdE>IfJ<JeJ:KaK:JaJ;IbH8KgL>KgK?JfN<N`L9M`M9N`N:O`J7K_J6J^K7L^I5J]I4J^L7M\I3K[I4KZI7I_H8H`E9EaD
//	suff: BhA>BgABEf@=?c?<AeC;Bc@<Bd@=Ac::<b=9<dF:C`A8C_<8;b;;<b>7:pHGEqEIDpDHEqFJDpCFFkFCElEDFlDBFmFDGmGEHnHFIoIDIqGHLnJDLmIBKlHBInLFMqLIMoMGNmMFNmLCNkMDMkLDKkKDJkHCJjHAHiIENhHAJiG@JcH>HdD?FgD9GcJ9LaI8IdM;LdL<KcK=KbL;MbM;NbN<GcG;FcE:FeG<HdI=HgI2J[J=FdE>IfJ<JeJ:KaK:JaJ;IbH8KgL>KgK?JfN<N`L9M`M9N`N:O`J7K_J6J^K7L^I5J]I4J^L7M\I3K[I4KZI7I_H8H`E9EaD
//	full: /SpectrGame 0000000000000008FbE7FaC7EbB8D^A4D\A2A[B2CZB1B[C2D[D3EZE3A\E3F]D4C\B4FZF2G[G1HZH1GYI1E\=2=Z?1@Y>3?[>2>[@1?\?3<];4;]:4:]948\939]847`B0F_C5E^D6E^655_475^564\656]463]745[638\<3;]334_362`286_795c5:4c487d6 H;FBFIEHAH<G;J:L:C8E3?2E4E-
//	suff: 8FbE7FaC7EbB8D^A4D\A2A[B2CZB1B[C2D[D3EZE3A\E3F]D4C\B4FZF2G[G1HZH1GYI1E\=2=Z?1@Y>3?[>2>[@1?\?3<];4;]:4:]948\939]847`B0F_C5E^D6E^655_475^564\656]463]745[638\<3;]334_362`286_795c5:4c487d6
//	full: /SpectrGame 486020200000000F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d>=>i><?j==BhEACkCBDgB?CfC@
//	suff: F?o?G@n@o>F>o;G=p=G<p<H;n;E=m<F=l<D?m@D>lBC@i<GAlAH>p?I>q@I<pBB<j;FBoBC<k;A;l:A:k8B6i8@8i7A9j9@7i6@5h6?6i5@4i4A3j3A2k2C1l1B2l3B4k3C5m6C7j8D8l9D7m8E7n8F7o8G7p8H7q8I7r8I4r7H5m4L5u7L7t8K7s8M8u9N8t:N9v:O:v;O<v<O=u>N?u?M@t@L?v@MAv>O?w>P>x?O@x=Q>w;P:x<Q;s?L>r@LBqAKEh<?9g;>;g:@9f9>8e8>:e9>6f<=;e<<;iA<<jBBGcA=@d===d>=>i><?j==BhEACkCBDgB?CfC@
//	full: /SpectrGame 000000000000000BhA>BgABEf@=?c?<AeC;Bc@<Bd@=Ac::<b=9<dF:C`A8C_<8;b;;<b>7:pHGEqEIDpDHEqFJDpCFFkFCElEDFlDBFmFDGmGEHnHFIoIDIqGHLnJDLmIBKlHBInLFMqLIMoMGNmMFNmLCNkMDMkLDKkKDJkHCJjHAHiIENhHAJiG@JcH>HdD?FgD9GcJ9LaI8IdM;LdL<KcK=KbL;MbM;NbN<GcG;FcE:FeG<HdI=HgI2J[J=FdE>IfJ<JeJ:KaK:JaJ;IbH8KgL>KgK?JfN<N`L9M`M9N`N:O`J7K_J6J^K7L^I5J]I4J^L7M\I3K[I4KZI7I_H8H`E9EaD
//	suff: BhA>BgABEf@=?c?<AeC;Bc@<Bd@=Ac::<b=9<dF:C`A8C_<8;b;;<b>7:pHGEqEIDpDHEqFJDpCFFkFCElEDFlDBFmFDGmGEHnHFIoIDIqGHLnJDLmIBKlHBInLFMqLIMoMGNmMFNmLCNkMDMkLDKkKDJkHCJjHAHiIENhHAJiG@JcH>HdD?FgD9GcJ9LaI8IdM;LdL<KcK=KbL;MbM;NbN<GcG;FcE:FeG<HdI=HgI2J[J=FdE>IfJ<JeJ:KaK:JaJ;IbH8KgL>KgK?JfN<N`L9M`M9N`N:O`J7K_J6J^K7L^I5J]I4J^L7M\I3K[I4KZI7I_H8H`E9EaD
//	full: /SpectrGame 0000000000000008FbE7FaC7EbB8D^A4D\A2A[B2CZB1B[C2D[D3EZE3A\E3F]D4C\B4FZF2G[G1HZH1GYI1E\=2=Z?1@Y>3?[>2>[@1?\?3<];4;]:4:]948\939]847`B0F_C5E^D6E^655_475^564\656]463]745[638\<3;]334_362`286_795c5:4c487d678^789e75<g5>5g4>4h3;3t4M6s6N7s5I2r4H3h2@1f3=3e2<2z=Q=y<R<z;S<w9P9w8P8w7P6x7Q7u5O6t6?1xILHf2=1q6H6s2JJnKIKZK3M]C6;YJLF_N H;FBFIEHAH<G;J:L:C8E3?2E4E@4M7
//	suff: 8FbE7FaC7EbB8D^A4D\A2A[B2CZB1B[C2D[D3EZE3A\E3F]D4C\B4FZF2G[G1HZH1GYI1E\=2=Z?1@Y>3?[>2>[@1?\?3<];4;]:4:]948\939]847`B0F_C5E^D6E^655_475^564\656]463]745[638\<3;]334_362`286_795c5:4c487d678^789e75<g5>5g4>4h3;3t4M6s6N7s5I2r4H3h2@1f3=3e2<2z=Q=y<R<z;S<w9P9w8P8w7P6x7Q7u5O6t6?1xILHf2=1q6H6s2JJnKIKZK3M]C6;YJLF_N
//	full: /SpectrGame 000000000000000:;O7L5492J-
//	suff: :;O7L5492J-
//	full: /SpectrGame -001000100000000 -
//	suff:
//	full: /SpectrGame 000000000000000:;O7L5492J-
//	suff: :;O7L5492J-
//	full: /SpectrGame -001000100000000 -
//	suff:
//	full: /SpectrGame 005000100000000j>ABl?HAX0=@ -
//	suff: j>ABl?HAX0=@

