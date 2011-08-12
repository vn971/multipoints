package ru.narod.vn91.pointsop.gui;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.narod.vn91.pointsop.data.Memory;
import ru.narod.vn91.pointsop.server.AiVirtualServer;
import ru.narod.vn91.pointsop.server.ServerInterface;
import ru.narod.vn91.pointsop.sounds.Sounds;
import ru.narod.vn91.pointsop.gui.GameRoom;

public class GuiController implements GuiForServerInterface {

	JTabbedPaneMod tabbedPane;
	JTextPane serverOutput;
	ServerInterface pointsxt_tochkiorg_server;
	ServerInterface pointsxt_ircworldru_server;
	ServerInterface pointsxt_vn91_server;
	ServerInterface pointsopServer;
	HashMap<ServerRoom, RoomInterface> roomInterfaces = new HashMap<ServerRoom, RoomInterface>();
	HashMap<ServerRoom, GameRoom> gameRooms = new HashMap<ServerRoom, GameRoom>();
	HashMap<ServerRoom, LangRoom> langRooms = new HashMap<ServerRoom, LangRoom>();
	HashMap<ServerUserName, PrivateChat> privateChatList = new HashMap<ServerUserName, PrivateChat>();

	GuiController(final JTabbedPaneMod tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#serverClosed(ru.narod.vn91.pointsop.server.ServerInterface)
	 */
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
		if (!(server instanceof AiVirtualServer)) {
			receiveRawServerInfo(
					server,
					"Отключился от сервера... \n"
							+ "К сожалению, переподключиться в \"тихом\" режиме "
							+ "пока-что невозможно. \n"
							+ "Чтобы подключиться, закройте приложение и откройте его заново.",
					MessageType.ERROR
			);
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#userJoinedLangRoom(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String, boolean, int, java.lang.String)
	 */
	public synchronized void userJoinedLangRoom(
			ServerInterface server,
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

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#userJoinedGameRoom(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String, boolean, int, java.lang.String)
	 */
	public synchronized void userJoinedGameRoom(
			ServerInterface server,
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

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#userLeavedRoom(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String)
	 */
	public synchronized void userLeftRoom(
			ServerInterface server,
			String room,
			String user) {
		RoomInterface roomInterface = roomInterfaces.get(
				new ServerRoom(
						room,
						server
				)
		);
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

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#userDisconnected(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String)
	 */
	public synchronized void userDisconnected(
			ServerInterface server,
			String user) {
		PrivateChat privateChat = privateChatList.get(
				new ServerUserName(
						user,
						server
				)
		);
		if ((privateChat != null) && (tabbedPane.indexOfComponent(
				privateChat
		) >= 0)) {
			privateChat.addChat("server", user + " вышел из игры...", true);
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#subscribedLangRoom(java.lang.String, ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, boolean)
	 */
	public synchronized void subscribedLangRoom(
			String roomNameOnServer,
			ServerInterface serverInterface,
			String guiRoomName,
			boolean isServersMainRoom) {
//		System.out.println("GuiController.subscribedLangRoom()");
		LangRoom langRoom = langRooms.get(
				new ServerRoom(
						roomNameOnServer,
						serverInterface
				)
		);

		if (langRoom != null) {
			// nothing
		} else {
//			System.out.println("GuiController.subscribedLangRoom()2");
			langRoom = new LangRoom(serverInterface, roomNameOnServer, this);
//			System.out.println("GuiController.subscribedLangRoom()3");
			langRooms.put(
					new ServerRoom(roomNameOnServer, serverInterface),
					langRoom
			);
			roomInterfaces.put(
					new ServerRoom(roomNameOnServer, serverInterface),
					langRoom
			);
			tabbedPane.addTab(guiRoomName, langRoom, false);
			tabbedPane.makeBold(langRoom);
			if (isServersMainRoom) {
				tabbedPane.setSelectedComponent(langRoom);
			}
			System.out.println("GuiController.subscribedLangRoom()4");
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#subscribedGame(java.lang.String, ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String, int, int, java.lang.String, boolean, java.lang.String, boolean, boolean)
	 */
	public synchronized void subscribedGame(
			String roomNameOnServer,
			ServerInterface server,
			String userFirst,
			String userSecond,
			int rank1,
			int rank2,
			String timeLimits,
			boolean isRated,
			String startingPosition,
			boolean chatReadOnly,
			boolean amIPlaying,
			boolean amIRed) {
		if (roomInterfaces.containsKey(new ServerRoom(roomNameOnServer, server))) {
			return;
		}
		GameRoom containerRoom_Game = new GameRoom(
				server, roomNameOnServer,
				this,
				userFirst, userSecond, rank1, rank2, timeLimits, isRated,
				startingPosition, chatReadOnly, amIPlaying, amIRed
				);
		gameRooms.put(
				new ServerRoom(roomNameOnServer, server),
				containerRoom_Game
		);
		roomInterfaces.put(
				new ServerRoom(roomNameOnServer, server),
				containerRoom_Game
		);
//		System.out.println(
//				"Memory.getPlayer1Color().toString() = " + Memory.getPlayer1Color().toString());
		tabbedPane.addTab(
				"<html><font color="
						+ GlobalGuiSettings.getHtmlColor(
						Memory.getPlayer1Color()
				)
						+ ">" + userFirst + "</font><font color=black> - </font><font color="
						+ GlobalGuiSettings.getHtmlColor(
						Memory.getPlayer2Color()
				)
						+ ">" + userSecond + "</font></html>",
				containerRoom_Game, true
		);
		tabbedPane.setSelectedComponent(containerRoom_Game);
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#unsubsribedRoom(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String)
	 */
	public synchronized void unsubsribedRoom(
			ServerInterface server,
			String room) {
//		System.out.println("gui.unsubscribedRoom " + room);
		RoomInterface roomInterface = roomInterfaces.get(
				new ServerRoom(
						room,
						server
				)
		);
		if (roomInterface != null) {
			tabbedPane.remove((Component) roomInterface);
		}
		roomInterfaces.remove(new ServerRoom(room, server));
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#unsubsribedGame(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String)
	 */
	public synchronized void unsubsribedGame(
			ServerInterface server,
			String room) {
		RoomInterface roomInterface = roomInterfaces.get(
				new ServerRoom(
						room,
						server
				)
		);
		if (roomInterface != null) {
			tabbedPane.remove((Component) roomInterface);
		}
		roomInterfaces.remove(new ServerRoom(room, server));
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#chatReceived(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String, java.lang.String)
	 */
	public synchronized void chatReceived(
			ServerInterface server,
			String room,
			String user,
			String message) {
		RoomInterface roomInterface = roomInterfaces.get(
				new ServerRoom(
						room,
						server
				)
		);
		if (roomInterface == null) {
		} else {
			RoomPart_Chat chat = roomInterface.getRoomPart_Chat();
			if (chat != null) {
				roomInterface.getRoomPart_Chat().addChat(user, message);
				tabbedPane.makeBold(roomInterface.getMainJPanel());
			}
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#privateMessageReceived(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String)
	 */
	public synchronized void privateMessageReceived(
			ServerInterface server,
			String user,
			String message) {
		PrivateChat privateChat = privateChatList.get(
				new ServerUserName(
						user,
						server
				)
		);
		if (privateChat == null) {
			// creating new panel
			privateChat = new PrivateChat(server, this, user);
			privateChatList.put(new ServerUserName(user, server), privateChat);
			tabbedPane.addTab(user, privateChat, true);
			tabbedPane.makeBold(privateChat);
		} else {
			if (tabbedPane.indexOfComponent(privateChat) == -1) {
				tabbedPane.addTab(
						user, privateChat, true
				); // resurrecting a closed panel
				tabbedPane.makeBold(privateChat);
			} else {
				// updating an old and not closed panel
				tabbedPane.makeBold(privateChat);
			}
		}
		privateChat.addChat(user, message, false);
	}

	@Override
	public void soundReceived(ServerInterface server, String user) {
		privateMessageReceived(server, user, "послан звуковой сигнал");
		new Sounds().playAlarmSignal();
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#createPrivateChatWindow(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String)
	 */
	public synchronized void createPrivateChatWindow(
			ServerInterface server,
			String user) {
		PrivateChat privateChat = privateChatList.get(
				new ServerUserName(
						user,
						server
				)
		);
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

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#serverNoticeReceived(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String)
	 */
	public synchronized void serverNoticeReceived(
			ServerInterface server,
			String room,
			String message) {
		RoomInterface roomInterface = roomInterfaces.get(
				new ServerRoom(
						room,
						server
				)
		);
		if (roomInterface == null) {
		} else {
			RoomPart_Chat chat = roomInterface.getRoomPart_Chat();
			if (chat != null) {
				roomInterface.getRoomPart_Chat().addServerNotice(message);
			}
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#gameCreated(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public synchronized void gameCreated(
			ServerInterface server,
			String masterRoom,
			String newRoom,
			String user1,
			String user2,
			String settings) {
		RoomInterface roomInterface = roomInterfaces.get(
				new ServerRoom(
						masterRoom, server
				)
		);
		if (roomInterface == null) {
			throw new UnsupportedOperationException(
					"creating a game with an incorrect MasterRoom"
			);
		} else {
			roomInterface.getRoomPart_GameList().
					gameCreated(newRoom, user1, user2, settings, false);
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#gameVacancyCreated(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public synchronized void gameVacancyCreated(
			ServerInterface server,
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
					true
			);
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#gameVacancyDestroyed(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String)
	 */
	public synchronized void gameVacancyDestroyed(
			ServerInterface server,
			String masterRoom,
			String oldRoom) {
		LangRoom room = langRooms.get(new ServerRoom(masterRoom, server));
//		room.getClass().getInterfaces();
		if (room == null) {
		} else {
			room.getRoomPart_GameList().gameDestroyed(oldRoom);
		}
	}

	@Override
	public void gameRequestReceived(ServerInterface server, String room,
			String possibleOpponent) {
		new Sounds().playAlarmSignal();
		server.acceptOpponent(room, possibleOpponent);
	}

/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#gameDestroyed(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String)
	 */
	public synchronized void gameDestroyed(
			ServerInterface server,
			String masterRoom,
			String oldRoom) {
		LangRoom room = langRooms.get(new ServerRoom(masterRoom, server));
		if (room == null) {
		} else {
			room.getRoomPart_GameList().gameDestroyed(oldRoom);
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#makedMove(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, boolean, int, int, boolean, int, int)
	 */
	public synchronized void makedMove(
			ServerInterface server,
			String room,
			boolean silent,
			int x,
			int y,
			boolean isRed,
			int timeLeftRed,
			int timeLeftBlue) {
		GameRoom gameRoom = gameRooms.get(new ServerRoom(room, server));
		if (gameRoom != null) {
			gameRoom.makeMove(silent, x, y, isRed,timeLeftRed,timeLeftBlue);
			tabbedPane.makeBold(gameRoom);
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#gameStop(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, boolean)
	 */
	public synchronized void gameStop(
			ServerInterface server,
			String room,
			boolean isRedPlayer) {
		GameRoom room_Game = gameRooms.get(new ServerRoom(room, server));
		if (room_Game != null) {
			gameRooms.get(new ServerRoom(room, server)).stopGame(isRedPlayer);
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#gameLost(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, boolean, boolean)
	 */
	public synchronized void gameLost(
			ServerInterface server,
			String room,
			boolean isRedLooser,
			boolean wantToSave) {
		GameRoom room_Game = gameRooms.get(new ServerRoom(room, server));
		if (room_Game != null) {
			gameRooms.get(new ServerRoom(room, server)).gameLost(
					isRedLooser,
					wantToSave
			);
		}
	}

	/* (non-Javadoc)
	 * @see ru.narod.vn91.pointsop.gui.GuiForServerInterface#receiveRawServerInfo(ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String, ru.narod.vn91.pointsop.gui.GuiController.MessageType)
	 */
	public synchronized void receiveRawServerInfo(
			ServerInterface server,
			String info,
			MessageType type) {
		String oldText = serverOutput.getText();
		serverOutput.setText(
				oldText + server.getServerName() + ": " + info + "\n"
		);
		if (type == MessageType.ERROR) {
			JOptionPane.showMessageDialog(
					tabbedPane,
					info,
					"Error: " + info,
					JOptionPane.ERROR_MESSAGE
			);
		}
	}

	public synchronized void activateGameRoom(
			ServerInterface server,
			String roomName) {
		try {
			tabbedPane.setSelectedComponent(
					gameRooms.get(
							new ServerRoom(
									roomName, server
							)
					)
			);
		} catch (Exception ignored) {
		}
	}
}

class ServerRoom {

	String roomName;
	ServerInterface serverInterface;

	public ServerRoom(
			String roomName,
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

	public ServerUserName(
			String userName,
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

class JTabbedPaneMod
		extends JTabbedPane {

	/**
	 * almost the same as the JTabbedPane.addTab method,
	 * but if isCloseable is true, then an icon for closing the tab is created.
	 *
	 * @param title
	 * @param component
	 * @param isCloseable if yes, generates an icon for closing the tab.
	 */
	public void addTab(
			String title,
			Component component,
			boolean isCloseable) {
		super.addTab(title, component);
		if (isCloseable) {
			TabComponent_Closeable buttonTabComponent = new TabComponent_Closeable(
					this, title
			);
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
			boolean immediateClose = roomInterface.userAsksClose();
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
		if (tabIndex < 0) {
			return;
		}
		String oldTitle = super.getTitleAt(tabIndex);
		String newTitle = deleteBoldness(oldTitle).replaceAll(
				"<html>", ""
		).replaceAll(
				"</html>", ""
		);
		newTitle = "<html>***" + newTitle + "</html>";
		if (newTitle.equals(oldTitle) == false) {
			if (TabComponent_Closeable.class.isInstance(
					getTabComponentAt(
							tabIndex
					)
			)) {
				TabComponent_Closeable buttonTabComponent = new TabComponent_Closeable(
						JTabbedPaneMod.this, newTitle
				);
				super.setTabComponentAt(tabIndex, buttonTabComponent);
				super.setTitleAt(tabIndex, newTitle);
			} else {
				super.setTabComponentAt(tabIndex, new JLabel(newTitle));
				super.setTitleAt(tabIndex, newTitle);
			}
		}
	}

	public JTabbedPaneMod() {
		addChangeListener(
				new ChangeListener() {

					public void stateChanged(ChangeEvent e) {
						int selectedIndex = getSelectedIndex();
						String oldTitle = getTitleAt(selectedIndex);
						String newTitle = deleteBoldness(oldTitle);
						if (newTitle.equals(oldTitle) == false) {
							if (TabComponent_Closeable.class.isInstance(
									getTabComponentAt(
											selectedIndex
									)
							)) {
								TabComponent_Closeable buttonTabComponent = new TabComponent_Closeable(
										JTabbedPaneMod.this, newTitle
								);
								setTabComponentAt(selectedIndex, buttonTabComponent);
								setTitleAt(selectedIndex, newTitle);
							} else {
								setTabComponentAt(selectedIndex, new JLabel(newTitle));
								setTitleAt(selectedIndex, newTitle);
							}
						}
					}
				}
		);
	}
}
