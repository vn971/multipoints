package ru.narod.vn91.pointsop.utils;

import java.util.Date;

public class Wait {

	public static void waitExactly(long milliseconds) {
		long timeout = new Date().getTime() + milliseconds;
		long remaining = milliseconds;
		while (remaining > 0) {
			try {
				new Object().wait(remaining);
			} catch (Exception e) {
			}
			remaining = timeout - new Date().getTime();
		}
	}

}
