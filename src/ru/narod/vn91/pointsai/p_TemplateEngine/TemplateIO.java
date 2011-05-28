package ru.narod.vn91.pointsai.p_TemplateEngine;

import java.io.File;
import ru.narod.vn91.pointsai.patterns_InputOutput.C_DataEncryption;
import ru.narod.vn91.pointsai.patterns_InputOutput.C_ReadAndWriteFile;

public class TemplateIO extends Variables{
	
	private Template[] base;//���� ���������
	private C_ReadAndWriteFile file=new C_ReadAndWriteFile();
	private C_DataEncryption enc=new C_DataEncryption();
	
protected Template[] getBase(boolean isOffline){
	String strLoad="";
	if(isOffline)strLoad=enc.getDecodedTXT(file.ReadTxtFile(templateFileName));
	else strLoad=enc.getDecodedTXT(file.ReadURLTxtFile(templateUrlName));
	
	base=new Template[strLoad.length()/125];//������� ������ ��� �������� ����
	
	for(int a=0;a<base.length;a++){//������ ��� �������� � �����
		base[a]=new Template(strLoad.substring(a*125,(a+1)*125));//��������� �������
	}	
	return base;
}

protected String getUpdateInfo(){return file.ReadURLTxtFile(templateUrlInfo);}//��������� �������� � �����
protected String getOfflineBaseAsFile(){return enc.getDecodedTXT(file.ReadTxtFile(templateFileName));}
protected void saveBase(Template[] base){String str="";for(int i=0;i<base.length;i++)str+=base[i].toString();saveBase(str,base.length);}
protected void addTemplate(String template){saveBase(enc.getDecodedTXT(file.ReadTxtFile(templateFileName))+template,base.length+1);}	

protected void deleteResult(Template[] base){//��������� ������� � ����
	String str="";for(int i=0;i<base.length;i++)if(!base[i].isDelete())str+=base[i].toString();saveBase(str,base.length-1);
}

private void saveBase(String str,int areas){//��������� ����
	File f=new File("PointsTEsaves");
	if(new File(templateFileName).exists()==false)try {f.mkdir();} catch (Exception e){e.printStackTrace();}
	file.CreateFileIfNotExists(templateFileName);
	file.WriteTxtFile(templateFileName, enc.getEncryptTXT(str));//�������� ����
}
	
}
