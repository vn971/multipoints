package com.google.sites.priymakpoints.pointsai.pointsAI_1_07;

public class Singleton implements Variables{
	   private static Singleton instance = null;
		private C_ReadAndWriteFile file=new C_ReadAndWriteFile();
		static Template[] base;
		
	   protected Singleton() {
		   	String strLoad="";
			strLoad=file.ReadURLTxtFile(templateUrlName);
			
			base=new Template[strLoad.length()/125];//������� ������ ��� �������� ����
			
			for(int a=0;a<base.length;a++){//������ ��� �������� � �����
				base[a]=new Template(strLoad.substring(a*125,(a+1)*125));//��������� �������
			}	
			
	   }
	   
	   public static Template[] getInstance() {
	      if(instance == null) {
	         instance = new Singleton();
	      }
	      return base;
	   }
	}