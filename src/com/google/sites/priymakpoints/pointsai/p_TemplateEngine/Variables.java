package com.google.sites.priymakpoints.pointsai.p_TemplateEngine;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Variables{
	
	public Font f8=new Font("tahoma", 8, 8);
	public int squareSize=16;//������� ������ ���� � ��������
	public int offsetX=4,offsetY=1,pointSize=(int)(squareSize/2);//������� (����������� - ������ �������� ����) � ������ �����
	public static int sizeX=13;//������ ���� ������ (f_s x f_s) max=13x9
	public static int sizeY=9;
	
	public String templateFileName="PointsTEsaves//template.txt";
	//public String templateFileInfo="PointsTEsaves//templateinfo.txt";
	public String templateUrlName="http://sites.google.com/site/priymakpoints/templates/template.txt";
	public String templateUrlInfo="http://sites.google.com/site/priymakpoints/templates/templateinfo.txt";
	
	public BufferedImage getIconImage(){
		BufferedImage i=new BufferedImage(20, 20,BufferedImage.TYPE_INT_RGB);
		Graphics2D g=(Graphics2D) i.getGraphics();
		
		g.setColor(new Color(21, 36, 249, 255));g.fillRect(0, 0, 20, 20);
		g.setColor(new Color(255,0,0,255));g.fillOval(1, 1, 18, 18);
		g.setColor(Color.black);g.drawString("TE", 3, 15);
		g.setColor(Color.orange);g.drawOval(0,0, 19, 19);
		
		return i;
	}
	
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
