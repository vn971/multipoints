package ru.narod.vn91.pointsop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
	public void connect() {
		gui.raw(this, "connecting...");
		getLinkContent("http://zagram.org/a.kropki?co=guestLogin&idGracza=" +
				secretId + "&opis=" +
				myNameOnServer + "&lang=ru");

		new ThreadMain().start();
	}

	@Override
	public void disconnecttt() {
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
				+ message;
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
	public void surrender(String roomName) {
		// TODO Auto-generated method stub
	}

	@Override
	public void unsubscribeRoom(String room) {
		String msgToSend = "q" + queue.sizePlusOne() + "." + room + ".";
		sendCommandToServer(msgToSend);
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
						urlConn.getInputStream());
			BufferedReader buff = null;
			buff = new BufferedReader(inStream);

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
			if (text.startsWith("ok/") && text.endsWith("/end")) {
				String[] splitted =
						text.substring("ok/".length(), text.length() - "/end".length())
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
						gui.subscribedLangRoom(
								message.substring(1),
								ServerZagram2.this,
								message.substring(1),
								true);
					} else if (message.startsWith("p")) {
						String[] dotSplitted = message.substring(1).split("\\.");
						for (String player : dotSplitted) {
							gui.userJoinedRoom(ServerZagram2.this, currentRoom, player,
									true, 0, "");
						}
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
			stringList.add(null);
		}
	}

	String get(int index) {
		if (index >= 0
				&& index < size()
				&& index >= size() - stackSize) {
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