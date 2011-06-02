package ru.narod.vn91.pointsop.ai;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;

public class RandomAi implements Gui2Ai_Interface {

	Ai2Gui_Interface gui;
	SingleGameEngineInterface engine;
	boolean aiColor = false;

	public RandomAi(Ai2Gui_Interface gui,
			int sizeX,
			int sizeY) {
		this.gui = gui;
		this.engine = new SingleGameEngine(sizeX, sizeY);
	}

	public void init() {
		{
			// makeRandomMove
			MoveResult moveResult = engine.tryRandomMove(!aiColor);

			if (moveResult != MoveResult.ERROR) {
				int x = engine.getLastDot().x;
				int y = engine.getLastDot().y;
				gui.makeMove(x, y, !aiColor, 0, null, 1);
			}
		}
	}

	public void receiveMove(int x,
			int y,
			boolean isRed,
			boolean toBeAnswered,
			long timeExpected) {
		MoveResult moveResult = engine.makeMove(x, y, !isRed);
		if (moveResult == MoveResult.GOOD) {
			// ai accepted this move
			gui.makeMove(x, y, !isRed, 1,
					"чёёёрт, меня съели!! Ну ничего, я ещё отомщу!", 1);
		} else if (moveResult != MoveResult.ERROR) {
			// ai accepted this move
			gui.makeMove(x, y, !isRed, 1, null, 1);
		}

		if ((moveResult != MoveResult.ERROR)
				&& (toBeAnswered)) {
			// return random answer
			MoveResult randomResult = engine.tryRandomMove(isRed);
			int answerX = engine.getLastDot().x;
			int answerY = engine.getLastDot().y;
			if (randomResult != MoveResult.ERROR) {
				gui.makeMove(answerX, answerY, isRed, 0, null, 1);
			} else {
			}
		}
	}

	public void dispose() {
		// we didn't use any threads so we do nothing
	}
}
