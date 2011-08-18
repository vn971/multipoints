package ru.narod.vn91.pointsop.data;

import java.util.ArrayList;
import java.util.Collection;

import ru.narod.vn91.pointsop.server.ServerInterface;


// pointsxt-blitz: free = 5, period = 1
// pointsxt-classical: free=180, period = 5
// zagram: starting+additional
public class GameInfo {

	ServerInterface server;
	String id;
	Player red;
	Player blue;

	Integer handicapRed = 0;

	// time is given in seconds

	// this time is given free every 'periodLength'
	// turns. If you use less - nothing gets stored for
	// future. If you use more - the overhead will be subtracted
	// from the reserve (if any).
	Integer freeTemporalTime = 0;

	// every 'periodLength' turns you'll get additional time
	Integer additionalAccumulatingTime = 0;

	Integer startingTime = 0;

	// period in turns. See above.
	Integer periodLength = 0;

	Collection<GameInfoListener> gameInfoListeners = new ArrayList<GameInfoListener>();

	public GameInfo(ServerInterface server, String id) {
		super();
		this.server = server;
		this.id = id;
	}

	public GameInfo(ServerInterface server, String id, Player red, Player blue,
			Integer handicapRed, Integer freeTemporalTime,
			Integer additionalAccumulatingTime, Integer startingTime,
			Integer periodLength) {
		super();
		this.server = server;
		this.id = id;
		this.red = red;
		this.blue = blue;
		this.handicapRed = handicapRed;
		this.freeTemporalTime = freeTemporalTime;
		this.additionalAccumulatingTime = additionalAccumulatingTime;
		this.startingTime = startingTime;
		this.periodLength = periodLength;
	}


}
