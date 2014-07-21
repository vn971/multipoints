package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RedSurroundSecurity implements Variables {

	Point moveAI;
	public int foundedNumber;
	public int foundedIndex;

	public boolean isFoundArea(String content, PointsAI pointsAI) {
		for (int i = PointsAI.base.base.length - 1; i >= 0; i--) {
			if (PointsAI.base.base[i].isEqualsLikeArea(content, TemplateType.RED_SURROUND)) {
				getAIcoordinatesFromRST(PointsAI.base.base[i].targetRotateTemplate, pointsAI);
				foundedNumber = i;
				foundedIndex = PointsAI.base.base[i].getTemplateIndex();
				return true;
			}
		}
		foundedNumber = 0;
		return false;
	}

	private void getAIcoordinatesFromRST(String content, PointsAI pointsAI) {
		String[][] fieldState = pointsAI.game.getFieldState();
		SingleGameEngine e = new SingleGameEngine(39, 32);
		for (int i = 0; i < 39; i++)
			for (int j = 0; j < 32; j++) {
				if (fieldState[i][j].equals("R")) e.makeMove(i + 1, j + 1, true);
				if (fieldState[i][j].equals("B")) e.makeMove(i + 1, j + 1, false);
			}

		Point point_RED_NORMAL = getAIcoordinates(content, DotType.RED_NORMAL);
		List<Point> point_RED_ATTACK = getCoordinates(content, DotType.RED_ATTACK);
		if (point_RED_ATTACK != null) {
			makeMove(e, point_RED_NORMAL, pointsAI, true);
			makeMove(e, point_RED_ATTACK, pointsAI, true);
		} else {
			makeMove(e, point_RED_NORMAL, pointsAI, true);
		}

		if (e.getRedScore() > 0) moveAI = getAIcoordinates(content, DotType.RED_NORMAL);
		else {
			moveAI = null;
		}
	}

	private List<Point> getCoordinates(String content, DotType dot) {
		List<Point> point = new ArrayList<>();
		for (int i = 0; i < content.length(); i++)
			if (content.substring(i, i + 1).equals(dot.toString())) {
				point.add(new Point(i % sizeX_TE, i / sizeX_TE));
			}
		return point;
	}

	private void makeMove(SingleGameEngine e, List<Point> point, PointsAI pointsAI, boolean isRed) {
		for (Point p : point) {
			int moveX = p.x + pointsAI.game.getLastX() - 4;
			int moveY = p.y + pointsAI.game.getLastY() - 4;
			e.makeMove(moveX, moveY, isRed);
		}
	}

	private void makeMove(SingleGameEngine e, Point point, PointsAI pointsAI, boolean isRed) {
		int moveX = point.x + pointsAI.game.getLastX() - 4;
		int moveY = point.y + pointsAI.game.getLastY() - 4;
		e.makeMove(moveX, moveY, isRed);
	}

	private Point getAIcoordinates(String content, DotType dot) {
		for (int i = 0; i < content.length(); i++)
			if (content.substring(i, i + 1).equals(dot.toString())) {
				return new Point(i % sizeX_TE, i / sizeX_TE);
			}
		return null;
	}

	public Point getMoveAI() {
		return moveAI;
	}

}
