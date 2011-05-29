package com.google.sites.priymakpoints.pointsai.p_PointsAI;

import java.awt.Point;
import com.google.sites.priymakpoints.pointsai.p_PointsAI.SingleGameEngineInterface.MoveType;
import com.google.sites.priymakpoints.pointsai.p_TemplateEngine.TemplateEngine;
import ru.narod.vn91.pointsop.gui.Paper;

public class PointsAI{

	public static TemplateEngine base;//=new TemplateEngine();
	public PointsAIGame game;//=new PointsAIGame();
	public MoveAI moveAI;//=new MoveAI();
	Paper paper;
	
public void newGame(){game.newGame();moveAI.deleteStatistics(PointsAI.this);}
	
public PointsAI(){
	moveAI=new MoveAI();
	base=new TemplateEngine(false);
	game=new PointsAIGame();
}

public void setPaper(Paper paper){this.paper=paper;}
	
public void makeMove(int x,int y,boolean isRed){
	if(isRed){paper.makeMove(x,y, true);game.makeMove(x,y, MoveType.RED);}
	else {paper.makeMove(x,y, false);game.makeMove(x,y, MoveType.BLUE);}
	
}

public Point getAIMove() {
	Point point=moveAI.getAImove(this);//����� ������� ������� ���������� ���� ��
	game.makeMove(point.x,point.y, MoveType.RED);
	return point;
}
	
}
