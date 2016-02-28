package ru.narod.vn91.pointsop.server;

import ru.narod.vn91.pointsop.data.GameOuterInfo.GameState;
import ru.narod.vn91.pointsop.data.TimeLeft;
import ru.narod.vn91.pointsop.data.TimeSettings;
import ru.narod.vn91.pointsop.model.GuiForServerInterface;
import ru.narod.vn91.pointsop.model.StartingPosition;
import ru.narod.vn91.pointsop.server.zagram.MessageQueue;
import ru.narod.vn91.pointsop.utils.Function;
import ru.narod.vn91.pointsop.utils.Sha1;
import ru.narod.vn91.pointsop.utils.Wait;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

public class ServerZagram implements ServerInterface {

	final String myNameOnServer;
	final boolean isPassworded;
	final boolean isInvisible;
	final GuiForServerInterface gui;
	final String secretId;
	volatile boolean isDisposed = false;
	final MessageQueue queue = new MessageQueue(10);
	final List<String> transiendQueue = new ArrayList<>(8);

	volatile boolean isBusy = false;
	Set<String> personalInvitesIncoming = new LinkedHashSet<>(8);
	Set<String> personalInvitesOutgoing = new LinkedHashSet<>(8);
	Set<String> subscribedRooms = new LinkedHashSet<>(8);
	final Map<String, Set<String>> playerRooms = new HashMap<>(8);
	ThreadMain threadMain;

	final Map<String, String> avatarUrls = new HashMap<>(8);
	final Map<String, ImageIcon> avatarImages = new HashMap<>(8);

	public ServerZagram(GuiForServerInterface gui, String myNameOnServer, String password, boolean isInvisible) {

		if (myNameOnServer.matches(".*[a-zA-Z].*")) {
			myNameOnServer = myNameOnServer.replaceAll("[^a-zA-Z0-9 ]", "");
		} else if (myNameOnServer.matches(".*[ёа-яЁА-Я].*")) {
			myNameOnServer = myNameOnServer.replaceAll("[^ёа-яЁА-Я0-9 ]", "");
		} else if (myNameOnServer.matches(".*[a-żA-Ż].*")) {
			myNameOnServer = myNameOnServer.replaceAll("[^a-żA-Ż0-9 ]", "");
		} else {
			myNameOnServer = myNameOnServer.replaceAll("[^0-9 ]", "");
		}
		isPassworded = !myNameOnServer.equals("") && !password.equals("");
		this.isInvisible = isInvisible;
		if (isPassworded) {
			this.myNameOnServer = myNameOnServer;
			this.gui = gui;
			this.secretId =
					getBase64ZagramVersion(
						Sha1.getSha1(myNameOnServer + "9WB2qGYzzWry1vbVjoSK" + password)).
							substring(0, 10);
		} else {
			if (myNameOnServer.equals("")) {
				myNameOnServer = String.format("Guest%04d", (int) (Math.random() * 9999));
			}
			myNameOnServer = "*" + myNameOnServer;
			this.myNameOnServer = myNameOnServer;
			this.gui = gui;
			Integer secretIdAsInt = (int) (Math.random() * 999999);
			secretId = secretIdAsInt.toString();
		}
	}

	class ZagramGameType {
		final int fieldX, FieldY;
		final boolean isStopEnabled, isEmptyScored;
		final int timeStarting, timeAdditional;
		final boolean isRated;
		final int instantWin;

		public ZagramGameType(int fieldX, int fieldY, boolean isStopEnabled, boolean isEmptyScored, int timeStarting, int timeAdditional,
				String startingPosition, boolean isRated, int instantWin) {
			this.fieldX = fieldX;
			this.FieldY = fieldY;
			this.isStopEnabled = isStopEnabled;
			this.isEmptyScored = isEmptyScored;
			this.timeStarting = timeStarting;
			this.timeAdditional = timeAdditional;
			this.isRated = isRated;
			this.instantWin = instantWin;
		}
	}

	private static String getGameTypeString(
			int fieldX, int fieldY, int startingTime, int periodAdditionalTime, boolean isRanked, StartingPosition startPos) {
		return String.format("%s%s%s%s.%s.%s.%s.%s.%s.%s.%s.",
			fieldX, fieldY,
			"n", // no territory
			"0", // instant win disabled
			(startPos == StartingPosition.CLEAN) ? "0" :
				(startPos == StartingPosition.ONE_CROSS) ? "1" : "4",
			"a", // who starts (you)
			isRanked ? "R" : "F",
			"a", // infinite time? (no)
			startingTime,
			periodAdditionalTime,
			"_SRATP_sratp" // capabilities (no undo, can add time, can pause time)
		);
	}

	private ZagramGameType getZagramGameType(String str) {
		// 2525noT4R0.180.15  or  "3932noT1r0.180.20_PWC 2012"
		String[] dotSplitted = str.split("\\.");
		try {
		    if (str.charAt(5) == 't' || str.charAt(5) == 'e' || str.charAt(5) == 'o') {  // old type
			int startingTime = Integer.parseInt(dotSplitted[1]);
			int addTime = Integer.parseInt(dotSplitted[2].split("_")[0]);
			String hellishString = dotSplitted[0];
			int sizeX = Integer.parseInt(hellishString.substring(0, 2));
			int sizeY = Integer.parseInt(hellishString.substring(2, 4));
			String rulesAsString = hellishString.substring(4, 8);
			boolean isStopEnabled = rulesAsString.matches("noT4|noT1");
			boolean isEmptyScored = rulesAsString.matches("terr|stan");
			// boolean manualEnclosings = rulesAsString.matches("terr");
			// boolean stopEnabled = rulesAsString.matches("noT4|noT1");
			String startingPosition = rulesAsString.replaceAll("no|terr|stan", "");
			boolean isRated = !(hellishString.substring(8, 9).equals("F"));
			Integer instantWin = Integer.parseInt(hellishString.substring(9));
			return new ZagramGameType(
				sizeX, sizeY, isStopEnabled, isEmptyScored,
				startingTime, addTime,
				startingPosition,
				isRated, instantWin);
		    } else {
			// new format: XXYYrI.(starting position).(who starts).(type).(time method).t0.t1.opt.tourn
                        int startingTime = (dotSplitted[4].charAt(0) == 'a') ? Integer.parseInt(dotSplitted[5]) : 0;
                        int addTime = (dotSplitted[4].charAt(0) == 'a') ? Integer.parseInt(dotSplitted[6]) : 0;
                        String hellishString = dotSplitted[0];
			int sizeX = Integer.parseInt(hellishString.substring(0, 2));
                        int sizeY = Integer.parseInt(hellishString.substring(2, 4));
                        String rulesAsString = hellishString.substring(4, 5);
			boolean isStopEnabled = rulesAsString.matches("n");
			boolean isEmptyScored = rulesAsString.matches("t|s");
			String startingPosition = dotSplitted[1];
                        // boolean manualEnclosings = rulesAsString.matches("t");
			// boolean stopEnabled = rulesAsString.matches("n");
			boolean isRated = (dotSplitted[3].charAt(0) == 'R');
                        Integer instantWin = Integer.parseInt(hellishString.substring(5));
                        return new ZagramGameType(
						  sizeX, sizeY, isStopEnabled, isEmptyScored,
						  startingTime, addTime,
						  startingPosition,
						  isRated, instantWin);
		    }
		} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getBase64ZagramVersion(String input) {
		if (input.length() % 3 == 2) {
			input = input + "0";
		} else if (input.length() % 3 == 1) {
			input = input + "00";
		}
		final char[] base64ZagramStyle = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_.".toCharArray();

		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i + 2 < input.length(); i = i + 3) {
			// see intersections between groups:
			// ____----^^^^
			// ++++++======
			int hex0 = Integer.parseInt(input.substring(i, i + 1), 16);
			int hex1 = Integer.parseInt(input.substring(i + 1, i + 2), 16);
			int hex2 = Integer.parseInt(input.substring(i + 2, i + 3), 16);
			int base64_1 = hex0 * 4 + hex1 / 4;
			int base64_2 = (hex1 & 3) * 16 + hex2;
			stringBuilder.append(base64ZagramStyle[base64_1]);
			stringBuilder.append(base64ZagramStyle[base64_2]);
		}
		return stringBuilder.toString();
	}

	@Override
	public void connect() {
		Thread threadStartup = new Thread() {
			@Override
			public void run() {
				if (isInvisible) {
					// do not authorize
					threadMain = new ThreadMain();
					threadMain.start();
				} else {
					gui.rawConnectionState(ServerZagram.this, "Подключение...");

					String authorizationURL;
					if (isPassworded) {
						authorizationURL = "http://zagram.org/auth.py?co=loguj&opisGracza=" +
							getServerEncoded(myNameOnServer) +
							"&idGracza=" +
							secretId +
							"&lang=ru";
					} else {
						authorizationURL = "http://zagram.org/a.kropki?co=guestLogin&idGracza=" +
							secretId + "&opis=" +
							getServerEncoded(myNameOnServer) + "&lang=ru";
					}

					String authorizationResult = getLinkContent(authorizationURL);
					boolean isAuthorized;
					if (authorizationResult.equals("")) {
						gui.rawConnectionState(ServerZagram.this, "Соединился. Подключаюсь к основной комнате...");
						isAuthorized = true;
					} else if (authorizationResult.startsWith("ok.zalogowanyNaSerwer.")) {
						gui.rawConnectionState(ServerZagram.this,
							"Авторизовался (" + myNameOnServer + "). Подключаюсь к основной комнате...");
						isAuthorized = true;
					} else {
						gui.rawConnectionState(ServerZagram.this, "Ошибка авторизации! Возможно, вы ввели неправильный пароль.");
						isAuthorized = false;
					}

					if (isAuthorized) {
						getLinkContent("http://zagram.org/a.kropki?idGracza=" + secretId + "&co=changeLang&na=ru");

						final Thread disconnectThread = new Thread() {
							@Override
							public void run() {
								disconnectServer();
							}
						};
						Thread killUltimatively = new Thread() {
							@Override
							public void run() {
								Wait.waitExactly(1000L);
								disconnectThread.interrupt();
							}
						};
						killUltimatively.setDaemon(true);
						Runtime.getRuntime().addShutdownHook(killUltimatively);
						Runtime.getRuntime().addShutdownHook(disconnectThread);
						threadMain = new ThreadMain();
						threadMain.start();
					}
				}
			}
		};
		threadStartup.setDaemon(true);
		threadStartup.setPriority(Thread.MIN_PRIORITY);
		threadStartup.setName("zagramThread");
		threadStartup.start();
	}

	@Override
	public void disconnectServer() {
		this.isDisposed = true;

		final Thread urlInformer = new Thread() {
			@Override
			public void run() {
				getLinkContent("http://zagram.org/a.kropki?playerId=" +
					secretId + "&co=usunGracza");
			}
		};
		urlInformer.start();

		Thread killer = new Thread() {
			@Override
			public void run() {
				Wait.waitExactly(1000L);
				urlInformer.interrupt();
			}
		};
		killer.start();
	}

	@Override
	public String getMainRoom() {
		return "0";
	}

	@Override
	public String getMyName() {
		return myNameOnServer;
	}

	@Override
	public String getServerName() {
		return "zagram.org";
	}

	@Override
	public int getMaximumMessageLength() {
		return 100;
	}

	@Override
	public void makeMove(String roomName, int x, int y) {
		String message = "s" + queue.sizePlusOne() +
			"." + roomName + "." + coordinatesToString(x, y);
		sendCommandToServer(message);
	}

	@Override
	public void askGameVacancyPlay(String gameRoomName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void acceptGameVacancyOpponent(String roomName, String newOpponent) {
		gui.raw(this, "MultiPoints пока-что не умеет оставлять заявки на игру на этом сервере..");
	}

	@Override
	public void rejectGameVacancyOpponent(String roomName, String notWantedOpponent) {
		gui.raw(this, "MultiPoints пока-что не умеет оставлять заявки на игру на этом сервере..");
	}

	@Override
	public void acceptPersonalGameInvite(String playerId) {
		String message = "v" + queue.sizePlusOne() +
			"." + getMainRoom() + "." + "a";
		sendCommandToServer(message);
	}

	@Override
	public void cancelPersonalGameInvite(String playerId) {
		String message = "v" + queue.sizePlusOne() +
			"." + getMainRoom() + "." + "c";
		sendCommandToServer(message);
		gui.youCancelledPersonalInvite(this, playerId, playerId + "@outgoing");
	}

	@Override
	public void rejectPersonalGameInvite(String playerId) {
		if (personalInvitesIncoming.contains(playerId)) {
			String message = "v" + queue.sizePlusOne() +
				"." + getMainRoom() + "." + "r";
			sendCommandToServer(message);
		}
	}

	@Override
	public void addPersonalGameInvite(String playerId, TimeSettings time, int fieldX, int fieldY, boolean isRanked, StartingPosition startingPosition) {
		String msgToSend =
				"v" + queue.sizePlusOne() + "." + "0"/* room# */+ "." +
					"s." + getServerEncoded(playerId) + "." +
					getGameTypeString(fieldX, fieldY, time.starting, time.periodAdditional, isRanked, startingPosition);
		sendCommandToServer(msgToSend);
		gui.updateGameInfo(this, playerId + "@outgoing", getMainRoom(), getMyName(), null,
			fieldX, fieldY, false, false, 0, 0, false,
			false, false, null, 0, time.periodAdditional, time.starting, 1, playerId);
	}

	@Override
	public void createGameVacancy() {
		gui.rawError(this, "невозможно оставлять заявки на игру на этом сервере");
	}

	@Override
	public void sendChat(String room, String message) {
		String msgToSend = "c" + queue.sizePlusOne() + "." + room + "."
			+ getServerEncoded(message);
		sendCommandToServer(msgToSend);
	}

	@Override
	public void sendPrivateMsg(String target, String message) {
		String msgToSend = "c" + queue.sizePlusOne() + "._" + getServerEncoded(target) + "." + getServerEncoded(message);
		sendCommandToServer(msgToSend);
	}

	@Override
	public void stopGameVacancy() {
		gui.rawError(this, "невозможно оставлять заявки на игру на этом сервере");
	}

	@Override
	public void subscribeRoom(String room) {
		String msgToSend = "b" + queue.sizePlusOne() + "." + room + ".";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void unsubscribeRoom(String room) {
		String msgToSend = "q" + queue.sizePlusOne() + "." + room + ".";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void setStatus(boolean isBusy) {
		this.isBusy = isBusy;
		sendCommandToServerTransient("i0nJ" + (isBusy ? "b" : "a"));
		gui.statusSet(this, isBusy);
	}

	@Override
	public void surrender(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "resign";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void stop(String roomId) {
		getLinkContent("http://zagram.org/a.kropki?idGracza=" + secretId +
			"&co=stopMove&stol=" + roomId);
	}

	@Override
	public void askNewGame(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "new";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void cancelAskingNewGame(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "cancel1new";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void acceptNewGame(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "new"; // same as ask
		sendCommandToServer(msgToSend);
	}

	@Override
	public void rejectNewGame(String roomId) {
		// cannot reject, just ignore
	}

	@Override
	public void askEndGameAndScore(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "score";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void cancelAskingEndGameAndScore(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "cancel1score";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void acceptEndGameAndScore(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "accept2score";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void rejectEndGameAndScore(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "reject2score";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void askUndo(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "undo";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void cancelAskingUndo(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "cancel1undo";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void acceptUndo(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "accept2undo";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void rejectUndo(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "reject2undo";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void askDraw(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "draw";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void cancelAskingDraw(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "cancel1draw";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void acceptDraw(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "accept2draw";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void rejectDraw(String roomId) {
		String msgToSend = "u" + queue.sizePlusOne() + "." + roomId + "." + "reject2draw";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void pauseOpponentTime(String roomId) {
		String msgToSend = "t" + queue.sizePlusOne() + "." + roomId + "." + "p";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void unpauseOpponentTime(String roomId) {
		String msgToSend = "t" + queue.sizePlusOne() + "." + roomId + "." + "u";
		sendCommandToServer(msgToSend);
	}

	@Override
	public void addOpponentTime(String roomId, int seconds) {
		getLinkContent("http://zagram.org/a.kropki" +
			"?idGracza=" + secretId +
			"&co=dodajeCzas&ile=" + seconds +
			"&stol=" + roomId);
	}

	private static synchronized String getLinkContent(String link) {
		StringBuilder result = new StringBuilder();
		try {
			URL url;
			URLConnection urlConn;
			InputStreamReader inStream;
			url = new URL(link);
			urlConn = url.openConnection();
			inStream = new InputStreamReader(
				urlConn.getInputStream(), "UTF-8");
			BufferedReader buff = new BufferedReader(inStream);
			while (true) {
				String nextLine;
				nextLine = buff.readLine();
				if (nextLine != null) {
					result.append(nextLine);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// if (Settings.isDebug()) {
		System.out.println("GET " + link + "  -->  " + result.toString());
		// }
		return result.toString();
	}

	private void sendCommandToServer(String message) {
		queue.add(message);
	}

	private void sendCommandToServerTransient(String message) {
		transiendQueue.add(message);
	}

	private static String get1SgfCoord(int i) {
		if (i <= 26) {
			return Character.toString((char) ('a' + i - 1));
		} else {
			return Character.toString((char) ('A' + i - 26 - 1));
		}
	}

	private static String coordinatesToString(int x, int y) {
		return get1SgfCoord(x) + get1SgfCoord(y);
	}

	private static Integer charToCoordinate(char c) {
		if (c >= 'a' && c <= 'z') {
			return Integer.class.cast(c - 'a' + 1);
		} else if (c >= 'A' && c <= 'Z') {
			return Integer.class.cast(c - 'A' + 27);
		} else {
			return null;
		}
	}

	private static Point stringToCoordinates(String twoLetterString) {
		if (twoLetterString.length() >= 2) {
			try {
				char xAsChar = twoLetterString.charAt(0);
				char yAsChar = twoLetterString.charAt(1);
				return new Point(
					charToCoordinate(xAsChar),
					charToCoordinate(yAsChar));
			} catch (NullPointerException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}

	private static String getServerEncoded(String s) {
		// the order of replacing matters
		s = s.replaceAll("@", "@A");
		s = s.replaceAll("/", "@S");
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}

	private static String getServerDecoded(String s) {
		// the order of replacing matters
		return s
				.replaceAll("@S", "/")
				.replaceAll("@A", "@")
				.replaceAll("&#60;", "<")
				.replaceAll("&#62;", ">")
				.replaceAll("&#39;", "'")
				.replaceAll("&#34;", "\"")
				.replaceAll("&#45;", "-");
	}

	private class ThreadMain extends Thread {

		final ServerInterface server = ServerZagram.this;
		Set<String> personalInvitesIncomingNew = new LinkedHashSet<>();
		Set<String> personalInvitesOutgoingNew = new LinkedHashSet<>();
		Set<String> modifiedSubscribedRooms = new HashSet<>();
		int lastSentCommandNumber = 0;
		int lastServerMessageNumber = 0;
		String currentRoom = "";

		@Override
		public void run() {
			sendCommandToServerTransient("i0nJ" + (isBusy ? "b" : "a"));

			while (isDisposed == false) {

				String commands = "";

				for (String message : transiendQueue) {
					commands = commands + message + "/";
				}
				transiendQueue.clear();

				for (int i = lastSentCommandNumber + 1; i < queue.size() + 1; i++) {
					// (non-standard interval. Here is: A < x <= B)
					commands = commands + queue.get(i) + "/";
				}
				lastSentCommandNumber = queue.size();

				if (commands.equals("")) {
					commands = "x";
				} else {
					commands = commands.substring(0, commands.length() - 1);
				}
				String text = getLinkContent(
						"http://zagram.org/a.kropki" +
							"?playerId=" + secretId +
							"&co=getBMsg" +
							"&msgNo=" + lastServerMessageNumber + "." + lastServerMessageNumber +
							"&msgFromClient=" + commands);
				handleText(text);

				Wait.waitExactly(1000L);
			}
		}

		private String nonEmpty(String s) {
			if (s == null || s.isEmpty()) return null; else return s;
		}

		private void handlePlayerInfo(String message) {
			String player = null, myStatus = null;
			Integer rating = null, winCount = null, lossCount = null, drawCount = null;
			try {
				String[] dotSplitted = message.substring("i".length()).split("\\.");
				player = nonEmpty(dotSplitted[0]);
				String avatar = nonEmpty(dotSplitted[1]);
				avatarUrls.put(player, avatar);
				String status = (dotSplitted[2].matches(".*(N|b).*") ? "" : "free");
				String language = nonEmpty(dotSplitted[3]);
				myStatus = language + " (" + status + ")";
				rating = Integer.parseInt(dotSplitted[4]);
				winCount = Integer.parseInt(dotSplitted[5]);
				lossCount = Integer.parseInt(dotSplitted[7]);
				drawCount = Integer.parseInt(dotSplitted[6]);
			} catch (Exception ignore) {
				// Hack-ish way quit on the first error.
				// The `finally` clause is aware of nulls and can handle them
			} finally {
				gui.updateUserInfo(server, player, player, null, rating,
					winCount, lossCount, drawCount, myStatus, null);
			}
		}

		private void handleGameFlags(String message) {
			String[] underlineSplit = message.substring("f".length()).split("_");
			final String timeLimitsAsString;
			if (underlineSplit.length >= 2) {
				timeLimitsAsString = message.split("_")[1];
			} else {
				timeLimitsAsString = "";
			}
			if (timeLimitsAsString.equals("")) {
				// no limits
			} else {
				String part1 = timeLimitsAsString.split("\\.")[0];
				String part2 = timeLimitsAsString.split("\\.")[1];
				boolean paused1 = part1.startsWith("p");
				boolean paused2 = part2.startsWith("p");
				Integer time1 = Integer.parseInt(part1.replaceFirst("p", ""));
				Integer time2 = Integer.parseInt(part2.replaceFirst("p", ""));
				// Unfortunately, the paused state can't be use.
				// This is because the GUI is very stupid and doesn't know who's turn it is now,
				// and if we pass him "not paused", he will start counting down time for the player that doesn't move.
				gui.timeUpdate(server, currentRoom, new TimeLeft(time1, time2, null, null));
			}
		}


		private synchronized void handleText(String text) {
			// if (text.startsWith("ok/") && text.endsWith("/end")) {
			if ((text.startsWith("ok") && text.endsWith("end"))
				|| (text.startsWith("sd") && text.endsWith("end") && isInvisible)) {
				String[] split = text.split("/");

				personalInvitesIncomingNew = new LinkedHashSet<>();
				personalInvitesOutgoingNew = new LinkedHashSet<>();
				modifiedSubscribedRooms = new HashSet<>();
				currentRoom = "";

				for (String message : split) {
					try {
						handleMessage(message);
					} catch (Exception e) {
						System.out.println(ServerZagram.class.getName() +
							" failed to parse the message " + message +
							". Exception below...");
						e.printStackTrace();
					}
				}

				{
					// warning: UGLY CODE
					// now I have to compare two sets of player invitations
					// if I don't have a player in the new set -
					// then the invitation is closed and I should delete it.
					for (String player : personalInvitesIncoming) {
						if (personalInvitesIncomingNew.contains(player) == false) {
							// invitation cancelled
							gui.personalInviteCancelled(server, player, player + "@incoming");
							sendCommandToServerTransient("i0nJa");
						}
					}
					personalInvitesIncoming = personalInvitesIncomingNew;
				}
				{
					for (String player : personalInvitesOutgoing) {
						if (personalInvitesOutgoingNew.contains(player) == false) {
							// invitation cancelled
							gui.yourPersonalInviteRejected(server, player, player + "@outgoing");
						}
					}
					personalInvitesOutgoing = personalInvitesOutgoingNew;
				}
				{
					for (String room : subscribedRooms) {
						if (modifiedSubscribedRooms.contains(room) == false) {
							// unsubscribed room
							gui.unsubscribedRoom(server, room);
						}
					}
					subscribedRooms = modifiedSubscribedRooms;
				}

			} else if (text.equals("")) {
				// we got an empty result. Well, lets treat this as normal.
			} else {
				gui.rawError(server, "error handling server response, exiting. Technical details: message: " + text);
				gui.serverClosed(server);
				isDisposed = true;
			}
		}


		private void handleMessage(String message) {
			if (message.startsWith("b")) { // room subscriptions
				// b*Вася.0.234.1234.1451.21
				String player = message.replaceAll("\\..*", "").substring(1);
				String[] dotSplit = message.replaceFirst(".*?\\.", "").split("\\.");

				final Set<String> newRooms = new LinkedHashSet<>(10);
				Collections.addAll(newRooms, dotSplit);

				final Set<String> oldRooms = playerRooms.get(player);
				playerRooms.put(player, newRooms);

				if (oldRooms == null) {
					for (String room : newRooms) {
						gui.userJoinedRoom(server, room, player, true);
					}
				} else {
					for (String room : oldRooms) {
						if (newRooms.contains(room) == false) {
							gui.userLeftRoom(server, room, player, "");
						}
					}
					for (String room : newRooms) {
						if (oldRooms.contains(room) == false) {
							gui.userJoinedRoom(server, room, player, false);
						}
					}
				}
			}
			else if (message.startsWith("ca") || message.startsWith("cr")) { // chat
				String[] dotSplitted = message.substring("ca".length())
					.split("\\.", 4);
				try {
					String timeString = dotSplitted[0].replaceAll("[^0-9].*","");
					long time = Long.parseLong(timeString) * 1000L;
					String nick = dotSplitted[1];
					String chatMessage = getServerDecoded(dotSplitted[3]);
					if (nick.contains("-")) {
						// this is private message
						String[] nicks = nick.split("-");
						String nickType = dotSplitted[2];
						// who may be 0,1,2
						// or -, nochat, del (which would raise an exception)
						Integer who = Integer.parseInt(nickType);
						if (who == 0) {
							nick = ""; // message from the server
						}
						else if (who == 1 || who == 2) {
							nick = nicks[who - 1];
						}
						String withWhom = nicks[0].equals(myNameOnServer) ? nicks[1] : nicks[0];
						// I talk 'withWhom', received a message from 'nick'
						// there's a problem receiving messages from myself...
						if (nick.equalsIgnoreCase(myNameOnServer)) {
							gui.privateMessageReceived(server, withWhom, getMyName(), chatMessage);
						}
						else {
							gui.privateMessageReceived(server, withWhom, withWhom, chatMessage);
						}
					}
					else {
						gui.chatReceived(server,
							currentRoom, nick, chatMessage, time);
					}
				} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					gui.raw(server, "unknown chat: " + message.substring(2));
				}
			}
			else if (message.startsWith("d")) { // game description

				// first section and last two sections
				String suffecientPart = message.
					replaceFirst("[^.]*.", "").
					replaceFirst("\\.[^.]*$", "").
					replaceFirst("\\.[^.]*$", "");
				ZagramGameType gameType = getZagramGameType(suffecientPart);

				String[] dotSplitted = message.split("\\.");
				String roomId = dotSplitted[0].replaceFirst("d", "");
				String player1 = dotSplitted[dotSplitted.length - 2];
				String player2 = dotSplitted[dotSplitted.length - 1];
				gui.updateGameInfo(
					server, roomId,
					currentRoom, player1, player2,
					gameType.fieldX, gameType.FieldY,
					false, gameType.isRated, 0, gameType.instantWin,
					false, gameType.isStopEnabled, false, null, 0,
					gameType.timeAdditional, gameType.timeStarting, 1,
					null);
			} else if (message.startsWith("f")) {
				handleGameFlags(message.replaceFirst("f", ""));
			} else if (message.startsWith("ga") || message.startsWith("gr")) { // +game
				String[] dotSplitted = message.substring("ga".length()).split("\\.");
				for (String gameId : dotSplitted) {
					if (gameId.length() != 0) {
						gui.gameRowCreated(server, currentRoom, gameId);
					}
				}
			} else if (message.startsWith("gd")) { // - game
				String[] dotSplitted = message.substring("gd".length())
					.split("\\.");
				for (String gameId : dotSplitted) {
					if (gameId.length() != 0) {
						gui.gameRowDestroyed(server, gameId);
					}
				}
			} else if (message.startsWith("h")) { // additional flags
			} else if (message.startsWith("i")) { // player info
				handlePlayerInfo(message);
			} else if (message.startsWith("m")) { // message numbers info
				// try {
				String tail = message.substring(1);
				String[] dotSplitted = tail.split("\\.");
				String a1 = dotSplitted.length >= 1 ? dotSplitted[0] : "";
				String a2 = dotSplitted.length >= 2 ? dotSplitted[1] : "";
				String a3 = dotSplitted.length >= 3 ? dotSplitted[2] : "";
				int i1 = Integer.parseInt(a1);
				int i2 = Integer.parseInt(a2);
				int i3 = Integer.parseInt(a3);
				if (i1 > lastServerMessageNumber + 1) {
					// don't thow exceptions -- we
					// want to stay alive in case of a server restart.
				}
				lastServerMessageNumber = i2;
				lastSentCommandNumber = i3;
			} else if (message.startsWith("pa") || message.startsWith("pr")) { // player joined
				String[] dotSplitted = message.substring("pa".length()).split("\\.");
				for (String player : dotSplitted) {
					if (player.length() != 0) {
						gui.userJoinedRoom(
							server, currentRoom, player, message.startsWith("pr"));
					}
				}
			} else if (message.startsWith("pd")) { // - player
				String[] dotSplitted = message.substring("pd".length())
					.split("\\.");
				for (String player : dotSplitted) {
					if (player.length() != 0) {
						gui.userLeftRoom(server, currentRoom, player, "");
					}
				}
			} else if (message.startsWith("q")) { // current room
				String room = message.substring(1);
				currentRoom = room;

				modifiedSubscribedRooms.add(room);
				if (subscribedRooms.contains(room)) {
					// old set
				} else {
					if (currentRoom.equals("0")) {
						gui.subscribedLangRoom(currentRoom, server, "общий чат: zagram", true);
					} else {
						gui.subscribedGame(server, currentRoom);
						for (Map.Entry<String, Set<String>> entry : playerRooms.entrySet()) {
							// we need to send all users in this room because we knew them
							// even before subscribing to the room (zagram specifics)
							if (entry.getValue().contains(currentRoom)) {
								gui.userJoinedRoom(server, currentRoom, entry.getKey(), true);
							}
						}
					}
				}
			} else if (message.matches("sa.*|sr.*")) { // game actions
				message = message.replaceAll("\\(|\\)", "");
				class FatalGameRoomError extends Exception {
					public FatalGameRoomError(String s) {
						super(s);
					}
				}
				try {
					String usefulPart = message.replaceFirst("sa;|sr;", "");
					String[] semiSplitted = usefulPart.split(";");
					for (String sgfNode : semiSplitted) {
						// ([A-Z]{1,2}\[([^\]|\\\\)*?\])*
						// ([A-Z]{1,2}\[.*?\])*
						if (sgfNode.matches("([A-Z]{1,2}\\[.*\\])*") == false) {
							gui.raw(server, "unknown message structure: " + message);
						} else {
							String lastMovePropertyName = "";
							String[] sgfPropertyList = sgfNode.split("\\]");
							for (String sgfProperty : sgfPropertyList) {
								String propertyName = sgfProperty.replaceAll("\\[.*", "");
								String propertyValue = sgfProperty.replaceFirst(".*\\[", "");
								if (propertyName.matches("U(B|W)")) {
									throw new FatalGameRoomError("UNDO is unsupported");
								} else if (propertyName.matches("B|W|AB|AW|")) {
									boolean isWhite = propertyName.matches("W|AW")
										|| (propertyName.equals("") && lastMovePropertyName.matches("A|AW"));
									if (!propertyName.equals(""))
										lastMovePropertyName = propertyName;
									gui.makedMove(
										server, currentRoom,
										message.startsWith("sr"),
										stringToCoordinates(propertyValue).x,
										stringToCoordinates(propertyValue).y,
										isWhite, !isWhite
									);
								}
							}
						}
					}
					gui.makedMove(server, currentRoom, false, -1, -1, true, false);
				} catch (FatalGameRoomError e) {
					// server.unsubscribeRoom(currentRoom);
					gui.raw(server, "ERROR in game room '" +
						e.getMessage() +
						"'. The game position is not guaranteed to be correct in this game for now on.");
				}
			} else if (message.matches("u.undo")) {
				boolean isRed = message.charAt(1) == '1';
				String playerAsString = isRed ? "red" : "blue";
				gui.chatReceived(
					server,
					currentRoom, "",
					"player " + playerAsString + " Запрос на 'undo'. Клиент MultiPoints " +
						"пока-что не умеет обрабатывать этот вызов.:(",
					null);
			} else if (message.startsWith("vg")) { // game invite
				String usefulPart = message.substring(2);
				String sender = usefulPart.replaceAll("\\..*", ""); // first part
				if (personalInvitesIncoming.contains(sender) == false) {
					String gameDescription = usefulPart.replaceFirst("[^.]*\\.", ""); // other
					ZagramGameType gameType = getZagramGameType(gameDescription);
					if (isBusy == true) {
						personalInvitesIncoming.add(sender);
						server.rejectPersonalGameInvite(sender);
						personalInvitesIncoming.remove(sender); // kind of a hack
					}
					else if (gameType.isEmptyScored == true) {

						personalInvitesIncoming.add(sender);
						server.rejectPersonalGameInvite(sender);
						personalInvitesIncoming.remove(sender); // kind of a hack

						ServerZagram.this.sendPrivateMsg(sender, "Sorry, my game client does not support \"territory\" rules.");
						gui.raw(server, String.format(
							"Игрок '%s' вызвал(а) тебя на игру: " +
								"К сожалению, принять заявку невозможно, " +
								"т.к. польские правила с ручными обводами территории " +
								"пока-что не поддерживаются программой. " +
								"Отослан отказ от игры. ",
							sender));
					} else {
						gui.updateGameInfo(
							server, sender + "@incoming", currentRoom,
							sender, null,
							gameType.fieldX, gameType.FieldY, false,
							gameType.isRated, 0,
							gameType.instantWin, false, gameType.isStopEnabled, false,
							GameState.SearchingOpponent,
							0, gameType.timeAdditional, gameType.timeStarting, 1,
							sender + "to YOU");
						gui.personalInviteReceived(server, sender, sender + "@incoming");
						personalInvitesIncomingNew.add(sender);
					}
				} else {
					personalInvitesIncomingNew.add(sender);
				}
			} else if (message.startsWith("vr")) {
				String user = message.substring(2);
				gui.yourPersonalInviteRejected(server, user, user + "@outgoing");

				// "OK to the fact that someone rejected your invitation" :-/
				sendCommandToServer("v" + queue.sizePlusOne() + "." + "0" + "." + "o");
			} else if (message.startsWith("vs")) {
				String user = message.substring(2).split("\\.")[0];
				if (personalInvitesOutgoing.contains(user) == false) {
					gui.yourPersonalInviteSent(server, user, user + "@outgoing");
				}
				personalInvitesOutgoingNew.add(user);
			}
		}

	}

	@Override
	public String coordinatesToString(Integer xOrNull, Integer yOrNull) {
		Function<Integer, String> getGuiX = new Function<Integer, String>() {
			@Override
			public String call(Integer i) {
				if (i <= 8) {
					// "a" .. "h"
					return Character.toString((char) ('a' + i - 1));
				} else if (i > 8 && i <= 25) {
					// "j" .. "z"
					// letter "i" is skipped in zagram coordinates
					return Character.toString((char) ('a' + i));
				} else if (i > 25 && i <= 25 + 8) {
					// "A" .. "H"
					return Character.toString((char) ('A' + i - 26));
				} else if (i > 25 + 8) {
					// "J" .. "Z"
					return Character.toString((char) ('A' + i - 26 + 1));
				} else {
					return "";
				}
			}
		};

		if (xOrNull != null && yOrNull != null) {
			return String.format("%s%s", getGuiX.call(xOrNull), yOrNull);
		} else if (xOrNull != null) {
			return String.format("%s", getGuiX.call(xOrNull));
		} else if (yOrNull != null) {
			return String.format("%s", yOrNull);
		} else {
			return "";
		}
	}

	public void getUserInfoText(String user) {
	}

	public void getUserpic(String user) {
		try {
			if (avatarImages.get(user) != null) {
			} else if (avatarUrls.get(user) != null
			) {
				URL url;
				if (avatarUrls.get(user).equals("") ||
					avatarUrls.get(user).equals("0")) {
					url = new URL("http://zagram.org/awatar2.png");
				} else {
					url = new URL("http://zagram.org/awatary/" + avatarUrls.get(user) + ".png");
				}
				// URL url = new URL("http://www.citilink.com/~grizzly/anigifs/bear.gif"); // animation
				ImageIcon imageIcon = new ImageIcon(url);
				gui.updateUserInfo(this, user, null, imageIcon, null, null, null, null, null, null);
			}
		} catch (Exception ignored) {
		}
	}

	@Override
	public boolean isIncomingYInverted() {
		return true;
	}

	@Override
	public boolean isGuiYInverted() {
		return false;
	}

	@Override
	public boolean isPrivateChatEnabled() {
		return true;
	}

	@Override
	public boolean isPingEnabled() {
		return false;
	}

	@Override
	public boolean isStopEnabled() {
		return true;
	}

	@Override
	public boolean isSoundNotifyEnabled() {
		return false;
	}

	@Override
	public boolean isField20x20Allowed() {
		return true;
	}

	@Override
	public boolean isField25x25Allowed() {
		return true;
	}

	@Override
	public boolean isField30x30Allowed() {
		return true;
	}

	@Override
	public boolean isField39x32Allowed() {
		return true;
	}

	@Override
	public boolean isStartingEmptyFieldAllowed() {
		return true;
	}

	@Override
	public boolean isStartingCrossAllowed() {
		return true; 
	}

	@Override
	public boolean isStarting4CrossAllowed() {
		return true;
	}

	@Override
	public TimeSettings getTimeSettingsMaximum() {
		return new TimeSettings(120 * 60 /* 120 minutes */, 60, 0, 1, 0);
	}

	@Override
	public TimeSettings getTimeSettingsMinimum() {
		return new TimeSettings(60, 2, 0, 1, 0);
	}

	@Override
	public TimeSettings getTimeSettingsDefault() {
		return new TimeSettings(3 * 60, 15, 0, 1, 0);
	}

	@Override
	public boolean isPrivateGameInviteAllowed() {
		return true;
	}

	@Override
	public boolean isGlobalGameVacancyAllowed() {
		return false;
	}
}
