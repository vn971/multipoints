package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Makros implements Variables {

	private int index = 0;
	final List<MakrosLevelMove> moves = new ArrayList<>();
	String str = "";

	public Makros(String str) {

		this.str = str;
		String move;

		while (str.length() > 7) {
			move = str.substring(str.indexOf("(") + 1, str.indexOf(")"));
			str = str.substring(str.indexOf(")") + 1);
			moves.add(new MakrosLevelMove(move));
		}

		index = new Integer(str.substring(str.indexOf("(") + 1, str.indexOf(")")));
	}

	public int getMakrosIndex() {
		return index;
	}

	public String toString() {
		return str;
	}

	public MakrosLevelMove[] getMoves() {
		MakrosLevelMove[] base;
		int count = 0;
		Iterator i = moves.iterator();
		while (i.hasNext()) {
			i.next();
			count++;
		}
		base = new MakrosLevelMove[count];
		i = moves.iterator();
		count = 0;
		while (i.hasNext()) {
			base[count] = (MakrosLevelMove) i.next();
			count++;
		}
		return base;
	}

}
