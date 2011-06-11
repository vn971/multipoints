package ru.narod.vn91.pointsop.gameEngine;

import ru.narod.vn91.pointsop.data.DotAbstract;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.DotType;

public class RandomMovesProvider {

	int dimX;
	int dimY;
	int cursor;
	int[] moves = null;

	private int getRandom(
			int from,
			int to) {
		return (int)(from + Math.random() * (to - from + 1) - 0.5);
	}

	public RandomMovesProvider(
			int dimX,
			int dimY) {
		this.dimX = dimX;
		this.dimY = dimY;
		cursor = -1;
		moves = new int[dimX * dimY];
		for (int i = 0; i < dimX * dimY; i++) {
			moves[i] = getRandom(i, dimX * dimY - 1);
		}
	}

	/**
	 * @return next random move. This is done _fast_.
	 */
	public DotAbstract getNextDot() {
		cursor += 1;
		if (cursor < dimX * dimY) {
			return new DotAbstract(
					1 + (moves[cursor] % dimX),
					1 + (moves[cursor] / dimX));
		} else {
			return null;
		}
	}

	public DotAbstract findEmptyRandomPlace(SingleGameEngineInterface engine) {
		DotAbstract dot;
		do {
			dot = getNextDot();
		} while (dot != null
				&& engine.getDotType(dot.x, dot.y).notIn(
				DotType.EMPTY, DotType.BLUE_CTRL, DotType.RED_CTRL));
		return dot;
	}
}
