package ru.narod.vn91.pointsop.gameEngine;

import java.util.ArrayList;
import java.util.List;

// all coordinates start with 1 (because coordinates on a real Chess/Goe board start with 1)
public interface SingleGameEngineInterface {

	public int getSizeX();

	public int getSizeY();

	public DotType getDotType(int x,
			int y);

	public MoveResult makeMove(int x,
			int y,
			MoveType moveType);

	public MoveResult makeMove(int x,
			int y,
			boolean isRed);

	public boolean canMakeMove(int x,
			int y);

	// public void setStopMode(MoveType moveType);
	// public DotAbstract getStopMove();
	/**
	 *
	 * @return list of NON-CTRL surroundings
	 */
	public List<SurroundingAbstract> getSurroundings();

	public DotAbstract getLastDot();

	public boolean getLastDotColor();

	public int getRedScore();

	public int getBlueScore();

	class DotAbstract {

		public int x, y;
	}

	public MoveResult tryRandomMove(boolean isRed);

	public enum DotType {

		EMPTY, BLUE, RED, RED_EATED_BLUE, BLUE_EATED_RED, RED_TIRED, BLUE_TIRED, BLUE_CTRL, RED_CTRL, BLUE_EATED_EMPTY, RED_EATED_EMPTY;

		// this method is placed here because I cannot add this method in the
		// interface implementation.
		@Override
		public String toString() {
			switch (this) {
				case EMPTY:
					return " ";
				case BLUE:
					return "o";
				case BLUE_CTRL:
					return "?";
				case BLUE_EATED_EMPTY:
					return ",";
				case BLUE_EATED_RED:
					return "@";
				case BLUE_TIRED:
					return "O";
				case RED:
					return "x";
				case RED_CTRL:
					return "¿";
				case RED_EATED_BLUE:
					return "#";
				case RED_EATED_EMPTY:
					return "'";
				case RED_TIRED:
					return "X";
				default:
					return "$";
			}
		}

		static boolean dotTypeIsIn(DotType element,
				DotType... set) {
			for (int i = 0; i < set.length; i++) {
				if (set[i] == element) {
					return true;
				}
			}
			return false;
		}

		public boolean isIn(DotType... set) {
			return dotTypeIsIn(this, set);
		}

		public boolean notIn(DotType... set) {
			return !isIn(set);
		}

		public boolean isEmpty() {
			return isIn(DotType.EMPTY,DotType.RED_CTRL,DotType.BLUE_CTRL);
		}
	}

	public enum MoveType {

		BLUE, RED;
	}

	public enum MoveResult {

		NOTHING, GOOD, BAD, ERROR
		// gooded = eated, bad = eated back, nothing = normal move, error =
		// error.
	}

	public class MoveInfoAbstract {

		public int coordX, coordY;
		public MoveType moveType;
		public MoveResult moveResult;
		public List<SurroundingAbstract> newSurroundings;

		public boolean isRed() {
			return moveType == MoveType.RED;
		}

		public MoveInfoAbstract() {
		}

		public MoveInfoAbstract(int coordX,
				int coordY,
				MoveType moveType,
				MoveResult moveResult,
				List<SurroundingAbstract> newSurroundings) {
			this.coordX = coordX;
			this.coordY = coordY;
			this.moveType = moveType;
			this.moveResult = moveResult;
			this.newSurroundings = newSurroundings;
		}
	}

	public class SurroundingAbstract {

		public SurroundingType type;
		public ArrayList<DotAbstract> path;
		public ArrayList<DotAbstract> capturedPoints;
		public DotAbstract firstCapturedEnemy;

		public enum SurroundingType {

			RED, BLUE, RED_CTRL, BLUE_CTRL
		}

		public boolean isRed() {
			if (type == SurroundingType.RED) {
				return true;
			} else if (type == SurroundingType.BLUE) {
				return false;
			} else {
				throw new UnsupportedOperationException(
						"Error casting SurroundingType to boolean.");
			}
		}
	}
}
