package com.google.sites.priymakpoints.pointsai.p_TemplateEngine;

import com.google.sites.priymakpoints.pointsai.patterns_InputOutput.C_ReadAndWriteFile;

public class TemplateIO extends Variables{
	
	private Template[] base;//���� ���������
	private C_ReadAndWriteFile file=new C_ReadAndWriteFile();
	
protected Template[] getBase(boolean isOffline){
	String strLoad="";
	strLoad=file.ReadURLTxtFile(templateUrlName);
	
	base=new Template[strLoad.length()/125];//������� ������ ��� �������� ����
	
	for(int a=0;a<base.length;a++){//������ ��� �������� � �����
		base[a]=new Template(strLoad.substring(a*125,(a+1)*125));//��������� �������
	}	
	return base;
}
	
}
