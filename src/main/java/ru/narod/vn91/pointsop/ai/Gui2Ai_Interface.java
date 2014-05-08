package ru.narod.vn91.pointsop.ai;

public interface Gui2Ai_Interface {

	void init();

	void receiveMove(
			int x,
			int y,
			boolean isRed,
			boolean toBeAnswered,
			long timeExpected);

	/**
	 * if AI used his own Thread - AI must free this thread.
	 */
	void dispose();

	String getName();
}
