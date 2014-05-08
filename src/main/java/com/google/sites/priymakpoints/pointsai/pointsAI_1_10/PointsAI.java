package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.awt.Point;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;

public class PointsAI implements Variables{

	public static TemplateEngine base;
	public PointsAIGame game;
	public MoveAI moveAI;
	
public void newGame(){game.newGame();moveAI.deleteStatistics(PointsAI.this);}
	
public PointsAI(){
	moveAI=new MoveAI();
	base=new TemplateEngine(false);
	game=new PointsAIGame();
}
	
public void makeMove(int x,int y,boolean isRed){
	if(isRed){game.makeMove(x,y, MoveType.RED);}
	else {game.makeMove(x,y, MoveType.BLUE);}
	
}

public Point getAIMove() {
	Point point=moveAI.getAImove(this);
	game.makeMove(point.x,point.y, MoveType.RED);
	return point;
}
	
}