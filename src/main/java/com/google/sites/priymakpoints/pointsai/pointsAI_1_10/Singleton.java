package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

public class Singleton implements Variables{
	   private static Singleton instanceTemplate = null;
	   private static Singleton instanceMakros = null;
		private C_ReadAndWriteFile file=new C_ReadAndWriteFile();
		static Template[] base;
		static String strMakrosBase;
		
	   protected Singleton(int isTemplate) {
		   	String strLoad="";
			strLoad=file.ReadResourceFile(templateFileName);
			
			base=new Template[strLoad.length()/125];
			
			for(int a=0;a<base.length;a++){
				base[a]=new Template(strLoad.substring(a*125,(a+1)*125));
			}	
			
	   }
	   
	   protected Singleton() {strMakrosBase=file.ReadResourceFile(makrosFileName);}
	   
	   public static Template[] getInstanceTemplate() {
	      if(instanceTemplate == null) {
	         instanceTemplate = new Singleton(0);
	      }
	      return base;
	   }
	   
	   public static String getInstanceMakros() {
		      if(instanceMakros == null) {
		    	  instanceMakros = new Singleton();
		      }
		      return strMakrosBase;
		   }
	}