package ru.narod.vn91.pointsai.p_PointsAI;

import java.applet.Applet;
import java.applet.AudioClip;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Variables {
	
	public AudioClip sound=Applet.newAudioClip(Variables.class.getResource("move.wav"));
	
	public int squareSize=16;//������� ������ ���� � ��������
	public int offsetX=1,offsetY=3,point_size=(int)(squareSize/2);//������� (����������� - ������ �������� ����) � ������ �����
	public int f_s_x=39,f_s_y=32;//������ ���� ������ (f_s x f_s)
	public boolean isDrawStringEquivalent=false;//�������� ����� ��� �� ��������� ����������
	
	String appName="PointsAI ";//�������� ���������
	String appVersion="версия 1.056 ";
	String appDate="(09.05.2011)";
	String site="http://sites.google.com/site/priymakpoints/intelligence";
	
	Random rand=new Random();
	
	public boolean isOfficialRelease=false;

	boolean isFirstAIstep=true;
	
	JLabel fonImage=new JLabel(new ImageIcon(Variables.class.getResource("fon.png")));
	
	String spc="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
	String text="<html><font size=5>"+appName+
		"<br><font size=2 color=gray>(Points Artificial Intelligence)<br><br>" +
		"<font size=3 color=black>"+appVersion+"урезанная "+appDate;
	
}
