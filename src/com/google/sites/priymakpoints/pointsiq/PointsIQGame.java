package com.google.sites.priymakpoints.pointsiq;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;

class PointsIQGame{

	SingleGameEngine singleGameEngine;
	DrawSingleGameEngine OP_paint=new DrawSingleGameEngine();
	private Graphics graphics;
	private PointsIQ pointsIQ;
	int qTrue=0,qThis=0;
	boolean isComplete=true;
	private int squareSize=16;
	private int offsetX=1;
	private int offsetY=2;
	String spc="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
PointsIQGame(PointsIQ pointsIQ){this.pointsIQ=pointsIQ;newGame();}
void newGame(){singleGameEngine=new SingleGameEngine(20,20);}	
void makeMove(int x, int y,boolean isRed){singleGameEngine.makeMove(x,y,isRed);repaint();}

boolean isCanMakeMove(int x, int y){return singleGameEngine.canMakeMove(x,y);}
int getMouseClickX(MouseEvent me){return (int)(((double)me.getX()-5-(double)((offsetX-1)*squareSize))/(double)squareSize);};
int getMouseClickY(MouseEvent me){return (int)(((double)me.getY()-5-(double)((offsetY-1)*squareSize))/(double)squareSize);};
void repaint(){
	graphics=pointsIQ.getGraphics();
	graphics.setColor(new Color(254,254,254));
	graphics.fillRect(0, 34, squareSize*(20+1+offsetX), 20*(squareSize+1));
	OP_paint.paint(graphics, singleGameEngine);
}

}
