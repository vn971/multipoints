package com.google.sites.priymakpoints.pointsai.p_PointsAI;

import java.awt.Point;
import java.util.Random;
import javax.swing.JFrame;

import com.google.sites.priymakpoints.pointsai.p_Walls.MoveByWall;

public class MoveAI extends com.google.sites.priymakpoints.pointsai.p_TemplateEngine.Variables{
	
	int countCorner=0,countSquareSide=0,countSquare=0,countLongSide=0,countShortSide=0,countRandom=0,
		countWall=0,countLong=0,countGround=0;
	String strLastMove="";
	int moveAIx=99,moveAIy=99;
	JFrame frame=new JFrame();
	public MoveByWall moveByWall=new MoveByWall();
	private Random rand=new Random();
			
Point getAImove(PointsAI pointsAI){//��������� �������� ������������ ���� ��
	if(!isMoveByAI(pointsAI)){
		while(!pointsAI.game.isCanMakeMove(moveAIx,moveAIy)){
			moveAIx=Math.abs(rand.nextInt(pointsAI.game.f_s_x))+1;
			moveAIy=Math.abs(rand.nextInt(pointsAI.game.f_s_y))+1;
		}
		countRandom++;
		strLastMove="random";
	}
return new Point(moveAIx, moveAIy);
}

void deleteStatistics(PointsAI pointsAI){moveByWall.deleteStatistics(pointsAI);}	

boolean isMoveByAI(PointsAI pointsAI){
	if(isMoveByTemplate(pointsAI)){moveByWall.wallCorrection(moveAIx, moveAIy);return true;}
	if(moveByWall.isMoveByWall(pointsAI)){countWall++;strLastMove="wall";return true;}
	if(moveByWall.isCanGroung)if(isMoveByGround(pointsAI)){countGround++;strLastMove="ground";return true;}
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
	
	if(x<6&y<6){if(isMoveBySCTTemplateLT(pointsAI)){countCorner++;strLastMove="corner left top";return true;}}//move by corner
	if(x>33&y<6){if(isMoveBySCTTemplateRT(pointsAI)){countCorner++;strLastMove="corner right top";return true;}}//move by corner
	if(x<6&y>26){if(isMoveBySCTTemplateLB(pointsAI)){countCorner++;strLastMove="corner left bottom";return true;}}//move by corner
	if(x>33&y>26){if(isMoveBySCTTemplateRB(pointsAI)){countCorner++;strLastMove="corner right bottom";return true;}}//move by corner
	if(x>26&y>7&y<25){if(isMoveByLSTTemplateRight(pointsAI)){countLongSide++;strLastMove="long side right";return true;}}//move by long side Right
	if(x<6&y>7&y<25){if(isMoveByLSTTemplateLeft(pointsAI)){countLongSide++;strLastMove="long side left";return true;}}//move by long side Left
	if(y>26&x>7&x<32){if(isMoveByLSTTemplateBottom(pointsAI)){countLongSide++;strLastMove="long side bottom";return true;}}//move by long side bottom
	if(y<6&x>7&x<32){if(isMoveByLSTTemplateTop(pointsAI)){countLongSide++;strLastMove="long side top";return true;}}//move by long side top
	if(x>27&y>5&y<27){if(isMoveBySHTTemplateRight(pointsAI)){countShortSide++;strLastMove="short side right";return true;}}//move by short side
	if(x<11&y>5&y<27){if(isMoveBySHTTemplateLeft(pointsAI)){countShortSide++;strLastMove="short side left";return true;}}//move by short side
	if(y<11&x>5&x<34){if(isMoveBySHTTemplateTop(pointsAI)){countShortSide++;strLastMove="short side top";return true;}}//move by short side
	if(y>22&x>5&x<34){if(isMoveBySHTTemplateBottom(pointsAI)){countShortSide++;strLastMove="short side bottom";return true;}}//move by short side
	if((x>33&y>5&y<27)|(x<6&y>5&y<27)|(y>26&x>5&x<34)|(y<6&x>5&x<34)){
		if(isMoveBySSTTemplate(pointsAI)){countSquareSide++;strLastMove="square side";return true;}}//move by side	
	if(isMoveByLGTTemplateHorizontal(pointsAI)){countLong++;strLastMove="long Horizontal";return true;}//long
	if(isMoveByLGTTemplateVertical(pointsAI)){countLong++;strLastMove="long Vertical";return true;}//long
	if(isMoveBySQTTemplate(pointsAI)){countSquare++;strLastMove="square";return true;}//square
	
	return false;
}

public boolean isAImove(PointsAI pointsAI){
	if(pointsAI.game.isCanMakeMove(moveAIx, moveAIy)){return true;}else return false;			
}

public boolean isAImove(PointsAI pointsAI,int moveAIx,int moveAIy){
	this.moveAIx=moveAIx;
	this.moveAIy=moveAIy;
	if(pointsAI.game.isCanMakeMove(moveAIx, moveAIy)){return true;}else return false;}

boolean isMoveBySQTTemplate(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSQTContent(),TemplateType.SQUARE)){
		moveAIx=pointsAI.base.getMoveAIx()+pointsAI.game.getLastX()-4;
		moveAIy=pointsAI.base.getMoveAIy()+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLGTTemplateHorizontal(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getLGTContentHorizontal(),TemplateType.LONG)){
		moveAIx=pointsAI.base.getMoveAIx()+pointsAI.game.getLastX()-6;
		moveAIy=pointsAI.base.getMoveAIy()+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLGTTemplateVertical(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getLGTContentVertical(),TemplateType.LONG)){
		moveAIx=pointsAI.base.getMoveAIy()+pointsAI.game.getLastX()-4;
		moveAIy=pointsAI.base.getMoveAIx()+pointsAI.game.getLastY()-6;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySCTTemplateLT(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSCTContent(),TemplateType.SQUARE_CORNER)){
		moveAIx=pointsAI.base.getMoveAIx();
		moveAIy=pointsAI.base.getMoveAIy();//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySCTTemplateRT(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSCTContent(),TemplateType.SQUARE_CORNER)){
		moveAIx=32-pointsAI.base.getMoveAIx();
		moveAIy=pointsAI.base.getMoveAIy();//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySCTTemplateLB(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSCTContent(),TemplateType.SQUARE_CORNER)){
		moveAIx=pointsAI.base.getMoveAIx();
		moveAIy=25-pointsAI.base.getMoveAIy();//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySCTTemplateRB(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSCTContent(),TemplateType.SQUARE_CORNER)){
		System.out.println(pointsAI.base.getMoveAIx()+";"+pointsAI.base.getMoveAIy());
		moveAIx=32-pointsAI.base.getMoveAIx();
		moveAIy=25-pointsAI.base.getMoveAIy();//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySSTTemplate(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSSTContent(),TemplateType.SQUARE_SIDE)){
		moveAIx=pointsAI.base.getMoveAIx()+pointsAI.game.getLastX()-4;
		moveAIy=pointsAI.base.getMoveAIy()+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLSTTemplateRight(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getLSTContent(),TemplateType.LONG_SIDE)){
		moveAIx=31+pointsAI.base.getMoveAIy();
		moveAIy=pointsAI.base.getMoveAIx()+pointsAI.game.getLastY()-6;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLSTTemplateLeft(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getLSTContent(),TemplateType.LONG_SIDE)){
		moveAIx=pointsAI.base.getMoveAIy();
		moveAIy=pointsAI.base.getMoveAIx()+pointsAI.game.getLastY()-6;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLSTTemplateBottom(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getLSTContent(),TemplateType.LONG_SIDE)){
		moveAIx=pointsAI.base.getMoveAIx()+pointsAI.game.getLastX()-6;
		moveAIy=25+pointsAI.base.getMoveAIy();//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveByLSTTemplateTop(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getLSTContent(),TemplateType.LONG_SIDE)){
		moveAIx=pointsAI.base.getMoveAIx()+pointsAI.game.getLastX()-6;
		moveAIy=pointsAI.base.getMoveAIy();//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySHTTemplateRight(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSHTContent(),TemplateType.SHORT_SIDE)){//System.out.println("+");	
		moveAIx=28+pointsAI.base.getMoveAIx();
		moveAIy=pointsAI.base.getMoveAIy()+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySHTTemplateLeft(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSHTContent(),TemplateType.SHORT_SIDE)){
		moveAIx=pointsAI.base.getMoveAIx();
		moveAIy=pointsAI.base.getMoveAIy()+pointsAI.game.getLastY()-4;//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySHTTemplateTop(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSHTContent(),TemplateType.SHORT_SIDE)){
		moveAIx=pointsAI.base.getMoveAIy()+pointsAI.game.getLastX()-4;
		moveAIy=pointsAI.base.getMoveAIx();//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

boolean isMoveBySHTTemplateBottom(PointsAI pointsAI){
	if(pointsAI.base.isFoundArea(pointsAI.game.getSHTContent(),TemplateType.SHORT_SIDE)){
		moveAIx=pointsAI.base.getMoveAIy()+pointsAI.game.getLastX()-4;
		moveAIy=21+pointsAI.base.getMoveAIx();//���������� ��� ��
		if(isAImove(pointsAI))return true;
	}
	return false;
}

}
