package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

public class MoveByWall extends WallEngine {

	int moveAIx = 99, moveAIy = 99;
	public boolean isCanGroung = false;
	int foundedNumber = 0;

	public void wallCorrection(int moveX, int moveY) {
		super.wallCorrection(moveX, moveY);
	}

	public boolean isMoveByWall(PointsAI pointsAI) {
		int x = 0, y = 0;
		int smaller = getSmallerWall(pointsAI);

		if (smaller == 0) {
			isCanGroung = true;
			return false;
		} else if (smaller == 1) {
			x = w1.x;
			y = w1.y;
		} else if (smaller == 2) {
			x = w2.x;
			y = w2.y;
		} else if (smaller == 3) {
			x = w3.x;
			y = w3.y;
		}

		if (isMoveByWallTemplates(pointsAI, x, y, smaller)) {
			return true;
		}

		if (smaller == 1) {
			if (isMoveByWallTemplates(pointsAI, w2.x, w2.y, 2)) return true;
			if (isMoveByWallTemplates(pointsAI, w3.x, w3.y, 3)) return true;
		} else if (smaller == 2) {
			if (isMoveByWallTemplates(pointsAI, w1.x, w1.y, 1)) return true;
			if (isMoveByWallTemplates(pointsAI, w3.x, w3.y, 3)) return true;
		} else if (smaller == 3) {
			if (isMoveByWallTemplates(pointsAI, w2.x, w2.y, 2)) return true;
			if (isMoveByWallTemplates(pointsAI, w1.x, w1.y, 1)) return true;
		}

		return false;
	}

	public boolean isMoveByWallTemplates(PointsAI pointsAI, int x, int y, int smaller) {

		if (x < 8 & y < 8) {
			if (isMoveByWCTTemplateLT(pointsAI, x, y, smaller)) {
				addMakros(pointsAI, x, y);
				return true;
			}
		}//move by corner
		else if (x > 32 & y < 8) {
			if (isMoveByWCTTemplateRT(pointsAI, x, y, smaller)) {
				addMakros(pointsAI, x, y);
				return true;
			}
		}//move by corner
		else if (x < 8 & y > 25) {
			if (isMoveByWCTTemplateLB(pointsAI, x, y, smaller)) {
				addMakros(pointsAI, x, y);
				return true;
			}
		}//move by corner
		else if (x > 32 & y > 25) {
			if (isMoveByWCTTemplateRB(pointsAI, x, y, smaller)) {
				addMakros(pointsAI, x, y);
				return true;
			}
		}//move by corner
		if (x < 8) {
			if (isMoveByWSTTemplateL(pointsAI, x, y, smaller)) {
				addMakros(pointsAI, x, y);
				return true;
			}
		} else if (x > 32) {
			if (isMoveByWSTTemplateR(pointsAI, x, y, smaller)) {
				addMakros(pointsAI, x, y);
				return true;
			}
		} else if (y < 8) {
			if (isMoveByWSTTemplateT(pointsAI, x, y, smaller)) {
				addMakros(pointsAI, x, y);
				return true;
			}
		} else if (y > 25) {
			if (isMoveByWSTTemplateB(pointsAI, x, y, smaller)) {
				addMakros(pointsAI, x, y);
				return true;
			}
		} else {
			if (isMoveByWLTTemplate(pointsAI, x, y, smaller)) {
				addMakros(pointsAI, x, y);
				return true;
			}
		}
		return false;
	}

	void addMakros(PointsAI pointsAI, int x, int y) {
		pointsAI.moveAI.addMakros(false, PointsAI.base.getTemplate(foundedNumber).getTemplateIndex(), foundedNumber, x, y);
	}

	boolean isMoveByWLTTemplate(PointsAI pointsAI, int x, int y, int smaller) {
		String content = TemplateType.WALL.getContent(pointsAI.game, x, y, false);
		if (PointsAI.base.isFoundArea(content, TemplateType.WALL)) {
			moveAIx = PointsAI.base.getMoveAI().x + x - 4;
			moveAIy = PointsAI.base.getMoveAI().y + y - 4;

			if (smaller == 1) {
				w1.x = moveAIx;
				w1.y = moveAIy;
			}
			if (smaller == 2) {
				w2.x = moveAIx;
				w2.y = moveAIy;
			}
			if (smaller == 3) {
				w3.x = moveAIx;
				w3.y = moveAIy;
			}

			foundedNumber = PointsAI.base.foundedNumber;

			if (pointsAI.moveAI.isAImove(pointsAI, moveAIx, moveAIy)) {
				pointsAI.moveAI.strLastMove = "wall";
				return true;
			}
		}
		return false;
	}

	boolean isMoveByWCTTemplateLT(PointsAI pointsAI, int x, int y, int smaller) {
		String content = TemplateType.WALL_CORNER.getContent(pointsAI.game, x, y, false);
		if (PointsAI.base.isFoundArea(content, TemplateType.WALL_CORNER)) {
			moveAIx = PointsAI.base.getMoveAI().x;
			moveAIy = PointsAI.base.getMoveAI().y;

			if (smaller == 1) {
				w1.x = moveAIx;
				w1.y = moveAIy;
			}
			if (smaller == 2) {
				w2.x = moveAIx;
				w2.y = moveAIy;
			}
			if (smaller == 3) {
				w3.x = moveAIx;
				w3.y = moveAIy;
			}

			foundedNumber = PointsAI.base.foundedNumber;

			if (pointsAI.moveAI.isAImove(pointsAI, moveAIx, moveAIy)) {
				pointsAI.moveAI.strLastMove = "wall corner left top";
				return true;
			}
		}
		return false;
	}

	boolean isMoveByWCTTemplateRT(PointsAI pointsAI, int x, int y, int smaller) {
		String content = TemplateType.WALL_CORNER.getContent(pointsAI.game, x, y, false);
		if (PointsAI.base.isFoundArea(content, TemplateType.WALL_CORNER)) {
			moveAIx = 32 - PointsAI.base.getMoveAI().x;
			moveAIy = PointsAI.base.getMoveAI().y;

			if (smaller == 1) {
				w1.x = moveAIx;
				w1.y = moveAIy;
			}
			if (smaller == 2) {
				w2.x = moveAIx;
				w2.y = moveAIy;
			}
			if (smaller == 3) {
				w3.x = moveAIx;
				w3.y = moveAIy;
			}

			foundedNumber = PointsAI.base.foundedNumber;

			if (pointsAI.moveAI.isAImove(pointsAI, moveAIx, moveAIy)) {
				pointsAI.moveAI.strLastMove = "wall corner right top";
				return true;
			}
		}
		return false;
	}

	boolean isMoveByWCTTemplateLB(PointsAI pointsAI, int x, int y, int smaller) {
		String content = TemplateType.WALL_CORNER.getContent(pointsAI.game, x, y, false);
		if (PointsAI.base.isFoundArea(content, TemplateType.WALL_CORNER)) {
			moveAIx = PointsAI.base.getMoveAI().x;
			moveAIy = 25 - PointsAI.base.getMoveAI().y;

			if (smaller == 1) {
				w1.x = moveAIx;
				w1.y = moveAIy;
			}
			if (smaller == 2) {
				w2.x = moveAIx;
				w2.y = moveAIy;
			}
			if (smaller == 3) {
				w3.x = moveAIx;
				w3.y = moveAIy;
			}

			foundedNumber = PointsAI.base.foundedNumber;

			if (pointsAI.moveAI.isAImove(pointsAI, moveAIx, moveAIy)) {
				pointsAI.moveAI.strLastMove = "wall corner left bottom";
				return true;
			}
		}
		return false;
	}

	boolean isMoveByWCTTemplateRB(PointsAI pointsAI, int x, int y, int smaller) {
		String content = TemplateType.WALL_CORNER.getContent(pointsAI.game, x, y, false);
		if (PointsAI.base.isFoundArea(content, TemplateType.WALL_CORNER)) {
			moveAIx = 32 - PointsAI.base.getMoveAI().x;
			moveAIy = 25 - PointsAI.base.getMoveAI().y;

			if (smaller == 1) {
				w1.x = moveAIx;
				w1.y = moveAIy;
			}
			if (smaller == 2) {
				w2.x = moveAIx;
				w2.y = moveAIy;
			}
			if (smaller == 3) {
				w3.x = moveAIx;
				w3.y = moveAIy;
			}

			foundedNumber = PointsAI.base.foundedNumber;

			if (pointsAI.moveAI.isAImove(pointsAI, moveAIx, moveAIy)) {
				pointsAI.moveAI.strLastMove = "wall corner right bottom";
				return true;
			}
		}
		return false;
	}

	boolean isMoveByWSTTemplateL(PointsAI pointsAI, int x, int y, int smaller) {
		String content = TemplateType.WALL_SIDE.getContent(pointsAI.game, x, y, false);
		if (PointsAI.base.isFoundArea(content, TemplateType.WALL_SIDE)) {
			moveAIx = PointsAI.base.getMoveAI().x;
			moveAIy = y - 4 + PointsAI.base.getMoveAI().y;

			if (smaller == 1) {
				w1.x = moveAIx;
				w1.y = moveAIy;
			}
			if (smaller == 2) {
				w2.x = moveAIx;
				w2.y = moveAIy;
			}
			if (smaller == 3) {
				w3.x = moveAIx;
				w3.y = moveAIy;
			}

			foundedNumber = PointsAI.base.foundedNumber;

			if (pointsAI.moveAI.isAImove(pointsAI, moveAIx, moveAIy)) {
				pointsAI.moveAI.strLastMove = "wall side left";
				return true;
			}
		}
		return false;
	}

	boolean isMoveByWSTTemplateR(PointsAI pointsAI, int x, int y, int smaller) {
		String content = TemplateType.WALL_SIDE.getContent(pointsAI.game, x, y, false);
		if (PointsAI.base.isFoundArea(content, TemplateType.WALL_SIDE)) {
			moveAIx = PointsAI.base.getMoveAI().x + 32;
			moveAIy = y - 4 + PointsAI.base.getMoveAI().y;

			if (smaller == 1) {
				w1.x = moveAIx;
				w1.y = moveAIy;
			}
			if (smaller == 2) {
				w2.x = moveAIx;
				w2.y = moveAIy;
			}
			if (smaller == 3) {
				w3.x = moveAIx;
				w3.y = moveAIy;
			}

			foundedNumber = PointsAI.base.foundedNumber;

			if (pointsAI.moveAI.isAImove(pointsAI, moveAIx, moveAIy)) {
				pointsAI.moveAI.strLastMove = "wall side right";
				return true;
			}
		}
		return false;
	}

	boolean isMoveByWSTTemplateT(PointsAI pointsAI, int x, int y, int smaller) {
		String content = TemplateType.WALL_SIDE.getContent(pointsAI.game, x, y, false);
		if (PointsAI.base.isFoundArea(content, TemplateType.WALL_SIDE)) {
			moveAIx = PointsAI.base.getMoveAI().x + x - 4;
			moveAIy = PointsAI.base.getMoveAI().y;

			if (smaller == 1) {
				w1.x = moveAIx;
				w1.y = moveAIy;
			}
			if (smaller == 2) {
				w2.x = moveAIx;
				w2.y = moveAIy;
			}
			if (smaller == 3) {
				w3.x = moveAIx;
				w3.y = moveAIy;
			}

			foundedNumber = PointsAI.base.foundedNumber;

			if (pointsAI.moveAI.isAImove(pointsAI, moveAIx, moveAIy)) {
				pointsAI.moveAI.strLastMove = "wall side top";
				return true;
			}
		}
		return false;
	}

	boolean isMoveByWSTTemplateB(PointsAI pointsAI, int x, int y, int smaller) {
		String content = TemplateType.WALL_SIDE.getContent(pointsAI.game, x, y, false);
		if (PointsAI.base.isFoundArea(content, TemplateType.WALL_SIDE)) {
			moveAIx = PointsAI.base.getMoveAI().x + x - 4;
			moveAIy = PointsAI.base.getMoveAI().y + 25;

			if (smaller == 1) {
				w1.x = moveAIx;
				w1.y = moveAIy;
			}
			if (smaller == 2) {
				w2.x = moveAIx;
				w2.y = moveAIy;
			}
			if (smaller == 3) {
				w3.x = moveAIx;
				w3.y = moveAIy;
			}

			foundedNumber = PointsAI.base.foundedNumber;

			if (pointsAI.moveAI.isAImove(pointsAI, moveAIx, moveAIy)) {
				pointsAI.moveAI.strLastMove = "wall side bottom";
				return true;
			}
		}
		return false;
	}

}
