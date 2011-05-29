package com.google.sites.priymakpoints.pointsai.patterns_InputOutput;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import javax.activation.URLDataSource;
import javax.swing.JFrame;

public class C_ReadAndWriteFile {

JFrame frame;//��������� ����, � ������� ��������� ���������

public C_ReadAndWriteFile(JFrame frame){this.frame=frame;}
public C_ReadAndWriteFile(){}

public void ReadAndWriteFile(String strInputFile,String strOutputFile){try {
	BufferedInputStream bis=new BufferedInputStream(new FileInputStream(strInputFile));
	BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(strOutputFile));
	int c=0;
	while(true){
		c=bis.read();
		if(c!=-1)bos.write(c);else break;
	}
 	bos.flush();
 	bos.close();
 	bos = null;
 	bis.close();
 	bis=null;
}catch(Exception e){}}

public void CreateDirectoryIfNotExists(String strOutputDirectory){try {
	File f=new File(strOutputDirectory);
	if(f.exists()==false)f.mkdirs();
}catch(Exception e){}}


public void CreateFileIfNotExists(String strOutputFile){try {
	File f=new File(strOutputFile);
	if(f.exists()==false)f.createNewFile();
}catch(Exception e){}}

public String ReadURLTxtFile(String url){
	StringBuffer s=new StringBuffer();
	try {
		URLDataSource d=new URLDataSource(new URL(url));
		InputStream r=d.getInputStream();
		CharArrayWriter w=new CharArrayWriter();
		int c=0;
		while(true){c=r.read();	if(c!=-1)w.write(c);else break;	}
		s.append(w.toCharArray());
	}catch(Exception e){return null;}
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

public void WriteTxtFile(String strOutputFile, String strOutputText){try {
	File f=new File(strOutputFile);
	f.delete();
	FileOutputStream fout=new FileOutputStream(strOutputFile);
	fout.write(strOutputText.getBytes());
}catch(Exception e){}}

public String[] ReadParametersFile(String fileName){
	String strLoad=ReadTxtFile(fileName);//��������� �������� � �����

	int masSize=0;
	for(int i=0;i<strLoad.length();i++)if(strLoad.substring(i, i+1).equals(";"))masSize++;
	
	String text[]=new String[masSize];
	if(strLoad.equals("")!=true){//������� ���� � �������� �� �����
		int index=0;
		for(int i=0;i<strLoad.length();i++){//������ ������ �����������
			String number="";
			if (strLoad.charAt(i)==';'){//���� ����� ";"
				for(int j=0;j<i;j++)number+=strLoad.charAt(j);//������ ����� ���� ������ ������ ";"
				text[index]=number;//������� ����� � ������
				strLoad=strLoad.substring(i+1,strLoad.length());
				i=0;index++;
			}
		}
	}
	return text;
}

}
