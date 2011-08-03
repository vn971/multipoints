package com.google.sites.priymakpoints.pointsiq;

import java.awt.Color;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class C_JFrame {//�����

	//����������� � �����������
public C_JFrame(JFrame frame, String title, boolean resizable,int width,int height,Color background){
	frame.setTitle(title);//���������� ���������
	frame.setResizable(resizable);//���������� ����������� ��������� ������ ����
	//���������� ���������� � ������� � ������� ����
	frame.getContentPane().setLayout(new C_Layout(width,height));
	frame.getContentPane().setBackground(background);//���������� ���� ����
	frame.pack();//��� ������ ���� �� �����
	frame.show();//��� ������ ���� �� �����
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//��� ����������� �������� ����
	setCenterAlign(frame);
}

public C_JFrame(){}

public void resize(JFrame frame,int width,int height){frame.setSize(width,height);setCenterAlign(frame);}

public void setCenterAlign(JFrame frame){
	frame.move((int)(Toolkit.getDefaultToolkit().getScreenSize().width*0.5f-frame.getWidth()/2), 
			(int)(Toolkit.getDefaultToolkit().getScreenSize().height*0.5f-frame.getHeight()/2));
}

}