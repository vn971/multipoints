package com.google.sites.priymakpoints.pointsai.pointsAI_1_056;

public class Singleton extends Variables{
	   private static Singleton instance = null;
		private C_ReadAndWriteFile file=new C_ReadAndWriteFile();
		static Template[] base;
		public String templateUrlName="http://sites.google.com/site/priymakpoints/templates/template.txt";
		
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