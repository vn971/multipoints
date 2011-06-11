package ru.narod.vn91.pointsop.data;

public class DotAbstract {

	public int x;
	public int y;

	public DotAbstract() {
	}

	public DotAbstract(int x,
			int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "[" + x + ":" + y + "]";
	}

	public DotAbstract zero2one() {
		return new DotAbstract(this.x + 1, this.y + 1);
	}

	public DotAbstract one2zero() {
		return new DotAbstract(this.x - 1, this.y - 1);
	}
}
