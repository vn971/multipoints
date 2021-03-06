package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BlueSurroundSecurity implements Variables {

	Point moveAI;
	public int foundedNumber;
	final List<Point[]> BSSbase = new ArrayList<>();
	Point bss[];

	public Point getAIcoordinatesFromBSSbase(PointsAI pointsAI) {
		String[][] fieldState = pointsAI.game.getFieldState();
		for (Object aBSSbase : BSSbase) {
			SingleGameEngine e = new SingleGameEngine(39, 32);
			for (int i = 0; i < 39; i++)
				for (int j = 0; j < 32; j++) {
					if (fieldState[i][j].equals("R")) e.makeMove(i + 1, j + 1, true);
					if (fieldState[i][j].equals("B")) e.makeMove(i + 1, j + 1, false);
				}
			bss = (Point[]) aBSSbase;
			for (Point bs : bss) {
				e.makeMove(bs.x, bs.y, false);
			}
			Point point_RED_PROTECTION = bss[0];
			if (e.getBlueScore() > 0) {
				return point_RED_PROTECTION;
			}
		}
		return null;
	}

	public boolean isFoundArea(String content, PointsAI pointsAI) {
		for (int i = PointsAI.base.base.length - 1; i >= 0; i--) {
			if (PointsAI.base.base[i].isEqualsLikeArea(content, TemplateType.BLUE_SURROUND)) {
				getAIcoordinatesFromBST(PointsAI.base.base[i].targetRotateTemplate, pointsAI);
				foundedNumber = i;
				return true;
			}
		}
		foundedNumber = 0;
		return false;
	}

	private void getAIcoordinatesFromBST(String content, PointsAI pointsAI) {
		String[][] fieldState = pointsAI.game.getFieldState();
		SingleGameEngine e = new SingleGameEngine(39, 32);
		for (int i = 0; i < 39; i++)
			for (int j = 0; j < 32; j++) {
				if (fieldState[i][j].equals("R")) e.makeMove(i + 1, j + 1, true);
				if (fieldState[i][j].equals("B")) e.makeMove(i + 1, j + 1, false);
			}

		Point point_RED_PROTECTION = getAIcoordinates(content, DotType.RED_PROTECTION);
		List<Point> point_BLUE_NORMAL = getCoordinates(content, DotType.BLUE_NORMAL);
		if (point_BLUE_NORMAL != null) {
			bss = new Point[point_BLUE_NORMAL.size() + 1];
			makeMove(e, point_RED_PROTECTION, pointsAI);
			makeMove(e, point_BLUE_NORMAL, pointsAI);
		} else {
			bss = new Point[1];
			makeMove(e, point_RED_PROTECTION, pointsAI);
		}

		if (e.getBlueScore() > 0)
			moveAI = getAIcoordinates(content, DotType.RED_PROTECTION);
		else {
			moveAI = getAIcoordinates(content, DotType.RED_NORMAL);
			BSSbase.add(bss);
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

	private void makeMove(SingleGameEngine e, List<Point> point, PointsAI pointsAI) {
		Iterator<Point> it = point.iterator();
		int index = 1;
		while (it.hasNext()) {
			Point p = it.next();
			int moveX = p.x + pointsAI.game.getLastX() - 4;
			int moveY = p.y + pointsAI.game.getLastY() - 4;
			e.makeMove(moveX, moveY, false);
			bss[index] = new Point(moveX, moveY);
			index++;
		}
	}

	private void makeMove(SingleGameEngine e, Point point, PointsAI pointsAI) {
		int moveX = point.x + pointsAI.game.getLastX() - 4;
		int moveY = point.y + pointsAI.game.getLastY() - 4;
		e.makeMove(moveX, moveY, false);
		bss[0] = new Point(moveX, moveY);
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
