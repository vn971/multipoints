package ru.narod.vn91.pointsop.data.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

public class TreeNode {

	final TreeNode parent;

	Set<TreeNode> children = new LinkedHashSet<TreeNode>();
	Collection<TreeNodeProperty> propertyList = new ArrayList<TreeNodeProperty>();

	public TreeNode(TreeNode parent, Set<TreeNode> children,
			Collection<TreeNodeProperty> propertyList) {
		this.parent = parent;
		this.children = children;
		this.propertyList = propertyList;
	}
}

class TreeNodeProperty {
	public final String property;
	public final String value;

	public TreeNodeProperty(String property, String value) {
		super();
		this.property = property;
		this.value = value;
	}

}