package com.google.sites.priymakpoints.pointsai.p_TemplateEngine;

public class TemplateEngine extends Variables{
	
public Template[] base;//���� ���������
private TemplateIO io=new TemplateIO();
private int moveAIx=99,moveAIy=99;
int foundedNumber=0;
int maxIndex=0;
public int SQUARE_SIDE=0,SQUARE_CORNER=0,SQUARE=0,AREA=0,SHORT_SIDE=0,LONG=0,LONG_SIDE=0,WALL=0;


public TemplateEngine(boolean isOffline){//��������� ���� �� ������
	SQUARE_SIDE=0;SQUARE_CORNER=0;SQUARE=0;AREA=0;SHORT_SIDE=0;LONG=0;LONG_SIDE=0;WALL=0;
	base=io.getBase(isOffline);
	for(int i=base.length-1;i>=0;i--)if(base[i].getTemplateIndex()>maxIndex)maxIndex=base[i].getTemplateIndex();
	
	for(int i=base.length-1;i>=0;i--){
		if(base[i].getTemplateType()==TemplateType.SQUARE_SIDE)SQUARE_SIDE++;
		if(base[i].getTemplateType()==TemplateType.SQUARE_CORNER)SQUARE_CORNER++;
		if(base[i].getTemplateType()==TemplateType.SQUARE)SQUARE++;
		if(base[i].getTemplateType()==TemplateType.AREA)AREA++;
		if(base[i].getTemplateType()==TemplateType.SHORT_SIDE)SHORT_SIDE++;
		if(base[i].getTemplateType()==TemplateType.LONG)LONG++;
		if(base[i].getTemplateType()==TemplateType.LONG_SIDE)LONG_SIDE++;
		if(base[i].getTemplateType()==TemplateType.WALL)WALL++;
	}
	
}

public boolean saveResultIfNotFound(String content,String type){
	for(int i=base.length-1;i>=0;i--){if(base[i].isEqualsWithoutTargets(content,type))return false;}
	io.addTemplate(content+(maxIndex+1)+type);
	base=io.getBase(true);
	return true;
}	

public boolean resaveResultIfNotFound(String content,int index,String type){
	for(int i=base.length-1;i>=0;i--){if(base[i].isEqualsWithoutTargets(content,type))return false;}
	base[index]=new Template(content+base[index].getTemplateIndex()+type);
	io.saveBase(base);return true;
}

public boolean isFoundArea(String content,TemplateType type){
	//content=content.replaceAll("0", "N");
	for(int i=base.length-1;i>=0;i--){
		if(base[i].isEqualsLikeArea(content,type,Dot.RED_NORMAL)){
			foundedNumber=i;moveAIx=base[i].getMoveAIx();moveAIy=base[i].getMoveAIy();return true;
		}
	}
	foundedNumber=0;
	return false;
}

public void sortBase(){
	Template[] baseSort=new Template[base.length];
	int i=0;
	for(int j=sizeX*sizeY;j>=0;j--)
		for(int k=0;k<base.length;k++)
			if(base[k].getDotsCount(Dot.ANY)==j){baseSort[i]=base[k];i++;}
	io.saveBase(baseSort);
	base=io.getBase(true);
}

public void saveBase(){io.saveBase(base);}
public void deleteResult(int index){base[index].setDelete(true);io.deleteResult(base);base=io.getBase(true);}
public String getOfflineBaseAsFile(){return io.getOfflineBaseAsFile();}
public void updateBase(){base=io.getBase(false);}
public int getBaseSize(){return base.length;}//������ ����
public String getUpdateInfo(){return io.getUpdateInfo();}//������ ����
public int getFoundedNumber(){return foundedNumber;}
public Template getTemplate(int index){return base[index];}
public int getMoveAIx(){return moveAIx;}//��� �� �� �
public int getMoveAIy(){return moveAIy;}//��� �� �� �

}
