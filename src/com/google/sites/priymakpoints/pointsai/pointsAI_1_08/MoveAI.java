package com.google.sites.priymakpoints.pointsai.pointsAI_1_08;

import java.awt.Point;

public class MoveAI implements Variables{
	
	String strLastMove="";
	int moveAIx=99,moveAIy=99;
	public MoveByWall moveByWall=new MoveByWall();
	BlueSurroundSecurity bss=new BlueSurroundSecurity();
	RedSurroundSecurity rss=new RedSurroundSecurity();
	
public MoveAI(){

}
		
void deleteStatistics(PointsAI pointsAI){
	moveByWall.deleteStatistics(pointsAI);
	bss=new BlueSurroundSecurity();rss=new RedSurroundSecurity();
}	


	
Point getAImove(PointsAI pointsAI){//��������� �������� ������������ ���� ��
	
	if(!isMoveByAI(pointsAI)){
		while(!pointsAI.game.isCanMakeMove(moveAIx,moveAIy)){
			moveAIx=Math.abs(rand.nextInt(pointsAI.game.sizeX))+1;
			moveAIy=Math.abs(rand.nextInt(pointsAI.game.sizeY))+1;
		}
		strLastMove="random";
	}
	
	moveByWall.paintFrame(pointsAI);
		
	
return new Point(moveAIx, moveAIy);
}

boolean isMoveByAI(PointsAI pointsAI){
	

	
	if(isMoveByTemplate(pointsAI)){moveByWall.wallCorrection(moveAIx, moveAIy);return true;}
	if(isMoveByGlobal(pointsAI)){strLastMove="global";return true;}
	if(moveByWall.isMoveByWall(pointsAI)){strLastMove="wall";return true;}
	if(moveByWall.isCanGroung)if(isMoveByGround(pointsAI)){strLastMove="ground";return true;}
	
	return false;
}

boolean isMoveByGlobal(PointsAI pointsAI){
	if(isFoundArea(TemplateType.GLOBAL,pointsAI,false)){
		
		moveAIx=(pointsAI.base.getMoveAI().x+1)*3;
		moveAIy=(int)((pointsAI.base.getMoveAI().y+1)*3.5f);//���������� ��� ��
		System.out.println(moveAIx+";"+moveAIy);
		String[][] fieldState=pointsAI.game.getFieldState();
		
		try{for(int i=moveAIx-1;i<moveAIx+2;i++)for(int j=moveAIy-2;j<moveAIy+2;j++)
			if(fieldState[i][j].equals("R")){
				moveAIx=i+1;moveAIy=j+1;//System.out.println(moveAIx+";"+moveAIy);
				if(moveByWall.isMoveByWLTTemplate(pointsAI, moveAIx, moveAIy, 0)){
					if(isAImove(pointsAI))return true;
				}				
			}
		}catch(Exception e){}
		
		if(isAImove(pointsAI))return true;
		//System.out.println("equals red");
	}
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
			strLastMove="bs security";moveAIx=point.x; moveAIy=point.y;return true;
		}else{}
	}
	
	try{if(isMoveByBSTTemplate(pointsAI)){strLastMove="bst template";return true;}}catch(Exception e){}//move by blue surround
	
	try{if(isMoveByRSTTemplate(pointsAI)){strLastMove="rst template";return true;}}catch(Exception e){}//move by blue surround
	
	if(x<6&y<6){if(isMoveBySCTTemplateLT(pointsAI)){strLastMove="corner left top";return true;}}//move by corner
	if(x>33&y<6){if(isMoveBySCTTemplateRT(pointsAI)){strLastMove="corner right top";return true;}}//move by corner
	if(x<6&y>26){if(isMoveBySCTTemplateLB(pointsAI)){strLastMove="corner left bottom";return true;}}//move by corner
	if(x>33&y>26){if(isMoveBySCTTemplateRB(pointsAI)){strLastMove="corner right bottom";return true;}}//move by corner
	if(x>26&y>7&y<25){if(isMoveByLSTTemplateRight(pointsAI)){strLastMove="long side right";return true;}}//move by long side Right
	if(x<6&y>7&y<25){if(isMoveByLSTTemplateLeft(pointsAI)){strLastMove="long side left";return true;}}//move by long side Left
	if(y>26&x>7&x<32){if(isMoveByLSTTemplateBottom(pointsAI)){strLastMove="long side bottom";return true;}}//move by long side bottom
	if(y<6&x>7&x<32){if(isMoveByLSTTemplateTop(pointsAI)){strLastMove="long side top";return true;}}//move by long side top
	if(x>27&y>5&y<27){if(isMoveBySHTTemplateRight(pointsAI)){strLastMove="short side right";return true;}}//move by short side
	if(x<11&y>5&y<27){if(isMoveBySHTTemplateLeft(pointsAI)){strLastMove="short side left";return true;}}//move by short side
	if(y<11&x>5&x<34){if(isMoveBySHTTemplateTop(pointsAI)){strLastMove="short side top";return true;}}//move by short side
	if(y>22&x>5&x<34){if(isMoveBySHTTemplateBottom(pointsAI)){strLastMove="short side bottom";return true;}}//move by short side
	if(x<6&y>5&y<27){if(isMoveBySSTTemplateL(pointsAI)){strLastMove="square side left";return true;}}//move by side	
	if(x>33&y>5&y<27){if(isMoveBySSTTemplateR(pointsAI)){strLastMove="square side right";return true;}}//move by side
	if(y<6&x>5&x<34){if(isMoveBySSTTemplateT(pointsAI)){strLastMove="square side top";return true;}}//move by side	
	if(y>26&x>5&x<34){if(isMoveBySSTTemplateB(pointsAI)){strLastMove="square side bottom";return true;}}//move by side
	if(isMoveByLGTTemplateHorizontal(pointsAI)){strLastMove="long Horizontal";return true;}//long
	if(isMoveByLGTTemplateVertical(pointsAI)){strLastMove="long Vertical";return true;}//long
	if(isMoveBySQTTemplate(pointsAI)){strLastMove="square";return true;}//square
	
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

boolean isMoveByRSTTemplate(PointsAI pointsAI){
	String content=TemplateType.RED_SURROUND.getContent(pointsAI.game, pointsAI.game.getLastX(), pointsAI.game.getLastY(),false);
	if(rss.isFoundArea(content,pointsAI)){
		pointsAI.base.foundedNumber=rss.foundedNumber;
		moveAIx=rss.getMoveAI().x+pointsAI.game.getLastX()-4;
		moveAIy=rss.getMoveAI().y+pointsAI.game.getLastY()-4;//���������� ��� ��
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
		
		//System.out.println
		
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
