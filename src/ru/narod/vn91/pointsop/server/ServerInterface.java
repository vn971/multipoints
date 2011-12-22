package ru.narod.vn91.pointsop.server;

import ru.narod.vn91.pointsop.data.TimeSettings;

public interface ServerInterface {

	void connect();

	void disconnectServer();

	void searchOpponent();

	void acceptOpponent(String roomName, String newOpponent);

	void rejectOpponent(String roomName, String notWantedOpponent);

	void requestPlay(String gameRoomName);

	void invitePlayer(String playerId, TimeSettings settings, int fieldX, int fieldY);

	public void stopSearchingOpponent();

	void makeMove(String roomName, int x, int y);

	void surrender(String roomName);

	void subscribeRoom(String room);

	void unsubscribeRoom(String room);

	void sendChat(String room, String message);

	void sendPrivateMsg(String target, String message);
	
	void getUserInfoText(String user);

	void getUserpic(String user);

	String getMyName();

	String getMainRoom();

	String getServerName();

	int getMaximumMessageLength();

	boolean isIncomingYInverted();

	boolean isGuiYInverted();

	boolean isPrivateChatEnabled();

	boolean isPingEnabled();

	boolean isSoundNotifyEnabled();
	
	boolean isPrivateGameInviteAllowed();

	boolean isField20x20Allowed();

	boolean isField25x25Allowed();

	boolean isField30x30Allowed();

	boolean isField39x32Allowed();

	boolean isStartingEmptyFieldAllowed();

	boolean isStartingCrossAllowed();

	boolean isStarting4CrossAllowed();

	TimeSettings getTimeSettingsMaximum();

	TimeSettings getTimeSettingsMinimum();

	String coordinatesToString(Integer xOrNull, Integer yOrNull);
//	CoordinatesFormatter getCoordinatesFormatter();
	
//	interface CoordinatesFormatter {
//		String format(Integer xOrNull, Integer yOrNull);
//	}
}

