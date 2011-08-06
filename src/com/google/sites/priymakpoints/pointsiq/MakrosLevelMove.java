package com.google.sites.priymakpoints.pointsiq;

import java.awt.Point;

class MakrosLevelMove {

	Point humanPoint, AIPoint;
	int levelNumber=0;
	String levelLetter="";
	String preLevels="";
	private int x,y;
	private String str;
	
MakrosLevelMove(String str){
	this.str=str;
	
	preLevels=str.substring(str.indexOf("[")+1,str.indexOf("]")-1);
	levelLetter=str.substring(str.indexOf("]")-1,str.indexOf("]"));
	str=str.substring(str.indexOf("]")+1);
	
	levelNumber=preLevels.length()+1;
	
	x=new Integer(str.substring(str.indexOf("[")+1,str.indexOf("]")));str=str.substring(str.indexOf("]")+1);
	y=new Integer(str.substring(str.indexOf("[")+1,str.indexOf("]")));str=str.substring(str.indexOf("]")+1);
	humanPoint=new Point(x,y);
	
	x=new Integer(str.substring(str.indexOf("[")+1,str.indexOf("]")));str=str.substring(str.indexOf("]")+1);
	y=new Integer(str.substring(str.indexOf("[")+1,str.indexOf("]")));str=str.substring(str.indexOf("]")+1);
	AIPoint=new Point(x,y);
}

public String toString(){return str;}
		
}
