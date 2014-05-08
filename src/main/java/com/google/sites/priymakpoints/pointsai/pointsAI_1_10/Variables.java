package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

import java.util.Random;

public interface Variables{

	public int squareSize=16;
	public int offsetX=1;
	public int offsetY=3;
	public int pointSize=(int)(squareSize/2);
	public int sizeX=39,sizeY=32;
	
	public int offsetX_TE=4;
	public int offsetY_TE=1;
	public int sizeX_TE=13;
	public int sizeY_TE=9;
	
	public String templateFileName="template.txt";
	public String makrosFileName="makros.txt";
	
	Random rand=new Random();
		
}
