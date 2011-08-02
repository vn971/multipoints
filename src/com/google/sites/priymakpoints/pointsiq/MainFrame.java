package com.google.sites.priymakpoints.pointsiq;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

public class MainFrame extends JFrame implements Runnable,MouseListener, WindowListener, MouseMotionListener,Variables{
	
	JLabel label=new JLabel();
	protected JLabel labelCoordinates=new JLabel();
	public Thread t=new Thread(this);
	
public void setLabelText(String text){label.setText(text);}	
	
public MainFrame(){
	
	this.setCursor(Cursor.HAND_CURSOR);
	new C_JFrame(this,appName,false,350,530,new Color(255,255,255));
	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	
	this.addMouseListener(this);
	this.addWindowListener(this);
	this.addMouseMotionListener(this);
	
	JMenuBar menu=new JMenuBar();
	menu.add(label);
	menu.add(labelCoordinates);
	this.setJMenuBar(menu);
		
	}

public void run(){}
public void mousePressed(MouseEvent me) {}
public void windowActivated(WindowEvent e) {}
public void windowClosed(WindowEvent e) {}
public void windowClosing(WindowEvent e) {}
public void windowDeactivated(WindowEvent e) {}
public void windowDeiconified(WindowEvent e) {}
public void windowIconified(WindowEvent e) {}
public void windowOpened(WindowEvent e) {}
public void mouseClicked(MouseEvent me) {}
public void mouseEntered(MouseEvent me) {}
public void mouseExited(MouseEvent me) {}
public void mouseReleased(MouseEvent me) {}
public void mouseDragged(MouseEvent arg0) {}
public void mouseMoved(MouseEvent me) {}

}
