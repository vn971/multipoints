package com.google.sites.priymakpoints.pointsiq;

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

}
