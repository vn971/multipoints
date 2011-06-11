package ru.narod.vn91.pointsop.gameEngine;

import java.util.ArrayList;
import java.util.List;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.SurroundingAbstract.SurroundingType;

public class SingleGameEngine implements SingleGameEngineInterface {

	private int sizeX, sizeY;
	private int redScore = 0, blueScore = 0;
	private Dot[][] field;
	private List<Surrounding> allSurroundings = new ArrayList<Surrounding>();
	private MoveInfo lastMoveInfo;
	private DotAbstract lastDot = null;
	RandomMovesProvider randomMovesProvider;

	public SingleGameEngine(int sizeX,
			int sizeY) {
		this.sizeX = sizeX + 2;
		this.sizeY = sizeY + 2;
		field = new Dot[this.sizeX][this.sizeY];
		for (int x = 0; x < this.sizeX; x++) {
			for (int y = 0; y < this.sizeY; y++) {
				field[x][y] = new Dot(x, y);
			}
		}
	}

	public MoveResult makeMove(int x,
			int y,
			boolean isRed) {
		return makeMove(x, y, (isRed) ? MoveType.RED : MoveType.BLUE);
	}

	public MoveResult makeMove(int x,
			int y,
			MoveType moveType) {
		if (!canMakeMove(x, y)) {
			return MoveResult.ERROR;
		}
		lastMoveInfo = new MoveInfo(x, y, moveType, MoveResult.ERROR);
		Dot lastPlacedDot = field[x][y];
		lastDot = lastPlacedDot;
		Surrounding ctrlSurrounding =
				(lastPlacedDot.isCtrl()) ? field[x][y].masterSurrounding : null;
		lastPlacedDot.height += 1;
		lastPlacedDot.type = getDotType(moveType);

		if (ctrlSurrounding == null) { // there was no Ctrl surrounding
			boolean surroundedSomething = false;
			for (int i = 0; i < Dot.DIRECTIONS; i++) {
				if (lastPlacedDot.getNeighbour(i).isSurrBuilder(moveType)) {
					surroundedSomething |= trySurround(lastPlacedDot, i,
							moveType, null);
				}
			}
			if (surroundedSomething) {
				lastMoveInfo.moveResult = MoveResult.GOOD;
				return lastMoveInfo.moveResult;
			} else {
				lastMoveInfo.moveResult = MoveResult.NOTHING;
				return lastMoveInfo.moveResult;
			}
		} else { // there was a Ctrl surrounding

			if (ctrlSurrounding.isEnemyCtrlFor(moveType)) { // dot was placed
				// into an enemy surrounding

				// try to eat for the moving player
				boolean surroundedSomething = false;
				for (int i = 0; i < Dot.DIRECTIONS; i++) {
					if (lastPlacedDot.getNeighbour(i).isSurrBuilder(moveType)) {
						surroundedSomething |=
								trySurround(lastPlacedDot, i, moveType, null);
					}
				}

				if (surroundedSomething == false) {
					// eat back if the player didn't surround anything
					// TODO
					ctrlSurrounding.wasDestroyed = false;
					ctrlSurrounding.type =
							(moveType == MoveType.BLUE) ? SurroundingType.RED
							: SurroundingType.BLUE;

					// getSurroundingType_Enemy(moveType);
					for (int indexOfDot = ctrlSurrounding.capturedPoints.size() - 1; indexOfDot >= 0; indexOfDot--) {
						DotAbstract abstractDot =
								ctrlSurrounding.capturedPoints.get(indexOfDot);
						((Dot)abstractDot).eat(ctrlSurrounding, null);
					}
					lastMoveInfo.moveResult = MoveResult.BAD;
					return lastMoveInfo.moveResult;
				} else {
					// the player moved into the Ctrl territory and
					// survived (surrounded himself)
					{
						// clear the old surrounding
						ctrlSurrounding.wasDestroyed = true;
						for (int i = 0; i < ctrlSurrounding.capturedPoints.size(); i++) {
							Dot dotToClear =
									(Dot)ctrlSurrounding.capturedPoints.get(i);
							if (dotToClear.masterSurrounding == ctrlSurrounding) {
								dotToClear.undoCtrl(ctrlSurrounding);
								dotToClear.masterSurrounding = null;
							}
						}
					}
					lastMoveInfo.moveResult = MoveResult.GOOD;
					return lastMoveInfo.moveResult;
				}

			} else {
				// dot was placed into your own surrounding
				// ctrlSurrounding.wasDestroyed = false;
				for (int i = 0; i < Dot.DIRECTIONS; i++) {
					if (lastPlacedDot.getNeighbour(i).isSurrBuilder(moveType)) {
						trySurround(lastPlacedDot, i, moveType, ctrlSurrounding);
					}
				}
				lastMoveInfo.moveResult = MoveResult.NOTHING;
				return lastMoveInfo.moveResult;
			}
		}
	}

	private boolean trySurround(Dot startDot,
			int startDirection,
			MoveType moveType,
			Surrounding outerCtrlSurrounding) {
		{
			// checking that the starting position is correct: if we start
			// making a path, we return to the beginning.
			int checkDirection = startDirection;
			checkDirection = startDot.rotateClockwise(checkDirection, moveType);
			if ((checkDirection - startDirection == 1)
					|| ((checkDirection - startDirection == 2) && (startDirection % 2 == 0))) {
				return false;
			}
		}

		Surrounding newSurrounding = new Surrounding();
		ArrayList<DotAbstract> path = newSurrounding.path;
		{
			// building surrounding path
			Dot loopDot = startDot;
			int loopDirection = startDirection;
			do {
				if (loopDot.masterSurrounding == newSurrounding) {
					int indexInPath = path.indexOf(loopDot);
					for (int indexToDelete = path.size() - 1; indexToDelete > indexInPath; indexToDelete--) {
						((Dot)(path.get(indexToDelete))).masterSurrounding = null;
						path.remove(indexToDelete);
					}
				} else {
					path.add(loopDot);
					loopDot.masterSurrounding = newSurrounding;
				}
				loopDot = loopDot.getNeighbour(loopDirection);
				loopDirection = loopDot.getOppositeDirection(loopDirection);
				loopDirection = loopDot.rotateAntiClockwise(loopDirection,
						moveType);
			} while ((loopDot != startDot)
					|| ((loopDirection - startDirection) % Dot.DIRECTIONS != 0));

			path.add(startDot);
			if (path.size() < 4) {
				// simple check - not necessary
				return false;
			}
		}

		// no optimization of using the smaller rectangle is used.
		// by the way, I think this can be done by the java optimizer. (vn91)
		boolean[][] verticalIntersectionPlaces = new boolean[sizeX][sizeY];
		{
			// marking intersection-points
			for (int pathIndex = 0; pathIndex < path.size() - 1; pathIndex++) {
				int dx = path.get(pathIndex + 1).x - path.get(pathIndex).x;
				int dy = path.get(pathIndex + 1).y - path.get(pathIndex).y;

				// this is a trick.
				// to understand how it works a <b>picture</b> is needed.
				// In short, we don't look at the point itself - but at a
				// very close place to it.
				// The reason why we do it - that it's easier.
				// We can't know if some point is inside the surrounding
				// path or not (it's may be exactly
				// on the border!). But we know it for each of
				// these "close" places.

				if ((dx == 1) && (dy == 1)) {
					verticalIntersectionPlaces[path.get(pathIndex).x][path.get(
							pathIndex).y + 1] ^=
							true;
				} else if ((dx == 1) && (dy == 0)) {
					verticalIntersectionPlaces[path.get(pathIndex).x][path.get(
							pathIndex).y] ^=
							true;
				} else if ((dx == 1) && (dy == -1)) {
					verticalIntersectionPlaces[path.get(pathIndex).x][path.get(
							pathIndex).y] ^=
							true;
				} else if ((dx == -1) && (dy == 1)) {
					verticalIntersectionPlaces[path.get(pathIndex).x - 1][path.get(
							pathIndex).y + 1] ^= true;
				} else if ((dx == -1) && (dy == 0)) {
					verticalIntersectionPlaces[path.get(pathIndex).x - 1][path.get(
							pathIndex).y] ^= true;
				} else if ((dx == -1) && (dy == -1)) {
					verticalIntersectionPlaces[path.get(pathIndex).x - 1][path.get(
							pathIndex).y] ^= true;
				}

				((Dot)path.get(pathIndex)).masterSurrounding = newSurrounding;
			}
		}

		{
			Dot mustBeInnerDot = startDot.getNeighbour(startDirection + 1);
			boolean isInside = false;
			for (int i = 1; i <= mustBeInnerDot.y; i++) {
				isInside ^= verticalIntersectionPlaces[mustBeInnerDot.x][i];
			}
			// exiting if the starting point is out of the hypothetical path
			if (isInside == false) {
				return false;
			} else {
				allSurroundings.add(newSurrounding);
				lastMoveInfo.newSurroundings.add(newSurrounding);
			}
		}

		boolean[][] innerTerritory = new boolean[sizeX][sizeY];
		{
			boolean isInside = false;
			for (int x = 0; x < sizeX; x++) {
				// isInside doesn't need to be reseted
				for (int y = 0; y < sizeY; y++) {
					isInside ^= verticalIntersectionPlaces[x][y];
					innerTerritory[x][y] =
							isInside && (field[x][y].masterSurrounding != newSurrounding);
				}
			}
		}

		boolean enemyCaptured = false;
		{
			// searching for enemies
			for (int x = 0; x < sizeX; x++) {
				for (int y = 0; y < sizeY; y++) {
					if ((innerTerritory[x][y] == true) && (field[x][y].isEnemy(
							moveType))) {
						enemyCaptured = true;
					}
				}
			}
		}
		newSurrounding.type = getSurroundingType(moveType, enemyCaptured);

		{
			// eating
			for (int x = 0; x < sizeX; x++) {
				for (int y = 0; y < sizeY; y++) {
					if (innerTerritory[x][y] == true) {
						field[x][y].eat(newSurrounding, outerCtrlSurrounding);
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean canMakeMove(int x,
			int y) {
		if ((x < 1) || (y < 1) || (x > getSizeX()) || (y > getSizeY())) {
			return false;
		}
		if (!field[x][y].isEmptyToPlace()) {
			return false;
		}
		return true;
	}

	@Override
	public List<SurroundingAbstract> getSurroundings() {
		List<SurroundingAbstract> result = new ArrayList<SurroundingAbstract>();
		for (int index = 0; index < allSurroundings.size(); index++) {
			Surrounding surrounding = allSurroundings.get(index);
			if (surrounding.isCtrl() == false) {
				result.add(allSurroundings.get(index));
			}
		}
		return result;
	}

	public int getSizeX() {
		return sizeX - 2;
	}

	public int getSizeY() {
		return sizeY - 2;
	}

	public DotType getDotType(int x,
			int y) {
		return field[x][y].type;
	}

	public DotAbstract getLastDot() {
		return lastDot;
	}

	public boolean getLastDotColor() {
		if (lastMoveInfo == null) {
			return true;
		}
		return lastMoveInfo.moveType == MoveType.RED;
	}

	public int getRedScore() {
		return redScore;
	}

	public int getBlueScore() {
		return blueScore;
	}

	@Override
	public MoveResult tryRandomMove(boolean isRed) {
		if (randomMovesProvider == null) {
			randomMovesProvider = new RandomMovesProvider(sizeX - 2, sizeY - 2);
		}
		ru.narod.vn91.pointsop.data.DotAbstract dot;
		do {
			dot = randomMovesProvider.getNextDot();
		} while (dot != null && getDotType(dot.x, dot.y).notIn(
				DotType.EMPTY, DotType.BLUE_CTRL, DotType.RED_CTRL));

//		System.out.println("dot = " + dot);
		if (dot == null) {
			return MoveResult.ERROR;
		}
		return makeMove(dot.x, dot.y, isRed ? MoveType.RED : MoveType.BLUE);
	}

	DotType getDotType(MoveType moveTypeFromUser) {
		if (moveTypeFromUser == MoveType.BLUE) {
			return DotType.BLUE;
		} else {
			return DotType.RED;
		}
	}

	SurroundingType getSurroundingType(MoveType moveType,
			boolean enemyInside) {
		if (enemyInside) {
			return (moveType == MoveType.BLUE) ? SurroundingType.BLUE
					: SurroundingType.RED;
		} else {
			return (moveType == MoveType.BLUE) ? SurroundingType.BLUE_CTRL
					: SurroundingType.RED_CTRL;
		}
	}

	class Surrounding extends SurroundingAbstract {

		boolean wasDestroyed = false;

		boolean isCtrl() {
			return (type == SurroundingType.BLUE_CTRL)
					|| (type == SurroundingType.RED_CTRL);
		}

		boolean isEnemyCtrlFor(MoveType moveType) {
			return ((type == SurroundingType.BLUE_CTRL) && (moveType == MoveType.RED))
					|| ((type == SurroundingType.RED_CTRL) && (moveType == MoveType.BLUE));
		}

		public Surrounding() {
			path = new ArrayList<DotAbstract>();
			capturedPoints = new ArrayList<DotAbstract>();
		}
	}

	class MoveInfo extends MoveInfoAbstract {

		public MoveInfo(int coordX,
				int coordY,
				MoveType moveType,
				MoveResult moveResult) {
			this.coordX = coordX;
			this.coordY = coordY;
			this.moveType = moveType;
			this.moveResult = moveResult;
			this.newSurroundings = new ArrayList<SurroundingAbstract>();
		}
	}

	class Dot extends DotAbstract {

		int height = 0;
		DotType type = DotType.EMPTY;
		Surrounding masterSurrounding;
		// number of all possible directions from a dot
		static final int DIRECTIONS = 8;

		Dot(int x,
				int y) {
			this.x = x;
			this.y = y;
		}

//		boolean dotTypeIsIn(DotType element, DotType... set) {
//			for (int i = 0; i < set.length; i++) {
//				if (set[i] == element) {
//					return true;
//				}
//			}
//			return false;
//		}
		boolean hasType(DotType... set) {
			return DotType.dotTypeIsIn(this.type, set);
//			return dotTypeIsIn(this.type, set);
		}

		boolean isEmptyToPlace() {
			return hasType(DotType.EMPTY, DotType.RED_CTRL, DotType.BLUE_CTRL);
		}

		boolean isEnemy(MoveType moveType) {
			return ((moveType == MoveType.BLUE) && (this.type == DotType.RED))
					|| ((moveType == MoveType.RED) && (this.type == DotType.BLUE));
		}

		boolean isUneatableFor(MoveType moveType) {
			return false;
		}

		boolean isSurrBuilder(MoveType moveType) {
			return ((moveType == MoveType.BLUE) && (type == DotType.BLUE))
					|| ((moveType == MoveType.RED) && (type == DotType.RED));
		}

		boolean isCtrl() {
			return type == DotType.BLUE_CTRL || type == DotType.RED_CTRL;
		}

		/**
		 * @param direction
		 *          - direction where we should find the neighbour.<br>
		 *          dir=0 --> up <br>
		 *          dir=1 --> up-right <br>
		 *          dir=2 --> right... and so on. <br>
		 *          Axis Y looks up, axis X - to the right.
		 */
		Dot getNeighbour(int direction) {
			direction %= DIRECTIONS; // the change of this variable is local in
			// java
			if (direction < 0) {
				direction = direction + DIRECTIONS;
			}
			int dx, dy;
			switch (direction) {
				case 0:
					dx = 0;
					dy = 1;
					break;
				case 1:
					dx = 1;
					dy = 1;
					break;
				case 2:
					dx = 1;
					dy = 0;
					break;
				case 3:
					dx = 1;
					dy = -1;
					break;
				case 4:
					dx = 0;
					dy = -1;
					break;
				case 5:
					dx = -1;
					dy = -1;
					break;
				case 6:
					dx = -1;
					dy = 0;
					break;
				case 7:
					dx = -1;
					dy = 1;
					break;
				default:
					throw new UnsupportedOperationException("unknown direction");
			}
			return field[x + dx][y + dy];
		}

		int rotateClockwise(int previousDirection,
				MoveType moveType) {
			int newDirection = previousDirection + 1;
			while (this.getNeighbour(newDirection).isSurrBuilder(moveType) == false) {
				newDirection += 1;
			}
			return newDirection;
		}

		int rotateAntiClockwise(int previousDirection,
				MoveType moveType) {
			int newDirection = previousDirection - 1;
			while (this.getNeighbour(newDirection).isSurrBuilder(moveType) == false) {
				newDirection -= 1;
			}
			return newDirection;
		}

		int getOppositeDirection(int direction) {
			return 4 + direction;
		}

		void eat(Surrounding newSurrounding,
				Surrounding outerCtrlSurrounding) {

			// TODO Remove all code that works with Surroundings from here..

			if (newSurrounding.isCtrl() == false) {
				this.height += 1;
			}
			if (this.masterSurrounding == null) {
				this.masterSurrounding = newSurrounding;
				newSurrounding.capturedPoints.add(this);
			} else if (this.masterSurrounding == newSurrounding) {
				// do nothing
			} else {
				if (this.masterSurrounding == outerCtrlSurrounding) {
					masterSurrounding.wasDestroyed = true;
					if (masterSurrounding.isCtrl()) {
						Surrounding surroundingToClear = masterSurrounding;
						for (int i = 0; i < surroundingToClear.capturedPoints.size(); i++) {
							Dot dotToClear = (Dot)surroundingToClear.capturedPoints.get(
									i);
							dotToClear.undoCtrl(surroundingToClear);
							dotToClear.masterSurrounding = null;
						}
					}
					this.masterSurrounding = newSurrounding;
				} else {
					newSurrounding.capturedPoints.add(this);
				}
			}

			// <changing Dot types>
			switch (newSurrounding.type) {
				case BLUE:
					if (this.hasType(DotType.BLUE)) {
						type = DotType.BLUE_TIRED;
					} else if (this.hasType(DotType.RED_EATED_BLUE)) {
						redScore -= 1;
						type = DotType.BLUE_TIRED;
					} else if (this.hasType(DotType.RED)) {
						blueScore += 1;
						newSurrounding.firstCapturedEnemy = this;
						type = DotType.BLUE_EATED_RED;
					} else if (this.hasType(DotType.RED_TIRED)) {
						blueScore += 1;
						type = DotType.BLUE_EATED_RED;
					} else if (this.hasType(DotType.BLUE_CTRL, DotType.EMPTY,
							DotType.RED_CTRL, DotType.RED_EATED_EMPTY)) {
						type = DotType.BLUE_EATED_EMPTY;
					}
					break;
				case RED:
					if (this.hasType(DotType.RED)) {
						type = DotType.RED_TIRED;
					} else if (this.hasType(DotType.BLUE_EATED_RED)) {
						blueScore -= 1;
						type = DotType.RED_TIRED;
					} else if (this.hasType(DotType.BLUE)) {
						redScore += 1;
						newSurrounding.firstCapturedEnemy = this;
						type = DotType.RED_EATED_BLUE;
					} else if (this.hasType(DotType.BLUE_TIRED)) {
						redScore += 1;
						type = DotType.RED_EATED_BLUE;
					} else if (this.hasType(DotType.RED_CTRL, DotType.EMPTY,
							DotType.BLUE_CTRL, DotType.BLUE_EATED_EMPTY)) {
						type = DotType.RED_EATED_EMPTY;
					}
					break;
				case BLUE_CTRL:
					if (this.hasType(DotType.EMPTY)) {
						type = DotType.BLUE_CTRL;
					}
					break;
				case RED_CTRL:
					if (this.hasType(DotType.EMPTY)) {
						type = DotType.RED_CTRL;
					}
					break;
				default:
					throw new UnsupportedOperationException(
							"unknown surrounding type");
			}
			// <changing Dot types>
		}

		void undoCtrl(Surrounding surrounding) {
			if (((type == DotType.BLUE_CTRL) || (type == DotType.RED_CTRL))
					&& (masterSurrounding == surrounding)) {
				type = DotType.EMPTY;
				masterSurrounding = null;
			}
		}
	}
}
