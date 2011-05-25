package ru.narod.vn91.pointsop.gui;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import ru.narod.vn91.pointsop.server.ServerInterface;

public class GuiController {

	JTabbedPaneMod tabbedPane;
	JTextPane serverOutput;
	ServerInterface pointsxt_tochkiorg_server;
	ServerInterface pointsxt_ircworldru_server;
	ServerInterface pointsopServer;
	HashMap<ServerRoom, RoomInterface> roomInterfaces = new HashMap<ServerRoom, RoomInterface>();
	HashMap<ServerRoom, GameRoom> gameRooms = new HashMap<ServerRoom, GameRoom>();
	HashMap<ServerRoom, LangRoom> langRooms = new HashMap<ServerRoom, LangRoom>();
	HashMap<ServerUserName, PrivateChat> privateChatList = new HashMap<ServerUserName, PrivateChat>();

	GuiController(final JTabbedPaneMod tabbedPane) {
		this.tabbedPane = tabbedPane;
//		new Thread() {
//
//			@Override
//			public void run() {
//				Component nu = null;
//				while (true) {
//					try {
//						Object o = new Object();
//						synchronized (o) {
//							o.wait(1000);
//						}
//					} catch (InterruptedException ex) {
//					}
//					if (nu == null) {
//						nu = tabbedPane.getComponentAt(1);
//					}
//					tabbedPane.makeBold(nu);
//				}
//			}
//		}; //.start();
	}

	public synchronized void serverClosed(ServerInterface server) {
		if (server == pointsxt_ircworldru_server) {
			pointsxt_ircworldru_server.disconnecttt();
			pointsxt_ircworldru_server = null;
		} else if (server == pointsxt_tochkiorg_server) {
			pointsxt_tochkiorg_server.disconnecttt();
			pointsxt_tochkiorg_server = null;
		} else if (server == pointsopServer) {
			pointsopServer.disconnecttt();
			pointsopServer = null;
		}
		receiveRawServerInfo(server, "disconnected...");
	}

	public synchronized void userJoinedLangRoom(ServerInterface server,
			String room,
			String user,
			boolean silent,
			int rank,
			String status) {
		LangRoom panel_Lang = langRooms.get(new ServerRoom(room, server));
		if (panel_Lang != null) {
			RoomPart_Userlist users = panel_Lang.getRoomPart_UserList();
			if (users != null) {
				users.userJoined(user, rank, status);
			}
			RoomPart_Chat chat = panel_Lang.getRoomPart_Chat();
			if ((chat != null) && (silent == false)) {
				chat.addUserJoinedNotice(user);
			}
		} else {
		}
	}

	public synchronized void userJoinedGameRoom(ServerInterface server,
			String room,
			String user,
			boolean silent,
			int rank,
			String status) {
		GameRoom gameRoomPanel = gameRooms.get(new ServerRoom(room, server));
		if (gameRoomPanel != null) {
			RoomPart_Userlist users = gameRoomPanel.getRoomPart_UserList();
			if (users != null) {
				users.userJoined(user, rank, status);
			}
			RoomPart_Chat chat = gameRoomPanel.getRoomPart_Chat();
			if ((chat != null) && (silent == false)) {
				chat.addUserJoinedNotice(user);
			}
		}
	}

	public synchronized void userLeavedRoom(ServerInterface server,
			String room,
			String user) {
		RoomInterface roomInterface = roomInterfaces.get(new ServerRoom(room,
				server));
		if (roomInterface == null) {
		} else {
			RoomPart_Userlist users = roomInterface.getRoomPart_UserList();
			if (users != null) {
				roomInterface.getRoomPart_UserList().userLeave(user);
			}
			RoomPart_Chat chat = roomInterface.getRoomPart_Chat();
			if (chat != null) {
				chat.addUserLeftNotice(user);
			}
		}
	}

	public synchronized void userDisconnected(ServerInterface server,
			String user) {
		PrivateChat privateChat = privateChatList.get(new ServerUserName(user,
				server));
		if ((privateChat != null) && (tabbedPane.indexOfComponent(privateChat) >= 0)) {
			privateChat.addChat("server", user + " вышел из игры...", true);
		}
	}

	public synchronized void subscribedLangRoom(String roomNameOnServer,
			ServerInterface serverInterface,
			String guiRoomName,
			boolean isServersMainRoom) {
		LangRoom langRoom = langRooms.get(new ServerRoom(roomNameOnServer,
				serverInterface));
		if (langRoom != null) {
			// nothing
		} else {
			langRoom = new LangRoom(serverInterface, roomNameOnServer, this);
			langRooms.put(new ServerRoom(roomNameOnServer, serverInterface),
					langRoom);
			roomInterfaces.put(new ServerRoom(roomNameOnServer, serverInterface),
					langRoom);
			tabbedPane.addTab(guiRoomName, langRoom, false);
			tabbedPane.makeBold(langRoom);
			if (isServersMainRoom) {
				tabbedPane.setSelectedComponent(langRoom);
			}
		}
	}

	public synchronized void subscribedGame(String roomNameOnServer,
			ServerInterface server,
			String userFirst,
			String userSecond,
			int rank1,
			int rank2,
			String timeLimits,
			boolean isRated,
			String startingPosition,
			boolean chatReadOnly,
			boolean amIPlaying) {
		if (roomInterfaces.containsKey(new ServerRoom(roomNameOnServer, server))) {
			return;
		}
		GameRoom containerRoom_Game = new GameRoom(server, roomNameOnServer,
				this,
				userFirst, userSecond, rank1, rank2, timeLimits, isRated,
				startingPosition, chatReadOnly, amIPlaying);
		gameRooms.put(new ServerRoom(roomNameOnServer, server),
				containerRoom_Game);
		roomInterfaces.put(new ServerRoom(roomNameOnServer, server),
				containerRoom_Game);
		tabbedPane.addTab(
				"<html><font color=red>" + userFirst + "</font>-<font color=blue>" + userSecond + "</font></html>",
				containerRoom_Game, true);
		tabbedPane.setSelectedComponent(containerRoom_Game);
	}

	public synchronized void unsubsribedRoom(ServerInterface server,
			String room) {
//		System.out.println("gui.unsubscribedRoom " + room);
		RoomInterface roomInterface = roomInterfaces.get(new ServerRoom(room,
				server));
		if (roomInterface != null) {
			tabbedPane.remove((Component)roomInterface);
		}
		roomInterfaces.remove(new ServerRoom(room, server));
	}

	public synchronized void unsubsribedGame(ServerInterface server,
			String room) {
		RoomInterface roomInterface = roomInterfaces.get(new ServerRoom(room,
				server));
		if (roomInterface != null) {
			tabbedPane.remove((Component)roomInterface);
		}
		roomInterfaces.remove(new ServerRoom(room, server));
	}

	public synchronized void chatReceived(ServerInterface server,
			String room,
			String user,
			String message) {
		RoomInterface roomInterface = roomInterfaces.get(new ServerRoom(room,
				server));
		if (roomInterface == null) {
		} else {
			RoomPart_Chat chat = roomInterface.getRoomPart_Chat();
			if (chat != null) {
				roomInterface.getRoomPart_Chat().addChat(user, message);
				tabbedPane.makeBold(roomInterface.getMainJPanel());
			}
		}
	}

	public synchronized void privateMessageReceived(ServerInterface server,
			String user,
			String message) {
		PrivateChat privateChat = privateChatList.get(new ServerUserName(user,
				server));
		if (privateChat == null) {
			// creating new panel
			privateChat = new PrivateChat(server, this, user);
			privateChatList.put(new ServerUserName(user, server), privateChat);
			tabbedPane.addTab(user, privateChat, true);
			tabbedPane.makeBold(privateChat);
		} else {
			if (tabbedPane.indexOfComponent(privateChat) == -1) {
				tabbedPane.addTab(user, privateChat, true); // resurrecting a closed panel
				tabbedPane.makeBold(privateChat);
			} else {
				// updating an old and not closed panel
				tabbedPane.makeBold(privateChat);
			}
		}
		privateChat.addChat(user, message, false);
	}

	public synchronized void createPrivateChatWindow(ServerInterface server,
			String user) {
		PrivateChat privateChat = privateChatList.get(new ServerUserName(user,
				server));
		if (privateChat == null) {
			privateChat = new PrivateChat(server, this, user);
			privateChatList.put(new ServerUserName(user, server), privateChat);
			tabbedPane.addTab(user, privateChat, true);
			tabbedPane.setSelectedComponent(privateChat);
		} else {
			if (tabbedPane.indexOfComponent(privateChat) == -1) {
				tabbedPane.addTab(user, privateChat, true);
			}
			tabbedPane.setSelectedComponent(privateChat);
		}
	}

	public synchronized void serverNoticeReceived(ServerInterface server,
			String room,
			String message) {
		RoomInterface roomInterface = roomInterfaces.get(new ServerRoom(room,
				server));
		if (roomInterface == null) {
		} else {
			RoomPart_Chat chat = roomInterface.getRoomPart_Chat();
			if (chat != null) {
				roomInterface.getRoomPart_Chat().addServerNotice(message);
			}
		}
	}

	public synchronized void gameCreated(ServerInterface server,
			String masterRoom,
			String newRoom,
			String user1,
			String user2,
			String settings) {
		RoomInterface roomInterface = roomInterfaces.get(new ServerRoom(
				masterRoom, server));
		if (roomInterface == null) {
			throw new UnsupportedOperationException(
					"creating a game with an incorrect MasterRoom");
		} else {
			roomInterface.getRoomPart_GameList().
					gameCreated(newRoom, user1, user2, settings, false);
		}
	}

	public synchronized void gameVacancyCreated(ServerInterface server,
			String masterRoom,
			String newRoom,
			String user,
			String settings) {
		LangRoom langRoom = langRooms.get(new ServerRoom(masterRoom, server));
		if (langRoom == null) {
		} else {
			langRoom.getRoomPart_GameList().gameCreated(
					newRoom,
					"<html><b>" + user + "</b></html>", "",
					"<html><b>" + settings + "</b></html>",
					true);
		}
	}

	public synchronized void gameDestroyed(ServerInterface server,
			String masterRoom,
			String oldRoom) {
		LangRoom room = langRooms.get(new ServerRoom(masterRoom, server));
		if (room == null) {
		} else {
			room.getRoomPart_GameList().gameDestroyed(oldRoom);
		}
	}

	public synchronized void gameVacancyDestroyed(ServerInterface server,
			String masterRoom,
			String oldRoom) {
		LangRoom room = langRooms.get(new ServerRoom(masterRoom, server));
//		room.getClass().getInterfaces();
		if (room == null) {
		} else {
			room.getRoomPart_GameList().gameDestroyed(oldRoom);
		}
	}

	public synchronized void makedMove(ServerInterface server,
			String room,
			boolean silent,
			int x,
			int y,
			boolean isRed) {
		GameRoom gameRoom = gameRooms.get(new ServerRoom(room, server));
		if (gameRoom != null) {
			gameRoom.makeMove(silent, x, y, isRed);
			tabbedPane.makeBold(gameRoom);
		}
	}

	public synchronized void gameStop(ServerInterface server,
			String room,
			boolean isRedPlayer) {
		GameRoom room_Game = gameRooms.get(new ServerRoom(room, server));
		if (room_Game != null) {
			gameRooms.get(new ServerRoom(room, server)).stopGame(isRedPlayer);
		}
	}

	public synchronized void gameLost(ServerInterface server,
			String room,
			boolean isRedLooser,
			boolean wantToSave) {
		GameRoom room_Game = gameRooms.get(new ServerRoom(room, server));
		if (room_Game != null) {
			gameRooms.get(new ServerRoom(room, server)).gameLost(isRedLooser,
					wantToSave);
		}
	}

	public synchronized void receiveRawServerInfo(ServerInterface server,
			String info) {
//		System.out.println(info);
		String oldText = serverOutput.getText();
		serverOutput.setText(
				oldText + server.getServerName() + ": " + info + "\n");
//		JOptionPane.showMessageDialog(tabbedPane, info);
//		throw new UnsupportedOperationException("Not supported yet.");
	}

	public synchronized void activateGameRoom(ServerInterface server,
			String roomName) {
		try {
			tabbedPane.setSelectedComponent(gameRooms.get(new ServerRoom(
					roomName, server)));
		} catch (Exception e) {
		}
	}
}

class ServerRoom {

	String roomName;
	ServerInterface serverInterface;

	public ServerRoom(String roomName,
			ServerInterface serverInterface) {
		this.roomName = roomName;
		this.serverInterface = serverInterface;
	}

	@Override
	public boolean equals(Object compareToObject) {
		if (compareToObject instanceof ServerRoom == false) {
			return false;
			// avoid this stupid NetBeans hint...
		}
		if ((compareToObject == null)
				|| (getClass().isInstance(compareToObject) == false)) {
			return false;
		}
		ServerRoom compareTo = getClass().cast(compareToObject);
		boolean roomNameEquals = (roomName == null)
				? compareTo.roomName == null
				: roomName.equals(compareTo.roomName);
		boolean serverEquals = (serverInterface == null)
				? compareTo.serverInterface == null
				: serverInterface.equals(compareTo.serverInterface);
		return roomNameEquals && serverEquals;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + (this.roomName != null ? this.roomName.hashCode() : 0);
		hash = 29 * hash + (this.serverInterface != null ? this.serverInterface.hashCode() : 0);
		return hash;
	}
}

class ServerUserName {

	String userName;
	ServerInterface serverInterface;

	public ServerUserName(String userName,
			ServerInterface serverInterface) {
		this.userName = userName;
		this.serverInterface = serverInterface;
	}

	@Override
	public boolean equals(Object compareToObject) {
		if (compareToObject instanceof ServerUserName == false) {
			return false;
			// avoid this stupid NetBeans hint...
		}
		if ((compareToObject == null)
				|| (getClass().isInstance(compareToObject) == false)) {
			return false;
		}
		ServerUserName compareTo = getClass().cast(compareToObject);
		boolean userNameEquals = (userName == null)
				? compareTo.userName == null
				: userName.equals(compareTo.userName);
		boolean serverEquals = (serverInterface == null)
				? compareTo.serverInterface == null
				: serverInterface.equals(compareTo.serverInterface);
		return userNameEquals && serverEquals;
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 29 * hash + (this.userName != null ? this.userName.hashCode() : 0);
		hash = 29 * hash + (this.serverInterface != null ? this.serverInterface.hashCode() : 0);
		return hash;
	}
}

class JTabbedPaneMod extends JTabbedPane {

	/**
	 * almost the same as the JTabbedPane.addTab method,
	 * but if isCloseable is true, then an icon for closing the tab is created.
	 *
	 * @param title
	 * @param component
	 * @param isCloseable if yes, generates an icon for closing the tab.
	 */
	public void addTab(String title,
			Component component,
			boolean isCloseable) {
		super.addTab(title, component);
		if (isCloseable) {
			TabComponent_Closeable buttonTabComponent = new TabComponent_Closeable(
					this, title);
			this.setTabComponentAt(getTabCount() - 1, buttonTabComponent);
		} else {
			this.setTabComponentAt(getTabCount() - 1, new JLabel(title));
		}
	}

	@Override
	public void remove(int index) {
		Component panelToRemove = super.getComponentAt(index);
		if (panelToRemove instanceof RoomInterface) {
			RoomInterface roomInterface = RoomInterface.class.cast(panelToRemove);
			boolean immediateClose = roomInterface.close();
			if (immediateClose == true) {
				super.remove(index);
			}
		} else {
			super.remove(index);
		}
	}

	private String deleteBoldness(String s) {
		return s.replaceAll("<b>", "").replaceAll("</b>", "").
				replaceAll("\\*", "").
				replaceAll("<font size=\\+1>", "").replaceAll("</font>", "");
	}

	public void makeBold(Component component) {
		if (super.getSelectedComponent() == component) {
			return;
		}
		int tabIndex = super.indexOfComponent(component);
		String oldTitle = super.getTitleAt(tabIndex);
		String newTitle = deleteBoldness(oldTitle).replaceAll("<html>", "").replaceAll(
				"</html>", "");
		newTitle = "<html>***" + newTitle + "</html>";
		if (newTitle.equals(oldTitle) == false) {
			if (TabComponent_Closeable.class.isInstance(getTabComponentAt(
					tabIndex))) {
				TabComponent_Closeable buttonTabComponent = new TabComponent_Closeable(
						JTabbedPaneMod.this, newTitle);
				super.setTabComponentAt(tabIndex, buttonTabComponent);
				super.setTitleAt(tabIndex, newTitle);
			} else {
				super.setTabComponentAt(tabIndex, new JLabel(newTitle));
				super.setTitleAt(tabIndex, newTitle);
			}
		}
	}

	public JTabbedPaneMod() {
		addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				int selectedIndex = getSelectedIndex();
				String oldTitle = getTitleAt(selectedIndex);
				String newTitle = deleteBoldness(oldTitle);
				if (newTitle.equals(oldTitle) == false) {
					if (TabComponent_Closeable.class.isInstance(getTabComponentAt(
							selectedIndex))) {
						TabComponent_Closeable buttonTabComponent = new TabComponent_Closeable(
								JTabbedPaneMod.this, newTitle);
						setTabComponentAt(selectedIndex, buttonTabComponent);
						setTitleAt(selectedIndex, newTitle);
					} else {
						setTabComponentAt(selectedIndex, new JLabel(newTitle));
						setTitleAt(selectedIndex, newTitle);
					}
				}
			}
		});
	}
}
