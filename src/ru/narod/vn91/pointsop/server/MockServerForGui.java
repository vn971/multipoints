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
	public void acceptPersonalGameInvite(String playerId) {
	}

	@Override
	public void cancelPersonalGameInvite(String playerId) {
	}

	@Override
	public void rejectPersonalGameInvite(String playerId) {
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
	public void stop(String roomName) {
	}

	@Override
	public void askNewGame(String roomName) {
	}

	@Override
	public void cancelAskingNewGame(String roomId) {
	}

	@Override
	public void acceptNewGame(String roomName) {
	}

	@Override
	public void rejectNewGame(String roomName) {
	}

	@Override
	public void askEndGameAndScore(String roomName) {
	}

	@Override
	public void cancelAskingEndGameAndScore(String roomId) {
	}

	@Override
	public void acceptEndGameAndScore(String roomName) {
	}

	@Override
	public void rejectEndGameAndScore(String roomName) {
	}

	@Override
	public void askUndo(String roomName) {
	}

	@Override
	public void cancelAskingUndo(String roomId) {
	}

	@Override
	public void acceptUndo(String roomName) {
	}

	@Override
	public void rejectUndo(String roomName) {
	}

	@Override
	public void askDraw(String roomName) {
	}

	@Override
	public void cancelAskingDraw(String roomId) {
	}

	@Override
	public void acceptDraw(String roomName) {
	}

	@Override
	public void rejectDraw(String roomName) {
	}

	@Override
	public void pauseOpponentTime(String roomName) {
	}

	@Override
	public void unpauseOpponentTime(String roomName) {
	}

	@Override
	public void addOpponentTime(String roomName, int seconds) {
	}

	@Override
	public boolean isStopEnabled() {
		return false;
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
		return false;
	}

	@Override
	public boolean isGuiYInverted() {
		return false;
	}

	@Override
	public String coordinatesToString(Integer xOrNull, Integer yOrNull) {
		if (xOrNull != null && yOrNull != null) {
			return String.format("%02d:%02d", xOrNull - 1, yOrNull - 1);
		} else if (xOrNull != null) {
			return String.format("%02d", xOrNull - 1);
		} else if (yOrNull != null) {
			return String.format("%02d", yOrNull - 1);
		} else {
			return "";
		}
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
	public TimeSettings getTimeSettingsDefault() {
		return new TimeSettings(5, 5, 0, 1, 0);
	}

	@Override
	public void addPersonalGameInvite(String playerId, TimeSettings settings, int fieldX, int fieldY) {
	}

	@Override
	public boolean isPrivateGameInviteAllowed() {
		return true;
	}

	@Override
	public boolean isGlobalGameVacancyAllowed() {
		return false;
	}

	@Override
	public void setStatus(boolean isBusy) {
	}

}
