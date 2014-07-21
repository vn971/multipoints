package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.awt.*;
import java.util.ArrayList;

public class MoveAI implements Variables {

	public String strLastMove = "";
	int moveAIx = 99, moveAIy = 99;
	public final MoveByWall moveByWall = new MoveByWall();
	final BlueSurroundSecurity bss = new BlueSurroundSecurity();
	final RedSurroundSecurity rss = new RedSurroundSecurity();
	final ArrayList<MakrosApplicationInGame> makros = new ArrayList<>();

	Point getAImove(PointsAI pointsAI) {

		if (!isMoveByAI(pointsAI)) {
			while (!pointsAI.game.isCanMakeMove(moveAIx, moveAIy)) {
				moveAIx = Math.abs(rand.nextInt(sizeX)) + 1;
				moveAIy = Math.abs(rand.nextInt(sizeY)) + 1;
			}
			strLastMove = "random";
		}

		moveByWall.wallCorrection(moveAIx, moveAIy);

		for (MakrosApplicationInGame app : makros) {
			if (app == null) continue;
			if (!app.isEnabled) {
				continue;
			}
			if (!strLastMove.equals("makros")) {
				if (app.template.getTemplateType().isSquare()) {
					if (Math.abs(app.centerX - moveAIx) > 4 | Math.abs(app.centerY - moveAIy) > 4)
						continue;
				} else {
					if (app.isVertical) {
						if (Math.abs(app.centerX - moveAIx) > 4 | Math.abs(app.centerY - moveAIy) > 6)
							continue;
					} else {
						if (Math.abs(app.centerX - moveAIx) > 6 | Math.abs(app.centerY - moveAIy) > 4)
							continue;
					}
				}

				int newX, newY;
				if (app.template.getTemplateType().isSquare()) {
					newX = moveAIx - app.centerX + 5;
					newY = moveAIy - app.centerY + 5;
				} else {
					if (app.isVertical) {
						newY = moveAIx - app.centerX + 5;
						newX = moveAIy - app.centerY + 7;
					} else {
						newX = moveAIx - app.centerX + 7;
						newY = moveAIy - app.centerY + 5;
					}
				}
				int index = newX - 1 + (newY - 1) * 13;
				String dotToString = app.getRotateTemplate(app.targetRotateType, app.template.getTemplate());
				dotToString = dotToString.substring(index, index + 1);
				if (dotToString.equals(DotType.RED_NORMAL.toString())) {
					continue;
				}
				if (!dotToString.equals(DotType.ANY.toString()) & !dotToString.equals(DotType.RED_EMPTY.toString())) {
					app.isEnabled = false;
				}
			}
		}
		return new Point(moveAIx, moveAIy);
	}

	boolean isMoveByAI(PointsAI pointsAI) {

		if (isMoveByMakros(pointsAI)) {
			strLastMove = "makros";
			return true;
		}
		if (isMoveByTemplate(pointsAI)) {
			return true;
		}
		if (isMoveByGlobal(pointsAI)) {
			strLastMove = "global";
			return true;
		}
		if (moveByWall.isMoveByWall(pointsAI)) {
			return true;
		}
		if (moveByWall.isCanGroung) if (isMoveByGround(pointsAI)) {
			strLastMove = "ground";
			return true;
		}

		return false;
	}

	boolean isMoveByGlobal(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.GLOBAL, pointsAI, false)) {
			moveAIx = (PointsAI.base.getMoveAI().x + 1) * 3;
			moveAIy = (int) ((PointsAI.base.getMoveAI().y + 1) * 3.5f);
			System.out.println(moveAIx + ";" + moveAIy);
			String[][] fieldState = pointsAI.game.getFieldState();

			try {
				for (int i = moveAIx - 1; i < moveAIx + 2; i++)
					for (int j = moveAIy - 2; j < moveAIy + 2; j++)
						if (fieldState[i][j].equals("R")) {
							moveAIx = i + 1;
							moveAIy = j + 1;
							if (moveByWall.isMoveByWallTemplates(pointsAI, moveAIx, moveAIy, 0)) {
								if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
							}
						}
			} catch (Exception ignored) {
			}

			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveByGround(PointsAI pointsAI) {
		String[][] f = pointsAI.game.getFieldState();
		for (int i = 0; i < 37; i++)
			for (int j = 0; j < 30; j++) {
				if (f[i][j].equals("R") & f[i + 2][j].equals("R") & f[i + 1][j].equals("N")) {
					moveAIx = i + 2;
					moveAIy = j + 1;
					if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
				}
				if (f[i][j].equals("R") & f[i][j + 2].equals("R") & f[i][j + 1].equals("N")) {
					moveAIx = i + 1;
					moveAIy = j + 2;
					if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
				}
			}
		for (int i = 1; i < 37; i++)
			for (int j = 1; j < 30; j++) {
				if (f[i][j].equals("R") & f[i + 1][j + 1].equals("R") & f[i + 1][j].equals("N") & f[i][j + 1].equals("N")) {
					moveAIx = i + 2;
					moveAIy = j + 1;
					if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
				}
				if (f[i][j].equals("R") & f[i + 1][j - 1].equals("R") & f[i + 1][j].equals("N") & f[i][j - 1].equals("N")) {
					moveAIx = i + 1;
					moveAIy = j;
					if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
				}
			}

		return false;
	}

	boolean isMoveByTemplate(PointsAI pointsAI) {
		int x = pointsAI.game.getLastX(), y = pointsAI.game.getLastY();

		Point point = bss.getAIcoordinatesFromBSSbase(pointsAI);
		if (point != null) {
			if (isAImove(pointsAI, point.x, point.y)) {
				strLastMove = "bs security";
				moveAIx = point.x;
				moveAIy = point.y;
				return true;
			}
		}

		if (x < 7 & y < 7) {
			if (isMoveBySCTTemplateLT(pointsAI)) {
				strLastMove = "corner left top";
				return true;
			}
		}//move by corner
		else if (x > 33 & y < 7) {
			if (isMoveBySCTTemplateRT(pointsAI)) {
				strLastMove = "corner right top";
				return true;
			}
		}//move by corner
		else if (x < 7 & y > 26) {
			if (isMoveBySCTTemplateLB(pointsAI)) {
				strLastMove = "corner left bottom";
				return true;
			}
		}//move by corner
		else if (x > 33 & y > 26) {
			if (isMoveBySCTTemplateRB(pointsAI)) {
				strLastMove = "corner right bottom";
				return true;
			}
		}//move by corner
		if (x > 26 & y > 7 & y < 25) {
			if (isMoveByLSTTemplateRight(pointsAI)) {
				strLastMove = "long side right";
				return true;
			}
		}//move by long side Right
		if (x < 7 & y > 7 & y < 25) {
			if (isMoveByLSTTemplateLeft(pointsAI)) {
				strLastMove = "long side left";
				return true;
			}
		}//move by long side Left
		if (y > 26 & x > 7 & x < 32) {
			if (isMoveByLSTTemplateBottom(pointsAI)) {
				strLastMove = "long side bottom";
				return true;
			}
		}//move by long side bottom
		if (y < 7 & x > 7 & x < 32) {
			if (isMoveByLSTTemplateTop(pointsAI)) {
				strLastMove = "long side top";
				return true;
			}
		}//move by long side top
		if (x > 27 & y > 5 & y < 27) {
			if (isMoveBySHTTemplateRight(pointsAI)) {
				strLastMove = "short side right";
				return true;
			}
		}//move by short side
		if (x < 11 & y > 5 & y < 27) {
			if (isMoveBySHTTemplateLeft(pointsAI)) {
				strLastMove = "short side left";
				return true;
			}
		}//move by short side
		if (y < 11 & x > 5 & x < 34) {
			if (isMoveBySHTTemplateTop(pointsAI)) {
				strLastMove = "short side top";
				return true;
			}
		}//move by short side
		if (y > 22 & x > 5 & x < 34) {
			if (isMoveBySHTTemplateBottom(pointsAI)) {
				strLastMove = "short side bottom";
				return true;
			}
		}//move by short side
		if (x < 7 & y > 5 & y < 27) {
			if (isMoveBySSTTemplateL(pointsAI)) {
				strLastMove = "square side left";
				return true;
			}
		}//move by side
		if (x > 33 & y > 5 & y < 27) {
			if (isMoveBySSTTemplateR(pointsAI)) {
				strLastMove = "square side right";
				return true;
			}
		}//move by side
		if (y < 7 & x > 5 & x < 34) {
			if (isMoveBySSTTemplateT(pointsAI)) {
				strLastMove = "square side top";
				return true;
			}
		}//move by side
		if (y > 26 & x > 5 & x < 34) {
			if (isMoveBySSTTemplateB(pointsAI)) {
				strLastMove = "square side bottom";
				return true;
			}
		}//move by side

		try {
			if (isMoveByBSTTemplate(pointsAI)) {
				strLastMove = "bst template";
				return true;
			}
		} catch (Exception ignored) {
		}//move by blue surround
		try {
			if (isMoveByRSTTemplate(pointsAI)) {
				strLastMove = "rst template";
				return true;
			}
		} catch (Exception ignored) {
		}//move by blue surround

		if (isMoveByLGTTemplateHorizontal(pointsAI)) {
			strLastMove = "long Horizontal";
			return true;
		}//long
		if (isMoveByLGTTemplateVertical(pointsAI)) {
			strLastMove = "long Vertical";
			return true;
		}//long
		if (isMoveBySQTTemplate(pointsAI)) {
			strLastMove = "square";
			return true;
		}//square

		return false;
	}

	public boolean isAImove(PointsAI pointsAI, int x, int y) {
		if (pointsAI.game.isCanMakeMove(x, y)) {
			if (isMoveByRWTTemplate(pointsAI, x, y)) {
				return false;
			} else {
				moveAIx = x;
				moveAIy = y;
				return true;
			}
		} else return false;
	}

	public boolean isMoveByRWTTemplate(PointsAI pointsAI, int x, int y) {
		String content = TemplateType.RED_WRONG.getContent(pointsAI.game, x, y, false);
		return PointsAI.base.isFoundArea(content, TemplateType.RED_WRONG);
	}

	boolean isMoveByBSTTemplate(PointsAI pointsAI) {
		String content = TemplateType.BLUE_SURROUND.getContent(pointsAI.game, pointsAI.game.getLastX(), pointsAI.game.getLastY(), false);
		if (bss.isFoundArea(content, pointsAI)) {
			PointsAI.base.foundedNumber = bss.foundedNumber;
			moveAIx = bss.getMoveAI().x + pointsAI.game.getLastX() - 4;
			moveAIy = bss.getMoveAI().y + pointsAI.game.getLastY() - 4;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveByRSTTemplate(PointsAI pointsAI) {
		String content = TemplateType.RED_SURROUND.getContent(pointsAI.game, pointsAI.game.getLastX(), pointsAI.game.getLastY(), false);
		if (rss.isFoundArea(content, pointsAI)) {
			moveAIx = rss.getMoveAI().x + pointsAI.game.getLastX() - 4;
			moveAIy = rss.getMoveAI().y + pointsAI.game.getLastY() - 4;
			PointsAI.base.foundedNumber = rss.foundedNumber;
			if (isAImove(pointsAI, moveAIx, moveAIy)) {
				addMakros(false, rss.foundedIndex, rss.foundedNumber,
					pointsAI.game.getLastX(), pointsAI.game.getLastY());
				return true;
			}
		}
		return false;
	}

	boolean isMoveBySQTTemplate(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SQUARE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x + pointsAI.game.getLastX() - 4;
			moveAIy = PointsAI.base.getMoveAI().y + pointsAI.game.getLastY() - 4;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveByLGTTemplateHorizontal(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.LONG, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x + pointsAI.game.getLastX() - 6;
			moveAIy = PointsAI.base.getMoveAI().y + pointsAI.game.getLastY() - 4;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveByLGTTemplateVertical(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.LONG, pointsAI, true)) {
			moveAIx = PointsAI.base.getMoveAI().y + pointsAI.game.getLastX() - 4;
			moveAIy = PointsAI.base.getMoveAI().x + pointsAI.game.getLastY() - 6;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySCTTemplateLT(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SQUARE_CORNER, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x;
			moveAIy = PointsAI.base.getMoveAI().y;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySCTTemplateRT(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SQUARE_CORNER, pointsAI, false)) {
			moveAIx = 32 - PointsAI.base.getMoveAI().x;
			moveAIy = PointsAI.base.getMoveAI().y;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySCTTemplateLB(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SQUARE_CORNER, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x;
			moveAIy = 25 - PointsAI.base.getMoveAI().y;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySCTTemplateRB(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SQUARE_CORNER, pointsAI, false)) {
			moveAIx = 32 - PointsAI.base.getMoveAI().x;
			moveAIy = 25 - PointsAI.base.getMoveAI().y;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySSTTemplateL(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SQUARE_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x;
			moveAIy = pointsAI.game.getLastY() - 4 + PointsAI.base.getMoveAI().y;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySSTTemplateR(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SQUARE_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x + 32;
			moveAIy = pointsAI.game.getLastY() - 4 + PointsAI.base.getMoveAI().y;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySSTTemplateT(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SQUARE_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x + pointsAI.game.getLastX() - 4;
			moveAIy = PointsAI.base.getMoveAI().y;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySSTTemplateB(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SQUARE_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x + pointsAI.game.getLastX() - 4;
			moveAIy = PointsAI.base.getMoveAI().y + 25;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveByLSTTemplateRight(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.LONG_SIDE, pointsAI, false)) {
			moveAIx = 31 + PointsAI.base.getMoveAI().y;
			moveAIy = PointsAI.base.getMoveAI().x + pointsAI.game.getLastY() - 6;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveByLSTTemplateLeft(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.LONG_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().y;
			moveAIy = PointsAI.base.getMoveAI().x + pointsAI.game.getLastY() - 6;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveByLSTTemplateBottom(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.LONG_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x + pointsAI.game.getLastX() - 6;
			moveAIy = 25 + PointsAI.base.getMoveAI().y;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveByLSTTemplateTop(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.LONG_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x + pointsAI.game.getLastX() - 6;
			moveAIy = PointsAI.base.getMoveAI().y;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySHTTemplateRight(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SHORT_SIDE, pointsAI, false)) {
			moveAIx = 28 + PointsAI.base.getMoveAI().x;
			moveAIy = PointsAI.base.getMoveAI().y + pointsAI.game.getLastY() - 4;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySHTTemplateLeft(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SHORT_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().x;
			moveAIy = PointsAI.base.getMoveAI().y + pointsAI.game.getLastY() - 4;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySHTTemplateTop(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SHORT_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().y + pointsAI.game.getLastX() - 4;
			moveAIy = PointsAI.base.getMoveAI().x;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	boolean isMoveBySHTTemplateBottom(PointsAI pointsAI) {
		if (isFoundArea(TemplateType.SHORT_SIDE, pointsAI, false)) {
			moveAIx = PointsAI.base.getMoveAI().y + pointsAI.game.getLastX() - 4;
			moveAIy = 21 + PointsAI.base.getMoveAI().x;
			if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
		}
		return false;
	}

	public boolean isFoundArea(TemplateType type, PointsAI pointsAI, boolean isVertical) {
		String content = type.getContent(pointsAI.game, pointsAI.game.getLastX(), pointsAI.game.getLastY(), isVertical);

		if (PointsAI.base.isFoundArea(content, type)) {
			addMakros(isVertical, PointsAI.base.getTemplate(PointsAI.base.getFoundedNumber()).getTemplateIndex(),
				PointsAI.base.getFoundedNumber(), pointsAI.game.getLastX(), pointsAI.game.getLastY());
			return true;
		}
		return false;
	}

	public void addMakros(boolean isVertical, int index, int number, int centerX, int centerY) {
		if (PointsAI.base.getMakros(index) != null) {
			MakrosApplicationInGame app = new MakrosApplicationInGame(PointsAI.base.getMakros(index));
			app.setTemplate(PointsAI.base.getTemplate(number));
			app.isVertical = isVertical;
			app.setCenter(centerX, centerY);
			app.targetRotateType = PointsAI.base.getTemplate(number).targetRotateType;
			makros.add(app);
		}
	}

	boolean isMoveByMakros(PointsAI pointsAI) {
		for (MakrosApplicationInGame app : makros) {
			if (app == null) continue;
			if (!app.isEnabled) {
				continue;
			}

			int x = pointsAI.game.getLastX(), y = pointsAI.game.getLastY();
			if (app.template.getTemplateType().isSquare()) {
				if (Math.abs(app.centerX - x) > 4 | Math.abs(app.centerY - y) > 4) {
					continue;
				}
			} else {
				if (app.isVertical) {
					if (Math.abs(app.centerX - x) > 4 | Math.abs(app.centerY - y) > 6)
						continue;
				} else {
					if (Math.abs(app.centerX - x) > 6 | Math.abs(app.centerY - y) > 4)
						continue;
				}
			}
			if (app.isExistsLevelMove(x, y)) {
				moveAIx = app.transformAIPoint.x;
				moveAIy = app.transformAIPoint.y;
				if (isAImove(pointsAI, moveAIx, moveAIy)) return true;
			}
		}

		return false;
	}

}
