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
import ru.narod.vn91.pointsop.data.DotAbstract;
import ru.narod.vn91.pointsop.gameEngine.RandomMovesProvider;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.SurroundingAbstract;
import ru.narod.vn91.pointsop.gui.GuiController;
import ru.narod.vn91.pointsop.sounds.Sounds;

public class ServerPointsxt
		extends PircBot
		implements ServerInterface {

	GuiController gui;
	String myNickOnServ, myNick_Originally;
	private String defaultServ;
	private String defaultChannel;
	private String defaultPass;
	private String defaultServer_Visible;
	protected String ircPassword;
	protected boolean ircAcceptsRussianNicks;
	static String pointsxtTail_RegExp = "_X[0-9]{12,12}\\[....\\]";
	static String gamePrefix = "#pxt";
	static String commandCommonPrefix = "OpCmd ";
	static String commandIWantJoinGame = "I want to join this game.";
	static String commandAcceptOpponent = "I accept opponent ";
	Date lastSpectrTime = new Date(0);
	HashMap<String, String> spectrGameData = new HashMap<String, String>();
	IrcNicknameManager nicknameManager = new IrcNicknameManager();
	MyGame myGame = new MyGame();

	public void connect() {
		Thread thread = new Thread(
				new Runnable() {

					@Override
					public void run() {
						try {
							gui.receiveRawServerInfo(
									ServerPointsxt.this,
									"Cоединение с сервером " + defaultServ
											+ ". Пожалуйста, подождите... (примерно 30 секунд)",
									GuiController.MessageType.INFO
							);
							connect(defaultServ, 6667, ircPassword);
							myNickOnServ = getNick();
							ServerPointsxt.super.sendMessage(
									"podbot",
									"!opConnect0423"
							);
//					subscribeRoom(defaultChannel);
						} catch (NickAlreadyInUseException ignored) {
						} catch (IOException ignored) {
						} catch (IrcException ignored) {
						}
					}
				}
		);
		thread.start();
	}

	public void disconnecttt() {
		super.disconnect();
		super.dispose();
	}

	public ServerPointsxt(
			String server,
			GuiController gui,
			String myName,
			String ircPassword,
			String roomPassword,
			boolean ircAcceptsRussianNicks
	) {
		super();
		this.gui = gui;
		this.defaultServ = server;
		this.ircPassword = ircPassword;
		defaultChannel = "#pointsxt";
		defaultPass = roomPassword;
		this.ircAcceptsRussianNicks = ircAcceptsRussianNicks;
//		defaultServer_Visible = defaultServ.equals("77.232.28.15") ? "pointsgame.info" : defaultServ;
		defaultServer_Visible = defaultServ;

		String login = "";
		try {
			login = InetAddress.getLocalHost().getHostName();
			login = login.replaceAll("[^a-zA-Z0-9-]", "");
		} catch (Exception ignored) {
		}
		super.setLogin(login);

		myName = getAllowedNick(myName, ircAcceptsRussianNicks);
		myNick_Originally = myName;
		myName = "^" + myName;
		myName = myName + "_X091000000000[free]";
		super.setName(myName);
		this.myNickOnServ = myName;

		super.setVersion("PointsOp (a client from vn91)");
		try {
			super.setEncoding("CP1251");
		} catch (UnsupportedEncodingException ignored) {
		}
	}

	public synchronized void searchOpponent() {

		myGame.leaveGame();
		int roomNumber;
		String roomAsString;
		boolean unoccupiedFound = false;
		do {
			roomNumber = (int) (Math.random() * 99999);
			roomAsString = gamePrefix + String.format("%05d", roomNumber);
			unoccupiedFound = true;
			User[] userList = getUsers(defaultChannel);
			for (User anUserList : userList) {
				String user = anUserList.getNick();
				if (roomAsString.equals(getPlayerRoom(user))) {
					// equal found
					unoccupiedFound = false;
				}
			}
		} while (unoccupiedFound == false);
		myGame.roomName = roomAsString;

		myGame.engine = new SingleGameEngine(39, 32);
		myGame.opponentName = "";
		super.joinChannel(roomAsString);
		super.changeNick(
				String.format(
						"%s_X0910000%05d[g101]", getMyName(),
						roomNumber
				)
		);
	}

	public void acceptOpponent(
			String roomName,
			String name) {
		if (!roomName.equals(myGame.roomName)) {
			return;
		}
//		if (opponentName.equals("") == false) {
//			return;
//		} this protection is already done earlier
		myGame.opponentName = nicknameManager.getIrcNick(name);
		if (myGame.opponentName == null) {
			myGame.opponentName = "";
		} else {
			myGame.amIRed = true;
			super.sendMessage(
					roomName,
					commandCommonPrefix + commandAcceptOpponent + myGame.opponentName
			);
			super.changeNick(
					String.format(
							"%s_X0910000%s[g101]", getMyName(), roomName.substring(
							4
					)
					)
			);
			gui.subscribedGame(
					roomName, this, getMyName(), name, 0, 0,
					"999мин/ход", false, "", true, true/*i am the player*/
			);
			for (User user : super.getUsers(roomName)) {
				String ircNick = user.getNick();
				gui.userJoinedGameRoom(
						this,
						roomName,
						nicknameManager.getOrCreateShortNick(ircNick),
						true,
						getPlayerRank(ircNick),
						extractUserStatus(ircNick)
				);
			}
		}
	}

	public void stopSearchingOpponent() {
		myGame.leaveGame();
	}

	public void requestJoinGame(String gameRoomName) {
		super.joinChannel(gameRoomName);
		super.sendMessage(
				gameRoomName,
				commandCommonPrefix + commandIWantJoinGame
		);
	}

	public static String getAllowedNick(
			String inputNick,
			boolean acceptRussian) {
		if (acceptRussian) {
			inputNick = inputNick.replaceAll("[^a-zA-Z0-9а-яА-Я]", "");
			if (inputNick.matches(".*[a-zA-Z].*")) {
				inputNick = inputNick.replaceAll("[а-яА-Я]", "");
				// delete all russian letters in case of a mixed nickname
			}
		} else {
			inputNick = inputNick.replaceAll("[^a-zA-Z0-9]", "");
		}
		if (inputNick.length() > 9) {
			inputNick = inputNick.substring(0, 9);
		}
		return inputNick;
	}

	public void subscribeRoom(String roomName) {
		if (roomName.equals(defaultChannel)) {
			gui.subscribedLangRoom(
					roomName,
					this,
					defaultServer_Visible,
					defaultChannel.equals(roomName)
			);
		} else {
			GameInfoAbstract gameInfoAbstract = getGameInfoFromRoomName(roomName);
			if (gameInfoAbstract != null) {
				gui.subscribedGame(
						roomName, this,
						gameInfoAbstract.userFirst, gameInfoAbstract.userSecond,
						gameInfoAbstract.rank1, gameInfoAbstract.rank2,
						gameInfoAbstract.timeLimits, gameInfoAbstract.isRated,
						"",
						true /* chat is read-only */, false/*I'm a spectator*/
				);
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
			if (roomName.equals(myGame.roomName)) {
				myGame.leaveGame();
			} else {
				super.partChannel(roomName);
				gui.unsubsribedRoom(this, roomName);
			}
		}
	}

	@Override
	protected void onJoin(
			String channel,
			String sender,
			String login,
			String hostname) {
		userConnected_PointsxtStyle(channel, sender, /*not silent*/ false);
		if ((channel.equals(defaultChannel)) && (sender.equals(myNickOnServ))) {
			gui.receiveRawServerInfo(
					this,
					"Успешно подключился к основной комнате.",
					GuiController.MessageType.INFO
			);
		}
		if (channel.equals(myGame.roomName) && myGame.amIRed) {
			super.sendMessage(sender, getSpectrGame_PointsxtStyle());
		}
	}

	@Override
	protected void onConnect() {
		if (super.isConnected()) {
			gui.receiveRawServerInfo(
					this,
					"Удалось соединиться с " + defaultServ
							+ ", пытаюсь подключиться к основной комнате...",
					GuiController.MessageType.INFO
			);
		}
	}

	@Override
	protected void onDisconnect() {
		gui.serverClosed(this);
	}

	@Override
	protected void onUserList(
			String channel,
			User[] users) {
		for (User user : users) {
			userConnected_PointsxtStyle(channel, user.getNick(),/*silent*/ true);
		}
	}

	@Override
	protected void onTopic(
			String channel,
			String topic,
			String setBy,
			long date,
			boolean changed) {
		gui.serverNoticeReceived(this, channel, "Topic: " + topic);
	}

	@Override
	protected void onKick(
			String channel,
			String kickerNick,
			String kickerLogin,
			String kickerHostname,
			String recipientNick,
			String reason) {
		userDisconnected_PointsxtStyle(channel, recipientNick);
	}

	@Override
	protected void onPart(
			String channel,
			String sender,
			String login,
			String hostname) {
		userDisconnected_PointsxtStyle(channel, sender);
		if (sender.equals(myGame.opponentName)
				&& channel.equals(myGame.roomName)) {
			myGame.leaveGame();
//			gui.chatReceived(
//					this, channel,
//					nicknameManager.getOrCreateShortNick(sender),
//					"Ваш оппонент закрыл игру.");
		}
	}

	@Override
	protected void onQuit(
			String sourceNick,
			String sourceLogin,
			String sourceHostname,
			String reason) {
		String[] channels = getChannels();
		for (String channelName : channels) {
			userDisconnected_PointsxtStyle(channelName, sourceNick);
		}
		gui.userDisconnected(
				this, nicknameManager.getOrCreateShortNick(
				sourceNick
		)
		);
	}

	@Override
	protected void onNickChange(
			String oldNick,
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
			gui.serverNoticeReceived(
					this, defaultChannel,
					"" + nicknameManager.getOrCreateShortNick(newNick)
							+ " " + rankOld + " -> " + rankNew
			);
		}
	}

	@Override
	protected void onMessage(
			String channel,
			String sender,
			String login,
			String hostname,
			String message) {
		String nick = nicknameManager.getOrCreateShortNick(sender);
		int playerNumb = getPlayerIngameNumber(sender);
		if (message.startsWith(commandCommonPrefix)) {
			if ((message.startsWith(commandCommonPrefix + commandIWantJoinGame))
					&& (channel.equals(myGame.roomName))
					&& (myGame.opponentName.equals(""))) {
				String opponentNick = nicknameManager.getOrCreateShortNick(
						sender
				);
				this.acceptOpponent(channel, opponentNick);
			} else if (message.startsWith(
					commandCommonPrefix + commandAcceptOpponent + myNickOnServ
			)) {
				super.changeNick(
						String.format(
								"%s_X0910000%s[g201]",
								getMyName(), channel.substring(4)
						)
				);
				myGame.amIRed = false;
				myGame.engine = new SingleGameEngine(39, 32);
				myGame.moveList = new ArrayList<SimpleMove>();
				myGame.opponentName = nick;
				myGame.roomName = channel;
				gui.subscribedGame(
						channel, this, nick, getMyName(), 0, 0,
						"999мин/ход", false, "", true, true/*i am the player*/
				);
				for (User user : super.getUsers(channel)) {
					String ircNick = user.getNick();
					gui.userJoinedGameRoom(
							this,
							channel,
							nicknameManager.getOrCreateShortNick(ircNick),
							true,
							getPlayerRank(ircNick),
							extractUserStatus(ircNick)
					);
				}
			}
			return; /* "if I won't take it - no one will."
			if we had a pointsOp message and failed to handle it - do nothing.
			Because this may be a message from higher versions of the protocol.
			And this message shouldn't be visible as text in this case */
		} else if (channel.equals(defaultChannel)) {
			if (message.startsWith("ACTION")) {
				gui.chatReceived(
						this, channel, "***" + nick, message.substring(
						7
				).replaceAll(pointsxtTail_RegExp, "")
				);
			} else {
				gui.chatReceived(
						this, channel, nick, message.replaceAll(
						pointsxtTail_RegExp, ""
				)
				);
			}
		} else {
			// game channel
			if ((playerNumb != -1) && (message.matches("..[0-9]{3,3}"))) {
				// new move
				int x = getCoordinate(message.charAt(0));
				int y = getCoordinate(message.charAt(1));
				x = x + 1; // pointsOp version of coordinates
				y = 31 - y + 1; // pointsOp version of coordinates
				sendMoveToGui(
						channel, false /*not silent*/,
						x,
						y,
						(playerNumb == 1)
				);
			} else if (message.equalsIgnoreCase("/ImLost")) {
				User[] users = getUsers(channel);
				boolean biggerFishFound = false;
				for (User user1 : users) {
					String user = user1.getNick();
					if ((user.compareTo(getNick()) > 0) && (user.startsWith("^"))) {
						biggerFishFound = true;
					}
				}
				gui.gameLost(
						this, getPlayerRoom(sender), playerNumb == 1,
						biggerFishFound == false
				);
			} else if (message.equalsIgnoreCase("/StopTheGame")) {
				gui.gameStop(this, getPlayerRoom(sender), playerNumb == 1);
			}
		}
	}

	@Override
	protected void onPrivateMessage(
			String sender,
			String login,
			String hostname,
			String message) {

		if (message.equals("/SendSOUND")) {
			new Sounds().playAlarmSignal();
			gui.serverNoticeReceived(
					this, defaultChannel, nicknameManager.getOrCreateShortNick(
					sender
			) + " sends you a sound"
			);
		} else if (message.startsWith("/PASSOK")
				&& (sender.equalsIgnoreCase("podbot"))) {
			subscribeRoom("#pointsxt");
		} else if (message.startsWith("/PASSOK")) {
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

			String suffPartAsString = message.substring(
					headerEndsPosition,
					tailStartsPosition
			);
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
					sendMoveToGui(
							targetRoom, true /*silent*/, x, y,
							isRed
					);
				}
				sendMoveToGui(targetRoom, false, -2, -2, true); // update the paper of the user
			}

		} else if (message.equals("/Ping")) {
			// pointsxt-style ping-ing
			super.sendMessage(sender, "/Pong");
		} else {
			String nick = nicknameManager.getOrCreateShortNick(sender);
			gui.privateMessageReceived(
					this, nick, message.replaceAll(
					pointsxtTail_RegExp, ""
			)
			);
		}
	}

	@Override
	protected void onUnknown(String line) {
		gui.receiveRawServerInfo(
				this,
				line.replaceAll(pointsxtTail_RegExp, ""),
				GuiController.MessageType.INFO
		);
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
		for (User user : users) {
			String userIrcName = user.getNick();
			if (getPlayerRoom(userIrcName).equals(roomName)) {
				String pxtGameInfo = userIrcName.substring(
						userIrcName.length() - 5, userIrcName.length() - 1
				);
				gameInfoAbstract.isRated = pxtGameInfo.startsWith("1", 2);
				gameInfoAbstract.timeLimits = pxtGameInfo.startsWith("1", 3) ? "5sec/turn" : "180sec/5turns";
				if (pxtGameInfo.startsWith("1", 1)) {
					gameInfoAbstract.userFirst = nicknameManager.getOrCreateShortNick(
							userIrcName
					);
					gameInfoAbstract.rank1 = getPlayerRank(userIrcName);
				} else {
					gameInfoAbstract.userSecond = nicknameManager.getOrCreateShortNick(
							userIrcName
					);
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
			String rankAsString = ircNick.substring(
					ircNick.length() - 15,
					ircNick.length() - 11
			);
			return Integer.parseInt(rankAsString);
		} else {
			return 0;
		}
	}

	private String extractUserStatus(String nick) {
		if (hasPointsxtNickname(nick)) {
			String stateType = nick.substring(
					nick.length() - 5,
					nick.length() - 1
			);
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
			String letter = fullNick.substring(
					fullNick.length() - 4,
					fullNick.length() - 3
			);
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

	public void userConnected_PointsxtStyle(
			String room,
			String fullNickname,
			boolean silent) {
		String pointsxtNick = nicknameManager.getOrCreateShortNick(fullNickname);
		if (room.equals(defaultChannel)) {
			// join Lang room
			gui.userJoinedLangRoom(
					this, room, pointsxtNick, silent,
					getPlayerRank(fullNickname), extractUserStatus(fullNickname)
			);
		} else {
			// join Game room
			gui.userJoinedGameRoom(
					this, room, pointsxtNick, silent,
					getPlayerRank(fullNickname), extractUserStatus(fullNickname)
			);
		}
		if (room.equals(defaultChannel)) {
			String newRoom = getPlayerRoom(fullNickname);
			if (newRoom.length() > 0) {
				User[] users = getUsers(defaultChannel);
				String opponent = "";
				for (User user : users) {
					String possibleOpponent = user.getNick();
					if ((getPlayerRoom(possibleOpponent).equals(newRoom))
							&& (!nicknameManager.getOrCreateShortNick(
							possibleOpponent
					).
							equals(pointsxtNick))) {
						opponent = nicknameManager.getOrCreateShortNick(
								possibleOpponent
						);
					}
				}
				if (opponent.length() > 0) {
					if (getPlayerIngameNumber(fullNickname) == 1) {
						gui.gameCreated(
								this, defaultChannel, newRoom,
								pointsxtNick, opponent, getPlayerGameType(
								fullNickname
						)
						);
					} else if (getPlayerIngameNumber(fullNickname) == 2) {
						gui.gameCreated(
								this, defaultChannel, newRoom,
								opponent, pointsxtNick, getPlayerGameType(
								fullNickname
						)
						);
					} else {
						throw new UnsupportedOperationException(
								"error creating a game"
						);
					}
				} else if (isPointsop(fullNickname)) {
					// no opponent found of a pointsOp player
					gui.gameVacancyCreated(
							this, defaultChannel, newRoom,
							pointsxtNick, "5сек/ ход"
					);
				}
			}
		}
	}

	public void userDisconnected_PointsxtStyle(
			String room,
			String user) {
		if (room.equals(defaultChannel)) {
			nicknameManager.removeIrcNick(user);
			gui.userLeavedRoom(
					this, room,
					nicknameManager.getOrCreateShortNick(user)
			);
			clearCreatedGames_PointsxtStyle(user);
		} else {
			gui.userLeavedRoom(
					this, room,
					nicknameManager.getOrCreateShortNick(user)
			);
			if (user.equals(myGame.opponentName)
					&& room.equals(myGame.roomName)) {
				myGame.clear();
				gui.chatReceived(this, room, "", "Оппонент покинул игру");
			}
		}
	}

	public void clearCreatedGames_PointsxtStyle(String user) {
		if (getPlayerRoom(user).length() > 0) {
			gui.gameDestroyed(this, defaultChannel, getPlayerRoom(user));
		}
	}

	public synchronized void sendMoveToGui(
			final String room,
			boolean silent,
			int x,
			int y,
			boolean isRed) {
		if (room.equals(myGame.roomName)) {
			MoveResult moveResult = myGame.engine.makeMove(x, y, isRed);
			if (moveResult != MoveResult.ERROR) {
				myGame.moveList.add(new SimpleMove(x, y, isRed));
			}
		}
		gui.makedMove(this, room, silent, x, y, isRed);
		boolean iHaveMoved = !myGame.isMyMoveNow();
		if (iHaveMoved) {
			myGame.lastTimeoutThread = null;
		} else {
			Thread timeOutThread = new Thread() {

				@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter"})
				@Override
				public void run() {
					long timeEnd = new Date().getTime() + 5 * 1000;
					long estimatedTime = timeEnd - new Date().getTime();
					while (estimatedTime > 0) {
						Object o = new Object();
						synchronized (o) {
							try {
								o.wait(estimatedTime + 10);
							} catch (InterruptedException ignored) {
							}
						}
						estimatedTime = timeEnd - new Date().getTime();
					}
					if (this.equals(myGame.lastTimeoutThread)) {
						DotAbstract dot = myGame.randomMovesProvider.findEmptyRandomPlace(
								myGame.engine
						);
						if (dot != null) {
							gui.serverNoticeReceived(
									ServerPointsxt.this,
									room,
									"Время вышло и точка сама поставилась в случаиное место на поле"
							);
							makeMove(room, dot.x, dot.y);
						}
					}
				}
			};
			myGame.lastTimeoutThread = timeOutThread;
			timeOutThread.start();
		}
	}

	public void surrender(String roomName) {
		myGame.surrender(roomName);
	}

	@Override
	synchronized public void makeMove(
			String roomName,
			int x,
			int y) {
		myGame.makeMove(roomName, x, y);
	}

	public void sendChat(
			String room,
			String message) {
		super.sendMessage(room, message);
		gui.chatReceived(
				this, room, getMyName(), message.replaceAll(
				"ACTION",
				"***"
		)
		);
	}

	public void sendPrivateMsg(
			String target,
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
		return defaultServer_Visible;
	}

	public String int2SpectrGameCharacter(int i) {
		return Character.toString((char) (i + '0'));
	}

	public String getSpectrGame_PointsxtStyle() {
		StringBuilder result = new StringBuilder();
		result.append("/SpectrGame ");
		String numberOfTurnsAsString =
				myGame.moveList.isEmpty() ? "-001"
						: String.format("%03d", myGame.moveList.size() - 1);
		result.append(numberOfTurnsAsString);
		result.append(
				String.format(
						"%03d",
						myGame.engine.getSurroundings().size()
				)
		);
		result.append(myGame.isRedMoveNow() ? "2" : "1");
		result.append("00000000");
		for (int moveIndex = 0; moveIndex < myGame.moveList.size(); moveIndex++) {
			SimpleMove simpleMove = myGame.moveList.get(moveIndex);
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
		List<SurroundingAbstract> surroundingsList = myGame.engine.getSurroundings();
		for (int surrIndex = 0; surrIndex < surroundingsList.size(); surrIndex++) {
			SurroundingAbstract surrounding = surroundingsList.get(surrIndex);
			int x = surrounding.firstCapturedEnemy.x - 1;
			int y = 32 - surrounding.firstCapturedEnemy.y;
			if ((myGame.engine.getDotType(x, y)
					== SingleGameEngineInterface.DotType.RED_EATED_BLUE)
					|| (myGame.engine.getDotType(x, y)
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

	class MyGame {

		String roomName = "";
		String opponentName = "";
		boolean amIRed;
		SingleGameEngineInterface engine;
		ArrayList<SimpleMove> moveList = new ArrayList<SimpleMove>();
		Thread lastTimeoutThread = null;
		RandomMovesProvider randomMovesProvider = new RandomMovesProvider(39, 32);

		private void clear() {
			randomMovesProvider = new RandomMovesProvider(39, 32);
			roomName = "";
			moveList.clear();
			opponentName = "";
			engine = null;
			ServerPointsxt.this.changeNick(
					String.format(
							"%s_X091000000000[free]",
							ServerPointsxt.this.getMyName()
					)
			);
		}

		void leaveGame() {
			if (opponentName != null) {
				clear();
				ServerPointsxt.this.partChannel(roomName);
				ServerPointsxt.this.gui.unsubsribedGame(
						ServerPointsxt.this, roomName
				);
			}
		}

		boolean isMyMoveNow() {
			boolean firstMove = amIRed && moveList.isEmpty();
			boolean myTurnNow = (moveList.isEmpty() == false)
					&& (moveList.get(
					moveList.size() - 1
			).isRed
					^ amIRed);
			return firstMove || myTurnNow;
		}

		boolean isRedMoveNow() {
			boolean firstMove = amIRed && moveList.isEmpty();
			boolean previousWasBlue = (moveList.isEmpty() == false)
					&& (moveList.get(
					moveList.size() - 1
			).isRed == false);
			return firstMove || previousWasBlue;
		}

		public void makeMove(
				String roomName,
				int x,
				int y) {
			if (roomName.equals(this.roomName)) {
				boolean isFirstMoveAllowed = ((moveList.size() >= 2))
						|| ((x - 1 >= 12) && (x - 1 <= 19)
						&& (32 - y >= 12) && (32 - y <= 26)); // 12<=x<=19, 12<=y<=26
				if (isMyMoveNow() && isFirstMoveAllowed) {
					ServerPointsxt.this.sendMoveToGui(
							roomName, false, x, y,
							amIRed
					);
					ServerPointsxt.this.sendMessage(
							roomName,
							"" + (char) ('0' + x - 1)
									+ (char) ('0' + 32 - y)
									+ "999"
					);
				}
			}
		}

		public void surrender(String roomName) {
			if (roomName.equals(this.roomName)
					&& isMyMoveNow()) {
				ServerPointsxt.this.sendMessage(roomName, "/ImLost");
			}
		}
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
					ServerPointsxt.pointsxtTail_RegExp, ""
			);
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

	void changeIrcNick(
			String oldIrcNick,
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

	public SimpleMove(
			int x,
			int y,
			boolean isRed) {
		this.x = x;
		this.y = y;
		this.isRed = isRed;
	}
}
