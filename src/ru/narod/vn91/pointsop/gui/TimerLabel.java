package ru.narod.vn91.pointsop.gui;

import java.util.Date;

import javax.swing.JLabel;

@SuppressWarnings("serial")
class TimerLabel extends JLabel {

	Thread thread = null;

	void init() {
		// Thread thread = new Thread() {
		// @Override
		// public void run() {
		// System.out.println("TimerLabel.this.isDisplayable() = "
		// + TimerLabel.this.isDisplayable());
		// while (TimerLabel.this.isDisplayable()) {
		// setText("" + Math.random());

		// }

		// while (true) {
		// long millisRemaining = timeOut - new Date().getTime();
		// int secondsRemaining = (int) (millisRemaining / 1000);
		// if (millisRemaining <= 0) {
		// showSeconds(0);
		// break;
		// }
		// if (secondsRemaining != secondsShownLast) {
		// showSeconds(secondsRemaining);
		// }
		//
		// // wait
		// Object o = new Object();
		// synchronized (o) {
		// try {
		// long millisecondsToSleep =
		// millisRemaining - 1000L * secondsRemaining + 50;
		// o.wait(millisecondsToSleep);
		// } catch (InterruptedException e) {
		// // e.printStackTrace();
		// }
		// }
		// }
		// // exiting, freeing the thread
		// thread = null;

		// };
		// };
		// thread.setPriority(Thread.MIN_PRIORITY);
		// thread.setDaemon(true);
		// thread.start();
	}

	private long timeOut = 0;
	// private int secondsShownLast = -1;
	private int secondsToShowNext = -1;
	boolean isActive = false;

	public TimerLabel() {
		super();
		this.showSeconds(-1);
		// super("00:00");
	}

	public synchronized void setRemainingTime(int seconds) {
		if (seconds > 0) {
			// long millis = 1000L * seconds;
			// showSeconds(seconds);
			// if (thread != null) {
			// showSeconds(seconds);
			// } else {
			// this.timeOut = new Date().getTime() + 1000L * seconds;
			// thread = new CounterThread();
			// thread.start();
			// }
			this.timeOut = new Date().getTime() + 1000L * seconds;
			showSeconds(seconds);
			secondsToShowNext = seconds - 1;
		} else {
			this.timeOut = 0;
			showSeconds(-1);
			secondsToShowNext = -1;
		}
		if (isActive == false) {
			isActive = true;

			Thread thread = new Thread() {

				void sleep_Custom(long t) {
					if (t > 0) {
						long stopSleep = new Date().getTime() + t;

					}
				}

				@Override
				public void run() {
					while (TimerLabel.this.isDisplayable()) {
						if (timeOut != 0) {
							showSeconds((int) (Math.random() * 911));

						} else {
							sleep_Custom(1000L);
						}
					}
				}
			};
			thread.setPriority(Thread.MIN_PRIORITY);
			thread.setDaemon(true);
			thread.start();
		}
	}

	private synchronized void showSeconds(int seconds) {
		// why +1? :
		// This is needed because the user must always
		// see a value 1 second's bigger than the real one.
		// for example, 0 seconds + 100 milliseconds is a positive value
		// for the computer but 00:00 is negative (time-out) value for a human.
		// int secondsVisual = seconds + 1;
		int minutesVisual = (seconds + 1) / 60;
		int secondsVisual = (seconds + 1) % 60;
		String string = String.format("%02d:%02d", minutesVisual, secondsVisual);
		super.setText(string);
	}

}
