package ru.narod.vn91.pointsop.server;

public class MockServerForGui implements ServerInterface{

	@Override
	public void connect() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void disconnectServer() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void searchOpponent() {
	}

	@Override
	public void acceptOpponent(String roomName, String newOpponent) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void rejectOpponent(String roomName, String notWantedOpponent) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void requestPlay(String gameRoomName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void stopSearchingOpponent() {
	}

	@Override
	public void makeMove(String roomName, int x, int y) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void surrender(String roomName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void subscribeRoom(String room) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void unsubscribeRoom(String room) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendChat(String room, String message) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void sendPrivateMsg(String target, String message) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getMyName() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getMainRoom() {
		return "";
	}

	@Override
	public String getServerName() {
		return "";
}

	@Override
	public int getMaximumMessageLength() {
		return 10;
	}

	@Override
	public boolean isIncomingYInverted() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isGuiYInverted() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String coordinatesToString(Integer xOrNull, Integer yOrNull) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public boolean isPrivateChatEnabled() {
		return false;
	}

	@Override
	public boolean isPingEnabled() {
		return false;
	}

	@Override
	public boolean isSoundNotifyEnabled() {
		return false;
	}

	public void getUserInfoText(String user) {
	}

	public void getUserpic(String user) {
	}
}
