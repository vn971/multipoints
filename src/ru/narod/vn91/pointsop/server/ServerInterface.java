package ru.narod.vn91.pointsop.server;

public interface ServerInterface {

	void connect();

	void disconnectServer();

	void searchOpponent();

	void acceptOpponent(String roomName, String newOpponent);

	void rejectOpponent(String roomName, String notWantedOpponent);

	void requestPlay(String gameRoomName);

	public void stopSearchingOpponent();

	void makeMove(String roomName, int x, int y);

	void surrender(String roomName);

	void subscribeRoom(String room);

	void unsubscribeRoom(String room);

	void sendChat(String room, String message);

	public void sendPrivateMsg(String target, String message);

	/**
	 * @return my name on the server
	 */
	String getMyName();

	String getMainRoom();

	String getServerName();

	int getMaximumMessageLength();
}

