package com.google.sites.priymakpoints.pointsiq;

import javax.activation.URLDataSource;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class QuestionIO {

	private final List<Question> baseList = new ArrayList<>();
	private Question base[];

	Question[] getBase(int level) {
		StringBuilder s = new StringBuilder();
		try {
			URLDataSource d = new URLDataSource(PointsIQ.class.getResource("PointsIQbase.txt"));
			InputStream r = d.getInputStream();
			Reader in = new InputStreamReader(r, "UTF-8");
			CharArrayWriter w = new CharArrayWriter();
			int c;
			while (true) {
				c = in.read();
				if (c != -1) w.write(c);
				else break;
			}
			s.append(w.toCharArray());
		} catch (Exception ignored) {
		}
		String strLoad = s.substring(0, s.length());

		String move;

		while (strLoad.length() > 2) {
			move = strLoad.substring(strLoad.indexOf("{") + 1, strLoad.indexOf("}"));
			strLoad = strLoad.substring(strLoad.indexOf("}") + 1);
			Question q = new Question(move);
			if (q.level == level) baseList.add(q);
		}

		int count = 0;
		Iterator i = baseList.iterator();
		while (i.hasNext()) {
			i.next();
			count++;
		}
		base = new Question[count];
		i = baseList.iterator();
		count = 0;
		while (i.hasNext()) {
			base[count] = (Question) i.next();
			count++;
		}
		return base;
	}

	Question getQuestion(int index) {
		for (Question aBase : base) if (aBase.index == index) return aBase;
		return null;
	}

}
