package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

public class Wall {

	int x, y, length;
	private final int xCenter = 19;
	final int yCenter = 15;
	private boolean isAtSide = false;

	Wall(int xBegin, int yBegin) {
		x = xBegin;
		y = yBegin;
		length = Math.abs(x - xCenter) + Math.abs(y - yCenter);
	}

	int getLength() {
		length = Math.abs(x - xCenter) + Math.abs(y - yCenter);
		return length;
	}

	int getLengthFromLastBlue(int xB, int yB) {
		length = Math.abs(x - xB) + Math.abs(y - yB);
		return length;
	}

	boolean isAtSide() {
		if (x < 3 | y < 3 | x > 37 | y > 30) isAtSide = true;
		return isAtSide;
	}

}
