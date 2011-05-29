package com.google.sites.priymakpoints.pointsai.p_Walls;

import com.google.sites.priymakpoints.pointsai.p_TemplateEngine.Variables.TemplateType;
import com.google.sites.priymakpoints.pointsai.p_PointsAI.PointsAI;

public class MoveByWall extends WallEngine{

int moveAIx=99,moveAIy=99;
public boolean isCanGroung=false;

public void deleteStatistics(PointsAI pointsAI){super.deleteStatistics(pointsAI);}
public void wallCorrection(int moveX,int moveY){super.wallCorrection(moveX,moveY);}

public boolean isMoveByWall(PointsAI pointsAI){
	int x=0,y=0;
	int smaller=getSmallerWall();
	
	if(smaller==0){isCanGroung=true;return false;}
	if(smaller==1){x=w1.x;y=w1.y;}
	else if(smaller==2){x=w2.x;y=w2.y;}
	else if(smaller==3){x=w3.x;y=w3.y;}
		
	if(isMoveByWLTTemplate(pointsAI,x,y,smaller)){return true;}
		
	if(smaller==1){
		if(isMoveByWLTTemplate(pointsAI,w2.x,w2.y,2))return true;
		if(isMoveByWLTTemplate(pointsAI,w3.x,w3.y,3))return true;
	}
	if(smaller==2){
		if(isMoveByWLTTemplate(pointsAI,w1.x,w1.y,1))return true;
		if(isMoveByWLTTemplate(pointsAI,w3.x,w3.y,3))return true;
	}
	if(smaller==3){
		if(isMoveByWLTTemplate(pointsAI,w2.x,w2.y,2))return true;
		if(isMoveByWLTTemplate(pointsAI,w1.x,w1.y,1))return true;
	}
	
	return false;
}
	
boolean isMoveByWLTTemplate(PointsAI pointsAI,int x,int y,int smaller){
	if(pointsAI.base.isFoundArea(pointsAI.game.getWLTContent(x,y),TemplateType.WALL)){//System.out.println("wlt");
		moveAIx=pointsAI.base.getMoveAIx()+x-4;
		moveAIy=pointsAI.base.getMoveAIy()+y-4;//���������� ��� ��
		
		if(smaller==1){w1.x=moveAIx;w1.y=moveAIy;}
		if(smaller==2){w2.x=moveAIx;w2.y=moveAIy;}
		if(smaller==3){w3.x=moveAIx;w3.y=moveAIy;}
		
		if(pointsAI.moveAI.isAImove(pointsAI,moveAIx,moveAIy))return true;
	}
	return false;
}
	
}
