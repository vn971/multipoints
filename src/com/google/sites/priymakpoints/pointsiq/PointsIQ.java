package com.google.sites.priymakpoints.pointsiq;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JOptionPane;

public class PointsIQ extends MainFrame implements Variables{

	public PointsIQGame game;//=new PointsAIGame();
	int preX=0,preY=0;
	JButton qText,next;
	C_AddComponent add=new C_AddComponent(this.getContentPane());
	Question[] base;
	Question question;
	int curQuestion=0;
	int curLevel=0;String preLevels="";Point AIAnswer=null;
	QuestionIO io=new QuestionIO();
	{		
		qText=(JButton) add.component("button", 10, 350, 330, 125, "", null);
		qText.setEnabled(false);qText.setForeground(Color.black);qText.setBackground(Color.white);
		
		next=(JButton) add.component("button", 10, 480, 330, 25, "Продолжить", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PointsIQ.this.setSize(PointsIQ.this.getWidth(), PointsIQ.this.getHeight()-33);
				curLevel=0;preLevels="";AIAnswer=null;
				
				for(int i=curQuestion;i<=base.length;i++){
					if(i==base.length){
						new JOptionPane().showMessageDialog(null,"<html>Тест пройден.<br>" +
								"Ваш результат "+game.qTrue+" правильных ответов из "+game.qThis);
						PointsIQ.this.dispose();
						break;
					}
					if(base[i].level==game.qLevel){
						curQuestion=i+1;question=io.getQuestion(base[i].index);
						game.newGame();game.isComplete=false;showQuestion();t=new Thread(PointsIQ.this);t.start();break;
					}
				}				
			}
		});	
	}
	
public PointsIQ(int level){
	base=io.getBase(level);//System.out.println("op");
	game=new PointsIQGame(PointsIQ.this);
	for(int i=0;i<base.length;i++){if(base[i].level==level)game.qNumber++;}
	if(game.qNumber==0){new JOptionPane().showMessageDialog(null, "Для данного уровня сложности в базе нет вопросов!");}
	game.qLevel=level;setLabelText(game.getLabelText());
	t=new Thread(PointsIQ.this);t.start();
	
	showStartPos("[4,7,B][4,8,B][4,9,B][4,10,B][4,11,B][4,12,B][4,6,B][4,5,B][5,5,B][5,6,B][5,7,B][5,8,B][5,9,B][5,10,B][5,11,B][5,12,B][4,3,B][5,3,B][5,2,B][4,2,B][3,5,B][6,5,B][6,12,B][3,12,B][10,5,R][10,6,R][10,7,R][10,9,R][10,8,R][10,10,R][11,11,R][12,12,R][11,4,R][12,3,R][13,3,R][14,3,R][13,12,R][14,12,R][15,12,R][16,11,R][17,10,R][15,3,R][16,4,R][17,5,R][17,6,R][17,7,R][17,8,R][17,9,R][15,10,R][17,12,R][18,12,R][4,17,R][5,17,B][5,16,R][9,17,B][6,17,R][10,16,B][5,18,R][11,17,B][10,17,R][10,18,B][13,17,R][14,17,B][14,16,R][17,17,B][15,17,R][18,16,B][14,18,R][19,17,B][18,17,R][18,18,B]");
}
	
public void mousePressed(MouseEvent me) {
	int x=game.getMouseClickX(me);
	int y=game.getMouseClickY(me);
		
	if(!game.isComplete){
		if(game.isCanMakeMove(x,y)){
			game.OP_paint.setLastHuman(x,y);game.OP_paint.setLastAI(-1,-1);game.makeMove(x,y, false);
			if(isExistsLevelMove(x,y)){
				if(game.isCanMakeMove(AIAnswer.x,AIAnswer.y)){game.OP_paint.setLastHuman(-1, -1);game.OP_paint.setLastAI(AIAnswer.x,AIAnswer.y);game.makeMove(AIAnswer.x,AIAnswer.y, true);}
				else {
					game.isComplete=true;game.qTrue++;game.qThis++;setLabelText(game.getLabelText());
					qText.setText("<html><font color=green>Задание выполнено правильно!");
					PointsIQ.this.setSize(PointsIQ.this.getWidth(), PointsIQ.this.getHeight()+33);
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
	game.isComplete=true;game.qThis++;setLabelText(game.getLabelText());
	qText.setText("<html><font color=red>Задание выполнено неверно!<br><font color=black>Как правильно выполнить задание:<br>"+question.comment);
	PointsIQ.this.setSize(PointsIQ.this.getWidth(), PointsIQ.this.getHeight()+33);
	t=new Thread(PointsIQ.this);t.start();
	return false;
}

public void windowActivated(WindowEvent e) {t.stop();t=new Thread(this);t.start();}

public void showQuestion(){
	game.qLevel=question.level;
	qText.setText("<html>Текст задания:<br>"+question.text);
	showStartPos(question.startPos);
	setLabelText(game.getLabelText());	
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

public void run(){try{try{t.sleep(200);}catch(Exception e){}game.repaint();t.stop();}catch(Exception e){}}

public void mouseMoved(MouseEvent me){try{//System.out.println("+++");
	if(game.getMouseClickX(me)!=preX|game.getMouseClickY(me)!=preY){
		Graphics graphics=this.getGraphics();
		if(game.isCanMakeMove(preX, preY)){
			graphics.setColor(Color.white);
			graphics.drawOval(preX*squareSize+9, preY*squareSize+41, 6, 6);
			graphics.setColor(new Color(225,225,225));
			graphics.drawLine(preX*squareSize+9, preY*squareSize+44, preX*squareSize+15, preY*squareSize+44);
			graphics.drawLine(preX*squareSize+12, preY*squareSize+41, preX*squareSize+12, preY*squareSize+47);
		}		
		preX=game.getMouseClickX(me);preY=game.getMouseClickY(me);	
		if(preX>0&preX<(sizeX+1)&preY>0&preY<(sizeY+1))labelCoordinates.setText("<HTML><font color=gray>"+preX+":"+preY);
		if(game.isCanMakeMove(preX, preY)){
			graphics.setColor(Color.blue);
			graphics.drawOval(preX*squareSize+9, preY*squareSize+41, 6, 6);
		}
	}
}catch(Exception e){}}

}