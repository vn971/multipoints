package ru.narod.vn91.pointsop.data;

import java.awt.Color;
import java.util.prefs.Preferences;

/**
 *
 * @author vasya
 */
public class PersistentMemory {

	static Preferences memory = Preferences.userRoot().node(
			"ru.narod.ru.pointsop.userdata");

	public static int getVersion() {
		return memory.getInt("version", 0);
	}

	public static void setVersion(int ver) {
		memory.putInt("version", ver);
	}

	public static void setUserName(String newName) {
		memory.put("username", newName);
	}
	
	public static void setFrameWidth(int frameWidth) {
		memory.putInt("frameWidth", frameWidth);
	}
	
	public static void setFrameHeight(int frameHeight) {
		memory.putInt("frameHeight", frameHeight);
	}

	public static int getFrameWidth() {
		return memory.getInt("frameWidth", 0);
	}
	
	public static int getFrameHeight() {
		return memory.getInt("frameHeight", 0);
	}
	
	public static String getUserName() {
		return memory.get("username", "");
	}

	public static ClickAudibility getClickAudibility() {
		return ClickAudibility.valueOf_Failsafe(
				memory.get("ClickAudibility", ""));
	}

	public static void setClickAudibility(ClickAudibility clickAudibility) {
		memory.put("ClickAudibility", clickAudibility.name());
	}

	public static void setClickAudibility(String s) {
		memory.put("ClickAudibility", ClickAudibility.valueOf_Failsafe(s).name());
	}

	private static double limitDotWidth(double d) {
		if (d > 1.0) {
			return 1.0;
		} else if (d < 0.1) {
			return 0.1;
		} else {
			return d;
		}
	}

	public static double getDotWidth() {
		try {
			String s = memory.get("DotWidth", "");
			return limitDotWidth(Double.parseDouble(s));
		} catch (Exception e) {
			return 0.5;
		}
	}

	public static void setDotWidth(double d) {
		d = limitDotWidth(d);
		memory.put("DotWidth", String.valueOf(d));
	}

	public static boolean getDrawConnections() {
		return memory.getBoolean("DrawConnections", true);
	}

	public static void setDrawConnections(boolean b) {
		memory.putBoolean("DrawConnections", b);
	}

	public static Color getPlayer1Color() {
		int r = memory.getInt("1Red", 255);
		int g = memory.getInt("1Green", 0);
		int b = memory.getInt("1Blue", 0);
		return new Color(r, g, b);
	}

	public static Color getPlayer2Color() {
		int r = memory.getInt("2Red", 21);
		int g = memory.getInt("2Green", 96);
		int b = memory.getInt("2Blue", 189);
		return new Color(r, g, b);
	}

	public static Color getBackgroundColor() {
		int r = memory.getInt("BRed", 0);
		int g = memory.getInt("BGreen", 0);
		int b = memory.getInt("BBlue", 0);
		return new Color(r, g, b);
	}

	public static void setPlayer1Color(Color c) {
		memory.putInt("1Red", c.getRed());
		memory.putInt("1Green", c.getGreen());
		memory.putInt("1Blue", c.getBlue());
	}

	public static void setPlayer2Color(Color c) {
		memory.putInt("2Red", c.getRed());
		memory.putInt("2Green", c.getGreen());
		memory.putInt("2Blue", c.getBlue());
	}

	public static void setBackgroundColor(Color c) {
		memory.putInt("BRed", c.getRed());
		memory.putInt("BGreen", c.getGreen());
		memory.putInt("BBlue", c.getBlue());
	}

	public static void resetColors() {
		setPlayer1Color(new Color(255, 0, 0, 255));
		setPlayer2Color(new Color(21, 96, 189, 255));
		setBackgroundColor(new Color(254, 254, 254, 255));
	}

	public enum ClickAudibility {

		IN_ALL_GAMES, IN_MY_GAMES, NOWHERE;

		public static ClickAudibility valueOf_Failsafe(String s) {
			try {
				return ClickAudibility.valueOf(s);
			} catch (Exception e) {
				return IN_MY_GAMES;
			}
		}
	}
}
