/*���� ����� ������������ ��������� �������� ��� ������������ ���� JFrame.
 * ����������� ������ ��������� ��������� ������� ���� � ���� ������ � ������ ����:
 *
 * 		int width,int height
 *
 *��� ��������� �������� ���� ���������� � ���������� ������ ��������� ���:
 *
 *		frame.getContentPane().setLayout(new p_GUI.C_Layout(width,height));
 */

package com.google.sites.priymakpoints.pointsiq;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class C_Layout implements LayoutManager {//�����

	int width;//������ ����
	int height;//������ ����

public C_Layout(int width,int height){//����������� ������
		this.width=width;//���������� ������, ������ �������� ��������
		this.height=height;//���������� ������, ������ �������� ��������
		}

public void addLayoutComponent(String name, Component comp) {  }
public void removeLayoutComponent(Component comp) { }
public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(width, height);        }
public Dimension minimumLayoutSize(Container parent) {
    	return new Dimension(width, height);        }
public void layoutContainer(Container parent) {  }
}

