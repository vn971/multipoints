package ru.narod.vn91.pointsop.data.game;

import ru.narod.vn91.pointsop.data.DotColored;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.SurroundingAbstract;

public interface FieldPositionListener {

	void addSurrounding(SurroundingAbstract surrounding);

	void addDot(DotColored dot);
}
