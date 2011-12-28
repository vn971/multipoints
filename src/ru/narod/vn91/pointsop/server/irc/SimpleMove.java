package ru.narod.vn91.pointsop.server.irc;
public class SimpleMove {

	public final int x, y;
	public final boolean isRed;

	public SimpleMove(
			int x,
			int y,
			boolean isRed) {
		this.x = x;
		this.y = y;
		this.isRed = isRed;
	}
}