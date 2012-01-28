package ru.narod.vn91.pointsop.model;

import javax.swing.ImageIcon;
import ru.narod.vn91.pointsop.data.TimeLeft;
import ru.narod.vn91.pointsop.data.GameOuterInfo.GameState;
import ru.narod.vn91.pointsop.server.ServerInterface;

public interface GuiForServerInterface {

	public enum MessageType {

		INFO, ERROR,
	}

	/**
	 * Server implementations should execute this method if they get disconnected
	 * from a physical server
	 * 
	 * @param server
	 *          always return <b>this</b>
	 */
	public void serverClosed(ServerInterface server);

	/*
	 * fields initialized by null will not be updated
	 */
	public void updateUserInfo(
			ServerInterface server, String id,
			String guiName, ImageIcon imageIcon,
			Integer rating, Integer winCount, Integer lossCount, Integer drawCount,
			String status);

	/*
	 * fields initialized by null will not be updated
	 */
	public void updateGameInfo(
			ServerInterface server, String id, String masterRoomId,
			String firstId, String secondId, Integer sizeX, Integer sizeY,
			Boolean isRedFirst, Boolean isRated,
			Integer handicapRed, Integer instantWin, Boolean manualEnclosings,
			Boolean stopEnabled, Boolean isEmptyScored, GameState state,
			Integer freeTemporalTime, Integer additionalAccumulatingTime,
			Integer startingTime, Integer periodLength, String comment
			);

	public void userJoinedRoom(ServerInterface server, String room, String userId, boolean isStartup);

	public void userLeftRoom(ServerInterface server, String roomId, String userId, String reason);

	public void userDisconnected(ServerInterface server, String user, String additionalMessage);

	// subscribtions
	public void subscribedLangRoom(
			String roomNameOnServer,
			ServerInterface serverInterface,
			String guiRoomName,
			boolean isServersMainRoom);

	public void subscribedGame(ServerInterface server, String roomId);

	public void unsubscribedRoom(ServerInterface server, String room);

	// game rows
	public void gameRowCreated(ServerInterface server, String masterRoom, String newRoom);

	public void gameRowDestroyed( ServerInterface server, String oldRoom);

	void statusSet(ServerInterface server, boolean isBusy);
	
	// personal gameInvites
	public void personalInviteReceived(ServerInterface server, String userId, String gameId);

	public void personalInviteCancelled(ServerInterface server, String userId, String gameId);

	public void yourPersonalInviteSent(ServerInterface server, String userId, String gameId);

	public void yourPersonalInviteRejected(ServerInterface server, String userId, String gameId);

	public void youCancelledPersonalInvite(ServerInterface server, String userId, String gameId);

	public void chatReceived(
			ServerInterface server,
			String room,
			String user,
			String message,
			Long time);

	public void privateMessageReceived(ServerInterface server, String user, String message);

	public void soundReceived(ServerInterface server, String user);

	public void serverNoticeReceived(ServerInterface server, String room, String message);

	public void askedPlay(ServerInterface server, String room, String possibleOpponent);

	public void makedMove(
			ServerInterface server,
			String roomId,
			boolean silent,
			int x,
			int y,
			boolean isRed,
			boolean nowPlays
			);

	public void gameStop(ServerInterface server, String room, boolean isRedPlayer);

	public void gameLost(ServerInterface server, String room, boolean isRedLooser, boolean wantToSave);

	public void askedNewGame(ServerInterface server, String roomId, boolean you);
	public void acceptedNewGame(ServerInterface server, String roomId, boolean you);
	public void rejectedNewGame(ServerInterface server, String roomId, boolean you);

	public void askedEndGameAndScore(ServerInterface server, String roomId, boolean you);
	public void acceptedEndGameAndScore(ServerInterface server, String roomId, boolean you);
	public void rejectedEndGameAndScore(ServerInterface server, String roomId, boolean you);

	public void askedUndo(ServerInterface server, String roomId, boolean you);
	public void acceptedUndo(ServerInterface server, String roomId, boolean you);
	public void rejectedUndo(ServerInterface server, String roomId, boolean you);

	public void askedDraw(ServerInterface server, String roomId, boolean you);
	public void acceptedDraw(ServerInterface server, String roomId, boolean you);
	public void rejectedDraw(ServerInterface server, String roomId, boolean you);

	public void pausedOpponentTime(ServerInterface server, String roomId, boolean you);
	public void unpausedOpponentTime(ServerInterface server, String roomId, boolean you);
	public void addedOpponentTime(ServerInterface server, String roomId, int seconds, boolean you);

	public void timeUpdate(ServerInterface server, String room, TimeLeft t);

	public void raw(ServerInterface server, String info);

	public void rawError(ServerInterface server, String info);

	public void rawConnectionState(ServerInterface server, String info);

}