package ru.narod.vn91.pointsop.data;

import java.util.ArrayList;

import ru.narod.vn91.pointsop.data.Sgf.GameResult;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;

public class LinearGameConstructor_Unfinished {

	String redName;
	String blueName;
	int rank1;
	int rank2;
	int fieldSizeX;
	int fieldSizeY;
	String timeLimits;
	GameResult gameResult;
	int scoreRedMinusBlue;
	ArrayList<DotColored> moveList;
	boolean upsideDown;

	SingleGameEngine engine;

	public LinearGameConstructor_Unfinished(String redName, String blueName, int rank1,
			int rank2, int fieldSizeX, int fieldSizeY, String timeLimits,
			boolean upsideDown) {
		this.redName = redName;
		this.blueName = blueName;
		this.rank1 = rank1;
		this.rank2 = rank2;
		this.fieldSizeX = fieldSizeX;
		this.fieldSizeY = fieldSizeY;
		this.timeLimits = timeLimits;
		this.upsideDown = upsideDown;
		engine = new SingleGameEngine(fieldSizeX, fieldSizeY);
	}

	public boolean makeMove_ReturnIfOk(int x, int y, boolean isRed) {
		MoveResult moveResult = engine.makeMove(x, y, isRed);
		if (moveResult != MoveResult.ERROR) {
			moveList.add(new DotColored(x, y, isRed));
		}
		return moveResult.equals(MoveResult.ERROR) == false;
	}

//	public String getSgf() {
//
//	}

}
