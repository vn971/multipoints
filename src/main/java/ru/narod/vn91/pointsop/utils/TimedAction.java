package ru.narod.vn91.pointsop.utils;

import java.util.Date;

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

	public final void executeWhen(final long timeOut) {
		new Thread() {
			public void run() {
				long estimatedTime;
				while (((estimatedTime = timeOut - new Date().getTime()) > 0)
						&& isAlive()) {
					Object o = new Object();
					synchronized (o) {
						try {
							o.wait(estimatedTime + 10);
						} catch (Exception ignored) {
						}
					}
				}
				if (isAlive()) {
					TimedAction.this.run();
				}
			};
		}.start();
	}

	public final void executeAfter(final long millis) {
		this.executeWhen(new Date().getTime()+millis);
	}

}
