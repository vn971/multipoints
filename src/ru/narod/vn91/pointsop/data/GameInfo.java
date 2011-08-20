package ru.narod.vn91.pointsop.data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import ru.narod.vn91.pointsop.server.ServerInterface;

/**
 * representation of a game on the server
 **/
public class GameInfo {

	public final ServerInterface server;
	public final String id;

	public String masterRoomId = "";
	public Player first;
	public Player second;

	public GameState state = GameState.Playing;
	public Boolean isRated = false;
	public Integer handicapRed = 0;

	// time is given in seconds
	//
	// pointsxt-blitz: free = 5, period = 1
	// pointsxt-classical: free=180, period = 5
	// zagram: starting+additional

	// this time is given free every 'periodLength'
	// turns. If you use less - nothing gets stored for
	// future. If you use more - the overhead will be subtracted
	// from the reserve (if any).
	public Integer freeTemporalTime = 0;

	// every 'periodLength' turns you'll get additional time
	public Integer additionalAccumulatingTime = 0;

	public Integer startingTime = 0;

	// period in turns. See above.
	public Integer periodLength = 0;

	private Collection<GameInfoListener> changeListenerList = new ArrayList<GameInfoListener>();

	public GameInfo(ServerInterface server, String id) {
		super();
		this.server = server;
		this.id = id;
	}

	public GameInfo(ServerInterface server, String id, String masterRoomId,
			Player red, Player blue, GameState state, Boolean isRated,
			Integer handicapRed, Integer freeTemporalTime,
			Integer additionalAccumulatingTime, Integer startingTime,
			Integer periodLength) {
		super();
		this.server = server;
		this.id = id;
		this.masterRoomId = masterRoomId;
		this.first = red;
		this.second = blue;
		this.state = state;
		this.isRated = isRated;
		this.handicapRed = handicapRed;
		this.freeTemporalTime = freeTemporalTime;
		this.additionalAccumulatingTime = additionalAccumulatingTime;
		this.startingTime = startingTime;
		this.periodLength = periodLength;
	}


	public void updateFrom(GameInfo g) {
		for (Field field : g.getClass().getFields()) {
			try {
				if (field.get(g) != null) {
					field.set(this, field.get(g));
				}
			} catch (Exception e) {
			}
		}
		for (GameInfoListener changeListener : changeListenerList) {
			changeListener.onChange(this);
		}
	}

	public void addChangeListener(GameInfoListener changeListener) {
		changeListenerList.add(changeListener);
	}

	public static int compare(GameInfo game1, GameInfo game2) {
		return 1; // game 1 is always bigger
	}

	public String getTimeAsString() {
		if (additionalAccumulatingTime == 0 && startingTime == 0) {
			return String.format("%ss / %st",
					freeTemporalTime, periodLength);
		} else if (freeTemporalTime == 0 && periodLength == 1) {
			return String.format("%s + *%s",
					startingTime, additionalAccumulatingTime);
		} else {
			return String.format("start=%s add=%s period=%s free=%s",
					startingTime, additionalAccumulatingTime,
					periodLength, freeTemporalTime);
		}
	}

	public enum GameState {
		SearchingOpponent, Playing, Reviewing, Closed
	}

}