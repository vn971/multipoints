package ru.narod.vn91.pointsop.server;

class GameInfoAbstract {

	String userFirst = null, userSecond = null; // users a.k.a. players
	int rank1 = 1, rank2 = 1;
	String timeLimits = null; // not rethinked good enough. Currently could be 5, 180/5,...
	boolean isRated = false;
	String startingPosition = null;

	String getTimeAndIsRated() {
		return timeLimits + ", " + ((isRated) ? "rated" : "unrated");
	}
}
