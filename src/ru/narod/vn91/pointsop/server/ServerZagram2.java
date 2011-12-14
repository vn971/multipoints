package ru.narod.vn91.pointsop.server;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import ru.narod.vn91.pointsop.data.TimeLeft;
import ru.narod.vn91.pointsop.gui.GuiForServerInterface;
import ru.narod.vn91.pointsop.utils.Function;
import ru.narod.vn91.pointsop.utils.Memory;
import ru.narod.vn91.pointsop.utils.Wait;


@SuppressWarnings("serial")
public class ServerZagram2 implements ServerInterface {

	String myNameOnServer;
	GuiForServerInterface gui;
	String currentTable = "";
	String secretId;
	volatile boolean isDisposed = false;
	MessageQueue queue = new MessageQueue(5);

	Map<String,String> avatarUrls = new HashMap<String, String>();
	Map<String,ImageIcon> avatarImages = new HashMap<String, ImageIcon>();

	public ServerZagram2(String myNameOnServer, GuiForServerInterface gui) {
		super();

		if (myNameOnServer.matches(".*[a-zA-Z].*")) {
			myNameOnServer = myNameOnServer.replaceAll("[^a-zA-Z0-9 ]", "");
		} else if (myNameOnServer.matches(".*[ёа-яЁА-Я].*")) {
			myNameOnServer = myNameOnServer.replaceAll("[^ёа-яЁА-Я0-9 ]", "");
		} else if (myNameOnServer.matches(".*[a-żA-Ż].*")) {
			myNameOnServer = myNameOnServer.replaceAll("[^a-żA-Ż0-9 ]", "");
		} else {
			myNameOnServer = myNameOnServer.replaceAll("[^0-9 ]", "");
		}
		// myNameOnServer = myNameOnServer.replaceAll("[^a-zA-Zёа-яЁА-Яa-żA-Ż0-9]",
		// "");
		if (myNameOnServer.equals("")) {
			myNameOnServer = String.format("Guest%04d", (int) (Math.random() * 9999));
		}

		this.myNameOnServer = "*" + myNameOnServer;
		this.gui = gui;

		Integer secretIdAsInt = (int) (Math.random() * 999999);
		secretId = secretIdAsInt.toString();
	}

	@Override
	public void acceptOpponent(String roomName, String newOpponent) {
		gui.raw(this, "MultiPoints пока-что не умеет оставлять заявки на игру на этом сервере..");
	}

	@Override
	public void rejectOpponent(String roomName, String notWantedOpponent) {
		String result =
				getLinkContent("http://zagram.org/a.kropki?idGracza=" +
						secretId + "&co=zaproszenieNie");
		if (result.equals("") == false) {
			gui.rawError(this,
					"Непустой ответ при попытке отказаться от игры: " + result);
		}
	}

	@Override
	public void connect() {
		Thread thread = new Thread() {
			public void run() {
				gui.rawConnectionState(ServerZagram2.this, "Подключение...");
				String authorization = getLinkContent(
						"http://zagram.org/a.kropki?co=guestLogin&idGracza=" +
								secretId + "&opis=" +
								getServerEncoded(myNameOnServer) + "&lang=en");
				if (authorization.equals("")) {
					gui.rawConnectionState(ServerZagram2.this, "Авторизовался. Подключаюсь к основной комнате...");
				} else {
					gui.rawConnectionState(ServerZagram2.this, "Ошибка авторизации!");
				}

				final Thread disconnectThread = new Thread() {
					@Override
					public void run() {
						disconnectServer();
					}
				};
				Thread killUltimatively = new Thread() {
					public void run() {
						// give the "disconnectThread" a little time, and after that kill it
						Wait.waitExactly(1000L);
						disconnectThread.interrupt();
					};
				};
				killUltimatively.setDaemon(true);
				Runtime.getRuntime().addShutdownHook(disconnectThread);
				Runtime.getRuntime().addShutdownHook(killUltimatively);
				new ThreadMain().start();
			};
		};
		thread.setDaemon(true);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.setName("zagramThread");
		thread.start();
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
	public void requestPlay(String gameRoomName) {
		gui.raw(this, "невозможно оставлять заявки на игру на этом сервере");
	}

	@Override
	public void searchOpponent() {
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
		gui.rawError(this, "невозможно писать приватные сообщения на этом сервере");
	}

	@Override
	public void stopSearchingOpponent() {
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
		gui.unsubscribedRoom(this, room);
	}

	@Override
	public void surrender(String roomName) {
	}

	private synchronized String getLinkContent(String link) {
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
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		if (Memory.isDebug()) {
			gui.raw(this, "visiting: " + link);
			gui.raw(this, "received: " + result.toString());
		}
		return result.toString();
	}

	private void sendCommandToServer(String message) {
		queue.add(message);
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
		} catch (UnsupportedEncodingException ignore) {
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

	private class ThreadMain extends java.lang.Thread {

		int lastSentCommandNumber = 0;
		int lastServerMessageNumber = 0;

		@Override
		public void run() {
			while (isDisposed == false) {

				Wait.waitExactly(1000L);

				String commands = "";
				for (int i = lastSentCommandNumber + 1; i < queue.size() + 1; i++) {
					// (non-standard interval. Here is: A < x <= B)
					commands = commands + queue.get(i) + "/";
				}
				lastSentCommandNumber = queue.size();
				if (commands.length() == 0) {
					commands = "x";
				} else {
					commands = commands.substring(0, commands.length() - 1);
				}
				String text = getLinkContent(
						"http://zagram.org/a.kropki?playerId=" +
							secretId + "&co=getMsg&msgNo=" +
							lastServerMessageNumber + "&msgFromClient=" + commands);
				// betBMsg
				handleText(text);
			}
		}

		private void handleText(String text) {
			// if (text.startsWith("ok/") && text.endsWith("/end")) {
			ServerInterface server = ServerZagram2.this;
			if (text.matches(".*ok/.*") && text.endsWith("/end")) {
				String[] splitted =
						text.substring(
										text.indexOf("ok/") + "ok/".length(),
										text.length() - "/end".length())
								.split("/");
				String currentRoom = "";
				for (String message : splitted) {
					if (message.startsWith("m")) { // message numbers info
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
							break;
						}
						lastServerMessageNumber = i2;
						lastSentCommandNumber = i3;
						// } catch (IndexOutOfBoundsException ignore) {
						// } catch (NumberFormatException e) {
						// }
						// don't catch anything.
						// If an exception would be thrown then we are completely
						// unfamiliar with this server protocol.
						// } else if (message.startsWith("b")) { // player in tables
						// String playerId = message.replaceAll("\\..*", "");
						// String[] roomList = message.replaceFirst(".*\\.",
						// "").split("\\.");
						// for (String roomId : roomList) {
						// gui.userJoinedRoom(server, roomId, playerId, false);
						// }
					} else if (message.startsWith("ca") || message.startsWith("cr")) { // chat
						String[] dotSplitted = message.substring("ca".length())
								.split("\\.", 4);
						try {
							long time = Long.parseLong(dotSplitted[0]) * 1000L;
							String nick = dotSplitted[1];
							// String nickType = dotSplitted[2];
							String chatMessage =
									getServerDecoded(dotSplitted[3]);
							gui.chatReceived(server,
									currentRoom, nick, chatMessage, time);
						} catch (NumberFormatException e) {
							gui.raw(server, "unkown chat: " + message.substring(2));
						} catch (ArrayIndexOutOfBoundsException e) {
							gui.raw(server, "unknown chat: " + message.substring(2));
						}
					} else if (message.startsWith("d")) { // game description
						String[] dotSplitted = message.substring("d".length()).split("\\.");
						try {
							String roomId = dotSplitted[0];
							int startingTime = Integer.parseInt(dotSplitted[2]);
							int addTime = Integer.parseInt(dotSplitted[3]);
							String player1 = dotSplitted[4];
							String player2 = dotSplitted[5];
							String hellishString = dotSplitted[1];
							int sizeX = Integer.parseInt(hellishString.substring(0, 2));
							int sizeY = Integer.parseInt(hellishString.substring(2, 4));
							String rulesAsString = hellishString.substring(4, 8);
							boolean manualEnclosings = rulesAsString.matches("terr");
							boolean stopEnabled = rulesAsString.matches("noT4|noT1");
							boolean isEmptyScored = rulesAsString.matches("terr");
							boolean isRated = !(hellishString.substring(8, 9).equals("F"));
							Integer instantWin = Integer.parseInt(hellishString.substring(9));
							gui.updateGameInfo(
									server, roomId,
									currentRoom, player1, player2,
									sizeX, sizeY,
									false, isRated, 0, instantWin,
									manualEnclosings, stopEnabled, isEmptyScored, null,
									0, addTime, startingTime, 1);
						} catch (NumberFormatException e) {
							gui.raw(server,
									"unknown game description: "
											+ message.substring(1));
						} catch (ArrayIndexOutOfBoundsException e) {
							gui.raw(server,
									"unknown game description: "
											+ message.substring(1));
						}
					} else if (message.startsWith("f")) { // flags
						try {
							String timeLimitsAsString = message.split("_")[1];
							if (timeLimitsAsString.equals("")) {
							} else {
								Integer time1 = Integer.parseInt(
										timeLimitsAsString.split("\\.")[0]);
								Integer time2 = Integer.parseInt(
										timeLimitsAsString.split("\\.")[1]);
								gui.timeUpdate(server, currentRoom,
									new TimeLeft(time1, time2, null, null));
							}
						} catch (Exception e) {
						}
					} else if (message.startsWith("ga") || message.startsWith("gr")) { // +game
						String[] dotSplitted = message.substring("ga".length())
								.split("\\.");
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
						String[] dotSplitted = message.substring("i".length()).split("\\.");
						String player = null, id = null, status = null, language = null, myStatus = null;
						Integer rating = null, winCount = null, lossCount = null, drawCount = null;
						if (dotSplitted.length == 4 || dotSplitted.length == 8) {
							player = dotSplitted[0];
							avatarUrls.put(player, dotSplitted[1]);
							status = dotSplitted[2].equals("F") ? "free" : "";
							language = dotSplitted[3];
							myStatus = language + " (" + status + ")";
						}
						if (dotSplitted.length == 8) {
							try {
								rating = Integer.parseInt(dotSplitted[4]);
								winCount = Integer.parseInt(dotSplitted[5]);
								lossCount = Integer.parseInt(dotSplitted[7]);
								drawCount = Integer.parseInt(dotSplitted[6]);
							} catch (NumberFormatException e) {
							}
						}
						gui.updateUserInfo(server, player, player, null, rating,
								winCount, lossCount, drawCount, myStatus);
					} else if (message.startsWith("pa") || message.startsWith("pr")) { // +player
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
						currentRoom = message.substring(1);
						if (currentRoom.equals("0")) {
							gui.subscribedLangRoom(
									currentRoom,
									server,
									"общий чат: zagram",
									true);
						} else {
							gui.subscribedGame(server, currentRoom);
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
											throw new FatalGameRoomError(
												"can't handle 'Undo black move'.");
										} else if (propertyName.matches("B|W|AB|AW|")) {
											boolean isWhite = propertyName.matches("W|AW") 
													|| (propertyName.equals("") && lastMovePropertyName.matches("A|AW"));
											lastMovePropertyName = propertyName;
											gui.makedMove(
													server, currentRoom,
													message.startsWith("sr"),
													stringToCoordinates(propertyValue).x,
													stringToCoordinates(propertyValue).y,
													isWhite, !isWhite
													);
										} else {
										}
									}
								}
							}
							gui.makedMove(server, currentRoom, false, -1, -1, true, false);
						} catch (FatalGameRoomError e) {
							server.unsubscribeRoom(currentRoom);
							gui.raw(server, "fatal error in game room: " +
								e.getMessage() +
								". Exiting the room....");
						}
					} else if (message.matches("u.undo")) {
						boolean isRed = message.charAt(1) == '1';
						String playerAsString = isRed ? "red" : "blue";
						gui.chatReceived(
							server,
							currentRoom,
							"",
							"player "
									+ playerAsString
									+
									" Запрос на 'undo'. Клиент MultiPoints пока-что не умеет обрабатывать этот вызов.:(",
							null
								);
					} else if (message.startsWith("vg")) { // game invite
						String usefulPart = message.substring(2);
						String sender = usefulPart.replaceAll("\\..*", "");
						String gameDescription = usefulPart.replaceFirst("[^.]*\\.", "");
						gui.raw(server, String.format(
							"Игрок '%s' вызвал(а) тебя на игру: %s. " +
								"MultiPoints пока не умеет обрабатывать это сообщение -- " +
								"отослан отказ от игры.",
							sender,
							gameDescription
								));
						server.rejectOpponent(null, sender);
					}
				}
			} else if (text.equals("")) {
				// we got an empty result. Well, let's treat that as normal.
			} else {
				gui.serverClosed(server);
				isDisposed = true;
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
		return false;
	}

	@Override
	public boolean isPingEnabled() {
		return false;
	}

	@Override
	public boolean isSoundNotifyEnabled() {
		return false;
	}

	public void getUserInfoText(String user) {
	}

	public void getUserpic(String user) {
		try {
			if (avatarImages.get(user)!=null){
			} else if (avatarUrls.get(user)!=null 
					&& !avatarUrls.get(user).equals("")
					&& !avatarUrls.get(user).equals("0")){
				URL url = new URL("http://zagram.org/awatary/" + avatarUrls.get(user) + ".gif");
				ImageIcon imageIcon = new ImageIcon(url);
				gui.updateUserInfo(this, user, null, imageIcon, null, null, null, null, null);
			} else {
				URL url = new URL("http://zagram.org/awatar2.png");
				ImageIcon imageIcon = new ImageIcon(url);
				gui.updateUserInfo(this, user, null, imageIcon, null, null, null, null, null);
			}
		} catch (Exception ex) {
		}
	}

}
class MessageQueue {
	ArrayList<String> stringList;
	int size = 0;
	final int stackSize;

	public MessageQueue(int stackSize) {
		this.stackSize = stackSize;
		stringList = new ArrayList<String>(stackSize);
		for (int i = 0; i < stackSize; i++) {
			stringList.add("");
		}
	}

	String get(int index) {
		if (index >= 0
				&& index <= size()
				&& index >= size() - stackSize + 1) {
			return stringList.get(index % stackSize);
		} else {
			return null;
		}
	}

	void add(String message) {
		size = size + 1;
		stringList.set(size % stackSize, message);
	}

	int size() {
		return size;
	}

	int sizePlusOne() {
		return size() + 1;
	}
}