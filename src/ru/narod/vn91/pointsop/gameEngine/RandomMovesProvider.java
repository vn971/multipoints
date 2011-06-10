package ru.narod.vn91.pointsop.gameEngine;

import ru.narod.vn91.pointsop.data.DotAbstract;

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
					moves[cursor] % dimX,
					moves[cursor] / dimY);
		} else {
			return null;
		}
	}
}
