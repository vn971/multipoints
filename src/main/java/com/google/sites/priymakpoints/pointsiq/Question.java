package com.google.sites.priymakpoints.pointsiq;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Question{
	
	int index=0,level=0;
	String str="",text="",startPos="",comment="";
	Makros makros;

Question(String str){	
	this.str=str;
	String move;
	
	move=str.substring(str.indexOf(":")+1,str.indexOf(";"));
	str=str.substring(str.indexOf(";")+1);
	makros=new Makros(move);
		
	index=new Integer(str.substring(str.indexOf(":")+1,str.indexOf(";")));str=str.substring(str.indexOf(";")+1);
	level=new Integer(str.substring(str.indexOf(":")+1,str.indexOf(";")));str=str.substring(str.indexOf(";")+1);
	text=str.substring(str.indexOf(":")+1,str.indexOf(";"));str=str.substring(str.indexOf(";")+1);
	startPos=str.substring(str.indexOf(":")+1,str.indexOf(";"));str=str.substring(str.indexOf(";")+1);
	comment=str.substring(str.indexOf(":")+1,str.indexOf(";"));
}	

public String toString(){return str;}

class Makros{
	
	private List<MakrosLevelMove> moves=new ArrayList<MakrosLevelMove>();
	private String str="";

Makros(String str){
	this.str=str;
	String move;
	while(str.length()>7){
		move=str.substring(str.indexOf("(")+1,str.indexOf(")"));
		str=str.substring(str.indexOf(")")+1);
		moves.add(new MakrosLevelMove(move));
	}
}	

public String toString(){return str;}
MakrosLevelMove[] getMoves(){
	MakrosLevelMove[] base;
	int count=0;
	Iterator i=moves.iterator();
	while(i.hasNext()){i.next();count++;}
	base=new MakrosLevelMove[count];i=moves.iterator();count=0;
	while(i.hasNext()){base[count]=(MakrosLevelMove)i.next();count++;}
	return base;
}

public class MakrosLevelMove {

	Point humanPoint, AIPoint;
	int levelNumber=0;
	String levelLetter="";
	String preLevels="";
	private int x,y;
	private String str;
	
public MakrosLevelMove(String str){
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

}

}

