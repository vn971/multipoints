package ru.narod.vn91.pointsop.ai;

public interface Ai2Gui_Interface {

	void makeMove(
			int x,
			int y,
			boolean isRed,
			double aiThinksOfHisPosition, // from 0 to 1
			String message,
			long timeTook);

	void receiveMessage(String message);
}
