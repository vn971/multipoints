package com.google.sites.priymakpoints.pointsai.p_PointsAI;

import java.applet.Applet;
import java.applet.AudioClip;

import ru.narod.vn91.pointsop.sounds.Sounds;

public class Variables {
	
	public AudioClip sound=Applet.newAudioClip(Sounds.class.getResource("makeMove.wav"));
	
	public int squareSize=16;//������� ������ ���� � ��������
	public int offsetX=1,offsetY=3,point_size=(int)(squareSize/2);//������� (����������� - ������ �������� ����) � ������ �����
	public int f_s_x=39,f_s_y=32;//������ ���� ������ (f_s x f_s)
	public boolean isDrawStringEquivalent=false;//�������� ����� ��� �� ��������� ����������
	
	String appName="PointsAI ";//�������� ���������
	String appVersion="версия 1.056 ";
	String appDate="(09.05.2011)";
	String site="http://sites.google.com/site/priymakpoints/intelligence";
		
	String text="<html><font size=5>"+appName+
		"<br><font size=2 color=gray>(Points Artificial Intelligence)<br>" +
		"<font size=3 color=black>"+appVersion+"урезанная "+appDate+"<br>автор Алексей Приймак";
	
}
