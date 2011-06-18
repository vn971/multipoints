package com.google.sites.priymakpoints.pointsai.pointsAI_1_08;

public class Wall {

int x,y,length;
private int xCenter=19;int yCenter=15;//����� ����
private boolean isAtSide=false;//������ ����� ���� ����, �� ����� �� ����������

Wall(int xBegin,int yBegin){
	x=xBegin;
	y=yBegin;
	length=Math.abs(x-xCenter)+Math.abs(y-yCenter);
}	

int getLength(){length=Math.abs(x-xCenter)+Math.abs(y-yCenter);return length;}

int getLengthFromLastBlue(int xB,int yB){length=Math.abs(x-xB)+Math.abs(y-yB);return length;}

boolean isAtSide(){if(x<4|y<4|x>36|y>29)isAtSide=true;return isAtSide;}

}
