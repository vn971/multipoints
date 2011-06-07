package com.google.sites.priymakpoints.pointsai.pointsAI_1_07;

import java.awt.Point;

public class TemplateEngine implements Variables{
	
public Template[] base;//���� ���������
private TemplateIO io=new TemplateIO();
private Point moveAI;
public int foundedNumber=0;
int maxIndex=0;
public int SQUARE_SIDE=0,SQUARE_CORNER=0,SQUARE=0,SHORT_SIDE=0,LONG=0,LONG_SIDE=0,WALL=0,BLUE_SURROUND=0;

public TemplateEngine(boolean isOffline){//��������� ���� �� ������
	SQUARE_SIDE=0;SQUARE_CORNER=0;SQUARE=0;SHORT_SIDE=0;LONG=0;LONG_SIDE=0;WALL=0;
	base=io.getBase(isOffline);
	for(int i=base.length-1;i>=0;i--)if(base[i].getTemplateIndex()>maxIndex)maxIndex=base[i].getTemplateIndex();
	
	for(int i=base.length-1;i>=0;i--){
		if(base[i].getTemplateType()==TemplateType.SQUARE_SIDE)SQUARE_SIDE++;
		if(base[i].getTemplateType()==TemplateType.SQUARE_CORNER)SQUARE_CORNER++;
		if(base[i].getTemplateType()==TemplateType.SQUARE)SQUARE++;
		if(base[i].getTemplateType()==TemplateType.SHORT_SIDE)SHORT_SIDE++;
		if(base[i].getTemplateType()==TemplateType.LONG)LONG++;
		if(base[i].getTemplateType()==TemplateType.LONG_SIDE)LONG_SIDE++;
		if(base[i].getTemplateType()==TemplateType.WALL)WALL++;
		if(base[i].getTemplateType()==TemplateType.BLUE_SURROUND)BLUE_SURROUND++;
	}
	
}

public boolean isFoundArea(String content,TemplateType type,PointsAI pointsAI){
	for(int i=base.length-1;i>=0;i--){
		if(base[i].isEqualsLikeArea(content,type)){
			foundedNumber=i;moveAI=base[i].getMoveAI();return true;
		}
	}
	foundedNumber=0;
	return false;
}

public int getBaseSize(){return base.length;}//������ ����
public int getFoundedNumber(){return foundedNumber;}
public Template getTemplate(int index){return base[index];}
public Point getMoveAI(){return moveAI;}//��� ��

}
