package ru.narod.vn91.pointsop.data.game;

import ru.narod.vn91.pointsop.data.DotColored;

public class AddDotCalculateEnclosings implements Action {

	int x, y;
	boolean isRed;

	public AddDotCalculateEnclosings(int x, int y, boolean isRed) {
		this.x = x;
		this.y = y;
		this.isRed = isRed;
	}

	@Override
	public void call(Game game) {
		game.addPaperDot(new DotColored(x, y, isRed));

		// calculate surroundings
	}

}
