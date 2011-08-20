package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.awt.Point;

public class TemplateEngine implements Variables{
	
public Template[] base;
private TemplateIO io=new TemplateIO();
private Point moveAI;
public int foundedNumber=0;
int maxIndex=0;
MakrosEngine makrosEngine;

public TemplateEngine(boolean isOffline){
	base=io.getBase(isOffline);
	makrosEngine=new MakrosEngine();
	for(int i=base.length-1;i>=0;i--)if(base[i].getTemplateIndex()>maxIndex)maxIndex=base[i].getTemplateIndex();
}

public boolean isFoundArea(String content,TemplateType type){
	for(int i=base.length-1;i>=0;i--){
		if(base[i].isEqualsLikeArea(content,type)){
			foundedNumber=i;
			moveAI=base[i].getMoveAI();return true;
		}
	}
	foundedNumber=0;
	return false;
}

public Makros getMakros(int index){return makrosEngine.getMakros(index);}
public int getBaseSize(){return base.length;}
public int getFoundedNumber(){return foundedNumber;}
public Template getTemplate(int index){return base[index];}
public Point getMoveAI(){return moveAI;}

}
