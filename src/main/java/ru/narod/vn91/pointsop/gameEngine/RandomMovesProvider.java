package ru.narod.vn91.pointsop.gameEngine;

import ru.narod.vn91.pointsop.data.Dot;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.DotType;

public class RandomMovesProvider {

	final int dimX;
	final int dimY;
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
			moves[i] = i;
		}
		for (int i = 0; i < dimX * dimY; i++) {
			int transposeIndex = getRandom(i, dimX * dimY - 1);
			int backup = moves[i];
			moves[i] = moves[transposeIndex];
			moves[transposeIndex] = backup;
		}
	}

	/**
	 * @return next random move. This is done _fast_.
	 */
	public Dot getNextDot() {
		cursor += 1;
		if (cursor < dimX * dimY) {
			return new Dot(
					1 + (moves[cursor] % dimX),
					1 + (moves[cursor] / dimX));
		} else {
			return null;
		}
	}

	public Dot findEmptyRandomPlace(SingleGameEngineInterface engine) {
		Dot dot;
		do {
			dot = getNextDot();
		} while (dot != null &&
				engine!=null &&
				engine.getDotType(dot.x, dot.y).notIn(
				DotType.EMPTY, DotType.BLUE_CTRL, DotType.RED_CTRL));
		return dot;
	}
}
