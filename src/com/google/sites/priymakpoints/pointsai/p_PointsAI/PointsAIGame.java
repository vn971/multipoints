package com.google.sites.priymakpoints.pointsai.p_PointsAI;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngine;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.DotType;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;

public class PointsAIGame extends Variables{

	private int moves;//����� ����� � ����
	int last_x;
	int last_y;
	private SingleGameEngine singleGameEngine;//������ �������� ����
	private boolean is_hod_human;//������������� ���� �������� � ��
	//private PointsAI pointsAI;
		
PointsAIGame(){newGame();}

public String getSQTContent(){
	String content="";
	String[][] fieldState=getFieldState();
	for(int j=0;j<9;j++){
		for(int i=0;i<9;i++){
			if((i+getLastX()-5<0)|(j+getLastY()-5<0)|(i+getLastX()-5>(f_s_x-1))|(j+getLastY()-5>(f_s_y-1)))content+="N";
			else content+=fieldState[i+getLastX()-5][j+getLastY()-5];}
		content+="OOOO";
	}
	return content;
}

public String getLGTContentHorizontal(){
	String content="";
	String[][] fieldState=getFieldState();
	for(int j=0;j<9;j++){
		for(int i=0;i<13;i++){
			if((i+getLastX()-7<0)|(j+getLastY()-5<0)|(i+getLastX()-7>(f_s_x-1))|(j+getLastY()-5>(f_s_y-1)))content+="N";
			else content+=fieldState[i+getLastX()-7][j+getLastY()-5];}
	}
	return content;
}

public String getLGTContentVertical(){
	String content="";
	String[][] fieldState=getFieldState();
	for(int i=0;i<9;i++){
		for(int j=0;j<13;j++){
			if((i+getLastX()-5<0)|(j+getLastY()-7<0)|(i+getLastX()-5>(f_s_x-1))|(j+getLastY()-7>(f_s_y-1)))content+="N";
			else content+=fieldState[i+getLastX()-5][j+getLastY()-7];}
	}
	//System.out.println(content);
	return content;
}

public String getWLTContent(int x,int y){
	String content="";
	String[][] fieldState=getFieldState();
	for(int j=0;j<9;j++){
		for(int i=0;i<9;i++){
			if((i+x-5<0)|(j+y-5<0)|(i+x-5>(f_s_x-1))|(j+y-5>(f_s_y-1)))content+="N";
			else content+=fieldState[i+x-5][j+y-5];}
		content+="OOOO";
	}
	return content;
}

public String getSCTContent(){try{
	String content="";
	String[][] fieldState=getFieldState();
	if(getLastX()<6&getLastY()<6){content+="LLLLLLLLLOOOO";for(int j=0;j<8;j++){content+="L";for(int i=0;i<8;i++)content+=fieldState[i][j];content+="OOOO";}}
	if(getLastX()<6&getLastY()>(f_s_y-6)){for(int j=(f_s_y-8);j<f_s_y;j++){content+="L";for(int i=0;i<8;i++)content+=fieldState[i][j];content+="OOOO";}content+="LLLLLLLLLOOOO";}
	if(getLastX()>(f_s_x-6)&getLastY()>(f_s_y-6)){for(int j=(f_s_y-8);j<f_s_y;j++){for(int i=(f_s_x-8);i<f_s_x;i++)content+=fieldState[i][j];content+="LOOOO";}content+="LLLLLLLLLOOOO";}
	if(getLastX()>(f_s_x-6)&getLastY()<6){content+="LLLLLLLLLOOOO";for(int j=0;j<8;j++){for(int i=(f_s_x-8);i<f_s_x;i++)content+=fieldState[i][j];content+="LOOOO";}}
	return content;
}catch(Exception e){return null;}}

public String getSSTContent(){try{
	String content="";
	String[][] fieldState=getFieldState();
	if(getLastX()>(f_s_x-6)&getLastY()>5&getLastY()<(f_s_y-5)){
		for(int j=getLastY()-5;j<getLastY()+4;j++){for(int i=31;i<39;i++)content+=fieldState[i][j];content+="LOOOO";}
	}
	if(getLastX()<6&getLastY()>5&getLastY()<(f_s_y-5)){
		for(int j=getLastY()-5;j<getLastY()+4;j++){content+="L";for(int i=0;i<8;i++)content+=fieldState[i][j];content+="OOOO";}
	}
	if(getLastY()>26&getLastX()>5&getLastX()<(f_s_x-5)){
		for(int j=(f_s_y-6);j<f_s_y;j++){for(int i=getLastX()-5;i<getLastX()+4;i++)content+=fieldState[i][j];content+="OOOO";}content+="LLLLLLLLLOOOO";
	}
	if(getLastY()<6&getLastX()>5&getLastX()<(f_s_x-5)){
		content+="LLLLLLLLLOOOO";for(int j=0;j<8;j++){for(int i=getLastX()-5;i<getLastX()+4;i++)content+=fieldState[i][j];content+="OOOO";}
	}
	return content;
}catch(Exception e){return null;}}

public String getLSTContent(){try{
	String content="";
	String[][] fieldState=getFieldState();
	if(getLastX()>(f_s_x-7)&getLastY()>7&getLastY()<(f_s_y-7)){
		for(int i=(f_s_x-8);i<f_s_x;i++){for(int j=getLastY()-7;j<getLastY()+6;j++)content+=fieldState[i][j];}content+="LLLLLLLLLLLLL";
	}
	if(getLastX()<6&getLastY()>7&getLastY()<(f_s_y-7)){
		content+="LLLLLLLLLLLLL";for(int i=0;i<8;i++){for(int j=getLastY()-7;j<getLastY()+6;j++)content+=fieldState[i][j];}
	}
	if(getLastY()>(f_s_y-6)&getLastX()>7&getLastX()<f_s_y){
		for(int j=(f_s_y-8);j<f_s_y;j++){for(int i=getLastX()-7;i<getLastX()+6;i++)content+=fieldState[i][j];}content+="LLLLLLLLLLLLL";
	}
	if(getLastY()<6&getLastX()>7&getLastX()<f_s_y){
		content+="LLLLLLLLLLLLL";for(int j=0;j<8;j++){for(int i=getLastX()-7;i<getLastX()+6;i++)content+=fieldState[i][j];}
	}
	return content;
}catch(Exception e){return null;}}

public String getSHTContent(){try{
	String content="";
	String[][] fieldState=getFieldState();
	if(getLastX()>(f_s_x-13)&getLastY()>5&getLastY()<(f_s_y-5)){
		for(int j=getLastY()-5;j<getLastY()+4;j++){for(int i=(f_s_x-12);i<f_s_x;i++)content+=fieldState[i][j];content+="L";}
	}
	if(getLastX()<11&getLastY()>5&getLastY()<(f_s_y-5)){
		for(int j=getLastY()-5;j<getLastY()+4;j++){content+="L";for(int i=0;i<12;i++)content+=fieldState[i][j];}
	}
	if(getLastY()>(f_s_y-10)&getLastX()>5&getLastX()<(f_s_x-5)){
		for(int i=getLastX()-5;i<getLastX()+4;i++){for(int j=(f_s_y-12);j<f_s_y;j++)content+=fieldState[i][j];content+="L";}
	}
	if(getLastY()<11&getLastX()>5&getLastX()<(f_s_x-5)){
		for(int i=getLastX()-5;i<getLastX()+4;i++){content+="L";for(int j=0;j<12;j++)content+=fieldState[i][j];}
	}
	return content;
}catch(Exception e){return null;}}

void newGame(){singleGameEngine=new SingleGameEngine(f_s_x,f_s_y);}	
	
public String[][] getFieldState(){
	String[][] fieldState=new String[39][32];
	for (int x = 1; x <= f_s_x; x++) 
		for (int y = 1; y <= f_s_y; y++) {
			DotType dotType = singleGameEngine.getDotType(x, y);
			String type="N";
			switch (dotType) {
				case EMPTY:	type="N";break;//empty
				case BLUE:	type="B";break;//blue
				case BLUE_CTRL: type="N";break;//blue домик
				case BLUE_EATED_EMPTY: type="B";break;
				case BLUE_EATED_RED: type="B";break;//my points
				case BLUE_TIRED: type="B";break;//null blue
				case RED: type="R";break;//red
				case RED_CTRL: type="N";break;//red домик
				case RED_EATED_BLUE: type="R";break;//
				case RED_EATED_EMPTY: type="R";break;
				case RED_TIRED: type="R";break;
			}
			fieldState[x-1][y-1]=type;// <get field state>
	}
	
	return fieldState;
	
}

void makeMove(int x, int y,MoveType moveType){
	singleGameEngine.makeMove(x,y,moveType);
	last_x=x;
	last_y=y;
	moves++;
	if(moveType==MoveType.BLUE)is_hod_human=false;
	if(moveType==MoveType.RED)is_hod_human=true;
	sound.play();
}

int getMovesCount(){return moves;};
public int getLastX(){return last_x;};
public int getLastY(){return last_y;};
public boolean isCanMakeMove(int x, int y){return singleGameEngine.canMakeMove(x,y);}
public boolean isHodHuman(){return is_hod_human;}
int getBluePlayerCount(){return singleGameEngine.getBlueScore();}
int getRedPlayerCount(){return singleGameEngine.getRedScore();} 

}
