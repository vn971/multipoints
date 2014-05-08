package ru.narod.vn91.pointsop.data.game;

import ru.narod.vn91.pointsop.data.DotColored;

public class AddDotWithoutEnclosings implements Action {

	int x, y;
	boolean isRed;
	boolean isLousy;

	public AddDotWithoutEnclosings(int x, int y, boolean isRed, boolean isLousy) {
		super();
		this.x = x;
		this.y = y;
		this.isRed = isRed;
		this.isLousy = isLousy;
	}


	@Override
	public void call(Game game) {
		game.addPaperDot(new DotColored(x, y, isRed));
	}

}
