package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.awt.*;

public class MakrosLevelMove {

	public final Point humanPoint;
	public final Point AIPoint;
	public int levelNumber = 0;
	public String levelLetter = "";
	public String preLevels = "";
	public int x, y;
	final String str;

	MakrosLevelMove(String str) {

		this.str = str;

		preLevels = str.substring(str.indexOf("[") + 1, str.indexOf("]") - 1);
		levelLetter = str.substring(str.indexOf("]") - 1, str.indexOf("]"));
		str = str.substring(str.indexOf("]") + 1);

		levelNumber = preLevels.length() + 1;

		x = new Integer(str.substring(str.indexOf("[") + 1, str.indexOf("]")));
		str = str.substring(str.indexOf("]") + 1);
		y = new Integer(str.substring(str.indexOf("[") + 1, str.indexOf("]")));
		str = str.substring(str.indexOf("]") + 1);
		humanPoint = new Point(x, y);

		x = new Integer(str.substring(str.indexOf("[") + 1, str.indexOf("]")));
		str = str.substring(str.indexOf("]") + 1);
		y = new Integer(str.substring(str.indexOf("[") + 1, str.indexOf("]")));
		AIPoint = new Point(x, y);

	}

	public String toString() {
		return str;
	}

}
