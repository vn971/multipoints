package ru.narod.vn91.pointsop.data.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import ru.narod.vn91.pointsop.data.DotColored;
import ru.narod.vn91.pointsop.data.GameOuterInfo;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.SurroundingAbstract;

public class Game {

	GameOuterInfo gameInfo;
	TreeNode root = new TreeNode(null);
	TreeNode currentTreeNode = root;

	private Collection<FieldPositionListener> fieldPositionListenerList =
			new ArrayList<FieldPositionListener>();

	void processAction(Action action) {
		action.call(this);
	}

	void addPaperSurrounding(final SurroundingAbstract surrounding) {
		for (final FieldPositionListener listener : fieldPositionListenerList) {
			new Thread() {
				@Override
				public void run() {
					listener.addSurrounding(surrounding);
				}
			}.start();
		}
	}

	void addPaperDot(final DotColored dot) {
		for (final FieldPositionListener listener : fieldPositionListenerList) {
			new Thread() {
				@Override
				public void run() {
					listener.addDot(dot);
				}
			}.start();
		}
	}

	Collection<DotColored> extractMovesCollectionFromTree() {
		LinkedList<DotColored> result = new LinkedList<DotColored>();
		TreeNode treeNode = currentTreeNode;
		while (treeNode != null) {
			for (TreeNodeProperty property : treeNode.propertyList) {
				if (property.keyIn("W", "B", "AW", "AB")) {
					// add moves
				}
			}
			treeNode = treeNode.parent;
		}
		return result;
	}

}
