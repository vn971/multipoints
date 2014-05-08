package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

public class WallEngine {
	
Wall w1=new Wall(18,16),w2=new Wall(19,17),w3=new Wall(21,16);
	
void deleteStatistics(PointsAI pointsAI){w1=new Wall(18,17);w2=new Wall(19,16);w3=new Wall(21,17);}	

int getSmallerWall(PointsAI pointsAI){
	if(w1.isAtSide()&w2.isAtSide()&w3.isAtSide())return 0;
	if(!w1.isAtSide()&w2.isAtSide()&w3.isAtSide())return 1;
	if(w1.isAtSide()&!w2.isAtSide()&w3.isAtSide())return 2;
	if(w1.isAtSide()&w2.isAtSide()&!w3.isAtSide())return 3;
	
	if(w1.isAtSide()&!w2.isAtSide()&!w3.isAtSide())
		if(w2.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY())<=w3.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY()))return 2;else return 3;
	if(!w1.isAtSide()&w2.isAtSide()&!w3.isAtSide())if(w1.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY())<=w3.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY()))return 1;else return 3;
	if(!w1.isAtSide()&!w2.isAtSide()&w3.isAtSide())if(w1.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY())<=w2.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY()))return 1;else return 2;
	
	int smaller=0;
	
	if(w1.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY())<=w2.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY()))smaller=1;else smaller=2;
	if((w3.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY())<=w1.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY())&smaller==1)|
			(w3.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY())<=w2.getLengthFromLastBlue(pointsAI.game.getLastX(),pointsAI.game.getLastY())&smaller==2))smaller=3;
		
	return smaller;
}

void wallCorrection(int moveX,int moveY){
	int l1=0,l2=0,l3=0;
	int smaller=0,length=0;
	
	length=Math.abs(moveX-19)+Math.abs(moveY-15);
	
	l1=Math.abs(moveX-w1.x)+Math.abs(moveY-w1.y);
	l2=Math.abs(moveX-w2.x)+Math.abs(moveY-w2.y);
	l3=Math.abs(moveX-w3.x)+Math.abs(moveY-w3.y);
	
	if(l1<l2)smaller=1;else smaller=2;
	if((l3<l1&smaller==1)|(l3<l2&smaller==2))smaller=3;
	
	if(smaller==1&length>=w1.getLength()){w1.x=moveX;w1.y=moveY;}
	if(smaller==2&length>=w2.getLength()){w2.x=moveX;w2.y=moveY;}
	if(smaller==3&length>=w3.getLength()){w3.x=moveX;w3.y=moveY;}
}


}
