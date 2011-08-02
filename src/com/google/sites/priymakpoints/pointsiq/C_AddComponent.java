/*���� ����� ��������� � ��������� �������� ����������, ����� �� ����� ����� ���� ����������
 * � ���� JFrame
 *
 * ����������� ������ ��������� � �������� ��������� ��������, � �������
 * ����� ���������� ����������� �������� ����������
 *
 *
 *� ���������� ������ ���������� ��������� ���
 *
 *C_AddComponent add=new C_AddComponent(frame.getContentPane());
 *
 *������� ���������:
 *	JButton butAdd=add.buttonSpecial("butAdd",10,765,actionAdd);
	JButton butDelete=add.buttonSpecial("butDelete",120,765,actionDelete);
	JButton butClose=add.buttonSpecial("butClose",500,765,null);
	List listAvtors=(List)add.component("list",10,40,200,410,"",itemListenerListAvtors_1);
	List listSongs=(List)add.component("list",10,460,200,300,"",itemListenerListSongs_1);
	JLabel labSong=(JLabel)add.component("label",220,5,370,30,"",null);
	JTextArea textArea=(JTextArea)add.componentInScroll("textArea",220,40,370,720,"",null);
 *
 */

package com.google.sites.priymakpoints.pointsiq;

import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.util.EventListener;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.CaretListener;

public class C_AddComponent {//�����

	Container con;//���������
	JOptionPane msg=new JOptionPane();//������� ����� ���������

public C_AddComponent(Container con){//�����������
		this.con=con;//���������� ���������
	}

//������� ����������� ������

public Component component(String strCom,int x,int y,int width,int height,
		String strText,EventListener listener){

	//�������� �������
	Component com=null;

	List list = null;//�������� ������
	JLabel label=null;//�������� �����
	JButton button=null;//�������� ������
	JTextField field=null;//�������� ��������� ����
	JFileChooser fc=null;//�������� ���� ������ �����
	Checkbox chk=null;//�������� �����������

	if(strCom.equalsIgnoreCase("checkbox")){//���� ���������� �����������
		chk=new Checkbox();chk.addItemListener((ItemListener)listener);chk.setLabel(strText);
		com=chk;com.setBounds(x, y, width, height);//�������� � ���������� ������� ��������
		}
	if(strCom.equalsIgnoreCase("list")){//���� ��������� ������
		list=new List();list.addItemListener((ItemListener)listener);com=list;
		com.setBounds(x, y, width, height);//�������� � ���������� ������� ��������
		}
	//���� ���������� �����
	if(strCom.equalsIgnoreCase("label")){label=new JLabel();label.setText(strText);com=label;
		com.setBounds(x, y, width, height);//�������� � ���������� ������� ��������
		}
	//���� ���������� ������
	if(strCom.equalsIgnoreCase("button")){button=new JButton();button.setText(strText);
		button.setCursor(new Cursor(12));button.addActionListener((ActionListener)listener);
		com=button;com.setBounds(x, y, width, height);//�������� � ���������� ������� ��������
		}
	//���� ���������� ��������� ����
	if(strCom.equalsIgnoreCase("field")){field=new JTextField();field.setText(strText);
		field.addCaretListener((CaretListener)listener);com=field;
		com.setBounds(x, y, width, height);//�������� � ���������� ������� ��������
		}
	//���� ���������� ���� ������ �����
	if(strCom.equalsIgnoreCase("fileChooser")){fc=new JFileChooser();
		fc.addActionListener((ActionListener)listener);com=fc;
		}

	//���� ����� strCom �� ������������, ������� �������� �� ������
	if(com==null){msg.showMessageDialog(con,"<HTML><FONT size=5 color=red>" +
			"������ �������� ���������� "+strCom+"!<BR>����� �� ���������������" +
					"<BR>�������� ������� ����������");return null;}//������ �� �������

	con.add(com);//com.setBounds(x, y, width, height);//�������� � ���������� ������� ��������
	com.requestFocus();//���������� �����, ����� ������� ��������� �� �����, � �� ���������

	return com;//������� �������
}

}
