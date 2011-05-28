package com.google.sites.priymakpoints.pointsai.p_PointsAI;

import java.util.ArrayList;
import java.util.List;

public interface SingleGameEngineInterface {

	public int getSizeX();

	public int getSizeY();

	public DotType getDotType(int x, int y);

	public MoveResult makeMove(int x, int y, MoveType moveType);

	public MoveResult makeMove(int x, int y, boolean isRed);

	public boolean canMakeMove(int x, int y);

	public List<SurroundingAbstract> getSurroundings();

	public DotAbstract getLastDot();

	public int getRedScore();

	public int getBlueScore();

	class DotAbstract {public int x, y;	}

	public void tryRandomMove();

	public enum DotType {
			EMPTY, BLUE, RED, RED_EATED_BLUE, BLUE_EATED_RED, RED_TIRED, BLUE_TIRED, BLUE_CTRL, RED_CTRL, BLUE_EATED_EMPTY, RED_EATED_EMPTY;
			public String toString() {
				switch (this) {
				case EMPTY:	return "N";//empty
				case BLUE:	return "B";//blue
				case BLUE_CTRL:return "N";//blue домик
				case BLUE_EATED_EMPTY:return "B";
				case BLUE_EATED_RED:return "B";//my points
				case BLUE_TIRED:return "B";//null blue
				case RED:return "R";//red
				case RED_CTRL:return "N";//red домик
				case RED_EATED_BLUE:return "R";//
				case RED_EATED_EMPTY:return "R";
				case RED_TIRED:return "R";
				default:return "$";
				}
			}
	}

	public enum MoveType {BLUE, RED;}

	public enum MoveResult {NOTHING, GOOD, BAD, ERROR}

	public class MoveInfoAbstract {

		public int coordX, coordY;
		public MoveType moveType;
		public MoveResult moveResult;
		public List<SurroundingAbstract> newSurroundings;
			public boolean isRed() {return moveType == MoveType.RED;}
			public MoveInfoAbstract() {}
			public MoveInfoAbstract(int coordX, int coordY, MoveType moveType, MoveResult moveResult, List<SurroundingAbstract> newSurroundings) {
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

		public enum SurroundingType {RED, BLUE, RED_CTRL, BLUE_CTRL	}

		public boolean isRed() {
			if (type == SurroundingType.RED) {
				return true;
			} else if (type == SurroundingType.BLUE) {
				return false;
			} else {
				throw new UnsupportedOperationException("Error casting SurroundingType to boolean.");
			}
		}
	}
}
