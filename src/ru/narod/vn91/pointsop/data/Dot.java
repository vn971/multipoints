package ru.narod.vn91.pointsop.data;

public class Dot {

	public int x;
	public int y;

//	public Dot() {
//	}

	public Dot(int x,
			int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + ":" + y + "]";
	}

	public Dot zero2one() {
		return new Dot(this.x + 1, this.y + 1);
	}

	public Dot one2zero() {
		return new Dot(this.x - 1, this.y - 1);
	}
}
