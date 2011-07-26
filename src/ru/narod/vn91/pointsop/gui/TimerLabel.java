package ru.narod.vn91.pointsop.gui;

import java.awt.Color;
import java.awt.Font;
import java.util.Date;

import javax.swing.JLabel;

@SuppressWarnings("serial")
class TimerLabel extends JLabel {

	TimerThread thread = null;
	Integer secondsLastTimeShown = -1;

	void init() {
	}

	public TimerLabel() {
		super();
		this.showSeconds(0);
	}

	public synchronized void setRemainingTime(int seconds, boolean freeze) {
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

	private void showSeconds(int seconds) {
		synchronized (secondsLastTimeShown) {
			if (secondsLastTimeShown != seconds) {
				super.setForeground((seconds <= 5) ? Color.RED : Color.BLACK);
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
		Boolean isTimerAlive = true;

		public void stopTimerThread() {
			synchronized (isTimerAlive) {
				isTimerAlive = false;
			}
		}

		@Override
		public void run() {
			while (true) {
				long current = new Date().getTime();
				int fullSecondsRemaining = (int) Math.floor
						((double) (goal - current) / 1000);

				synchronized (isTimerAlive) {
					if (isTimerAlive &&
							TimerLabel.this.isDisplayable()
							&& fullSecondsRemaining >= -1) {
						TimerLabel.this.showSeconds(fullSecondsRemaining + 1);
					} else {
						break;
					}
				}

				try {
					System.out.println("going to sleep for="
							+ (goal - current - fullSecondsRemaining * 1000L + 50));
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
