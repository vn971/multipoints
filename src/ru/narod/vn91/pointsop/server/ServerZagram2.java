package ru.narod.vn91.pointsop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import ru.narod.vn91.pointsop.gui.GuiForServerInterface;
import ru.narod.vn91.pointsop.utils.Wait;

public class ServerZagram2 implements ServerInterface {

	String myNameOnServer;
	GuiForServerInterface gui;
	String currentTable = "";
	String secretId;
	volatile boolean isDisposed = false;
	MessageQueue queue = new MessageQueue(5);

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
		gui.raw(this, "невозможно оставлять заявки на игру на этом сервере");
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
		new Thread() {
			public void run() {
				gui.raw(ServerZagram2.this, "connecting...");
				getLinkContent("http://zagram.org/a.kropki?co=guestLogin&idGracza=" +
						secretId + "&opis=" +
						getUrlEncoded(myNameOnServer) + "&lang=ru");
				Runtime.getRuntime().addShutdownHook(new Thread() {
					@Override
					public void run() {
						disconnectServer();
					}
				});
				new ThreadMain().start();
			};
		}.start();
	}

	@Override
	public void disconnectServer() {
		getLinkContent("http://zagram.org/a.kropki?playerId=" +
				secretId + "&co=usunGracza");
		this.isDisposed = true;
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
				+ getUrlEncoded(message);
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
	}

	@Override
	public void surrender(String roomName) {
	}

	private synchronized String getLinkContent(String link) {
		gui.raw(this, "visiting: " + link);
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
		gui.raw(this, "received: " + result.toString());
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

	private static String getUrlEncoded(String s) {
		try {
			return URLEncoder.encode(s, "UTF-8");
		} catch (UnsupportedEncodingException ignore) {
			return "";
		}
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
				handleText(text);
			}
		}

		private void handleText(String text) {
			// if (text.startsWith("ok/") && text.endsWith("/end")) {
			if (text.matches(".*ok/.*") && text.endsWith("/end")) {
				String[] splitted =
						text.substring(
										text.indexOf("ok/") + "ok/".length(),
										text.length() - "/end".length())
								.split("/");
				String currentRoom = "";
				for (String message : splitted) {
					if (message.startsWith("m")) { // message numbers info
						try {
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
						} catch (Exception ignore) {
						}
					} else if (message.startsWith("q")) {
						currentRoom = message.substring(1);
						if (currentRoom.equals("0")) {
							gui.subscribedLangRoom(
									currentRoom,
									ServerZagram2.this,
									"общий чат: zagram",
									true);
						} else {
							// game room not handled yet
						}
					} else if (message.startsWith("pr")) {
						// player mass-join
						String[] dotSplitted = message.substring("pr".length())
								.split("\\.");
						for (String player : dotSplitted) {
							gui.userJoinedRoom(ServerZagram2.this, currentRoom, player, true);
						}
					} else if (message.startsWith("pa")) {
						// player joined
						String[] dotSplitted = message.substring("pa".length())
								.split("\\.");
						for (String player : dotSplitted) {
							gui.userJoinedRoom(
									ServerZagram2.this, currentRoom, player, false);
						}
					} else if (message.startsWith("pd")) {
						// player left
						String[] dotSplitted = message.substring("pd".length())
								.split("\\.");
						for (String player : dotSplitted) {
							gui.userLeftRoom(ServerZagram2.this, currentRoom, player);
						}
					} else if (message.startsWith("ca")) {
						// new chat
						String[] dotSplitted = message.substring("ca".length())
								.split("\\.", 4);
						try {
							long time = Long.parseLong(dotSplitted[0]) * 1000L;
							String nick = dotSplitted[1];
							String nickType = dotSplitted[2];
							String chatMessage = dotSplitted[3]
									.replaceAll("&#60;", "<")
									.replaceAll("&#62;", ">")
									.replaceAll("&#39;", "'")
									.replaceAll("&#34;", "\\")
									.replaceAll("&#45;", "-")
									;
							gui.chatReceived(ServerZagram2.this,
									currentRoom, nick, chatMessage, time);
						} catch (NumberFormatException e) {
							gui.raw(ServerZagram2.this,
									"An error has occured while processing chat message: " +
											message);
						}
					} else if (message.matches("u.undo")) {
						boolean isRed = message.charAt(1) == '1';
						String playerAsString = isRed ? "red" : "blue";
						gui
								.chatReceived(
										ServerZagram2.this,
										currentRoom,
										"",
										"player "
												+ playerAsString
												+ " Запрос на 'undo'. Клиент MultiPoints пока-что не умеет обрабатывать этот вызов.:(",
										null);
					} else if (message.startsWith("i")) {
						// player left
						String[] dotSplitted = message.substring("i".length()).split("\\.");
						String player = null, status = null, language = null, myStatus = null;
						Integer rating = null, winCount = null, lossCount = null, drawCount = null;
						if (dotSplitted.length == 4 || dotSplitted.length == 8) {
							player = dotSplitted[0];
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
						gui.addUserInfo(ServerZagram2.this, player, player, null, rating,
								winCount, lossCount, drawCount, myStatus);
					} else if (message.startsWith("vg")) {
						String usefulPart = message.substring(2);
						String sender = usefulPart.replaceAll("\\..*", "");
						String gameDescription = usefulPart.replaceFirst("[^.].", "");
						gui
								.privateMessageReceived(
										ServerZagram2.this,
										"server",
										"Игрок '"
												+ sender
												+ "' вызвал(а) тебя на игру: "
												+ gameDescription
												+
												// ". The invitation is rejected because MultiPoints couldn't handle the game."
												". MultiPoints пока не умеет обрабатывать это сообщение -- отослан отказ от игры."
								);
						ServerZagram2.this.rejectOpponent(null, sender);
						// send reje
					}
				}
			} else {
				gui.serverClosed(ServerZagram2.this);
				isDisposed = true;
			}
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