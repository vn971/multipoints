package com.google.sites.priymakpoints.pointsai.pointsAI_1_08;

import java.util.Random;

public interface Variables{
	
	public int squareSize=16;//������� ������ ���� � ��������
	public int offsetX=1;
	public int offsetY=3;
	public int pointSize=(int)(squareSize/2);//������� (����������� - ������ �������� ����) � ������ �����
	public int sizeX=39,sizeY=32;//������ ���� ������ (X x Y)
		
	public String appName="PointsAI ";//�������� ���������
	public String appSite="http://sites.google.com/site/priymakpoints/intelligence";

	public String templateUrlName="http://sites.google.com/site/priymakpoints/templates/template.txt";
	
	Random rand=new Random();
	
}
