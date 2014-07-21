package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.DotType;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;

public class PointsAIGame implements Variables {

	int last_x;
	int last_y;
	private SingleGameEngine singleGameEngine;

	PointsAIGame() {
		newGame();
	}

	void newGame() {
		singleGameEngine = new SingleGameEngine(sizeX, sizeY);
	}

	public String[][] getFieldState() {
		String[][] fieldState = new String[39][32];
		for (int x = 1; x <= sizeX; x++)
			for (int y = 1; y <= sizeY; y++) {
				DotType dotType = singleGameEngine.getDotType(x, y);
				String type = "N";
				switch (dotType) {
					case EMPTY:	type="N";break;//empty
					case BLUE:	type="B";break;//blue
					case BLUE_CTRL: type="N";break;//blue домик
					case BLUE_EATED_EMPTY: type="B";break;
					case BLUE_EATED_RED: type="B";break;//my points
					case BLUE_TIRED: type="B";break;//null blue
					case RED: type="R";break;//red
					case RED_CTRL: type="N";break;//red домик
					case RED_EATED_BLUE: type="R";break;//
					case RED_EATED_EMPTY: type="R";break;
					case RED_TIRED: type="R";break;
				}
				fieldState[x - 1][y - 1] = type;// <get field state>
			}
		return fieldState;
	}

	void makeMove(int x, int y, MoveType moveType) {
		singleGameEngine.makeMove(x, y, moveType);
		last_x = x;
		last_y = y;
	}

	public int getLastX() {
		return last_x;
	}

	public int getLastY() {
		return last_y;
	}

	public boolean isCanMakeMove(int x, int y) {
		return singleGameEngine.canMakeMove(x, y);
	}

}
