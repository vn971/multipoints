package com.google.sites.priymakpoints.pointsai.pointsAI_1_07;

import java.awt.Point;

public class Template implements Variables{
	
	private int index=0;//,sizeX=13,sizeY=9;
	private TemplateType type=null;
	private String template,template90,template180,template270,templateVert,templateGor,templateVert90,templateGor90;
	private String templateWithoutTargets,template90WithoutTargets,template180WithoutTargets,template270WithoutTargets,
		templateVertWithoutTargets,templateGorWithoutTargets,templateVert90WithoutTargets,templateGor90WithoutTargets;
	private String strTemplate="";
	private boolean isDeleted=false,isSquare=false;
	public String targetRotate;

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

public int getDotsCount(DotType dot){
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
public Point getMoveAI(){return getAIcoordinates(targetRotate,DotType.RED_NORMAL);}

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

public boolean isEqualsLikeArea(String str,TemplateType type){
	if(!this.getTemplateType().toString().equals(type.toString()))return false;
	str=getTemplateWithoutTargets(str);
	//System.out.print("+");
	if(isEquals(str,templateWithoutTargets)){targetRotate=template;return true;}//������� � ���� ����
	if(isEquals(str,template180WithoutTargets)){targetRotate=template180;return true;}//������� � ���� ����
	if(isEquals(str,templateGorWithoutTargets)){targetRotate=templateGor;return true;}//������� � ���� ����
	if(isEquals(str,templateVertWithoutTargets)){targetRotate=templateVert;return true;}//������� � ���� ����
	if(isEquals(str,template90WithoutTargets)){targetRotate=template90;return true;}//������� � ���� ����
	if(isEquals(str,template270WithoutTargets)){targetRotate=template270;return true;}//������� � ���� ����
	if(isEquals(str,templateGor90WithoutTargets)){targetRotate=templateGor90;return true;}//������� � ���� ����
	if(isEquals(str,templateVert90WithoutTargets)){targetRotate=templateVert90;return true;}//������� � ���� ����
	return false;
}

boolean isEquals(String str,String template){try{
	int similarity=0;
	for(int i=0;i<template.length();i++){
		if(str.substring(i, i+1).equals(DotType.RED.toString())){
			if(template.substring(i, i+1).equals(DotType.ANY.toString())|template.substring(i, i+1).equals(DotType.RED_EMPTY.toString())|
					template.substring(i, i+1).equals(DotType.RED.toString())){similarity++;}
		}
		if(str.substring(i, i+1).equals(DotType.BLUE.toString())){
			if(template.substring(i, i+1).equals(DotType.ANY.toString())|template.substring(i, i+1).equals(DotType.BLUE.toString())|
					template.substring(i, i+1).equals(DotType.BLUE_EMPTY.toString())|template.substring(i, i+1).equals(DotType.BLUE_TARGET.toString())){similarity++;}
		}
		if(str.substring(i, i+1).equals(DotType.LAND.toString())){if(template.substring(i, i+1).equals(DotType.LAND.toString())){similarity++;}}
		if(str.substring(i, i+1).equals(DotType.OUT.toString())){if(template.substring(i, i+1).equals(DotType.OUT.toString())){similarity++;}}
		if(str.substring(i, i+1).equals(DotType.NULL.toString())){
			if(template.substring(i, i+1).equals(DotType.NULL.toString())|template.substring(i, i+1).equals(DotType.ANY.toString())|
				template.substring(i, i+1).equals(DotType.RED_EMPTY.toString())|template.substring(i, i+1).equals(DotType.BLUE_EMPTY.toString())){similarity++;}
		}
	}
	if(similarity==117){return true;}
	return false;
}catch(Exception e){return false;}}

private Point getAIcoordinates(String content,DotType dot){
	for(int i=0;i<content.length();i++)if(content.substring(i, i+1).equals(dot.toString())){return new Point(i%sizeX_TE,i/sizeX_TE);}
	return null;
}

private String getTemplateWithoutTargets(String str){
	str=str.replaceAll(DotType.RED_NORMAL.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_ATTACK.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_CAPTURE.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_DEFENCE.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_GROUND.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_PROTECTION.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.BLUE_TARGET.toString(),DotType.BLUE.toString());
	str=str.replaceAll(DotType.BLUE_NORMAL.toString(),DotType.NULL.toString());
	return str;
}

}
