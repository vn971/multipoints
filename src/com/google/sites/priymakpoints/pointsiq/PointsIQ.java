package com.google.sites.priymakpoints.pointsiq;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.EventListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class PointsIQ extends JFrame implements Runnable,MouseListener, WindowListener, MouseMotionListener{

	private PointsIQGame game;
	private int preX=0,preY=0;
	private JButton butText,butNext,butSGF;
	private Question[] base;
	private Question question;
	private int curQuestion=0,curLevel=0;private String preLevels="";private Point AIAnswer=null;
	private int squareSize=16;
	private QuestionIO io=new QuestionIO();
	JLabel labelCoordinates;
	public Thread t=new Thread(this);
	{		
		labelCoordinates=new JLabel();labelCoordinates.setBounds(290, 480, 70, 20);this.add(labelCoordinates);
		butText=getButton(10, 350, 330, 125, "", null);
		butText.setEnabled(false);butText.setForeground(Color.black);butText.setBackground(Color.white);
		butNext=getButton(10, 480, 130, 20, "Продолжить", new ActionListener(){public void actionPerformed(ActionEvent e){butNext.setEnabled(false);nextQuestion();}});	
		butSGF=getButton(150, 480, 130, 20, "Экспорт в .sgf", new ActionListener(){
			public void actionPerformed(ActionEvent e){
				
			}
		});
		butSGF.setEnabled(false); 
	}
	
public PointsIQ(int level){
	//this.setCursor(Cursor.HAND_CURSOR);
	new C_JFrame(this,"PointsIQ",false,345,505,new Color(255,255,255));
	this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	this.setTitle("PointsIQ. Уровень сложности "+level);	
	this.addMouseListener(this);this.addWindowListener(this);this.addMouseMotionListener(this);
	
	base=io.getBase(level);
	game=new PointsIQGame(PointsIQ.this);
	t=new Thread(PointsIQ.this);t.start();
	
	butNext.setEnabled(false);nextQuestion();

}

private void nextQuestion(){
	curLevel=0;preLevels="";AIAnswer=null;
	
	for(int i=curQuestion;i<=base.length;i++){
		if(i==base.length){
			new JOptionPane().showMessageDialog(null,"<html>Тест пройден.<br>" +
					"Ваш результат "+game.qTrue+" правильных ответов из "+game.qThis);
			PointsIQ.this.dispose();
			break;
		}
		curQuestion=i+1;question=io.getQuestion(base[i].index);
		game.newGame();game.isComplete=false;showQuestion();t=new Thread(PointsIQ.this);t.start();break;
	}
};
	
public void mousePressed(MouseEvent me) {
	int x=game.getMouseClickX(me);
	int y=game.getMouseClickY(me);
		
	if(!game.isComplete){
		if(game.isCanMakeMove(x,y)){
			game.OP_paint.setLastHuman(x,y);game.OP_paint.setLastAI(-1,-1);game.makeMove(x,y, false);
			if(isExistsLevelMove(x,y)){
				if(game.isCanMakeMove(AIAnswer.x,AIAnswer.y)){game.OP_paint.setLastHuman(-1, -1);game.OP_paint.setLastAI(AIAnswer.x,AIAnswer.y);game.makeMove(AIAnswer.x,AIAnswer.y, true);}
				else {
					game.isComplete=true;game.qTrue++;game.qThis++;
					butText.setText("<html><font color=green>Задание "+game.qThis+" выполнено правильно!");
					butNext.setEnabled(true);
					t=new Thread(PointsIQ.this);t.start();
				}
			}
		}
	}
}

public boolean isExistsLevelMove(int x,int y){
	MakrosLevelMove moves[]=question.makros.getMoves();
	for(int i=0;i<moves.length;i++){
		if(moves[i].levelNumber==(curLevel+1)){
			if((moves[i].humanPoint.x)==x&(moves[i].humanPoint.y)==y&preLevels.equals(moves[i].preLevels)){
				AIAnswer=new Point(moves[i].AIPoint.x,moves[i].AIPoint.y);
				preLevels=moves[i].preLevels+moves[i].levelLetter;
				curLevel++;return true;
			}
		}
	}
	game.isComplete=true;game.qThis++;
	butText.setText("<html><font color=red>Задание "+game.qThis+" выполнено неверно!<br><font color=black>Как правильно выполнить задание:<br><font color=blue>"+question.comment);
	butNext.setEnabled(true);
	t=new Thread(PointsIQ.this);t.start();
	return false;
}

public void windowActivated(WindowEvent e) {t.stop();t=new Thread(this);t.start();}

public void showQuestion(){
	butText.setText("<html>Текст задания "+(game.qThis+1)+":<br><font color=blue>"+question.text);
	showStartPos(question.startPos);
	game.repaint();
}

public void showStartPos(String str){
	String move;
	while(str.length()>1){
		move=str.substring(str.indexOf("[")+1,str.indexOf("]"));
		str=str.substring(str.indexOf("]")+1);
		int x=new Integer(move.substring(0,move.indexOf(",")));move=move.substring(move.indexOf(",")+1);
		int y=new Integer(move.substring(0,move.indexOf(",")));move=move.substring(move.indexOf(",")+1);
		if(move.equals("R")){game.OP_paint.setLastHuman(-1, -1);game.OP_paint.setLastAI(x, y);game.singleGameEngine.makeMove(x,y, true);}
		else {game.OP_paint.setLastAI(-1, -1);game.OP_paint.setLastHuman(x, y);game.singleGameEngine.makeMove(x,y, false);}
	}
}

public void run(){try{try{t.sleep(100);}catch(Exception e){}game.repaint();t.stop();}catch(Exception e){}}

public void mouseMoved(MouseEvent me){try{
	if(game.getMouseClickX(me)!=preX|game.getMouseClickY(me)!=preY){
		Graphics graphics=this.getGraphics();
		if(game.isCanMakeMove(preX, preY)){
			graphics.setColor(Color.white);
			graphics.drawOval(preX*squareSize+9, preY*squareSize+25, 6, 6);
			graphics.setColor(new Color(225,225,225));
			graphics.drawLine(preX*squareSize+9, preY*squareSize+28, preX*squareSize+15, preY*squareSize+28);
			graphics.drawLine(preX*squareSize+12, preY*squareSize+25, preX*squareSize+12, preY*squareSize+31);
		}		
		preX=game.getMouseClickX(me);preY=game.getMouseClickY(me);	
		if(preX>0&preX<(20+1)&preY>0&preY<(20+1))labelCoordinates.setText("<HTML><font color=gray>"+preX+":"+preY);
		if(game.isCanMakeMove(preX, preY)){
			graphics.setColor(Color.blue);
			graphics.drawOval(preX*squareSize+9, preY*squareSize+25, 6, 6);
		}
	}
}catch(Exception e){}}

//public static void main(String[] args){new PointsIQ(4);}
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

JButton getButton(int x,int y,int width,int height,String strText,EventListener listener){
	JButton button=new JButton(strText);
	button.addActionListener((ActionListener)listener);
	button.setBounds(x, y, width, height);
	this.add(button);
	return button;
}

}