package ru.narod.vn91.pointsop.gui;

import java.util.Date;

import javax.swing.JLabel;

@SuppressWarnings("serial")
class TimerLabel extends JLabel {


	void init() {
	}

	TimerThread thread = null;
	private Long timeOut = 0L;
//	private Object synchObject = new Object();

	public TimerLabel() {
		super();
		this.showSeconds(0);
	}

	public void setRemainingTime(int seconds, boolean freeze) {
		if (thread!=null) {
			thread.stopTimerThread();
		}
		if (freeze == true) {
			// synchronized (timeOut) {
			timeOut = 0L;
			// }
			showSeconds(seconds);
		} else if (seconds <= 0){
			timeOut = 0L;
			showSeconds(seconds);
		} else {
			timeOut = new Date().getTime() + 1000L * seconds;
			showSeconds(seconds);
		}
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
							showSeconds(0);
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

	private class TimerThread extends Thread{

		Long goal;
		Boolean isAlive = true;

		public void stopTimerThread() {
			synchronized (isAlive) {
				isAlive = false;
			}
		}

		public void init(int seconds) {
			goal = new Date().getTime() + 1000L * seconds;


		}

	}


}
