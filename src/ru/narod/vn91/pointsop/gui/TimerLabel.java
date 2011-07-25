package ru.narod.vn91.pointsop.gui;

import java.util.Date;

import javax.swing.JLabel;

@SuppressWarnings("serial")
class TimerLabel extends JLabel {

	private Thread thread = null;
	private Long timeOut = null;

	public TimerLabel() {
		super("00:00");
	}

	public void setRemainingTime(int seconds) {
		long millis = 1000L * seconds;
		if (millis > 0) {
			showSeconds(seconds);
			timeOut = new Date().getTime() + millis;
			//
		} else {
			timeOut = null;
			showSeconds(0);
		}
	}

	private void showSeconds(int seconds) {
		int minutes = seconds / 60;
		int secondsVisual = seconds % 60;
		String string = String.format("%02d:%02d", minutes, secondsVisual);
		super.setText(string);
	}

//	private void updateText() {
//		if (timeOut == null) {
//			super.setText("00:00");
//		} else {
//			long millis = timeOut - new Date().getTime();
//			if (millis < 0) {
//				super.setText("00:00");
//			} else {
//				int secondsTotal = (int) (millis / 1000);
//				int minutes = secondsTotal / 60;
//				int secondsVisual = secondsTotal % 60;
//				String string = String.format("%02d:%02d", minutes, secondsVisual);
//				super.setText(string);
//			}
//		}
//	}
}
