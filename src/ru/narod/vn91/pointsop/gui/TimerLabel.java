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
						long remainSleep;
						while ((remainSleep = stopSleep - new Date().getTime()) > 0) {
							try {
								sleep(remainSleep + 50);
							} catch (Exception e) {
							}
						}
					}
				}

				@Override
				public void run() {
					while (TimerLabel.this.isDisplayable()) {
						long current = new Date().getTime();
						long suggestedToShow = timeOut - 1000L * secondsToShowNext;
						System.out.println("updating timer..., current=" + current
								+ ", suggToSh=" + suggestedToShow);

						if (timeOut <= 0) {
							System.out.println("-");
							sleep_Custom(1000L);
						}
					// else if (current < suggestedToShow) {
					// System.out.println("0");
					// sleep_Custom(suggestedToShow - current);
					// } else if (current >= suggestedToShow && current < timeOut) {
					// System.out.println("1");
					// int secondsToShow = (int) ((timeOut - current) / 1000L);
					// showSeconds(secondsToShow);
					// secondsToShowNext = secondsToShow - 1;
					// sleep_Custom(timeOut - current - 1000L * secondsToShowNext);
					// }
					else if (current < suggestedToShow) {
						System.out.println("0");
						sleep_Custom(suggestedToShow - current);
					} else if (current >= suggestedToShow && current < timeOut) {
						System.out.println("1");
						showSeconds(secondsToShowNext);
						// secondsToShowNext = (int) ((timeOut - current) / 1000L);
						secondsToShowNext -= 1;
						sleep_Custom(Math.min(1000L,
								timeOut - current - 1000L * secondsToShowNext));
						// int secondsToShow = (int) ((timeOut - current) / 1000L);
						// showSeconds(secondsToShow);
						// secondsToShowNext = secondsToShow - 1;
						// sleep_Custom(timeOut - current - 1000L * secondsToShowNext);
					}
						else if (current >= timeOut) {
							System.out.println("+");
							secondsToShowNext = -1;
							showSeconds(-1);
							sleep_Custom(1000L);
						} else {
							// cannot be.
						}
					}
				}
			};
			thread.setPriority(Thread.MAX_PRIORITY);
			thread.setDaemon(true);
			thread.start();
		}
	}

	private synchronized void showSeconds(int seconds) {
		int minutesVisual = (seconds ) / 60;
		int secondsVisual = (seconds ) % 60;
		String string = String.format("%02d:%02d", minutesVisual, secondsVisual);
		super.setText(string);
	}

}
