package ru.narod.vn91.pointsop.server;

public interface ServerInterface {

	void connect();

	void disconnecttt();

	void searchOpponent();

	void requestJoinGame(String gameRoomName);

	void acceptOpponent(String roomName, String newOpponent);

	public void stopSearchingOpponent();

	void makeMove(String roomName, int x, int y);

	void surrender(String roomName);

	void subscribeRoom(String name);

	void unsubscribeRoom(String name);

	void sendChat(String room, String message);

	public void sendPrivateMsg(String target, String message);

	/**
	 * @return my name on the server
	 */
	String getMyName();

	String getMainRoom();

	String getServerName();
}

