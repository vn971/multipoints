package com.google.sites.priymakpoints.pointsai.p_TemplateEngine;

public class Template extends Variables{
	
	private int index=0;//,sizeX=13,sizeY=9;
	private TemplateType type=null;
	private String template,template90,template180,template270,templateVert,templateGor,templateVert90,templateGor90;
	private String templateWithoutTargets,template90WithoutTargets,template180WithoutTargets,template270WithoutTargets,
		templateVertWithoutTargets,templateGorWithoutTargets,templateVert90WithoutTargets,templateGor90WithoutTargets;
	private String strTemplate="";
	private boolean isDeleted=false,isSquare=false;
	private int moveAIx=99,moveAIy=99;

public Template(String strTemplate){
	this.strTemplate=strTemplate;
	
	index=new Integer(strTemplate.substring(117,122)).intValue();
	type=TemplateType.getTemplateType(strTemplate.substring(122,125));
	//getTemplateSize(type);
	isSquare=type.isSquare();
	
	template=strTemplate.substring(0, 117);
	//System.out.println(template.replaceAll("O", "").length());
	template180=RotationType.getTransform(RotationType.r180,template);
	templateGor=RotationType.getTransform(RotationType.GORIZONTAL,template);
	templateVert=RotationType.getTransform(RotationType.VERTICAL,template);

	if(isSquare){
		template90=RotationType.getTransform(RotationType.r90,template);
		template270=RotationType.getTransform(RotationType.r270,template);
		templateGor90=RotationType.getTransform(RotationType.GORIZONTAL90,template);
		templateVert90=RotationType.getTransform(RotationType.VERTICAL90,template);
	}
	
	templateWithoutTargets=getTemplateWithoutTargets(template);
	template180WithoutTargets=getTemplateWithoutTargets(template180);
	templateVertWithoutTargets=getTemplateWithoutTargets(templateVert);
	templateGorWithoutTargets=getTemplateWithoutTargets(templateGor);
	
	if(isSquare){
		template90WithoutTargets=getTemplateWithoutTargets(template90);
		template270WithoutTargets=getTemplateWithoutTargets(template270);
		templateVert90WithoutTargets=getTemplateWithoutTargets(templateVert90);
		templateGor90WithoutTargets=getTemplateWithoutTargets(templateGor90);
	}
	
}	

/*public int getTemplateSize(TemplateType type){	
	if(type==TemplateType.SQUARE_SIDE){sizeX=9;sizeY=9;return 0;}
	if(type==TemplateType.SQUARE_CORNER){sizeX=9;sizeY=9;return 0;}
	if(type==TemplateType.SQUARE){sizeX=9;sizeY=9;return 0;}
	if(type==TemplateType.SQUARE_AREA){sizeX=9;sizeY=9;return 0;}
	if(type==TemplateType.SHORT_SIDE){sizeX=13;sizeY=9;return 0;}
	if(type==TemplateType.LONG){sizeX=13;sizeY=9;return 0;}
	if(type==TemplateType.LONG_SIDE){sizeX=13;sizeY=9;return 0;}
	if(type==TemplateType.WALL){sizeX=9;sizeY=9;return 0;}
	sizeX=0;sizeY=0;
	return 0;
}*/

public int getDotsCount(Dot dot){
	int count=0;
	for(int i=0;i<template.length();i++){if(template.substring(i, i+1).equals(dot.toString()))count++;}
	return count;
}
public TemplateType getTemplateType(){return type;}
public int getTemplateIndex(){return index;}
public void setDelete(boolean delete){isDeleted=delete;}
public boolean isDelete(){return isDeleted;}
public String toString(){return strTemplate;}
public String getTemplate(){return template;}
public int getMoveAIx(){return moveAIx;}
public int getMoveAIy(){return moveAIy;}

public String getRotateTemplate(RotationType type){
	if(type==RotationType.r0)return template;
	if(type==RotationType.r180)return template180;
	if(type==RotationType.GORIZONTAL)return templateGor;
	if(type==RotationType.GORIZONTAL90)return templateGor90;
	if(type==RotationType.r90)return template90;
	if(type==RotationType.r270)return template270;
	if(type==RotationType.VERTICAL)return templateVert;
	if(type==RotationType.VERTICAL90)return templateVert90;
	return template;
}

boolean isEqualsWithTargets(String str,String type){
	if(!this.getTemplateType().toString().equals(type))return false;
	if(str.equals(template)){return true;}//������� � ���� ����
	if(str.equals(template180)){return true;}//������� � ���� ����
	if(str.equals(templateGor)){return true;}//������� � ���� ����
	if(str.equals(templateVert)){return true;}//������� � ���� ����
	if(str.equals(template90)){return true;}//������� � ���� ����
	if(str.equals(template270)){return true;}//������� � ���� ����
	if(str.equals(templateGor90)){return true;}//������� � ���� ����
	if(str.equals(templateVert90)){return true;}//������� � ���� ����
	return false;
}

boolean isEqualsWithoutTargets(String str,String type){
	if(!this.getTemplateType().toString().equals(type))return false;
	str=getTemplateWithoutTargets(str);
	if(str.equals(templateWithoutTargets)){return true;}//������� � ���� ����
	if(str.equals(template180WithoutTargets)){return true;}//������� � ���� ����
	if(str.equals(templateGorWithoutTargets)){return true;}//������� � ���� ����
	if(str.equals(templateVertWithoutTargets)){return true;}//������� � ���� ����
	if(str.equals(template90WithoutTargets)){return true;}//������� � ���� ����
	if(str.equals(template270WithoutTargets)){return true;}//������� � ���� ����
	if(str.equals(templateGor90WithoutTargets)){return true;}//������� � ���� ����
	if(str.equals(templateVert90WithoutTargets)){return true;}//������� � ���� ����
	return false;
}

boolean isEqualsLikeArea(String str,TemplateType type,Dot dot){
	if(!this.getTemplateType().toString().equals(type.toString()))return false;
	str=getTemplateWithoutTargets(str);
	//System.out.print("+");
	if(isEquals(str,templateWithoutTargets)){getAIcoordinates(template,dot);return true;}//������� � ���� ����
	if(isEquals(str,template180WithoutTargets)){getAIcoordinates(template180,dot);return true;}//������� � ���� ����
	if(isEquals(str,templateGorWithoutTargets)){getAIcoordinates(templateGor,dot);return true;}//������� � ���� ����
	if(isEquals(str,templateVertWithoutTargets)){getAIcoordinates(templateVert,dot);return true;}//������� � ���� ����
	if(isEquals(str,template90WithoutTargets)){getAIcoordinates(template90,dot);return true;}//������� � ���� ����
	if(isEquals(str,template270WithoutTargets)){getAIcoordinates(template270,dot);return true;}//������� � ���� ����
	if(isEquals(str,templateGor90WithoutTargets)){getAIcoordinates(templateGor90,dot);return true;}//������� � ���� ����
	if(isEquals(str,templateVert90WithoutTargets)){getAIcoordinates(templateVert90,dot);return true;}//������� � ���� ����
	return false;
}

boolean isEquals(String str,String template){try{
	int similarity=0;
	for(int i=0;i<template.length();i++){
		if(str.substring(i, i+1).equals(Dot.RED.toString())){
			if(template.substring(i, i+1).equals(Dot.ANY.toString())|template.substring(i, i+1).equals(Dot.RED_EMPTY.toString())|
					template.substring(i, i+1).equals(Dot.RED.toString())){similarity++;}
		}
		if(str.substring(i, i+1).equals(Dot.BLUE.toString())){
			if(template.substring(i, i+1).equals(Dot.ANY.toString())|template.substring(i, i+1).equals(Dot.BLUE.toString())|
					template.substring(i, i+1).equals(Dot.BLUE_EMPTY.toString())|template.substring(i, i+1).equals(Dot.BLUE_TARGET.toString())){similarity++;}
		}
		if(str.substring(i, i+1).equals(Dot.LAND.toString())){if(template.substring(i, i+1).equals(Dot.LAND.toString())){similarity++;}}
		if(str.substring(i, i+1).equals(Dot.OUT.toString())){if(template.substring(i, i+1).equals(Dot.OUT.toString())){similarity++;}}
		if(str.substring(i, i+1).equals(Dot.NULL.toString())){
			if(template.substring(i, i+1).equals(Dot.NULL.toString())|template.substring(i, i+1).equals(Dot.ANY.toString())|
				template.substring(i, i+1).equals(Dot.RED_EMPTY.toString())|template.substring(i, i+1).equals(Dot.BLUE_EMPTY.toString())){similarity++;}
		}
	}
	if(similarity==117){return true;}
	return false;
}catch(Exception e){return false;}}

private void getAIcoordinates(String content,Dot dot){
	for(int i=0;i<content.length();i++)if(content.substring(i, i+1).equals(dot.toString())){moveAIx=i%sizeX;moveAIy=i/sizeX;}
	//System.out.println(moveAIx+";"+moveAIy);
};

private String getTemplateWithoutTargets(String str){
	str=str.replaceAll(Dot.RED_NORMAL.toString(),Dot.NULL.toString());
	str=str.replaceAll(Dot.RED_ATTACK.toString(),Dot.NULL.toString());
	str=str.replaceAll(Dot.RED_CAPTURE.toString(),Dot.NULL.toString());
	str=str.replaceAll(Dot.RED_DEFENCE.toString(),Dot.NULL.toString());
	str=str.replaceAll(Dot.RED_GROUND.toString(),Dot.NULL.toString());
	str=str.replaceAll(Dot.RED_PROTECTION.toString(),Dot.NULL.toString());
	str=str.replaceAll(Dot.BLUE_TARGET.toString(),Dot.BLUE.toString());
	return str;
}

private boolean isEquals(String str){	

	return false;
}

/*public String getAreaFromTemplate(String template){
	String area="";
	area=template.replaceAll("1", "N");area=area.replaceAll("2", "N");area=area.replaceAll("3", "N");
	area=area.replaceAll("x", "N");area=area.replaceAll("X", "N");
	area=area.replaceAll("y", "N");area=area.replaceAll("Y", "N");
	area=area.replaceAll("z", "N");area=area.replaceAll("Z", "N");
	return area;
}*/

//public String get9x9Template(String template){String area="";for(int i=0;i<sizeY;i++){area+=template.substring(sizeX*i+2,sizeX*i+11);}return area;}

/*public String getInvert90(String template){//���������� ������ �� 90 ��������
	String str="";
	for(int i=0;i<sizeX;i++){for(int j=sizeY-1;j>=0;j--)str+=template.substring(sizeX*j+i,sizeX*j+i+1);}
	return str;
}*/

/*public String getInvert180(String template){//���������� ������ �� 90 ��������
	String str="";
	for(int i=sizeY-1;i>=0;i--)for(int j=sizeX-1;j>=0;j--)str+=template.substring(sizeX*i+j,i*sizeX+j+1);
	return str;
}*/

/*public String getGorizontalSimmetry(String template){//���������� ������ ����������� ����� �� �����
	String str="";
	for(int j=0;j<sizeY;j++)for(int i=0;i<sizeX;i++)str+=template.substring((j+1)*sizeX-i-1,(j+1)*sizeX-i);
	return str;
}*/

/*public String getVerticalSimmetry(String template){//���������� ������ ����������� ������ ����
	String str="";
	for(int i=sizeY-1;i>=0;i--)str+=template.substring(sizeX*i,(i+1)*sizeX);
	return str;
}*/

}
