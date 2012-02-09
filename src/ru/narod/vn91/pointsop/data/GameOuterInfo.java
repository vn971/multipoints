package ru.narod.vn91.pointsop.data;

import java.awt.Color;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import ru.narod.vn91.pointsop.server.ServerInterface;
import ru.narod.vn91.pointsop.utils.Settings;

/**
 * information about the game that can be achieved not joining the game itself
 **/
public class GameOuterInfo {

	public final ServerInterface server;
	public final String id;

	public String masterRoomId = "";
	public Player first;
	public Player second;

	public Integer sizeX = 30, sizeY = 30;
	// public Boolean invertIncomingY = false;
	public Boolean isRedFirst = true;
	public Boolean isRated = false;
	// public Boolean iAmPlaying = false;
	public Integer handicapRed = 0;
	public Integer instantWin = 0;
	public Boolean manualEnclosings = false;
	public Boolean stopEnabled = true;
	public Boolean isEmptyScored = false;

	public String comment = "";
	public GameState state = GameState.Playing;

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

	Collection<WeakReference<GameInfoListener>> weak = new LinkedList<WeakReference<GameInfoListener>>();
	// private Set<GameInfoListener> changeListenerList = new LinkedHashSet<GameInfoListener>();

	public GameOuterInfo(ServerInterface server, String id) {
		this.server = server;
		this.id = id;
		this.first = null;
		this.first = null;
//		this.first = new Player(server, "first"); // avoid nulls
//		this.second = new Player(server, "second"); // avoid nulls
	}

	public GameOuterInfo(ServerInterface server, String id, String masterRoomId,
			Player first, Player second, Integer sizeX, Integer sizeY,
			Boolean isRedFirst, Boolean isRated, Integer handicapRed,
			Integer instantWin, Boolean manualEnclosings, Boolean stopEnabled,
			Boolean isEmptyScored, GameState state, Integer freeTemporalTime,
			Integer additionalAccumulatingTime, Integer startingTime,
			Integer periodLength, String comment) {
		this.server = server;
		this.id = id;
		this.masterRoomId = masterRoomId;
		this.first = first;
		this.second = second;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.isRedFirst = isRedFirst;
		this.isRated = isRated;
		this.handicapRed = handicapRed;
		this.instantWin = instantWin;
		this.manualEnclosings = manualEnclosings;
		this.stopEnabled = stopEnabled;
		this.isEmptyScored = isEmptyScored;
		this.state = state;
		this.freeTemporalTime = freeTemporalTime;
		this.additionalAccumulatingTime = additionalAccumulatingTime;
		this.startingTime = startingTime;
		this.periodLength = periodLength;
		this.comment = comment;
	}

	public void updateFrom(GameOuterInfo g) {
		for (Field field : g.getClass().getFields()) {
			try {
				if (field.get(g) != null) {
					field.set(this, field.get(g));
				}
			} catch (Exception e) {
			}
		}

		Iterator<WeakReference<GameInfoListener>> i = weak.iterator();
		while (i.hasNext()) {
			GameInfoListener changeListener = i.next().get();
			if (changeListener != null) {
				changeListener.onChange(this);
			} else {
				i.remove();
			}
		}

		// for (GameInfoListener changeListener : changeListenerList) {
		// changeListener.onChange(this);
		// }
	}

	public void addChangeListener(GameInfoListener changeListener) {
		weak.add(new WeakReference<GameInfoListener>(changeListener));
		// changeListenerList.add(changeListener);
	}

	public static int compare(GameOuterInfo game1, GameOuterInfo game2) {
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

	public boolean amIPlaying() {
		boolean amIFirst = first != null &&
			server != null &&
			server.getMyName().equals(first.id);
		boolean amISecond = second != null &&
			server != null &&
			server.getMyName().equals(second.id);
		return amIFirst || amISecond;
	}

	public Boolean amIRed() {
		if (amIPlaying() == false || first == null || second == null) {
			return null;
		} else if (server.getMyName().equals(first.id)) {
			return true;
		} else {
			return false;
		}
	}

	public Color player1Color() {
		Color red = Settings.getPlayer1Color();
		Color blue = Settings.getPlayer2Color();
		return isRedFirst ? red : blue;
	}

	public Color player2Color() {
		Color red = Settings.getPlayer1Color();
		Color blue = Settings.getPlayer2Color();
		return isRedFirst ? blue : red;
	}

	public String firstGuiNameFailsafe() {
		if (first == null) {
			return "???";
		} else {
			return first.guiName;
		}
	}

	public String secondGuiNameFailsafe() {
		if (second == null) {
			return "???";
		} else {
			return second.guiName;
		}
	}

	public enum GameState {
		SearchingOpponent, Playing, Reviewing, Closed
	}

}
