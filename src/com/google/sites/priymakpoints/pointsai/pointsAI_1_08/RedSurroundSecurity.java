package com.google.sites.priymakpoints.pointsai.pointsAI_1_08;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;

public class RedSurroundSecurity implements Variables{

	Point moveAI;
	public int foundedNumber;
	
public boolean isFoundArea(String content,PointsAI pointsAI){
	for(int i=pointsAI.base.base.length-1;i>=0;i--){
		if(pointsAI.base.base[i].isEqualsLikeArea(content,TemplateType.RED_SURROUND)){
			//System.out.println("+"+pointsAI.base.base[i].toString());
			getAIcoordinatesFromBST(pointsAI.base.base[i].targetRotate,pointsAI);foundedNumber=i;return true;
		}
	}
	foundedNumber=0;
	return false;
}	
		
private void getAIcoordinatesFromBST(String content,PointsAI pointsAI){
	String[][] fieldState=pointsAI.game.getFieldState();
	SingleGameEngine e=new SingleGameEngine(39,32);
	for(int i=0;i<39;i++)for(int j=0;j<32;j++){
		if(fieldState[i][j].equals("R"))e.makeMove(i+1, j+1, true);
		if(fieldState[i][j].equals("B"))e.makeMove(i+1, j+1, false);
	}
	
	Point point_RED_NORMAL=getAIcoordinates(content,DotType.RED_NORMAL);
	List<Point> point_RED_ATTACK=getCoordinates(content,DotType.RED_ATTACK);
	if(point_RED_ATTACK!=null){
		//rss=new Point[point_RED_ATTACK.size()+1];
		makeMove(e,point_RED_NORMAL,pointsAI,true);
		makeMove(e,point_RED_ATTACK,pointsAI,true);
	}
	else {
		//rss=new Point[1];
		makeMove(e,point_RED_NORMAL,pointsAI,true);
	}
	
	//System.out.println("score rst "+e.getBlueScore()+";"+e.getRedScore());
	if(e.getRedScore()>0)moveAI=getAIcoordinates(content,DotType.RED_NORMAL);
	else {
		moveAI=null;//getAIcoordinates(content,DotType.RED_NORMAL);
		//RSSbase.add(rss);		
		//System.out.println("save to base: "+BSSbase.size());//save
	}
	//new EngineFrame(e);
};

private List<Point> getCoordinates(String content,DotType dot){
	List<Point> point=new ArrayList<Point>();
	for(int i=0;i<content.length();i++)if(content.substring(i, i+1).equals(dot.toString())){point.add(new Point(i%13,i/13));}
	return point;
}

private void makeMove(SingleGameEngine e,List<Point> point,PointsAI pointsAI,boolean isRed){
	Iterator it=point.iterator();
	int index=1;
	while(it.hasNext()){
		Point p=(Point)it.next();
		int moveX=p.x+pointsAI.game.getLastX()-4;
		int moveY=p.y+pointsAI.game.getLastY()-4;
		e.makeMove(moveX, moveY, isRed);
		//System.out.println("blue moved "+moveX+";"+moveY);
		//rss[index]=new Point(moveX, moveY);
		index++;
	}
}

private void makeMove(SingleGameEngine e,Point point,PointsAI pointsAI,boolean isRed){
	int moveX=point.x+pointsAI.game.getLastX()-4;
	int moveY=point.y+pointsAI.game.getLastY()-4;
	e.makeMove(moveX, moveY, isRed);
	//System.out.println("red_protection moved "+moveX+";"+moveY);
	//rss[0]=new Point(moveX, moveY);
}

private Point getAIcoordinates(String content,DotType dot){
	for(int i=0;i<content.length();i++)if(content.substring(i, i+1).equals(dot.toString())){return new Point(i%13,i/13);}
	return null;
}

public Point getMoveAI(){return moveAI;}
	
}
