package ru.narod.vn91.pointsop.gui;

import java.util.Date;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class TimerLabel extends JLabel {

	TimerThread thread = null;
	Object synchronization_SetText = new Object();
	Object synchronization_SetRemainingTime = new Object();
	Integer secondsLastTimeShown = -1;

	public TimerLabel() {
		super();
		this.showSeconds(0);
	}

	public void setRemainingTime(int seconds, boolean freeze) {
		synchronized (synchronization_SetRemainingTime) {
			if (thread != null) {
				thread.stopTimerThread();
			}
			if (freeze == true) {
				showSeconds(seconds);
			} else {
				thread = new TimerThread();
				thread.init(seconds);
			}
		}
	}

	// cannot hold
	private void showSeconds(int seconds) {
		// cannot hold
		synchronized (synchronization_SetText) {
			if (secondsLastTimeShown != seconds) {
				// super.setForeground((seconds <= 5) ? Color.RED : Color.BLACK);
				int minutesVisual = (seconds) / 60;
				int secondsVisual = (seconds) % 60;
				String string = String
						.format("%02d:%02d", minutesVisual, secondsVisual);
				super.setText(string);
				secondsLastTimeShown = seconds;
			}
		}
	}

	private class TimerThread extends Thread {

		Long goal;
		Object synchronization_IsAlive = new Object();
		Boolean isTimerAlive = true;

		public void stopTimerThread() {
			// does not hold
			synchronized (synchronization_IsAlive) {
				isTimerAlive = false;
			}
		}

		@Override
		public void run() {
			while (true) {
				long current = new Date().getTime();
				int fullSecondsRemaining = (int) Math.floor
						((double) (goal - current) / 1000);

				// cannot hold
				synchronized (synchronization_IsAlive) {
					if (isTimerAlive
							&& TimerLabel.this.isDisplayable()
							&& fullSecondsRemaining >= -1) {
						TimerLabel.this.showSeconds(fullSecondsRemaining + 1);
					} else {
						break;
					}
				}

				try {
					super.sleep(goal - current - fullSecondsRemaining * 1000L + 50);
				} catch (Exception e) {
				}
			}
		};

		public void init(int seconds) {
			super.setDaemon(true);
			super.setPriority(Thread.MAX_PRIORITY);
			goal = new Date().getTime() + 1000L * seconds - 1;
			super.start();
		}
	}
}
