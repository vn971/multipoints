package ru.narod.vn91.pointsop.server;

import ru.narod.vn91.pointsop.data.TimeSettings;

public interface ServerInterface {

	void connect();

	void disconnectServer();

	// game vacancy:
	void createGameVacancy();

	public void stopGameVacancy();

	void askGameVacancyPlay(String gameRoomName);

	void acceptGameVacancyOpponent(String roomName, String newOpponent);

	void rejectGameVacancyOpponent(String roomName, String notWantedOpponent);

	// personal invites:
	void addPersonalGameInvite(String playerId, TimeSettings settings, int fieldX, int fieldY);

	void cancelPersonalGameInvite(String playerId);

	void acceptPersonalGameInvite(String playerId);

	void rejectPersonalGameInvite(String playerId);

	// room actions:
	void subscribeRoom(String room);

	void unsubscribeRoom(String room);

	// game actions:
	void makeMove(String roomName, int x, int y);

	void surrender(String roomName);

	void sendChat(String room, String message);

	void sendPrivateMsg(String target, String message);

	// other:
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

}
