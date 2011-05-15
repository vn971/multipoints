package ru.narod.vn91.pointsop.data;

import java.awt.Color;

/**
 *
 * @author vasya
 */
public class CustomColors {

	public static Color getContrastColor(Color source) {
		return new Color(255 - source.getRed(), 255 - source.getGreen(), 255 - source.getBlue(), source.getAlpha());
	}

	public static Color getAlphaModifiedColor(Color source, int newAlpha) {
		return new Color(source.getRed(), source.getGreen(), source.getBlue(), newAlpha);
	}

	public static Color getMiddleColor(Color c1, Color c2) {
		return new Color(
				(c1.getRed() + c2.getRed()) / 2,
				(c1.getGreen() + c2.getGreen()) / 2,
				(c1.getBlue() + c2.getBlue()) / 2,
				(c1.getAlpha() + c2.getAlpha()) / 2);
	}
}
