package ru.narod.vn91.pointsop.data;


public class Dot {

	public final int x;
	public final int y;

	public Dot(int x,
			int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + ":" + y + "]";
	}

}
