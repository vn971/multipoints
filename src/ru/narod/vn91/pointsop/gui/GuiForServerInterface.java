package ru.narod.vn91.pointsop.gui;

import java.awt.Image;

import ru.narod.vn91.pointsop.server.ServerInterface;

public interface GuiForServerInterface {

	public enum MessageType {

		INFO, ERROR,
	}

	/**
	 * Server implementations should execute this method if they get disconnected from a physical server
	 *
	 * @param server always return <b>this</b>
	 */
	public abstract void serverClosed(ServerInterface server);

	public abstract void addUserInfo(
			ServerInterface server, String id,
			String guiName, Image image,
			Integer rating, Integer winCount, Integer lossCount, Integer drawCount,
			String status);

	public abstract void userJoinedRoom(
			ServerInterface server,
			String room,
			String id,
			boolean isStartup);

	public abstract void userLeftRoom(
			ServerInterface server,
			String room,
			String id);

	public abstract void userDisconnected(
			ServerInterface server,
			String user);

	public abstract void subscribedLangRoom(
			String roomNameOnServer,
			ServerInterface serverInterface,
			String guiRoomName,
			boolean isServersMainRoom);

	public abstract void subscribedGame(
			String roomNameOnServer,
			ServerInterface server,
			String idRed,
			String idBlue,
			String timeLimits,
			boolean isRated,
			String startingPosition,
			boolean chatReadOnly,
			boolean amIPlaying,
			boolean amIRed);

	public abstract void unsubsribedRoom(
			ServerInterface server,
			String room);

	public abstract void unsubsribedGame(
			ServerInterface server,
			String room);

	public abstract void chatReceived(
			ServerInterface server,
			String room,
			String user,
			String message);

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

	public abstract void gameCreated(
			ServerInterface server,
			String masterRoom,
			String newRoom,
			String user1,
			String user2,
			String settings);

	public abstract void gameVacancyCreated(
			ServerInterface server,
			String masterRoom,
			String newRoom,
			String user,
			String settings);

	public abstract void gameVacancyDestroyed(
			ServerInterface server,
			String masterRoom,
			String oldRoom);

	public abstract void gameRequestReceived(
			ServerInterface server,
			String room,
			String possibleOpponent);

	/**
	 * game is destroyed from list of games (NOT tabs of Lang-rooms!)
	 *
	 * @param server
	 * @param masterRoom
	 * @param oldRoom
	 */
	public abstract void gameDestroyed(
			ServerInterface server,
			String masterRoom,
			String oldRoom);

	public abstract void makedMove(
			ServerInterface server,
			String room,
			boolean silent,
			int x,
			int y,
			boolean isRed,
			boolean nowPlays,
			int timeLeftRed, int timeLeftBlue);

	public abstract void gameStop(
			ServerInterface server,
			String room,
			boolean isRedPlayer);

	public abstract void gameLost(
			ServerInterface server,
			String room,
			boolean isRedLooser,
			boolean wantToSave);

	public abstract void raw(
			ServerInterface server,
			String info);

	public abstract void rawError(
			ServerInterface server,
			String info);

}