package ru.narod.vn91.pointsop.data.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class TreeNode {

	final TreeNode parent;

	Set<TreeNode> children = new LinkedHashSet<TreeNode>();
	Collection<TreeNodeProperty> propertyList = new ArrayList<TreeNodeProperty>();

	public TreeNode( TreeNode parent ) {
		this.parent = parent;
	}
}

class TreeNodeProperty {
	public final String key;
	public final String value;

	public TreeNodeProperty(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public boolean keyIn(String... keyList) {
		for (String keyInList : keyList) {
			if (this.key.equals(keyInList)) {
				return true;
			}
		}
		return false;
	}
}