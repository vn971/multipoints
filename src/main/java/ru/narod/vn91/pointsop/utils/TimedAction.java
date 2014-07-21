package ru.narod.vn91.pointsop.utils;


public abstract class TimedAction {

	/*
	 * Overridable. If this is false - the run() method won't execute. Also, the
	 * thread may be stopped earlier, not waiting for the timeout. If you don't
	 * override this method - then not condition would be checked.
	 */
	public boolean isAlive() {
		return true;
	}

	/**
	 * action to run on time-out
	 */
	public abstract void run();

}
