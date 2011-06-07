package com.google.sites.priymakpoints.pointsai.pointsAI_1_07;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Font;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public interface Variables{
	
	public Font f20=new Font("tahoma",20,20);
	public Font f12=new Font("tahoma",12,12);
	public Font f10=new Font("tahoma",10,10);
	
	public int squareSize=16;//������� ������ ���� � ��������
	public int offsetX=1;
	public int offsetY=3;
	public int pointSize=(int)(squareSize/2);//������� (����������� - ������ �������� ����) � ������ �����
	public int sizeX=39,sizeY=32;//������ ���� ������ (X x Y)
	
	public int offsetX_TE=4;
	public int offsetY_TE=1;//������� (����������� - ������ �������� ����)
	public int sizeX_TE=13;
	public int sizeY_TE=9;//������ ���� ������ (X x Y) max=13x9
	
	public String appName="PointsAI ";//�������� ���������
	public String appVersion="������ 1.071 ";
	public String appDate="(10.06.2011)";
	public String appSite="http://sites.google.com/site/priymakpoints/intelligence";
	public String appText="<html><font size=3>��������� ��� ���� � ����� ������ ��<br><br><font size=5>"+
		appName+"<font size=3>(Points Artificial Intelligence)<br><br>" +
		appVersion+appDate+"<br><br>����� ������� �������";

	public String templateFileName="PointsTEsaves//template.txt";
	public String templateUrlName="http://sites.google.com/site/priymakpoints/templates/template.txt";
	public String templateUrlInfo="http://sites.google.com/site/priymakpoints/templates/templateinfo.txt";
	
	Random rand=new Random();
	
	public boolean isDrawStringEquivalent=false;//�������� ����� ��� �� ��������� ����������
	
	String spc="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
}
