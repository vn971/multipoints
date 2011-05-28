package com.google.sites.priymakpoints.pointsai.patterns_InputOutput;

import java.util.Random;

public class C_DataEncryption {


public String getEncryptTXT(String str){//�������� ������������� ����� �� ��������� String str

	Random r=new Random();//������� ��������� ��������� �����
	int h=r.nextInt(40)+10;//�������� ��������� �����
	
	String buf="";//������� StringBuffer
	for(int i=0;i<str.length();i++){//������ ��� ������� � buf
		buf+=(char)((str.substring(i,i+1).toCharArray()[0])-h+10);//�������� ��� ������� � buf
	}

	str=buf.toString();////�������� ������ �������� ��� ����������� �����������

	return buf+h;//������� ������������� �����

}

public String getDecodedTXT(String str){//�������� �������������� ����� �� ��������� String str

	int h=new Integer(str.substring(str.length()-2)).intValue();//�������� ������ ��������
	
	String buf="";//������� StringBuffer
	for(int i=0;i<str.length();i++){//������ ��� ������� � buf
		buf+=(char)((str.substring(i,i+1).toCharArray()[0])+h-10);//�������� ��� ������� � buf
	}

	buf=buf.substring(0, buf.length()-2);//�������� �������������� �����
	
	return buf;//������� �������������� �����

}


}
