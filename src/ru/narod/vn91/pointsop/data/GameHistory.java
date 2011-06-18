package ru.narod.vn91.pointsop.data;

import java.util.ArrayList;
import java.util.List;

public class GameHistory {

	int dimX, dimY;
	boolean redStarts;
	List<Move> moves = new ArrayList<Move>();

	boolean isRedMoveNow() {
		return (redStarts && moves.isEmpty())
				|| (moves.get(moves.size() - 1).isBlue());
	}

	public String toSgf() {
		return "";
	}

	public class Entry {
	}

	public class Move extends Entry {

		boolean isRed;
		DotAbstract coords;

		boolean isBlue() {
			return !isRed;
		}
	}

	public class Unto extends Entry {
	}
}
