package ru.narod.vn91.pointsop.server;

import ru.narod.vn91.pointsop.data.TimeSettings;

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
	public void createGameVacancy() {
	}

	@Override
	public void acceptGameVacancyOpponent(String roomName, String newOpponent) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void rejectGameVacancyOpponent(String roomName, String notWantedOpponent) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void askGameVacancyPlay(String gameRoomName) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void stopGameVacancy() {
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

	@Override
	public boolean isField20x20Allowed() {
		return true;
	}

	@Override
	public boolean isField25x25Allowed() {
		return false;
	}

	@Override
	public boolean isField30x30Allowed() {
		return false;
	}

	@Override
	public boolean isField39x32Allowed() {
		return false;
	}

	@Override
	public boolean isStartingEmptyFieldAllowed() {
		return true;
	}

	@Override
	public boolean isStartingCrossAllowed() {
		return false;
	}

	@Override
	public boolean isStarting4CrossAllowed() {
		return false;
	}

	@Override
	public TimeSettings getTimeSettingsMaximum() {
		return new TimeSettings(10, 10, 0, 1, 0);
	}

	@Override
	public TimeSettings getTimeSettingsMinimum() {
		return new TimeSettings(1, 1, 0, 1, 0);
	}

	@Override
	public void askPersonalGame(String playerId, TimeSettings settings, int fieldX, int fieldY) {
	}

	@Override
	public boolean isPrivateGameInviteAllowed() {
		return true;
	}
}
