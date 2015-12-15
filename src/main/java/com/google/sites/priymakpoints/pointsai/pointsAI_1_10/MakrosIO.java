package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MakrosIO implements Variables {

	private final List<Makros> baseList = new ArrayList<>();
	private final C_ReadAndWriteFile file = new C_ReadAndWriteFile();

	protected Makros[] getBase() {
		String strLoad = file.ReadResourceFile(makrosFileName);
		String move;

		while (strLoad.length() > 1) {
			move = strLoad.substring(strLoad.indexOf("{") + 1, strLoad.indexOf("}"));
			strLoad = strLoad.substring(strLoad.indexOf("}") + 1);
			baseList.add(new Makros(move));
		}

		int count = 0;
		Iterator<Makros> i = baseList.iterator();
		while (i.hasNext()) {
			i.next();
			count++;
		}
		Makros[] base = new Makros[count];
		i = baseList.iterator();
		count = 0;
		while (i.hasNext()) {
			base[count] = i.next();
			count++;
		}

		return base;
	}

}
