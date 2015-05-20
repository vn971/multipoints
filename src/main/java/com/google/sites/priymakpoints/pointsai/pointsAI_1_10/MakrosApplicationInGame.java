package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.awt.*;

public class MakrosApplicationInGame {

	public Template template;
	public int curLevel;
	public int centerX, centerY;
	public boolean isEnabled = true;
	public boolean isVertical;
	public RotationType targetRotateType;
	public final MakrosLevelMove[] moves;
	public Point transformAIPoint;
	String preLevels = "";

	public MakrosApplicationInGame(Makros makros) {
		curLevel = 0;
		moves = makros.getMoves();
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public void setCenter(int x, int y) {
		if (template.getTemplateType().isSide() & template.getTemplateType().isSquare()) {
			if (x < 9) {
				centerX = 4;
				centerY = y;
			} else if (x > 30) {
				centerX = 36;
				centerY = y;
			} else if (y < 9) {
				centerY = 4;
				centerX = x;
			} else if (y > 23) {
				centerY = 29;
				centerX = x;
			}
		} else if (template.getTemplateType().isSide() & !template.getTemplateType().isSquare()) {
			if (isVertical) {
				if (x < 9) {
					centerX = 4;
					centerY = y;
				} else if (x > 30) {
					centerX = 36;
					centerY = y;
				} else if (y < 13) {
					centerY = 6;
					centerX = x;
				} else if (y > 19) {
					centerY = 27;
					centerX = x;
				}
			} else {
				if (x < 13) {
					centerX = 6;
					centerY = y;
				} else if (x > 26) {
					centerX = 34;
					centerY = y;
				} else if (y < 9) {
					centerY = 4;
					centerX = x;
				} else if (y > 23) {
					centerY = 29;
					centerX = x;
				}
			}
		} else {
			centerX = x;
			centerY = y;
		}
	}

	public boolean isExistsLevelMove(int x, int y) {
		for (MakrosLevelMove move : moves) {
			if (move.levelNumber == (curLevel + 1)) {
				String templateWithMakrosPoints = template.getTemplate();

				int index = move.humanPoint.x - 1 + (move.humanPoint.y - 1) * 13;
				templateWithMakrosPoints = templateWithMakrosPoints.substring(0, index) + DotType.MAKROS_BLUE.toString() + templateWithMakrosPoints.substring(index + 1);

				index = move.AIPoint.x - 1 + (move.AIPoint.y - 1) * 13;
				try {
					templateWithMakrosPoints = templateWithMakrosPoints.substring(0, index) + DotType.MAKROS_RED.toString() + templateWithMakrosPoints.substring(index + 1);
				} catch (Exception ignored) {
				}

				templateWithMakrosPoints = getRotateTemplate(targetRotateType, templateWithMakrosPoints);

				Point transformHumanPoint = new Point(template.getPointCoordinates(templateWithMakrosPoints, DotType.MAKROS_BLUE));

				try {
					transformAIPoint = new Point(template.getPointCoordinates(templateWithMakrosPoints, DotType.MAKROS_RED));
				} catch (Exception exc) {
					if (template.getTemplateType().isSquare()) {
						if ((centerX + transformHumanPoint.x - 4) == x & (centerY + transformHumanPoint.y - 4) == y & preLevels.equals(move.preLevels)) {
							transformAIPoint = new Point(99, 99);
							preLevels = move.preLevels + move.levelLetter;
							curLevel++;
							return true;
						}
					} else {
						if (isVertical) {
							if ((centerX + transformHumanPoint.x - 4) == x & (centerY + transformHumanPoint.y - 6) == y & preLevels.equals(move.preLevels)) {
								transformAIPoint = new Point(99, 99);
								preLevels = move.preLevels + move.levelLetter;
								curLevel++;
								return true;
							}
						} else {
							if ((centerX + transformHumanPoint.x - 6) == x & (centerY + transformHumanPoint.y - 4) == y & preLevels.equals(move.preLevels)) {
								transformAIPoint = new Point(99, 99);
								preLevels = move.preLevels + move.levelLetter;
								curLevel++;
								return true;
							}
						}
					}
				}

				if (template.getTemplateType().isSquare()) {
					if ((centerX + transformHumanPoint.x - 4) == x & (centerY + transformHumanPoint.y - 4) == y & preLevels.equals(move.preLevels)) {
						transformAIPoint = new Point(centerX + transformAIPoint.x - 4, centerY + transformAIPoint.y - 4);
						preLevels = move.preLevels + move.levelLetter;
						curLevel++;
						return true;
					}
				} else {
					if (isVertical) {
						if ((centerX + transformHumanPoint.y - 4) == x & (centerY + transformHumanPoint.x - 6) == y & preLevels.equals(move.preLevels)) {
							transformAIPoint = new Point(centerX + transformAIPoint.y - 4, centerY + transformAIPoint.x - 6);
							preLevels = move.preLevels + move.levelLetter;
							curLevel++;
							return true;
						}
					} else {
						if ((centerX + transformHumanPoint.x - 6) == x & (centerY + transformHumanPoint.y - 4) == y & preLevels.equals(move.preLevels)) {
							transformAIPoint = new Point(centerX + transformAIPoint.x - 6, centerY + transformAIPoint.y - 4);
							preLevels = move.preLevels + move.levelLetter;
							curLevel++;
							return true;
						}
					}
				}
			}
		}

		int newX = x - centerX + 5, newY = y - centerY + 5;
		int index = newX - 1 + (newY - 1) * 13;
		String dotToString = getRotateTemplate(targetRotateType, template.getTemplate());
		dotToString = dotToString.substring(index, index + 1);
		if (!dotToString.equals(DotType.ANY.toString()) & !dotToString.equals(DotType.BLUE_EMPTY.toString())) {
			isEnabled = false;
		}
		return false;
	}

	public String getRotateTemplate(RotationType type, String templateWithMakrosPoints) {
		return RotationType.getTransform(type, templateWithMakrosPoints);
	}

}
