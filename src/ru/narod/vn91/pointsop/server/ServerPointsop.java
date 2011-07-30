package ru.narod.vn91.pointsop.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import ru.narod.vn91.pointsop.gui.GuiForServerInterface;

public class ServerPointsop implements ServerInterface {

	Socket socket = null;
	GuiForServerInterface gui;
	String serverName = "pointsOp";

	public ServerPointsop(GuiForServerInterface guiController) {
		this.gui = guiController;
	}

	public void connect() {
//		new InputThread().start();
	}

	public void disconnecttt() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void searchOpponent() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void requestJoinGame(String gameRoomName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void acceptOpponent(String roomName,
			String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void stopSearchingOpponent() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void makeMove(String roomName,
			int x,
			int y) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void subscribeRoom(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void unsubscribeRoom(String name) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void sendChat(String room,
			String message) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void sendPrivateMsg(String target,
			String message) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getMyName() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String getServerName() {
		return serverName;
	}

	public String getMainRoom() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public void surrender(String roomName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	String getLinkContent(String link) {
		System.out.println("getting page " + link);
		String result = "";
		try {
			URL url = new URL(link);
			URLConnection urlConn = url.openConnection();
			InputStreamReader inStream = new InputStreamReader(
					urlConn.getInputStream());
			BufferedReader buff = null;
			buff = new BufferedReader(inStream);
			while (true) {
				String nextLine;
				nextLine = buff.readLine();
				if (nextLine != null) {
					result += nextLine;
				} else {
					break;
				}
			}
			return result;
		} catch (MalformedURLException e) {
			gui.receiveRawServerInfo(this, "Please check the URL:" + e, GuiForServerInterface.MessageType.INFO);
			return null;
		} catch (IOException e1) {
			gui.receiveRawServerInfo(this,
					"Can't read  from the Internet: " + e1, GuiForServerInterface.MessageType.INFO);
			return null;
		}
	}

	private class InputThread extends Thread {

		@Override
		public void run() {
			try {
				String ipAsString = "";
				//					String servToConnect = null;
				ipAsString = getLinkContent(
						"http://vn91.narod.ru/pop/myserv.txt");
				if (ipAsString == null || ipAsString.equals("")) {
					ipAsString = "127.0.0.1"; // "188.123.232.164";
				}
				serverName = "pointsOp " + ipAsString;
				ServerPointsop.this.socket = new Socket(ipAsString, 4444);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						ServerPointsop.this.socket.getInputStream()));
				gui.receiveRawServerInfo(ServerPointsop.this, in.readLine(),
						GuiForServerInterface.MessageType.INFO);
				ServerPointsop.this.socket.close();
			} catch (Exception e) {
			}
		}
	}
}
