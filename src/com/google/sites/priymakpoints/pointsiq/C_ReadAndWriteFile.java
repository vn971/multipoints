package com.google.sites.priymakpoints.pointsiq;

import java.io.CharArrayWriter;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.activation.URLDataSource;

public class C_ReadAndWriteFile {

public String ReadURLTxtFile(){
	StringBuffer s=new StringBuffer();
	try {
		URLDataSource d=new URLDataSource(PointsIQ.class.getResource("PointsIQbase.txt"));
		InputStream r=d.getInputStream();
		Reader in = new InputStreamReader(r, "UTF-8");
		CharArrayWriter w=new CharArrayWriter();
		int c=0;
		while(true){c=in.read();	if(c!=-1)w.write(c);else break;	}
		s.append(w.toCharArray());
	}catch(Exception e){}
	return s.substring(0, s.length());
}

public String ReadTxtFile(String strInputFile){
	StringBuffer s=new StringBuffer();
	try {
		FileReader r=new FileReader(strInputFile);
		CharArrayWriter w=new CharArrayWriter();
		int c=0;
		while(true){c=r.read();	if(c!=-1)w.write(c);else break;	}
		s.append(w.toCharArray());
	}catch(Exception e){}
	return s.substring(0, s.length());
}



}
