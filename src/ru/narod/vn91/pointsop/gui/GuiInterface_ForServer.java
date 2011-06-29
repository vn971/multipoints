package ru.narod.vn91.pointsop.gui;

import ru.narod.vn91.pointsop.server.ServerInterface;

public interface GuiInterface_ForServer {
	void serverClosed(ServerInterface server);

	void userJoinedLangRoom(
			ServerInterface server,
			String room,
			String user,
			boolean silent,
			int rank,
			String status);

	void userJoinedGameRoom(
			ServerInterface server,
			String room,
			String user,
			boolean silent,
			int rank,
			String status);

	void userLeavedRoom(
			ServerInterface server,
			String room,
			String user);

	void userDisconnected(
			ServerInterface server,
			String user);

	void subscribedLangRoom(
			String roomNameOnServer,
			ServerInterface serverInterface,
			String guiRoomName,
			boolean isServersMainRoom);

	void subscribedGame(
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
			boolean amIPlaying);

	void unsubsribedRoom(
			ServerInterface server,
			String room);

	void unsubsribedGame(
			ServerInterface server,
			String room);

	void chatReceived(
			ServerInterface server,
			String room,
			String user,
			String message);

	void privateMessageReceived(
			ServerInterface server,
			String user,
			String message);

	void createPrivateChatWindow(
			ServerInterface server,
			String user);

	void serverNoticeReceived(
			ServerInterface server,
			String room,
			String message);

	void gameCreated(
			ServerInterface server,
			String masterRoom,
			String newRoom,
			String user1,
			String user2,
			String settings);

	void gameVacancyCreated(
			ServerInterface server,
			String masterRoom,
			String newRoom,
			String user,
			String settings);

	void gameDestroyed(
			ServerInterface server,
			String masterRoom,
			String oldRoom);

	void gameVacancyDestroyed(
			ServerInterface server,
			String masterRoom,
			String oldRoom);

	void makedMove(
			ServerInterface server,
			String room,
			boolean silent,
			int x,
			int y,
			boolean isRed);

	void gameStop(
			ServerInterface server,
			String room,
			boolean isRedPlayer);

	void gameLost(
			ServerInterface server,
			String room,
			boolean isRedLooser,
			boolean wantToSave);

	void receiveRawServerInfo(
			ServerInterface server,
			String info,
			GuiController.MessageType type);
}
