package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import javax.activation.URLDataSource;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class C_ReadAndWriteFile {

	public String ReadResourceFile(String strInputFile) {
		StringBuilder s = new StringBuilder();
		try {
			URLDataSource d = new URLDataSource(PointsAI.class.getResource(strInputFile));
			InputStream r = d.getInputStream();
			Reader in = new InputStreamReader(r);
			CharArrayWriter w = new CharArrayWriter();
			int c;
			while (true) {
				c = in.read();
				if (c != -1) w.write(c);
				else break;
			}
			s.append(w.toCharArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s.substring(0, s.length());
	}

}
