package ru.narod.vn91.pointsop.utils;

import java.awt.*;


public class CustomColors {

	public static Color getContrastColor(Color source) {
		return new Color((source.getRed() + 128) % 256,
				(source.getGreen() + 128) % 256,
				(source.getBlue() + 128) % 256,
				source.getAlpha());
	}

	public static Color getAlphaModifiedColor(
			Color source,
			int newAlpha) {
		return new Color(
				source.getRed(),
				source.getGreen(),
				source.getBlue(),
				newAlpha);
	}

	public static Color getMixedColor(
			Color c1,
			Color c2,
			float color1Share) {
		float q = 1 - color1Share;
		return new Color(
				(int)(c1.getRed() * color1Share + c2.getRed() * q),
				(int)(c1.getGreen() * color1Share + c2.getGreen() * q),
				(int)(c1.getBlue() * color1Share + c2.getBlue() * q),
				(int)(c1.getAlpha() * color1Share + c2.getAlpha() * q));
	}

	public static Color getMiddleColor(Color c1,
			Color c2) {
		return CustomColors.getMixedColor(c1, c2, 0.5f);
	}
}
