package ru.narod.vn91.pointsop.gui;

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
	public abstract void serverClosed(ServerInterface server);

	/*
	 * fields initialized by null will not be updated
	 */
	public abstract void updateUserInfo(
			ServerInterface server, String id,
			String guiName, ImageIcon imageIcon,
			Integer rating, Integer winCount, Integer lossCount, Integer drawCount,
			String status);

	/*
	 * fields initialized by null will not be updated
	 */
	public abstract void updateGameInfo(
			ServerInterface server, String id, String masterRoomId,
			String firstId, String secondId, Integer sizeX, Integer sizeY,
			Boolean isRedFirst, Boolean isRated,
			Integer handicapRed, Integer instantWin, Boolean manualEnclosings,
			Boolean stopEnabled, Boolean isEmptyScored, GameState state,
			Integer freeTemporalTime, Integer additionalAccumulatingTime,
			Integer startingTime, Integer periodLength, String comment
			);

	public abstract void userJoinedRoom(
			ServerInterface server,
			String room,
			String id,
			boolean isStartup);

	public abstract void userLeftRoom(
			ServerInterface server,
			String roomId,
			String userId,
			String reason);

	public abstract void userDisconnected(
			ServerInterface server,
			String user,
			String additionalMessage);

	// subscribtions
	public abstract void subscribedLangRoom(
			String roomNameOnServer,
			ServerInterface serverInterface,
			String guiRoomName,
			boolean isServersMainRoom);

	public abstract void subscribedGame(
			ServerInterface server,
			String roomId
			);

	public abstract void unsubscribedRoom(
			ServerInterface server,
			String room);

	// game rows
	public abstract void gameRowCreated(
			ServerInterface server,
			String masterRoom,
			String newRoom);

	public abstract void gameRowDestroyed(
			ServerInterface server,
			String oldRoom);

	// personal gameInvites
	public void personalInviteReceived(
			ServerInterface server,
			String userId,
			String gameId);

	public void personalInviteCancelled(
			ServerInterface server,
			String userId,
			String gameId);

	public void yourPersonalInviteSent(
			ServerInterface server,
			String userId,
			String gameId);

	public void yourPersonalInviteRejected(
			ServerInterface server,
			String userId,
			String gameId);
	
	public void youCancelledPersonalInvite(
			ServerInterface server,
			String userId,
			String gameId);

	public abstract void chatReceived(
			ServerInterface server,
			String room,
			String user,
			String message,
			Long time);

	public abstract void privateMessageReceived(
			ServerInterface server,
			String user,
			String message);

	public abstract void soundReceived(
			ServerInterface server,
			String user);

	public abstract void serverNoticeReceived(
			ServerInterface server,
			String room,
			String message);

	public abstract void askedPlay(
			ServerInterface server,
			String room,
			String possibleOpponent);

	public abstract void makedMove(
			ServerInterface server,
			String roomId,
			boolean silent,
			int x,
			int y,
			boolean isRed,
			boolean nowPlays
			);

	public abstract void gameStop(
			ServerInterface server,
			String room,
			boolean isRedPlayer);

	public abstract void gameLost(
			ServerInterface server,
			String room,
			boolean isRedLooser,
			boolean wantToSave);

	public abstract void timeUpdate(
			ServerInterface server,
			String room,
			TimeLeft t
			);

	public abstract void raw(
			ServerInterface server,
			String info);

	public abstract void rawError(
			ServerInterface server,
			String info);

	public abstract void rawConnectionState(
			ServerInterface server,
			String info);

}