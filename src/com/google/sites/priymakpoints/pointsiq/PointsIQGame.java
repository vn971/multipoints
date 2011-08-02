package com.google.sites.priymakpoints.pointsiq;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;

public class PointsIQGame implements Variables{

	public SingleGameEngine singleGameEngine;//������ �������� ����
	public DrawSingleGameEngine OP_paint=new DrawSingleGameEngine();//��������� �������� ����
	private Graphics graphics;
	private PointsIQ pointsIQ;
	int qNumber=0,qLevel=0,qTrue=0,qThis=0;
	boolean isComplete=true;
		
PointsIQGame(PointsIQ pointsIQ){this.pointsIQ=pointsIQ;newGame();}

void newGame(){
	pointsIQ.setLabelText(getLabelText());	
	singleGameEngine=new SingleGameEngine(sizeX,sizeY);//������ �������� ����
}	
	
public String[][] getFieldState(){return OP_paint.getFieldState(singleGameEngine);}

void makeMove(int x, int y,boolean isRed){
	singleGameEngine.makeMove(x,y,isRed);
	pointsIQ.setLabelText(getLabelText());	
	repaint();
}

boolean isCanMakeMove(int x, int y){return singleGameEngine.canMakeMove(x,y);}
int getMouseClickX(MouseEvent me){return (int)(((double)me.getX()-5-(double)((offsetX-1)*squareSize))/(double)squareSize);};
int getMouseClickY(MouseEvent me){return (int)(((double)me.getY()-5-(double)((offsetY-1)*squareSize))/(double)squareSize);};
void repaint(){
	graphics=pointsIQ.getGraphics();
	graphics.setColor(new Color(254,254,254));
	graphics.fillRect(0, 50, squareSize*(sizeX+1+offsetX), sizeY*(squareSize+1));
	OP_paint.paint(graphics, singleGameEngine);
}
int getBluePlayerCount(){return singleGameEngine.getBlueScore();}
int getRedPlayerCount(){return singleGameEngine.getRedScore();} 

String getLabelText(){
	return "<HTML>"+spc+"Заданий <FONT color=blue>"+qNumber+"</FONT>"
		+spc+"Ответов "+"<FONT color=blue> "+qTrue+" из "+qThis+"</FONT>"+spc+"Сложность <FONT color=blue>"+qLevel+"</HTML>";
}

}
