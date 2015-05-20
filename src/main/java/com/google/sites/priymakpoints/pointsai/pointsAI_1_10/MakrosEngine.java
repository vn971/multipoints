package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

public class MakrosEngine implements Variables {

	public final Makros[] base;

	public MakrosEngine() {
		MakrosIO io = new MakrosIO();
		base = io.getBase();
	}

	public Makros getMakros(int index) {
		for (Makros aBase : base) if (aBase.getMakrosIndex() == index) return aBase;
		return null;
	}

}
