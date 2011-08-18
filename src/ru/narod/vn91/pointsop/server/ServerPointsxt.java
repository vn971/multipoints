// welcome to ___HELL___
// this is the most ugly code in pointsOp,
// because it deals with the most ugly protocol (IRC-pointsxt)
//
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
import ru.narod.vn91.pointsop.data.Dot;
import ru.narod.vn91.pointsop.gameEngine.RandomMovesProvider;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.SurroundingAbstract;
import ru.narod.vn91.pointsop.gui.GuiForServerInterface;

public class ServerPointsxt
		extends PircBot
		implements ServerInterface {

	GuiForServerInterface gui;
	String myNickOnServ, myNick_Originally;
	private String defaultServ;
	private String defaultChannel;
	private String defaultPass;
	private String defaultServer_Visible;
	protected String ircPassword;
	protected boolean ircAcceptsRussianNicks;
	static String pointsxtTail_RegExp = "_X[0-9]{12,12}\\[....\\]";
	String pointsxtVersion;
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
							gui.raw(
									ServerPointsxt.this,
									"Cоединение с сервером " + defaultServ
											+ ". Пожалуйста, подождите... (примерно 30 секунд)"
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

	public void disconnectServer() {
		super.disconnect();
		super.dispose();
	}

	public ServerPointsxt(
			String server,
			GuiForServerInterface gui,
			String myName,
			String ircPassword,
			String roomPassword,
			boolean ircAcceptsRussianNicks,
			String pointsxtVersion
	) {
		super();
		this.gui = gui;
		this.defaultServ = server;
		this.ircPassword = ircPassword;
		defaultChannel = "#pointsxt";
		defaultPass = roomPassword;
		this.ircAcceptsRussianNicks = ircAcceptsRussianNicks;
		this.pointsxtVersion = pointsxtVersion;
		defaultServer_Visible = defaultServ;

		String login = "";
		try {
			login = InetAddress.getLocalHost().getHostName();
			login = login.replaceAll("[^a-zA-Z0-9-]", "");
			if (login.length() > 9) {
				login = login.substring(0, 9);
			}
		} catch (Exception ignored) {
		}
		if (login.equals("")) {
			login = "l";
		}
		super.setLogin(login);

		myName = getAllowedNick(myName, ircAcceptsRussianNicks);
		if (myName.equals("")) {
			myName = String.format("Guest%04d", (int) (Math.random() * 9999));
		}
		myNick_Originally = myName;
		myName = "^" + myName;
		myName = myName + "_X" + pointsxtVersion + "000000000[free]";
		super.setName(myName);
		this.myNickOnServ = myName;
		super.setAutoNickChange(false);

//		super.setVerbose(true);

		super.setMessageDelay(100L);
		super.setVersion("Op");
		try {
			super.setEncoding("CP1251");
		} catch (UnsupportedEncodingException ignored) {
		}
	}

	public synchronized void searchOpponent() {
		myGame.leaveGame(false);
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

		this.setPointsxtNickname(
				""+roomNumber, true, true);

//		super.changeNick(
//				String.format(
//						"%s_X" + pointsxtVersion + "0000%05d[g101]",
//						getMyName(),
//						roomNumber
//						)
//		);
		{
			// chat notify...
			 String notifyMessage =
			 "оставил(а) заявку на игру";
			 super.sendMessage(defaultChannel, "ACTION " + notifyMessage);
			 gui.chatReceived(
			 this,
			 defaultChannel,
			 getMyName(),
			 "*** " + notifyMessage
			 );
		}
	}

	public void acceptOpponent(
			String roomName,
			String newOpponent) {
		if (! roomName.equals(myGame.roomName)) {
			return;
		}
		// if (opponentName.equals("") == false) {
		// return;
		// } this protection is already done earlier
		String ircOpponentName = nicknameManager.getIrcNick(newOpponent);
		if (ircOpponentName == null) {
			// do nothing
		} else if (isPointsXTNickname(ircOpponentName)) {
			tryInvitePointsxt(ircOpponentName, false);
		} else if (isPointsopSameVersionNickname(ircOpponentName)) {
			myGame.opponentName = newOpponent;
			myGame.amIRed = true;
			super.sendMessage(
					roomName,
					commandCommonPrefix + commandAcceptOpponent + myGame.getOpponentIrcName()
			);
			this.setPointsxtNickname(roomName.substring(4)
					, true, true);

			gui.subscribedGame(
					roomName, this,
					getMyName(), newOpponent,
					"999мин/ход", false, "", true, true/*i am the player*/, myGame.amIRed
			);
			for (User user : super.getUsers(roomName)) {
				String ircNick = user.getNick();
				gui.userJoinedRoom(this, roomName, nicknameManager.getOrCreateShortNick(ircNick), true);
			}
		}
	}

	@Override
	public void rejectOpponent(String roomName, String notWantedOpponent) {
		super.sendMessage(
				nicknameManager.getIrcNick(notWantedOpponent),
				"Приглашение на игру отклонено.");
	}

	public void stopSearchingOpponent() {
		myGame.leaveGame(false);
	}

	public void requestPlay(String gameRoomName) {
		super.joinChannel(gameRoomName);
		super.sendMessage(
				gameRoomName,
				commandCommonPrefix + commandIWantJoinGame
		);
	}

	public void tryInvitePointsxt(String target, boolean isVerbose) {
//		if (target.startsWith("\\^")) {
//			super.sendMessage(
//					target,
//					"чтобы принять заявку клиентом pointsOp надо дважды кликнуть по заявке."
//			);
//			return;
//		} else if (myGame.isInSearchNow() == false) {
//			if (isVerbose) {
//				super.sendMessage(
//						target,
//						"На игру можно вызывать только игрока " +
//								"который в состоянии поиска оппонента. " +
//								"(Это системное сообщение.)"
//						);
//			}
//			return;
//		} else

//		super.sendMessage(target, "/PointsXTStart NotRait Blits Chisto");
		super.sendMessage(target, "/PointsXTStart NotRait Normal Chisto");
	}

	public static String getAllowedNick(
			final String myNameOnServer,
			final boolean acceptNonEnglish) {
		// final = I'm testing Functional Programming approach:)
		final String filtered;
		if (myNameOnServer.matches(".*[a-zA-Z].*")) {
			filtered = myNameOnServer.replaceAll("[^a-zA-Z0-9]", "");
		} else if (myNameOnServer.matches(".*[ёа-яЁА-Я].*") && acceptNonEnglish) {
			filtered = myNameOnServer.replaceAll("[^ёа-яЁА-Я0-9]", "");
		} else if (myNameOnServer.matches(".*[a-żA-Ż].*") && acceptNonEnglish) {
			filtered = myNameOnServer.replaceAll("[^a-żA-Ż0-9]", "");
		} else {
			filtered = myNameOnServer.replaceAll("[^0-9]", "");
		}
		final String result;
		if (filtered.length()>9) {
			result = filtered.substring(0,9);
		} else {
			result = filtered;
		}
		// myNameOnServer = myNameOnServer.replaceAll("[^a-zA-Zёа-яЁА-Яa-żA-Ż0-9]",
		// "");
		return result;
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
						gameInfoAbstract.timeLimits, gameInfoAbstract.isRated,
						"",
						true /* chat is read-only */, false/*I'm a spectator*/, true
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
				myGame.leaveGame(true);
			} else {
				super.partChannel(roomName);
				gui.unsubsribedRoom(this, roomName);
			}
		}
	}

	@Override
	protected void onConnect() {
		if (super.isConnected()) {
			gui.raw(
					this,
					"Удалось соединиться с " + defaultServ
					+ ", пытаюсь подключиться к основной комнате...");
		}
	}

	@Override
	protected void onJoin(
			String channel,
			String sender,
			String login,
			String hostname) {
		userConnected_PointsxtStyle(channel, sender, /*not silent*/ false);
// other code is executed only when user joins a channel, not changes his nick.
		if ((channel.equals(defaultChannel)) && (sender.equals(myNickOnServ))) {
			gui.raw(
					this,
					"Успешно подключился к основной комнате.");
		}
		if (channel.equals(myGame.roomName) && myGame.amIRed) {
			this.sendSpectr(sender);
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
//		if (nicknameManager.getOrCreateShortNick(sender)
//				.equals(myGame.getOpponentShortName())
//				&& channel.equals(myGame.roomName)) {
//			myGame.leaveGame(false);
//			//			gui.chatReceived(
//			//					this, channel,
//			//					nicknameManager.getOrCreateShortNick(sender),
//			//					"Ваш оппонент закрыл игру.");
//		}
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
		clearCreatedGames_PointsxtStyle(oldNick);
		nicknameManager.changeIrcNick(oldNick, newNick);
//		if (nicknameManager.getOrCreateShortNick(newNick).
//				equals(nicknameManager.getOrCreateShortNick(oldNick)) == false) {
//			userDisconnected_PointsxtStyle(defaultChannel, oldNick);
//		}
		userConnected_PointsxtStyle(defaultChannel, newNick, /*silent*/ true);

		int rankOld = getPlayerRank(oldNick);
		int rankNew = getPlayerRank(newNick);
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
		if (channel.equals(defaultChannel) && (
				message.equalsIgnoreCase("!s")
						|| message.equalsIgnoreCase("!ы"))) {

			if (isPointsopNickname(sender)) {
//				super.sendMessage(
//						target,
//						"чтобы принять заявку клиентом pointsOp надо дважды кликнуть по заявке."
//				);
//				return;
			} else if (myGame.isSearching() == false) {
			} else {
				gui.gameRequestReceived(
						this, myGame.roomName, nicknameManager.getOrCreateShortNick(sender));
			}
		}
		// catch game-invite
		String nick = nicknameManager.getOrCreateShortNick(sender);
		int playerNumb = getPlayerIngameNumber(sender);
		if (message.startsWith(commandCommonPrefix)) {
			if ((message.startsWith(commandCommonPrefix + commandIWantJoinGame))
					&& (channel.equals(myGame.roomName))
					&& (myGame.isPlaying() == false)) {
				String opponentNick = nicknameManager.getOrCreateShortNick(
						sender
				);
//				System.out.println("received Op-game request");
				gui.gameRequestReceived(this, channel, opponentNick);
			} else if (message.startsWith(commandCommonPrefix
					+ commandAcceptOpponent + myNickOnServ
					) && isPointsopSameVersionNickname(sender)) {
				this.setPointsxtNickname(
						channel.substring(4),
						true, false);
//				super.changeNick(
//						String.format(
//								"%s_X" + pointsxtVersion + "0000%s[g201]",
//								getMyName(), channel.substring(4)
//								)
//				);
				myGame.amIRed = false;
				myGame.engine = new SingleGameEngine(39, 32);
				myGame.moveList = new ArrayList<SimpleMove>();
				myGame.opponentName = nick;
				myGame.roomName = channel;
				gui.subscribedGame(
						channel, this, nick, getMyName(),
						"999мин/ход", false, "", true, true/*i am the player*/, myGame.amIRed
				);
				for (User user : super.getUsers(channel)) {
					String ircNick = user.getNick();
					gui.userJoinedRoom(this, channel, nicknameManager.getOrCreateShortNick(ircNick), true);
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
			if ((playerNumb != - 1) && (message.matches("..[0-9]{3,3}"))) {
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
//			new Sounds().playAlarmSignal();
//			gui.serverNoticeReceived(
//					this, defaultChannel, nicknameManager.getOrCreateShortNick(
//					sender
//			) + " sends you a sound"
//			);

			if (isPointsopNickname(sender)) {
				gui.soundReceived(this, nicknameManager.getOrCreateShortNick(sender));
			} else if (myGame.isSearching() == false) {
				gui.soundReceived(this, nicknameManager.getOrCreateShortNick(sender));
				super.sendMessage(
						sender,
						"На игру можно вызывать только игрока " +
								"который в состоянии поиска оппонента. " +
								"(Это системное сообщение.)"
						);
			} else {
				gui.gameRequestReceived(
								this, myGame.roomName, nicknameManager
										.getOrCreateShortNick(sender));
//			tryInvitePointsxt(sender, true);
			}
		} else if (message.equals("/GetTehGame")) {
			//			if (myGame.isActive()) {
			//				super.sendMessage(
			//						sender,
			//						"Игра уже начата, извините. (Это системное сообщение.)"
			//				);
			//			} else {
			this.sendSpectr(sender);
			//			}
		} else if (message.startsWith("/PointsXTAccept ")) {
			String room = getPlayerRoom(sender);
			String roomNumber = getPlayerRoomNumber(sender);
			if (isGamerNickname(sender)
					&& (getPlayerRoomNumber(sender).equals("")) == false) {
				if (myGame.isPlaying()) {
					super.sendMessage(
							sender,
							"Игра уже начата, извините. (Это системное сообщение.)"
					);
				} else {
					super.partChannel(myGame.roomName);
					this.setPointsxtNickname(roomNumber, true, true);
//					super.changeNick(
//							String.format(
//									"%s_X" + pointsxtVersion + "0000%s[g101]",
//									getMyName(),
//									roomNumber
//									)
//					);
					super.joinChannel(room);
					myGame.amIRed = true;
					myGame.engine = new SingleGameEngine(39, 32);
					myGame.moveList = new ArrayList<SimpleMove>();
					myGame.opponentName = nicknameManager.getOrCreateShortNick(sender);
					myGame.roomName = room;
					gui.subscribedGame(
							room,
							this,
							getMyName(),
							nicknameManager.getOrCreateShortNick(sender),
							"999мин/ход",
							false,
							"",
							true,
							true
							/*i am the player*/,
							myGame.amIRed
					);
					for (User user : super.getUsers(room)) {
						String ircNick = user.getNick();
						gui.userJoinedRoom(this, room, nicknameManager.getOrCreateShortNick(ircNick), true);
					}
				}
			}
		} else if (message.startsWith("/PointsXTDiscard")) {
		} else if (message.startsWith("/PointsXTStart")) {
		} else if (
				message.equalsIgnoreCase("!s")
						|| message.startsWith("!opstart")
						|| message.equalsIgnoreCase("!ы")) {
//			tryInvitePointsxt(sender, true);
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

				for (
						int moveN = 0; ((moveN * 2 + 1) < sufficiendPart.length); moveN++) {
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
				sendMoveToGui(
						targetRoom, false, - 2, - 2, true
				); // update the paper of the user
			}

		} else if (message.equals("/Ping")) {
			// pointsxt-style ping-ing
			super.sendMessage(sender, "/Pong");
		} else if (message.equals("/GameMinimaze")) {
		} else if (message.equals("/GameMaximaze")) {
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
		gui.raw(
				this,
				line.replaceAll(pointsxtTail_RegExp, ""));
	}

	private boolean isGamerNickname(String fullNick) {
		return fullNick.matches(".*" + pointsxtTail_RegExp);
	}

	private boolean isPointsXTNickname(String ircNick) {
		return (isPointsopNickname(ircNick) == false)
				&& ircNick.matches(".*" + pointsxtTail_RegExp);
	}

	private String getPlayerRoom(String nick) {
		if (isGamerNickname(nick) == false) {
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

	private String getPlayerRoomNumber(String nick) {
		if (isGamerNickname(nick) == false) {
			return "";
		} else {
			String roomSuffix =
					nick.substring(nick.length() - 11, nick.length() - 6);
			if (roomSuffix.equals("00000")) {
				return "";
			} else {
				return roomSuffix;
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
				gameInfoAbstract.timeLimits = pxtGameInfo.startsWith(
						"1", 3
				) ? "5sec/turn" : "180sec/5turns";
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
		if ((gameInfoAbstract.userFirst != null) &&
				(gameInfoAbstract.userSecond != null)) {
			return gameInfoAbstract;
		} else {
			return null;
		}
	}

	private int getPlayerRank(String ircNick) {
		if (isGamerNickname(ircNick)) {
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
		if (isGamerNickname(nick)) {
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

	private void setPointsxtNickname(
			String roomName,
			boolean amIPlaying,
			boolean amIRed
//			boolean isRanked,
//			boolean isBlits
			) {
		String result = "";
		result = getMyName();
		result += "_X";
//		result += String.format("%03d", programVersion);
		result += pointsxtVersion;
		result += "0000";
		while (roomName.length() < 5) {
			roomName = "0" + roomName;
		}
		result += roomName;
		if (amIPlaying == false) {
			result += "[free]";
		} else {
			result += "[g";
			result += amIRed ? "1" : "2";
//			result += isRanked ? "1" : "0";
//			result += isBlits ? "1" : "0";
			result += "00]";
		}
		super.changeNick(result);
	}

	private String getPlayerGameType(String nick) {
		if (isGamerNickname(nick)) {
			return getGameInfoFromIrcNick(nick).getTimeAndIsRated();
		} else {
			return "";
		}
	}

	private int getPlayerIngameNumber(String fullNick) {
		if (isGamerNickname(fullNick)) {
			String letter = fullNick.substring(
					fullNick.length() - 4,
					fullNick.length() - 3
			);
			if (letter.equals("1")) {
				return 1;
			} else if (letter.equals("2")) {
				return 2;
			} else {
				return - 1;
			}
		} else {
			return - 1;
		}

	}

	private int getCoordinate(char charr) {
		return charr - "0".charAt(0);
	}

	private boolean isPointsopNickname(String ircNick) {
		return ircNick.startsWith("^")
		&& ircNick.matches(".*" + pointsxtTail_RegExp + ".*");
	}

	private boolean isPointsopSameVersionNickname(String ircNick) {
		return ircNick.startsWith("^")
				&& ircNick.matches(
						".*" + "_X" + pointsxtVersion + "[0-9]{9,9}\\[....\\]" + ".*");
	}

	public void userConnected_PointsxtStyle(
			String room,
			String ircNick,
			boolean silent) {
		String pointsxtNick = nicknameManager.getOrCreateShortNick(ircNick);
		gui.addUserInfo(
				this, pointsxtNick,
				pointsxtNick, null, getPlayerRank(ircNick),
				0, 0, 0, extractUserStatus(ircNick));
		if (room.equals(defaultChannel)) {
			// join Lang room
			gui.userJoinedRoom(this, room, pointsxtNick, silent);
		} else {
			// join Game room
			if (ircNick.equalsIgnoreCase("podbot") == false) {
				gui.userJoinedRoom(this, room, pointsxtNick, silent);
			}
		}
		if (room.equals(defaultChannel)) {
			String newRoom = getPlayerRoom(ircNick);
			if (newRoom.length() > 0) {
				User[] users = getUsers(defaultChannel);
				String opponent = "";
				for (User user : users) {
					String possibleOpponent = user.getNick();
					if ((getPlayerRoom(possibleOpponent).equals(newRoom))
							&& (! nicknameManager.getOrCreateShortNick(
							possibleOpponent
					).
							equals(pointsxtNick))) {
						opponent = nicknameManager.getOrCreateShortNick(
								possibleOpponent
						);
					}
				}
				if (opponent.length() > 0) {
					if (getPlayerIngameNumber(ircNick) == 1) {
						gui.gameCreated(
								this, defaultChannel, newRoom,
								pointsxtNick, opponent, getPlayerGameType(
										ircNick
						)
						);
					} else if (getPlayerIngameNumber(ircNick) == 2) {
						gui.gameCreated(
								this, defaultChannel, newRoom,
								opponent, pointsxtNick, getPlayerGameType(
										ircNick
						)
						);
					} else {
						throw new UnsupportedOperationException(
								"error creating a game"
						);
					}
				} else if (isPointsopNickname(ircNick)) {
					// no opponent found of a pointsOp player
					gui.gameVacancyCreated(
							this, defaultChannel, newRoom,
							pointsxtNick, "" + myGame.getDefaultTime() + "сек/ ход"
					);
				}
			}
		}
	}

	public void userDisconnected_PointsxtStyle(
			String room,
			String user) {
		if (room.equals(defaultChannel)) {
			gui.userLeftRoom(
					this, room,
					nicknameManager.getOrCreateShortNick(user)
			);
			clearCreatedGames_PointsxtStyle(user);
			nicknameManager.removeIrcNick(user);
		} else {
			if (user.equalsIgnoreCase("podbot") == false) {
				String userShort = nicknameManager.getOrCreateShortNick(user);
//				System.out.println("userShort = " + userShort);
//				System.out.println("myNickOnServ = " + myNickOnServ);
				if (userShort.equals(getMyName())) {
					gui.unsubsribedGame(this, room);
				} else {
					gui.userLeftRoom(
							this, room,
							userShort
							);
				}
				if (userShort.
						equals(myGame.getOpponentShortName())
						&& room.equals(myGame.roomName)) {
					myGame.clear();
					gui.chatReceived(this, room, "", "Оппонент покинул игру");
				}
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

				gui.makedMove(this, room, silent, x, y, isRed, !isRed, myGame.getTimeLeftRed(), myGame.getTimeLeftForBlue());
				boolean iHaveMoved = ! myGame.isMyMoveNow();
				if (iHaveMoved) {
					myGame.lastTimeoutThread = null;
				} else if (myGame.moveList.size() >= 2){

					Thread timeOutThread = new Thread() {
//						@SuppressWarnings({"SynchronizationOnLocalVariableOrMethodParameter"})
						@Override
						public void run() {
							long timeEnd = new Date().getTime() + myGame.getTimeLeftForMe() * 1000;
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
								Dot dot = myGame.randomMovesProvider.findEmptyRandomPlace(
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
		} else {
			gui.makedMove(this, room, silent, x, y, isRed, !isRed, myGame.getTimeLeftRed(), myGame.getTimeLeftForBlue());
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
		myGame.makeMove(roomName, x, y, myGame.getDefaultTime());
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
	 * @return GUI name
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

	protected String getSpectr_MainPart() {
		StringBuilder result = new StringBuilder();
		for (SimpleMove simpleMove : myGame.moveList) {
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
		List<SurroundingAbstract> surroundingsList =
				myGame.engine.getSurroundings();
		for (SurroundingAbstract surrounding : surroundingsList) {
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

	protected String getSpectr_CommonPrefix() {
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
		return result.toString();
	}

	public void sendSpectr(String targetIrcNick) {
		String mainSpectrData = getSpectr_MainPart();
		final int blockStep = 260;
		int blockStart = 0;
		while (blockStart < mainSpectrData.length()) {
			int blockEnd = Math.min(mainSpectrData.length(), blockStart + blockStep);
			String blockAsString = mainSpectrData.substring(blockStart, blockEnd);
			super.sendMessage(
					targetIrcNick,
					getSpectr_CommonPrefix() + blockAsString
			);
			blockStart += blockStep;
		}
	}


	class MyGame {

		String roomName = "";
		String opponentName = "";
		boolean amIRed;
		SingleGameEngineInterface engine;
		ArrayList<SimpleMove> moveList = new ArrayList<SimpleMove>();
		Thread lastTimeoutThread = null;
		RandomMovesProvider randomMovesProvider = new RandomMovesProvider(39, 32);
		int timeAllowedPerTurn = 36;

		public String getOpponentIrcName() {
			return nicknameManager.getIrcNick(opponentName);
		}

		public String getOpponentShortName() {
			return opponentName;
		}

		public int getDefaultTime() {
			return this.timeAllowedPerTurn;
		}

		public int getTimeLeftRed() {
			return getDefaultTime();
		}

		public int getTimeLeftForBlue() {
			return getDefaultTime();
		}

		public int getTimeLeftForColor(boolean color) {
			if (color==true) {
				return getTimeLeftRed();
			} else {
				return getTimeLeftForBlue();
			}
		}

		public int getTimeLeftForMe() {
			return getTimeLeftForColor(amIRed);
		}

		private boolean isPlaying() {
			return "".equals(opponentName) == false;
		}

		private boolean isSearching() {
			return roomName != null
					&& roomName.equals("") == false
					&& isPlaying() == false;
		}

		private void clear() {
//			System.out.println("myGame.clear()");
			randomMovesProvider = new RandomMovesProvider(39, 32);
			roomName = "";
			moveList.clear();
			opponentName = "";
			engine = null;
			ServerPointsxt.this.setPointsxtNickname("", false, false);
//			ServerPointsxt.this.changeNick(
//					String.format(
//							"%s_X" + pointsxtVersion + "000000000[free]",
//							ServerPointsxt.this.getMyName()
//					)
//			);
		}

		void leaveGame(boolean isGuiVisible) {
			if (isPlaying() || isSearching()) {
				String roomNameCopy = roomName;
				ServerPointsxt.this.partChannel(roomNameCopy);
				clear();
				if (isGuiVisible) {
					ServerPointsxt.this.gui.unsubsribedGame(
							ServerPointsxt.this, roomNameCopy
							);
				}
			}
		}

//		void stopSearchingOpponent

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
				int y,
				int timeLeft) {
			if (roomName.equals(this.roomName)) {
				boolean isFirstMoveAllowed = ((moveList.size() >= 2))
						|| ((x - 1 >= 12) && (x - 1 <= 19)
						&& (32 - y >= 12) && (32 - y <= 26)); // 12<=x<=19, 12<=y<=26
				if (isMyMoveNow()
						&& isFirstMoveAllowed
						&& engine.getDotType(x, y).isEmpty()) {
					ServerPointsxt.this.sendMoveToGui(
							roomName, false, x, y,
							amIRed
					);
					String timeLeftAsString = String.format("%03d", timeLeft);
					ServerPointsxt.this.sendMessage(
							roomName,
							"" + (char) ('0' + x - 1)
									+ (char) ('0' + 32 - y)
									+ timeLeftAsString
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
		IrcNick2ShortNick.remove(oldIrcNick);

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
