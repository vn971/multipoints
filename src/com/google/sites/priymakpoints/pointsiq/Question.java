package com.google.sites.priymakpoints.pointsiq;

public class Question{
	
	public int index=0,level=0;//,sizeX=13,sizeY=9;
	public boolean isDelete;
	public String str="",text="",startPos="",comment="";
	public Makros makros;

public Question(String str){
	//str="{:([41][6][5][3][4])([4][5][4][5][3]);:1;:1;:Select best move;:00000RBRBRBRBB000000;}";
	
	this.str=str;
	String move;
	
	move=str.substring(str.indexOf(":")+1,str.indexOf(";"));
	str=str.substring(str.indexOf(";")+1);
	makros=new Makros(move);
	
	//System.out.println(str.substring(str.indexOf(":")+1,str.indexOf(";")));
	
	index=new Integer(str.substring(str.indexOf(":")+1,str.indexOf(";")));str=str.substring(str.indexOf(";")+1);
	level=new Integer(str.substring(str.indexOf(":")+1,str.indexOf(";")));str=str.substring(str.indexOf(";")+1);
	text=str.substring(str.indexOf(":")+1,str.indexOf(";"));str=str.substring(str.indexOf(";")+1);
	startPos=str.substring(str.indexOf(":")+1,str.indexOf(";"));str=str.substring(str.indexOf(";")+1);
	//System.out.println("index "+index+"; str="+str); 
	comment=str.substring(str.indexOf(":")+1,str.indexOf(";"));
}	

public boolean isDelete(){return isDelete;}
public String toString(){return str;}

//public static void main(String[] args){new Question("");}

}
