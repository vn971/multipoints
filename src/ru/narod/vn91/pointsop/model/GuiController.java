package ru.narod.vn91.pointsop.model;

import java.awt.Component;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

import ru.narod.vn91.pointsop.data.GameInfoListener;
import ru.narod.vn91.pointsop.data.GameOuterInfo;
import ru.narod.vn91.pointsop.data.GamePool;
import ru.narod.vn91.pointsop.data.Player;
import ru.narod.vn91.pointsop.data.PlayerPool;
import ru.narod.vn91.pointsop.data.TimeLeft;
import ru.narod.vn91.pointsop.data.GameOuterInfo.GameState;
import ru.narod.vn91.pointsop.gui.GameRoom;
import ru.narod.vn91.pointsop.gui.GuiCommon;
import ru.narod.vn91.pointsop.gui.JTabbedPaneMod;
import ru.narod.vn91.pointsop.gui.LangRoom;
import ru.narod.vn91.pointsop.gui.PrivateChat;
import ru.narod.vn91.pointsop.gui.RoomInterface;
import ru.narod.vn91.pointsop.gui.RoomPart_Chat;
import ru.narod.vn91.pointsop.gui.RoomPart_Userlist;
import ru.narod.vn91.pointsop.server.AiVirtualServer;
import ru.narod.vn91.pointsop.server.ServerInterface;
import ru.narod.vn91.pointsop.sounds.Sounds;
import ru.narod.vn91.pointsop.utils.Function;
import ru.narod.vn91.pointsop.utils.Function0;
import ru.narod.vn91.pointsop.utils.Settings;

public class GuiController implements GuiForServerInterface {

	World world;
	JTabbedPaneMod tabbedPane;
	public JTextPane serverOutput;
	ServerInterface pointsxt_tochkiorg_server;
	ServerInterface pointsxt_ircworldru_server;
	public ServerInterface pointsxt_vn91_server;
	public ServerInterface zagram_server;
	ServerInterface pointsopServer;
	HashMap<ServerRoom, RoomInterface> roomInterfaces = new HashMap<ServerRoom, RoomInterface>();
	HashMap<ServerRoom, GameRoom> gameRooms = new HashMap<ServerRoom, GameRoom>();
	HashMap<ServerRoom, LangRoom> langRooms = new HashMap<ServerRoom, LangRoom>();
	HashMap<Player, PrivateChat> privateChatList = new HashMap<Player, PrivateChat>();
	PlayerPool playerPool = new PlayerPool();
	GamePool gamePool = new GamePool();

	public GuiController(final JTabbedPaneMod tabbedPane) {
		this.tabbedPane = tabbedPane;
	}

	public synchronized void serverClosed(ServerInterface server) {
		if (server == pointsxt_ircworldru_server) {
			pointsxt_ircworldru_server.disconnectServer();
			pointsxt_ircworldru_server = null;
		} else if (server == pointsxt_tochkiorg_server) {
			pointsxt_tochkiorg_server.disconnectServer();
			pointsxt_tochkiorg_server = null;
		} else if (server == zagram_server) {
			zagram_server.disconnectServer();
			zagram_server = null;
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
	public synchronized void updateUserInfo(
			ServerInterface server, String id,
			String guiName, ImageIcon imageIcon,
			Integer rating, Integer winCount, Integer lossCount, Integer drawCount,
			String status) {
		Player player = playerPool.get(server, id);
		int previousRating = player.rating;
		Player updateInstance = new Player(server, id, guiName, rating, winCount,
			lossCount, drawCount, imageIcon, status);
		player.updateFrom(updateInstance);
		if (rating != null &&
			previousRating != rating &&
			previousRating != 0) {
			this.serverNoticeReceived(server, server.getMainRoom(),
				player.guiName + " " + previousRating + " -> " + rating);
		}
	}

	@Override
	public synchronized void updateGameInfo(
			ServerInterface server, String id, String masterRoomId,
			String firstId, String secondId, Integer sizeX, Integer sizeY,
			Boolean isRedFirst, Boolean isRated,
			Integer handicapRed, Integer instantWin, Boolean manualEnclosings,
			Boolean stopEnabled, Boolean isEmptyScored, GameState state,
			Integer freeTemporalTime, Integer additionalAccumulatingTime,
			Integer startingTime, Integer periodLength, String comment
			) {
		GameOuterInfo gameOuterInfo = gamePool.get(server, id);
		Player first = playerPool.get(server, firstId);
		Player second = playerPool.get(server, secondId);

		GameOuterInfo updateInstance = new GameOuterInfo(
			server, secondId, masterRoomId, first, second,
			sizeX, sizeY,
			isRedFirst, isRated, handicapRed, instantWin,
			manualEnclosings, stopEnabled, isEmptyScored, state, freeTemporalTime,
			additionalAccumulatingTime, startingTime, periodLength, comment);
		gameOuterInfo.updateFrom(updateInstance);
	}

	@Override
	public synchronized void userJoinedRoom(ServerInterface server, String room, String id,
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

	@Override
	public synchronized void userLeftRoom(
			ServerInterface server,
			String roomId,
			String userId,
			String reason) {
		Player player = playerPool.get(server, userId);
		RoomInterface roomInterface = roomInterfaces.get(
				new ServerRoom(
					roomId,
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
				chat.addUserLeftNotice(player.guiName, reason);
			}
		}
	}

	public synchronized void userDisconnected(
			ServerInterface server,
			String id,
			String additionalMessage) {
		Player player = playerPool.get(server, id);
		PrivateChat privateChat = privateChatList.get(player);
		if ((privateChat != null) &&
			(tabbedPane.contains(privateChat))) {
			if (additionalMessage != null && additionalMessage.equals("") == false) {
				privateChat.addChat("server",
					player.guiName + " вышел из игры: " + additionalMessage,
					true);
			} else {
				privateChat.addChat("server", player.guiName + " вышел из игры.", true);
			}
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
	public synchronized void subscribedGame(
			ServerInterface server, String roomId) {
		final GameOuterInfo game = gamePool.get(server, roomId);
		if (roomInterfaces.containsKey(new ServerRoom(game.id, server))) {
			return;
		}
		final GameRoom containerRoom_Game = new GameRoom(
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

		final Function<Void, String> game2html = new Function<Void, String>() {
			@Override
			public String call(Void input) {
				return String.format("<html><font color=%s>%s</font>" +
					"<font color=black> - </font>" +
					"<font color=%s>%s</font></html>",
					GuiCommon.getHtmlColor(game.player1Color()),
					game.first.guiName,
					GuiCommon.getHtmlColor(game.player2Color()),
					game.second.guiName
						);
			}
		};
		tabbedPane.addTab(game2html.call(null), containerRoom_Game, true);
		tabbedPane.setCloseListener_FalseIfStopClosing(
			containerRoom_Game,
			new Function0<Boolean>() {
				@Override
				public Boolean call() {
					return containerRoom_Game.userAsksClose();
				}
			});
		tabbedPane.setSelectedComponent(containerRoom_Game);
		Settings.AddColorsChangeListener(new Function<Void, Void>() {
			@Override
			public Void call(Void input) {
				synchronized (GuiController.this) {
					tabbedPane.updateTabText(containerRoom_Game, game2html.call(null));
				}
				return null;
			}
		});
		game.addChangeListener(
				new GameInfoListener() {
					@Override
					public void onChange(GameOuterInfo gameOuterInfo) {
						synchronized (GuiController.this) {
							tabbedPane.updateTabText(containerRoom_Game, game2html.call(null));
						}
					}
				}
				);

	};

	public synchronized void unsubscribedRoom(
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

	public synchronized void privateMessageReceived(
			final ServerInterface server,
			final String userId,
			String message) {

		Player player = playerPool.get(server, userId);
		PrivateChat privateChat = privateChatList.get(player);
		if (privateChat == null) {
			// creating new panel
			privateChat = new PrivateChat(player);
			privateChatList.put(player, privateChat);
			tabbedPane.addTab(player.guiName, privateChat, true);
		} else {
			if (tabbedPane.contains(privateChat) == false) {
				tabbedPane.addTab(
					player.guiName, privateChat, true
						); // resurrecting a closed panel
			}
		}
		tabbedPane.setCloseListener_FalseIfStopClosing(privateChat, new Function0<Boolean>() {
			@Override
			public Boolean call() {
				server.rejectPersonalGameInvite(userId);
				// server.cancelPersonalGameInvite(userId);
				return true;
			}
		});

		tabbedPane.makeBold(privateChat);
		privateChat.addChat(player.guiName, message, false);
	}

	@Override
	public synchronized void soundReceived(ServerInterface server, String user) {
		privateMessageReceived(server, user, "послан звуковой сигнал");
		Sounds.playAlarmSignal();
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
			if (tabbedPane.contains(privateChat) == false) {
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
	public synchronized void gameRowCreated(
			ServerInterface server,
			String masterRoom,
			String newRoom) {
		GameOuterInfo gameOuterInfo = gamePool.get(server, newRoom);
		RoomInterface roomInterface = roomInterfaces.get(
				new ServerRoom(masterRoom, server));
		if (roomInterface == null) {
			this.raw(server, "creating a game with an incorrect MasterRoom");
		} else {
			roomInterface.getRoomPart_GameList().gameCreated(gameOuterInfo);
		}

	}

	@Override
	public synchronized void gameRowDestroyed(
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

	@Override
	public synchronized void statusSet(ServerInterface server, boolean isBusy) {
		
	}
	
	@Override
	public synchronized void personalInviteCancelled(ServerInterface server, String userId, String gameId) {
		Player player = playerPool.get(server, userId);
		PrivateChat privateChat = privateChatList.get(player);
		if (privateChat == null) {
			// creating new panel
			privateChat = new PrivateChat(player);
			privateChatList.put(player, privateChat);
			tabbedPane.addTab(player.guiName, privateChat, true);
		} else {
			if (tabbedPane.contains(privateChat) == false) {
				tabbedPane.addTab(
					player.guiName, privateChat, true
						); // resurrecting a closed panel
			} else {
				// updating an old and not closed panel
			}
		}
		tabbedPane.makeBold(privateChat);

		privateChat.personalInviteCancelled();
	}

	@Override
	public synchronized void personalInviteReceived(ServerInterface server, String userId, String gameId) {
		Player player = playerPool.get(server, userId);
		PrivateChat privateChat = privateChatList.get(player);
		if (privateChat == null) {
			// creating new panel
			privateChat = new PrivateChat(player);
			privateChatList.put(player, privateChat);
			tabbedPane.addTab(player.guiName, privateChat, true);
		} else {
			if (tabbedPane.contains(privateChat) == false) {
				tabbedPane.addTab(
					player.guiName, privateChat, true
						); // resurrecting a closed panel
			} else {
				// updating an old and not closed panel
			}
		}
		tabbedPane.makeBold(privateChat);

		GameOuterInfo gameOuterInfo = gamePool.get(server, gameId);

		privateChat.personalInviteReceived(gameOuterInfo);
	}

	@Override
	public synchronized void youCancelledPersonalInvite(ServerInterface server, String userId, String gameId) {
		Player player = playerPool.get(server, userId);
		PrivateChat privateChat = privateChatList.get(player);
		if (privateChat == null) {
			// creating new panel
			privateChat = new PrivateChat(player);
			privateChatList.put(player, privateChat);
			tabbedPane.addTab(player.guiName, privateChat, true);
		} else {
			if (tabbedPane.contains(privateChat) == false) {
				tabbedPane.addTab(
					player.guiName, privateChat, true
						); // resurrecting a closed panel
			} else {
				// updating an old and not closed panel
			}
		}
		tabbedPane.makeBold(privateChat);

		privateChat.yourPersonalInviteCancelled();
	}

	@Override
	public synchronized void yourPersonalInviteRejected(ServerInterface server, String userId, String gameId) {
		Player player = playerPool.get(server, userId);
		PrivateChat privateChat = privateChatList.get(player);
		if (privateChat == null) {
			// creating new panel
			privateChat = new PrivateChat(player);
			privateChatList.put(player, privateChat);
			tabbedPane.addTab(player.guiName, privateChat, true);
		} else {
			if (tabbedPane.contains(privateChat) == false) {
				tabbedPane.addTab(
					player.guiName, privateChat, true
						); // resurrecting a closed panel
			} else {
				// updating an old and not closed panel
			}
		}
		tabbedPane.makeBold(privateChat);

		privateChat.yourPersonalInviteRejected();
	}

	@Override
	public synchronized void yourPersonalInviteSent(ServerInterface server, String userId, String gameId) {
		Player player = playerPool.get(server, userId);
		PrivateChat privateChat = privateChatList.get(player);
		if (privateChat == null) {
			// creating new panel
			privateChat = new PrivateChat(player);
			privateChatList.put(player, privateChat);
			tabbedPane.addTab(player.guiName, privateChat, true);
		} else {
			if (tabbedPane.contains(privateChat) == false) {
				tabbedPane.addTab(
					player.guiName, privateChat, true
						); // resurrecting a closed panel
			} else {
				// updating an old and not closed panel
			}
		}
		tabbedPane.makeBold(privateChat);

		privateChat.yourPersonalInviteSent();
	}

	@Override
	public synchronized void askedPlay(
			ServerInterface server,
			String room,
			String possibleOpponent) {
		Sounds.playAlarmSignal();
		server.acceptGameVacancyOpponent(room, possibleOpponent);
	}

	@Override
	public synchronized void makedMove(ServerInterface server, String roomId, boolean silent,
			int x, int y, boolean isRed, boolean nowPlays) {
		GameRoom gameRoom = gameRooms.get(new ServerRoom(roomId, server));
		if (gameRoom != null) {
			gameRoom.makeMove(silent, x, y, isRed, nowPlays
					);
			tabbedPane.makeBold(gameRoom);
		}
	}

	public synchronized void makedMove(
			ServerInterface server,
			String room,
			boolean silent,
			int x,
			int y,
			boolean isRed,
			boolean nowPlays,
			int timeLeftRed, int timeLeftBlue) {
	}

	@Override
	public synchronized void timeUpdate(
			ServerInterface server,
			String room,
			TimeLeft t) {
		GameRoom gameRoom = gameRooms.get(new ServerRoom(room, server));
		if (gameRoom != null) {
			gameRoom.updateTime(t);
		}
	}

	public synchronized void gameStop(
			ServerInterface server,
			String room,
			boolean isRedPlayer) {
		GameRoom room_Game = gameRooms.get(new ServerRoom(room, server));
		if (room_Game != null) {
			gameRooms.get(new ServerRoom(room, server)).stopGame(isRedPlayer);
		}
	}

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
	public synchronized void askedNewGame(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент предложил вам начать новую игру.", null);
		// }
	}

	@Override
	public synchronized void acceptedNewGame(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент согласился на ваше предложение новой игры.", null);
		// }
	}

	@Override
	public synchronized void rejectedNewGame(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент отклонил ваше предложение на новую игру.", null);
		// }
	}

	@Override
	public synchronized void askedEndGameAndScore(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент предложил завершить игру и подсчитать очки.", null);
		// }
	}

	@Override
	public synchronized void acceptedEndGameAndScore(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент согласился на завершение игры и подсчёт очков.", null);
		// }
	}

	@Override
	public synchronized void rejectedEndGameAndScore(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент отклонил предложение на завершение игры и подсчёт очков.",
		// null);
		// }
	}

	@Override
	public synchronized void askedUndo(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент спрашивает взятие хода назад.", null);
		// }
	}

	@Override
	public synchronized void acceptedUndo(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент согласился взять ход назад.", null);
		// }
	}

	@Override
	public synchronized void rejectedUndo(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент отказался взять ход назад.", null);
		// }
	}

	@Override
	public synchronized void askedDraw(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент предлагает вам закончить игру ничьёй.", null);
		// }
	}

	@Override
	public synchronized void acceptedDraw(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент согласился на ничью.", null);
		// }
	}

	@Override
	public synchronized void rejectedDraw(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент отказался от ничьей.", null);
		// }
	}

	@Override
	public synchronized void pausedOpponentTime(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент запаузил ваше время.", null);
		// }
	}

	@Override
	public synchronized void unpausedOpponentTime(ServerInterface server, String roomId, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server",
		// "Оппонент возобновил течение вашего времени.", null);
		// }
	}

	@Override
	public synchronized void addedOpponentTime(ServerInterface server, String roomId, int seconds, boolean you) {
		// GameRoom room_Game = gameRooms.get(new ServerRoom(roomId, server));
		// if (room_Game != null) {
		// room_Game.getRoomPart_Chat().addChat("server", "Оппонент прибавил вам " +
		// seconds + " секунд", null);
		// }
	}
	
	@Override
	public synchronized void raw(ServerInterface server, String info) {
		String add = new Date() + " " + server.getServerName() + ": " + info + "\n";

		this.privateMessageReceived(server, server.getServerName(), info);
		if (Settings.isDebug() == true) {
			System.out.print(add);
		} else {
		}
	}

	@Override
	public synchronized void rawError(ServerInterface server, final String info) {
		this.raw(server, info);
		new Thread() {
			public void run() {
				JOptionPane.showMessageDialog(
					null,
					info,
					"Error: " + info,
					JOptionPane.ERROR_MESSAGE
						);
			};
		}.start();
	}

	@Override
	public synchronized void rawConnectionState(ServerInterface server, String info) {
		String add = new Date() + " " + server.getServerName() + ": " + info + "\n";
		if (Settings.isDebug() == true) {
			System.out.print(add);
		} else {
		}
		String oldText = serverOutput.getText();
		serverOutput.setText(oldText + add);
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


