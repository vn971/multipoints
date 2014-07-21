package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;

import java.awt.*;

public class PointsAI implements Variables {

	public static TemplateEngine base;
	public final PointsAIGame game;
	public final MoveAI moveAI;

	public PointsAI() {
		moveAI = new MoveAI();
		base = new TemplateEngine();
		game = new PointsAIGame();
	}

	public void makeMove(int x, int y, boolean isRed) {
		if (isRed) {
			game.makeMove(x, y, MoveType.RED);
		} else {
			game.makeMove(x, y, MoveType.BLUE);
		}

	}

	public Point getAIMove() {
		Point point = moveAI.getAImove(this);
		game.makeMove(point.x, point.y, MoveType.RED);
		return point;
	}

}
