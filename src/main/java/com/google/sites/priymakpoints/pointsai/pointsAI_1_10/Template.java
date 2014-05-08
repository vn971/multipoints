package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.awt.Point;

public class Template implements Variables{
	
	private int index=0;
	private TemplateType type=null;
	private String template,template90,template180,template270,templateVert,templateGor,templateVert90,templateGor90;
	private String templateWithoutTargets,template90WithoutTargets,template180WithoutTargets,template270WithoutTargets,
		templateVertWithoutTargets,templateGorWithoutTargets,templateVert90WithoutTargets,templateGor90WithoutTargets;
	private boolean isDeleted=false,isSquare=false,isSide=false;
	public String targetRotateTemplate;
	public RotationType targetRotateType;

public Template(String strTemplate){
	
	index=new Integer(strTemplate.substring(117,122)).intValue();
	type=TemplateType.getTemplateType(strTemplate.substring(122,125));
	isSquare=type.isSquare();isSide=type.isSide();
	
	template=strTemplate.substring(0, 117);
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
public String toString(){return template+index+type;}//strTemplate;}
public String getTemplate(){return template;}
public Point getMoveAI(){
	return getPointCoordinates(targetRotateTemplate,DotType.RED_NORMAL);}

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
	if(str.equals(template)){return true;}
	if(str.equals(template180)){return true;}
	if(str.equals(templateGor)){return true;}
	if(str.equals(templateVert)){return true;}
	if(str.equals(template90)){return true;}
	if(str.equals(template270)){return true;}
	if(str.equals(templateGor90)){return true;}
	if(str.equals(templateVert90)){return true;}
	return false;
}

boolean isEqualsWithoutTargets(String str,String type){
	if(!this.getTemplateType().toString().equals(type))return false;
	str=getTemplateWithoutTargets(str);
	if(str.equals(templateWithoutTargets)){return true;}
	if(str.equals(template180WithoutTargets)){return true;}
	if(str.equals(templateGorWithoutTargets)){return true;}
	if(str.equals(templateVertWithoutTargets)){return true;}
	if(str.equals(template90WithoutTargets)){return true;}
	if(str.equals(template270WithoutTargets)){return true;}
	if(str.equals(templateGor90WithoutTargets)){return true;}
	if(str.equals(templateVert90WithoutTargets)){return true;}
	return false;
}

public boolean isEqualsLikeArea(String str,TemplateType type){
	if(this.type!=type)return false;
	try{str=getTemplateWithoutTargetsExceptE(str);}catch(Exception e){
		System.out.println("occured error string result: "+str);
		System.out.println("occured error on template type: "+type.toString());
		e.printStackTrace();
		//System.exit(-1);
	}
		
	if(!isSide){
		if(isEquals(str,templateWithoutTargets)){targetRotateTemplate=template;targetRotateType=RotationType.r0;return true;}//������� � ���� ����
		if(isEquals(str,template180WithoutTargets)){targetRotateTemplate=template180;targetRotateType=RotationType.r180;return true;}//������� � ���� ����
		if(isEquals(str,templateGorWithoutTargets)){targetRotateTemplate=templateGor;targetRotateType=RotationType.GORIZONTAL;return true;}//������� � ���� ����
		if(isEquals(str,templateVertWithoutTargets)){targetRotateTemplate=templateVert;targetRotateType=RotationType.VERTICAL;return true;}//������� � ���� ����
		if(isEquals(str,template90WithoutTargets)){targetRotateTemplate=template90;targetRotateType=RotationType.r90;return true;}//������� � ���� ����
		if(isEquals(str,template270WithoutTargets)){targetRotateTemplate=template270;targetRotateType=RotationType.r270;return true;}//������� � ���� ����
		if(isEquals(str,templateGor90WithoutTargets)){targetRotateTemplate=templateGor90;targetRotateType=RotationType.GORIZONTAL90;return true;}//������� � ���� ����
		if(isEquals(str,templateVert90WithoutTargets)){targetRotateTemplate=templateVert90;targetRotateType=RotationType.VERTICAL90;return true;}//������� � ���� ����
	}
	
	if(isSide){
		if(isEquals(str,getTemplateWithoutTargetsExceptE(template))){targetRotateTemplate=template;targetRotateType=RotationType.r0;return true;}//������� � ���� ����
		if(isEquals(str,getTemplateWithoutTargetsExceptE(template180))){targetRotateTemplate=template180;targetRotateType=RotationType.r180;return true;}//������� � ���� ����
		if(isEquals(str,getTemplateWithoutTargetsExceptE(templateGor))){targetRotateTemplate=templateGor;targetRotateType=RotationType.GORIZONTAL;return true;}//������� � ���� ����
		if(isEquals(str,getTemplateWithoutTargetsExceptE(templateVert))){targetRotateTemplate=templateVert;targetRotateType=RotationType.VERTICAL;return true;}//������� � ���� ����
		if(isEquals(str,getTemplateWithoutTargetsExceptE(template90))){targetRotateTemplate=template90;targetRotateType=RotationType.r90;return true;}//������� � ���� ����
		if(isEquals(str,getTemplateWithoutTargetsExceptE(template270))){targetRotateTemplate=template270;targetRotateType=RotationType.r270;return true;}//������� � ���� ����
		if(isEquals(str,getTemplateWithoutTargetsExceptE(templateGor90))){targetRotateTemplate=templateGor90;targetRotateType=RotationType.GORIZONTAL90;return true;}//������� � ���� ����
		if(isEquals(str,getTemplateWithoutTargetsExceptE(templateVert90))){targetRotateTemplate=templateVert90;targetRotateType=RotationType.VERTICAL90;return true;}//������� � ���� ����
	}
	
	return false;
}

boolean isEquals(String str,String template){try{
	int similarity=0;
	for(int i=0;i<template.length();i++){
		if(str.substring(i, i+1).equals(DotType.RED.toString())){
			if(template.substring(i, i+1).equals(DotType.ANY.toString())|template.substring(i, i+1).equals(DotType.RED_EMPTY.toString())|
					template.substring(i, i+1).equals(DotType.RED.toString())){similarity++;}else return false;
		}
		if(str.substring(i, i+1).equals(DotType.BLUE.toString())){
			if(template.substring(i, i+1).equals(DotType.ANY.toString())|template.substring(i, i+1).equals(DotType.BLUE.toString())|
					template.substring(i, i+1).equals(DotType.BLUE_EMPTY.toString())|
					(template.substring(i, i+1).equals(DotType.BLUE_TARGET.toString())&!isSide)
				){similarity++;}else return false;
		}
		if(str.substring(i, i+1).equals(DotType.BLUE_TARGET.toString())){
			if(template.substring(i, i+1).equals(DotType.BLUE_TARGET.toString())&isSide){similarity++;}else return false;
		}
		if(str.substring(i, i+1).equals(DotType.LAND.toString())){if(template.substring(i, i+1).equals(DotType.LAND.toString())){similarity++;}else return false;}
		if(str.substring(i, i+1).equals(DotType.OUT.toString())){if(template.substring(i, i+1).equals(DotType.OUT.toString())){similarity++;}else return false;}
		if(str.substring(i, i+1).equals(DotType.NULL.toString())){
			if(template.substring(i, i+1).equals(DotType.NULL.toString())|template.substring(i, i+1).equals(DotType.ANY.toString())|
				template.substring(i, i+1).equals(DotType.RED_EMPTY.toString())|template.substring(i, i+1).equals(DotType.BLUE_EMPTY.toString())){similarity++;}else return false;
		}
		if(str.substring(i, i+1).equals(DotType.GLOBAL.toString())){
			if(template.substring(i, i+1).equals(DotType.GLOBAL.toString())|
				template.substring(i, i+1).equals(DotType.ANY.toString())){similarity++;}else return false;}
	}
	if(similarity==117){return true;}
	return false;
}catch(Exception e){return false;}}

public Point getPointCoordinates(String content,DotType dot){
	for(int i=0;i<content.length();i++)if(content.substring(i, i+1).equals(dot.toString())){return new Point(i%sizeX_TE,i/sizeX_TE);}
	return null;
}

private String getTemplateWithoutTargetsExceptE(String str){
	str=str.replaceAll(DotType.RED_NORMAL.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_ATTACK.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_CAPTURE.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_DEFENCE.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_GROUND.toString(),DotType.NULL.toString());
	str=str.replaceAll(DotType.RED_PROTECTION.toString(),DotType.NULL.toString());
	if(!isSide)str=str.replaceAll(DotType.BLUE_TARGET.toString(),DotType.BLUE.toString());
	str=str.replaceAll(DotType.BLUE_NORMAL.toString(),DotType.NULL.toString());
	return str;
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
