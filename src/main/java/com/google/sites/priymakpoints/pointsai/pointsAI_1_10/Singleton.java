package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

public class Singleton implements Variables {
	private static Singleton instanceTemplate = null;
	static Template[] base;

	protected Singleton() {
		String strLoad;
		C_ReadAndWriteFile file = new C_ReadAndWriteFile();
		strLoad = file.ReadResourceFile(templateFileName);

		base = new Template[strLoad.length() / 125];

		for (int a = 0; a < base.length; a++) {
			base[a] = new Template(strLoad.substring(a * 125, (a + 1) * 125));
		}

	}

	public static Template[] getInstanceTemplate() {
		if (instanceTemplate == null) {
			instanceTemplate = new Singleton();
		}
		return base;
	}

}
