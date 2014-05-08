package com.google.sites.priymakpoints.pointsiq;

import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.activation.URLDataSource;

class QuestionIO{
	
	private List<Question> baseList=new ArrayList<Question>();
	private Question base[];
	
Question[] getBase(int level){
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
	String strLoad=s.substring(0, s.length());
	
	String move;
	
	while(strLoad.length()>2){
		move=strLoad.substring(strLoad.indexOf("{")+1,strLoad.indexOf("}"));//}catch(Exception exc){break;}
		strLoad=strLoad.substring(strLoad.indexOf("}")+1);
		Question q=new Question(move);
		if(q.level==level)baseList.add(q);
	}
	
	int count=0;
	Iterator i=baseList.iterator();
	while(i.hasNext()){i.next();count++;}
	base=new Question[count];i=baseList.iterator();count=0;
	while(i.hasNext()){base[count]=(Question)i.next();count++;}
	
	return base;
}

Question getQuestion(int index){for(int i=0;i<base.length;i++)if(base[i].index==index)return base[i];return null;}
	
}
