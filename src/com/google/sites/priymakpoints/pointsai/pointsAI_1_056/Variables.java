package com.google.sites.priymakpoints.pointsai.pointsAI_1_056;

import java.applet.Applet;
import java.applet.AudioClip;
import ru.narod.vn91.pointsop.sounds.Sounds;

public class Variables {
	
	public AudioClip sound=Applet.newAudioClip(Sounds.class.getResource("makeMove.wav"));
	
	public int squareSize=16;//������� ������ ���� � ��������
	public int offsetX=1,offsetY=3,point_size=(int)(squareSize/2);//������� (����������� - ������ �������� ����) � ������ �����
	public int f_s_x=39,f_s_y=32;//������ ���� ������ (f_s x f_s)
	public boolean isDrawStringEquivalent=false;//�������� ����� ��� �� ��������� ����������
	public static int sizeX=13;

	public static int sizeY=9;	

	String appName="PointsAI ";//�������� ���������
	String appVersion="версия 1.056 ";
	String appDate="(09.05.2011)";
	String site="http://sites.google.com/site/priymakpoints/intelligence";
		
	String text="<html><font size=5>"+appName+
		"<br><font size=2 color=gray>(Points Artificial Intelligence)<br>" +
		"<font size=3 color=black>"+appVersion+"урезанная "+appDate+"<br>автор Алексей Приймак";

	
	public enum Dot {ANY,NULL,OUT,LAND,BLUE,RED,RED_EMPTY,BLUE_EMPTY,BLUE_TARGET,
			RED_NORMAL,RED_ATTACK,RED_PROTECTION,RED_GROUND,RED_CAPTURE,RED_DEFENCE;
		@Override
		public String toString() {
			switch (this) {
			case ANY:return "A";
			case NULL:return "N";
			case OUT:return "O";
			case LAND:return "L";
			case BLUE:return "B";
			case RED:return "R";
			case RED_EMPTY:return "P";
			case BLUE_EMPTY:return "C";
			case BLUE_TARGET:return "E";
			case RED_NORMAL:return "0";
			case RED_ATTACK:return "1";
			case RED_PROTECTION:return "2";
			case RED_CAPTURE:return "3";
			case RED_GROUND:return "4";
			case RED_DEFENCE:return "5";
			default:return "$";
			}
		}
	}
	
	public enum TemplateType {
		SQUARE_SIDE,SQUARE_CORNER,SQUARE,LONG,AREA,SHORT_SIDE,LONG_SIDE,WALL,ERROR;
		@Override
		public String toString() {
			switch (this) {
			case SQUARE_SIDE:	return "sst";
			case SQUARE_CORNER:	return "sct";
			case SQUARE:		return "sqt";
			case AREA:			return "art";
			case SHORT_SIDE:	return "sht";
			case LONG:			return "lgt";
			case LONG_SIDE:		return "lst";
			case WALL:			return "wlt";
			case ERROR:			return "err";
			default:return "err";
			}
		}
		public boolean isSquare(){
			switch (this) {
			case SQUARE_SIDE:	return true;
			case SQUARE_CORNER:	return true;
			case SQUARE:		return true;
			case AREA:			return true;
			case SHORT_SIDE:	return false;
			case LONG:			return false;
			case LONG_SIDE:		return false;
			case WALL:			return true;
			case ERROR:			return true;
			default:return true;
			}
		}
		public static TemplateType getTemplateType(String str){	
			if(str.equals(TemplateType.SQUARE_SIDE.toString()))return TemplateType.SQUARE_SIDE;
			if(str.equals(TemplateType.SQUARE_CORNER.toString()))return TemplateType.SQUARE_CORNER;
			if(str.equals(TemplateType.SQUARE.toString()))return TemplateType.SQUARE;
			if(str.equals(TemplateType.AREA.toString()))return TemplateType.AREA;
			if(str.equals(TemplateType.SHORT_SIDE.toString()))return TemplateType.SHORT_SIDE;
			if(str.equals(TemplateType.LONG.toString()))return TemplateType.LONG;
			if(str.equals(TemplateType.LONG_SIDE.toString()))return TemplateType.LONG_SIDE;
			if(str.equals(TemplateType.WALL.toString()))return TemplateType.WALL;
			return TemplateType.ERROR;
		}
	}
	
	public enum RotationType{r0,r90,r180,r270,GORIZONTAL,VERTICAL,GORIZONTAL90,VERTICAL90,ERROR;
		public static String getTransform(RotationType type,String str) {
			String str1="";//String str2=""; //int sizeX=13,sizeY=9;
			boolean isSquare=isSquareTemplate(str);
			switch (type) {
			case r0:return str;
			case r90:
				//System.out.println(str);
				if(!isSquare)return str;
				str=str.replaceAll("O", "");
				for(int i=0;i<9;i++){for(int j=9-1;j>=0;j--)str1+=str.substring(9*j+i,9*j+i+1);str1+="OOOO";};
				return str1;
			case r180:
				if(isSquare){str=getTransform(RotationType.r90,str);return(getTransform(RotationType.r90,str));}
				else{for(int i=sizeY-1;i>=0;i--)for(int j=sizeX-1;j>=0;j--)str1+=str.substring(sizeX*i+j,i*sizeX+j+1);return str1;}
			case r270:if(!isSquare)return str;str=getTransform(RotationType.r180,str);return(getTransform(RotationType.r90,str));
			case GORIZONTAL:
				if(isSquare){str=getTransform(RotationType.r180,str);return(getTransform(RotationType.VERTICAL,str));}
				else{for(int j=0;j<sizeY;j++)for(int i=0;i<sizeX;i++)str1+=str.substring((j+1)*sizeX-i-1,(j+1)*sizeX-i);return str1;}
			case VERTICAL:for(int i=sizeY-1;i>=0;i--)str1+=str.substring(sizeX*i,(i+1)*sizeX);return str1;
			case GORIZONTAL90:if(!isSquare)return str;str=getTransform(RotationType.GORIZONTAL,str);return(getTransform(RotationType.r90,str));
			case VERTICAL90:if(!isSquare)return str;str=getTransform(RotationType.VERTICAL,str);return(getTransform(RotationType.r90,str));
			case ERROR:return str;
			default:return str;
			}
		}
		public static boolean isSquareTemplate(String template){
			int squareEfficient=0;
			for(int i=0;i<sizeY;i++){
				if(template.substring(sizeX*i+9,sizeX*i+10).equals("O"))squareEfficient++;
				if(template.substring(sizeX*i+10,sizeX*i+11).equals("O"))squareEfficient++;
				if(template.substring(sizeX*i+11,sizeX*i+12).equals("O"))squareEfficient++;
				if(template.substring(sizeX*i+12,sizeX*i+13).equals("O"))squareEfficient++;
			}
			if(squareEfficient==36)return true;else return false;
		}
	}	
	
}
