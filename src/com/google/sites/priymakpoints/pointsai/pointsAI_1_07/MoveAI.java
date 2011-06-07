package com.google.sites.priymakpoints.pointsai.pointsAI_1_07;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import javax.swing.JFrame;

public class MoveAI implements Variables{
	
	int moveAIx=99,moveAIy=99;
	public MoveByWall moveByWall=new MoveByWall();
	BlueSurroundSecurity bss=new BlueSurroundSecurity();
	
void deleteStatistics(PointsAI pointsAI){
	moveByWall.deleteStatistics(pointsAI);
	bss=new BlueSurroundSecurity();
}	

Point getAImove(PointsAI pointsAI){//��������� �������� ������������ ���� ��
	if(!isMoveByAI(pointsAI)){
		while(!pointsAI.game.isCanMakeMove(moveAIx,moveAIy)){
			moveAIx=Math.abs(rand.nextInt(pointsAI.game.sizeX))+1;
			moveAIy=Math.abs(rand.nextInt(pointsAI.game.sizeY))+1;
		}
	}
return new Point(moveAIx, moveAIy);
}

boolean isMoveByAI(PointsAI pointsAI){

	if(isMoveByTemplate(pointsAI)){moveByWall.wallCorrection(moveAIx, moveAIy);return true;}
	if(moveByWall.isMoveByWall(pointsAI)){return true;}
	if(moveByWall.isCanGroung)if(isMoveByGround(pointsAI)){return true;}
	
	return false;
}

boolean isMoveByGround(PointsAI pointsAI){
	String[][] f=pointsAI.game.getFieldState();
	for(int i=0;i<37;i++)for(int j=0;j<30;j++){
		if(f[i][j].equals("R")&f[i+2][j].equals("R")&f[i+1][j].equals("N")){moveAIx=i+2; moveAIy=j+1;if(isAImove(pointsAI))return true;}
		if(f[i][j].equals("R")&f[i][j+2].equals("R")&f[i][j+1].equals("N")){moveAIx=i+1; moveAIy=j+2;if(isAImove(pointsAI))return true;}
	}
	for(int i=1;i<37;i++)for(int j=1;j<30;j++){
		if(f[i][j].equals("R")&f[i+1][j+1].equals("R")&f[i+1][j].equals("N")&f[i][j+1].equals("N")){
			moveAIx=i+2; moveAIy=j+1;if(isAImove(pointsAI))return true;
		}
		if(f[i][j].equals("R")&f[i+1][j-1].equals("R")&f[i+1][j].equals("N")&f[i][j-1].equals("N")){
			moveAIx=i+1; moveAIy=j;if(isAImove(pointsAI))return true;
		}
	}
	//for(int i=1;i<37;i++)for(int j=1;j<30;j++){}

	return false;
}

boolean isMoveByTemplate(PointsAI pointsAI){
	int x=pointsAI.game.getLastX(),y=pointsAI.game.getLastY();
	
	Point point=bss.getAIcoordinatesFromBSSbase(pointsAI);
	if(point==null){}else{
		if(pointsAI.game.isCanMakeMove(point.x, point.y)){
			moveAIx=point.x; moveAIy=point.y;return true;
		}else{}
	}
	
	try{if(isMoveByBSTTemplate(pointsAI)){return true;}}catch(Exception e){return false;}//move by blue surround
	
	if(x<6&y<6){if(isMoveBySCTTemplateLT(pointsAI)){return true;}}//move by corner
	if(x>33&y<6){if(isMoveBySCTTemplateRT(pointsAI)){return true;}}//move by corner
	if(x<6&y>26){if(isMoveBySCTTemplateLB(pointsAI)){return true;}}//move by corner
	if(x>33&y>26){if(isMoveBySCTTemplateRB(pointsAI)){return true;}}//move by corner
	if(x>26&y>7&y<25){if(isMoveByLSTTemplateRight(pointsAI)){return true;}}//move by long side Right
	if(x<6&y>7&y<25){if(isMoveByLSTTemplateLeft(pointsAI)){return true;}}//move by long side Left
	if(y>26&x>7&x<32){if(isMoveByLSTTemplateBottom(pointsAI)){return true;}}//move by long side bottom
	if(y<6&x>7&x<32){if(isMoveByLSTTemplateTop(pointsAI)){return true;}}//move by long side top
	if(x>27&y>5&y<27){if(isMoveBySHTTemplateRight(pointsAI)){return true;}}//move by short side
	if(x<11&y>5&y<27){if(isMoveBySHTTemplateLeft(pointsAI)){return true;}}//move by short side
	if(y<11&x>5&x<34){if(isMoveBySHTTemplateTop(pointsAI)){return true;}}//move by short side
	if(y>22&x>5&x<34){if(isMoveBySHTTemplateBottom(pointsAI)){return true;}}//move by short side
	if(x<6&y>5&y<27){if(isMoveBySSTTemplateL(pointsAI)){return true;}}//move by side	
	if(x>33&y>5&y<27){if(isMoveBySSTTemplateR(pointsAI)){return true;}}//move by side
	if(y<6&x>5&x<34){if(isMoveBySSTTemplateT(pointsAI)){return true;}}//move by side	
	if(y>26&x>5&x<34){if(isMoveBySSTTemplateB(pointsAI)){return true;}}//move by side
	if(isMoveByLGTTemplateHorizontal(pointsAI)){return true;}//long
	if(isMoveByLGTTemplateVertical(pointsAI)){return true;}//long
	if(isMoveBySQTTemplate(pointsAI)){return true;}//square
	
	return false;
}

public boolean isAImove(PointsAI pointsAI){
	if(pointsAI.game.isCanMakeMove(moveAIx, moveAIy)){
		return true;
	}else return false;			
}

public boolean isAImove(PointsAI pointsAI,int moveAIx,int moveAIy){
	this.moveAIx=moveAIx;
	this.moveAIy=moveAIy;
	if(pointsAI.game.isCanMakeMove(moveAIx, moveAIy)){
		return true;
	}else return false;			
}

boolean isMoveByBSTTemplate(PointsAI pointsAI){
	String content=TemplateType.BLUE_SURROUND.getContent(pointsAI.game, pointsAI.game.getLastX(), pointsAI.game.getLastY(),false);
	if(bss.isFoundArea(content,pointsAI)){
		pointsAI.base.foundedNumber=bss.foundedNumber;
		moveAIx=bss.getMoveAI().x+pointsAI.game.getLastX()-4;
		moveAIy=bss.getMoveAI().y+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySQTTemplate(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SQUARE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x+pointsAI.game.getLastX()-4;
		moveAIy=pointsAI.base.getMoveAI().y+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLGTTemplateHorizontal(PointsAI pointsAI){
	if(isFoundArea(TemplateType.LONG,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x+pointsAI.game.getLastX()-6;
		moveAIy=pointsAI.base.getMoveAI().y+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLGTTemplateVertical(PointsAI pointsAI){
	if(isFoundArea(TemplateType.LONG,pointsAI,true)){
		moveAIx=pointsAI.base.getMoveAI().y+pointsAI.game.getLastX()-4;
		moveAIy=pointsAI.base.getMoveAI().x+pointsAI.game.getLastY()-6;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySCTTemplateLT(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SQUARE_CORNER,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x;
		moveAIy=pointsAI.base.getMoveAI().y;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySCTTemplateRT(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SQUARE_CORNER,pointsAI,false)){
		moveAIx=32-pointsAI.base.getMoveAI().x;
		moveAIy=pointsAI.base.getMoveAI().y;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySCTTemplateLB(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SQUARE_CORNER,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x;
		moveAIy=25-pointsAI.base.getMoveAI().y;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySCTTemplateRB(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SQUARE_CORNER,pointsAI,false)){
		//System.out.println(pointsAI.base.getMoveAI().x+";"+pointsAI.base.getMoveAI().y);
		moveAIx=32-pointsAI.base.getMoveAI().x;
		moveAIy=25-pointsAI.base.getMoveAI().y;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySSTTemplateL(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SQUARE_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x;
		moveAIy=pointsAI.game.getLastY()-4+pointsAI.base.getMoveAI().y;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySSTTemplateR(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SQUARE_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x+32;
		moveAIy=pointsAI.game.getLastY()-4+pointsAI.base.getMoveAI().y;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySSTTemplateT(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SQUARE_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x+pointsAI.game.getLastX()-4;
		moveAIy=pointsAI.base.getMoveAI().y;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySSTTemplateB(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SQUARE_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x+pointsAI.game.getLastX()-4;
		moveAIy=pointsAI.base.getMoveAI().y+25;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLSTTemplateRight(PointsAI pointsAI){
	if(isFoundArea(TemplateType.LONG_SIDE,pointsAI,false)){
		moveAIx=31+pointsAI.base.getMoveAI().y;
		moveAIy=pointsAI.base.getMoveAI().x+pointsAI.game.getLastY()-6;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLSTTemplateLeft(PointsAI pointsAI){
	if(isFoundArea(TemplateType.LONG_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().y;
		moveAIy=pointsAI.base.getMoveAI().x+pointsAI.game.getLastY()-6;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLSTTemplateBottom(PointsAI pointsAI){
	if(isFoundArea(TemplateType.LONG_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x+pointsAI.game.getLastX()-6;
		moveAIy=25+pointsAI.base.getMoveAI().y;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLSTTemplateTop(PointsAI pointsAI){
	if(isFoundArea(TemplateType.LONG_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x+pointsAI.game.getLastX()-6;
		moveAIy=pointsAI.base.getMoveAI().y;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySHTTemplateRight(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SHORT_SIDE,pointsAI,false)){//System.out.println("+");	
		moveAIx=28+pointsAI.base.getMoveAI().x;
		moveAIy=pointsAI.base.getMoveAI().y+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySHTTemplateLeft(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SHORT_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().x;
		moveAIy=pointsAI.base.getMoveAI().y+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySHTTemplateTop(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SHORT_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().y+pointsAI.game.getLastX()-4;
		moveAIy=pointsAI.base.getMoveAI().x;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySHTTemplateBottom(PointsAI pointsAI){
	if(isFoundArea(TemplateType.SHORT_SIDE,pointsAI,false)){
		moveAIx=pointsAI.base.getMoveAI().y+pointsAI.game.getLastX()-4;
		moveAIy=21+pointsAI.base.getMoveAI().x;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

public boolean isFoundArea(TemplateType type,PointsAI pointsAI,boolean isVertical){
	String content=type.getContent(pointsAI.game, pointsAI.game.getLastX(), pointsAI.game.getLastY(),isVertical);
	if(pointsAI.base.isFoundArea(content,type,pointsAI))return true;
	return false;
}

}
