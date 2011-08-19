package ru.narod.vn91.pointsop.gui;

import java.awt.Image;

import ru.narod.vn91.pointsop.data.GameInfo;
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

	/*
	 * if something is unknown yet -- null may be passed
	 */
	public abstract void addUserInfo(
			ServerInterface server, String id,
			String guiName, Image image,
			Integer rating, Integer winCount, Integer lossCount, Integer drawCount,
			String status);

	/*
	 * if something is unknown yet -- null may be passed
	 */
	public abstract void addGameInfo(
			ServerInterface server, String id, String masterRoomId,
			String redId, String blueId,
			GameInfo.GameState gameState, Boolean isRated, Integer handicapRed,
			Integer freeTemporalTime,
			Integer additionalAccumulatingTime,
			Integer startingTime,
			Integer periodLength
			);

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
			ServerInterface server,
			String roomId,
			boolean chatReadOnly,
			boolean amIPlaying,
			boolean amIRed);

	public abstract void unsubsribedRoom(
			ServerInterface server,
			String room);

	public abstract void gameRowCreated(
			ServerInterface server,
			String masterRoom,
			String newRoom);

	public abstract void gameRowDestroyed(
			ServerInterface server,
			String oldRoom);

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

	public abstract void gameInviteReceived(
			ServerInterface server,
			String room,
			String possibleOpponent);

	public abstract void makedMove(
			ServerInterface server,
			String roomId,
			boolean silent,
			int x,
			int y,
			boolean wasRed,
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