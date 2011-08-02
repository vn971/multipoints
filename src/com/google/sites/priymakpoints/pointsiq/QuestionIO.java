package com.google.sites.priymakpoints.pointsiq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;

public class QuestionIO implements Variables{
	
	private List<Question> baseList=new ArrayList<Question>();//���� ���������
	private Question base[];//���� ���������
	private C_ReadAndWriteFile file=new C_ReadAndWriteFile();
	
public Question[] getBase(int level){
	String strLoad="";
	try{strLoad=file.ReadURLTxtFile();}catch(Exception e){new JOptionPane().showMessageDialog(null, "Base not found. Program exit");};
	
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

public Question getQuestion(int index){
	for(int i=0;i<base.length;i++)if(base[i].index==index)return base[i];
	return null;
}
	
}
