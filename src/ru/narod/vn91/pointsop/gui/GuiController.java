package ru.narod.vn91.pointsop.gui;

import java.awt.Component;
import java.awt.Image;
import java.util.HashMap;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ru.narod.vn91.pointsop.data.GameOuterInfo;
import ru.narod.vn91.pointsop.data.GamePool;
import ru.narod.vn91.pointsop.data.Player;
import ru.narod.vn91.pointsop.data.PlayerPool;
import ru.narod.vn91.pointsop.data.GameOuterInfo.GameState;
import ru.narod.vn91.pointsop.server.AiVirtualServer;
import ru.narod.vn91.pointsop.server.ServerInterface;
import ru.narod.vn91.pointsop.sounds.Sounds;
import ru.narod.vn91.pointsop.utils.Memory;

public class GuiController implements GuiForServerInterface {

	JTabbedPaneMod tabbedPane;
	JTextPane serverOutput;
	ServerInterface pointsxt_tochkiorg_server;
	ServerInterface pointsxt_ircworldru_server;
	ServerInterface pointsxt_vn91_server;
	ServerInterface zagramTwo_server;
	ServerInterface zagram_server;
	ServerInterface pointsopServer;
	HashMap<ServerRoom, RoomInterface> roomInterfaces = new HashMap<ServerRoom, RoomInterface>();
	HashMap<ServerRoom, GameRoom> gameRooms = new HashMap<ServerRoom, GameRoom>();
	HashMap<ServerRoom, LangRoom> langRooms = new HashMap<ServerRoom, LangRoom>();
	HashMap<Player, PrivateChat> privateChatList = new HashMap<Player, PrivateChat>();
	PlayerPool playerPool = new PlayerPool();
	GamePool gamePool = new GamePool();

	GuiController(final JTabbedPaneMod tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.narod.vn91.pointsop.gui.GuiForServerInterface#serverClosed(ru.narod.
	 * vn91.pointsop.server.ServerInterface)
	 */
	public synchronized void serverClosed(ServerInterface server) {
		if (server == pointsxt_ircworldru_server) {
			pointsxt_ircworldru_server.disconnectServer();
			pointsxt_ircworldru_server = null;
		} else if (server == pointsxt_tochkiorg_server) {
			pointsxt_tochkiorg_server.disconnectServer();
			pointsxt_tochkiorg_server = null;
		} else if (server == pointsopServer) {
			pointsopServer.disconnectServer();
			pointsopServer = null;
		}
		if (!(server instanceof AiVirtualServer)) {
			rawError(
					server,
					"Отключился от сервера... \n"
							+ "К сожалению, переподключиться в \"тихом\" режиме "
							+ "пока-что невозможно. \n"
							+ "Чтобы подключиться, закройте приложение и откройте его заново.");
		}
	}

	@Override
	public void updateUserInfo(
			ServerInterface server, String id,
			String guiName, Image image,
			Integer rating, Integer winCount, Integer lossCount, Integer drawCount,
			String status) {
		Player player = playerPool.get(server, id);
		Player updateInstance = new Player(server, id, guiName, rating, winCount,
					lossCount, drawCount, image, status);
		player.updateFrom(updateInstance);
	}

	@Override
	public void updateGameInfo(
			ServerInterface server, String id, String masterRoomId,
			String firstId, String secondId, Integer sizeX, Integer sizeY,
			Boolean isRedFirst, Boolean isRated, Integer handicapRed,
			Integer instantWin, Boolean manualEnclosings, Boolean stopEnabled,
			Boolean isEmptyScored, GameState state, Integer freeTemporalTime,
			Integer additionalAccumulatingTime, Integer startingTime,
			Integer periodLength
			) {
	GameOuterInfo gameOuterInfo = gamePool.get(server, id);
	Player first = playerPool.get(server, firstId);
	Player second = playerPool.get(server, secondId);

		GameOuterInfo updateInstance = new GameOuterInfo(
				server, secondId, masterRoomId, first, second, sizeX, sizeY,
				isRedFirst, isRated, handicapRed, instantWin,
				manualEnclosings, stopEnabled, isEmptyScored, state, freeTemporalTime,
				additionalAccumulatingTime, startingTime, periodLength);
	gameOuterInfo.updateFrom(updateInstance);
	}

	@Override
	public void userJoinedRoom(ServerInterface server, String room, String id,
			boolean isStartup) {
		Player player = playerPool.get(server, id);
		LangRoom langRoom = langRooms.get(new ServerRoom(room, server));
		GameRoom gameRoom = gameRooms.get(new ServerRoom(room, server));
		if (langRoom != null) {
			RoomPart_Userlist users = langRoom.getRoomPart_UserList();
			if (users != null) {
				users.userJoined(player);
			}
			RoomPart_Chat chat = langRoom.getRoomPart_Chat();
			if ((chat != null) && (isStartup == false)) {
				chat.addUserJoinedNotice(player);
			}
		} else if (gameRoom != null) {
			RoomPart_Userlist users = gameRoom.getRoomPart_UserList();
			if (users != null) {
				users.userJoined(player);
			}
			RoomPart_Chat chat = gameRoom.getRoomPart_Chat();
			if ((chat != null) && (isStartup == false)) {
				chat.addUserJoinedNotice(player);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.narod.vn91.pointsop.gui.GuiForServerInterface#userLeavedRoom(ru.narod
	 * .vn91.pointsop.server.ServerInterface, java.lang.String, java.lang.String)
	 */
	public synchronized void userLeftRoom(
			ServerInterface server,
			String room,
			String id) {
		Player player = playerPool.get(server, id);
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
				roomInterface.getRoomPart_UserList().userLeave(player);
			}
			RoomPart_Chat chat = roomInterface.getRoomPart_Chat();
			if (chat != null) {
				chat.addUserLeftNotice(player.guiName);
			}
		}
	}

	public synchronized void userDisconnected(
			ServerInterface server,
			String id) {
		Player player = playerPool.get(server, id);
		PrivateChat privateChat = privateChatList.get(player);
		if ((privateChat != null) && (tabbedPane.
				indexOfComponent(privateChat) >= 0)) {
			privateChat.addChat("server", player.guiName + " вышел из игры...", true);
		}
		playerPool.remove(server, id);
	}

	public synchronized void subscribedLangRoom(
			String roomNameOnServer,
			ServerInterface serverInterface,
			String guiRoomName,
			boolean isServersMainRoom) {
		LangRoom langRoom = langRooms.get(
				new ServerRoom(
						roomNameOnServer,
						serverInterface
				)
				);

		if (langRoom != null) {
			// nothing
		} else {
			langRoom = new LangRoom(serverInterface, roomNameOnServer, this);
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
		}
	}

	@Override
	public void subscribedGame(
			ServerInterface server, String roomId) {
		GameOuterInfo game = gamePool.get(server, roomId);
		if (roomInterfaces.containsKey(new ServerRoom(game.id, server))) {
			return;
		}
		GameRoom containerRoom_Game = new GameRoom(
				game,
				this);
		gameRooms.put(
				new ServerRoom(game.id, server),
				containerRoom_Game
				);
		roomInterfaces.put(
				new ServerRoom(game.id, server),
				containerRoom_Game
				);
		tabbedPane.addTab(
				"<html><font color="
						+ GlobalGuiSettings.getHtmlColor(
								Memory.getPlayer1Color()
								)
						+ ">" + game.first.guiName
						+ "</font><font color=black> - </font><font color="
						+ GlobalGuiSettings.getHtmlColor(
								Memory.getPlayer2Color()
								)
						+ ">" + game.second.guiName + "</font></html>",
				containerRoom_Game, true
				);

		tabbedPane.setSelectedComponent(containerRoom_Game);
	};

	public synchronized void unsubsribedRoom(
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

	public synchronized void chatReceived(
			ServerInterface server,
			String room,
			String user,
			String message,
			Long time) {
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
				roomInterface.getRoomPart_Chat().addChat(user, message, time);
				tabbedPane.makeBold(roomInterface.getMainJPanel());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.narod.vn91.pointsop.gui.GuiForServerInterface#privateMessageReceived
	 * (ru.narod.vn91.pointsop.server.ServerInterface, java.lang.String,
	 * java.lang.String)
	 */
	public synchronized void privateMessageReceived(
			ServerInterface server,
			String userId,
			String message) {
		Player player = playerPool.get(server, userId);
		PrivateChat privateChat = privateChatList.get(player);
		if (privateChat == null) {
			// creating new panel
			privateChat = new PrivateChat(player);
			privateChatList.put(player, privateChat);
			tabbedPane.addTab(player.guiName, privateChat, true);
			tabbedPane.makeBold(privateChat);
		} else {
			if (tabbedPane.indexOfComponent(privateChat) == -1) {
				tabbedPane.addTab(
						player.guiName, privateChat, true
						); // resurrecting a closed panel
				tabbedPane.makeBold(privateChat);
			} else {
				// updating an old and not closed panel
				tabbedPane.makeBold(privateChat);
			}
		}
		privateChat.addChat(player.guiName, message, false);
	}

	@Override
	public void soundReceived(ServerInterface server, String user) {
		privateMessageReceived(server, user, "послан звуковой сигнал");
		new Sounds().playAlarmSignal();
	}

	public synchronized void createPrivateChatWindow(
			Player player
			) {
		PrivateChat privateChat = privateChatList.get(player);
		if (privateChat == null) {
			privateChat = new PrivateChat(player);
			privateChatList.put(player, privateChat);
			tabbedPane.addTab(player.guiName, privateChat, true);
			tabbedPane.setSelectedComponent(privateChat);
		} else {
			if (tabbedPane.indexOfComponent(privateChat) == -1) {
				tabbedPane.addTab(player.guiName, privateChat, true);
			}
			tabbedPane.setSelectedComponent(privateChat);
		}
	}

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

	@Override
	public void gameRowCreated(
			ServerInterface server,
			String masterRoom,
			String newRoom) {
		GameOuterInfo gameOuterInfo = gamePool.get(server, newRoom);
		RoomInterface roomInterface = roomInterfaces.get(
				new ServerRoom(masterRoom, server));
		if (roomInterface == null) {
			throw new UnsupportedOperationException(
					"creating a game with an incorrect MasterRoom");
		} else {
			roomInterface.getRoomPart_GameList().gameCreated(gameOuterInfo);
		}

	}

	@Override
	public void gameRowDestroyed(
			ServerInterface server,
			String oldRoom) {
		GameOuterInfo gameOuterInfo = gamePool.get(server, oldRoom);
		if (gameOuterInfo != null) {
			LangRoom room = langRooms.get(
					new ServerRoom(gameOuterInfo.masterRoomId, server));
			if (room != null) {
				room.getRoomPart_GameList().gameDestroyed(gameOuterInfo);
			}
		}
	}
//public synchronized void gameDestroyed(
//ServerInterface server,
//String masterRoom,
//String oldRoom) {
//GameInfo gameInfo = gamePool.get(server, oldRoom, masterRoom);
//LangRoom room = langRooms.get(new ServerRoom(masterRoom, server));
//if (room == null) {
//} else {
//room.getRoomPart_GameList().gameDestroyed(gameInfo);
//}
//}


//public synchronized void unsubsribedGame(
//		ServerInterface server,
//		String room) {
//	RoomInterface roomInterface = roomInterfaces.get(
//			new ServerRoom(
//					room,
//					server
//			)
//			);
//	if (roomInterface != null) {
//		tabbedPane.remove((Component) roomInterface);
//	}
//	roomInterfaces.remove(new ServerRoom(room, server));
//}

	@Override
	public void gameInviteReceived(
			ServerInterface server,
			String room,
			String possibleOpponent) {
		new Sounds().playAlarmSignal();
		server.acceptOpponent(room, possibleOpponent);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.narod.vn91.pointsop.gui.GuiForServerInterface#makedMove(ru.narod.vn91
	 * .pointsop.server.ServerInterface, java.lang.String, boolean, int, int,
	 * boolean, int, int)
	 */
	public synchronized void makedMove(
			ServerInterface server,
			String room,
			boolean silent,
			int x,
			int y,
			boolean isRed,
			boolean nowPlays,
			int timeLeftRed, int timeLeftBlue) {
		GameRoom gameRoom = gameRooms.get(new ServerRoom(room, server));
		if (gameRoom != null) {
			gameRoom.makeMove(silent, x, y, isRed, nowPlays, timeLeftRed,
					timeLeftBlue);
			tabbedPane.makeBold(gameRoom);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.narod.vn91.pointsop.gui.GuiForServerInterface#gameStop(ru.narod.vn91
	 * .pointsop.server.ServerInterface, java.lang.String, boolean)
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

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * ru.narod.vn91.pointsop.gui.GuiForServerInterface#gameLost(ru.narod.vn91
	 * .pointsop.server.ServerInterface, java.lang.String, boolean, boolean)
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

	@Override
	public synchronized void raw(ServerInterface server, String info) {
		String add = server.getServerName() + ": " + info + "\n";

		System.out.print(add);

		// String oldText = serverOutput.getText();
		// serverOutput.setText(oldText + add);
	}

	@Override
	public void rawError(ServerInterface server, String info) {
		this.raw(server, info);
		JOptionPane.showMessageDialog(
				tabbedPane,
				info,
				"Error: " + info,
				JOptionPane.ERROR_MESSAGE
				);
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
		hash = 29 * hash
				+ (this.serverInterface != null ? this.serverInterface.hashCode() : 0);
		return hash;
	}
}

//class ServerUserName {
//
//	String userName;
//	ServerInterface serverInterface;
//
//	public ServerUserName(
//			String userName,
//			ServerInterface serverInterface) {
//		this.userName = userName;
//		this.serverInterface = serverInterface;
//	}
//
//	@Override
//	public boolean equals(Object compareToObject) {
//		if (compareToObject instanceof ServerUserName == false) {
//			return false;
//			// avoid this stupid NetBeans hint...
//		}
//		if ((compareToObject == null)
//				|| (getClass().isInstance(compareToObject) == false)) {
//			return false;
//		}
//		ServerUserName compareTo = getClass().cast(compareToObject);
//		boolean userNameEquals = (userName == null)
//				? compareTo.userName == null
//				: userName.equals(compareTo.userName);
//		boolean serverEquals = (serverInterface == null)
//				? compareTo.serverInterface == null
//				: serverInterface.equals(compareTo.serverInterface);
//		return userNameEquals && serverEquals;
//	}
//
//	@Override
//	public int hashCode() {
//		int hash = 3;
//		hash = 29 * hash + (this.userName != null ? this.userName.hashCode() : 0);
//		hash = 29 * hash
//				+ (this.serverInterface != null ? this.serverInterface.hashCode() : 0);
//		return hash;
//	}
//}

class JTabbedPaneMod
		extends JTabbedPane {

	/**
	 * almost the same as the JTabbedPane.addTab method, but if isCloseable is
	 * true, then an icon for closing the tab is created.
	 *
	 * @param title
	 * @param component
	 * @param isCloseable
	 *          if yes, generates an icon for closing the tab.
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
		addChangeListener(new ChangeListener() {

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
		});
	}
}
