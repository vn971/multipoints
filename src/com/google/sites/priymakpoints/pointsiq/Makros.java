package com.google.sites.priymakpoints.pointsiq;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Makros{
	
	List<MakrosLevelMove> moves=new ArrayList<MakrosLevelMove>();
	boolean isDelete;
	String str="";

public Makros(String str){
	
	this.str=str;
	String move;
	
	while(str.length()>7){
		move=str.substring(str.indexOf("(")+1,str.indexOf(")"));
		str=str.substring(str.indexOf(")")+1);
		moves.add(new MakrosLevelMove(move));
	}
}	

public boolean isDelete(){return isDelete;}
public String toString(){return str;}
public MakrosLevelMove[] getMoves(){
	MakrosLevelMove[] base;
	int count=0;
	Iterator i=moves.iterator();
	while(i.hasNext()){i.next();count++;}
	base=new MakrosLevelMove[count];i=moves.iterator();count=0;
	while(i.hasNext()){base[count]=(MakrosLevelMove)i.next();count++;}
	return base;
}

//public static void main(String[] args){new Makros("");}

}
