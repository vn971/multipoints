package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

public class MakrosEngine implements Variables{
	
public Makros[] base;
private MakrosIO io=new MakrosIO();

public MakrosEngine(){base=io.getBase();}

public Makros getMakros(int index){
	for(int i=0;i<base.length;i++)if(base[i].getMakrosIndex()==index)return base[i];
	return null;
}

}
