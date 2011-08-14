package com.google.sites.priymakpoints.pointsiq;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.ByteArrayInputStream;
import java.util.EventListener;
import javax.jnlp.FileSaveService;
import javax.jnlp.ServiceManager;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;
import ru.narod.vn91.pointsop.gui.Paper;

public class PointsIQ extends javax.swing.JPanel{

	private JButton butText,butNext,butSGF,butRePlay;
	private Question[] base;
	private Question question;
	private int curQuestion=0,curLevel=0,squareSize=16,qTrue=0,qThis=0,offsetX=1,offsetY=0;
	private String preLevels="";
	private Point AIAnswer=null;
	private boolean isComplete=true,isRePlay=false;
	private QuestionIO io=new QuestionIO();
	private JLabel labelCoordinates=new JLabel();
	private JPanel jPanel_Paper;
	private Paper paper;
	{		
		labelCoordinates.setBounds(290, 360, 70, 20);this.add(labelCoordinates);
		butText=getButton(350, 10, 240, 340, "", null);
		butText.setEnabled(false);butText.setForeground(Color.black);butText.setBackground(Color.white);
		butNext=getButton(10, 360, 130, 20, "Продолжить", new ActionListener(){public void actionPerformed(ActionEvent e){nextQuestion();}});	
		butRePlay=getButton(150, 360, 130, 20, "Повторить", new ActionListener(){public void actionPerformed(ActionEvent e){preQuestion();}});
		butSGF=getButton(350, 360, 240, 20, "Экспорт в .sgf", new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String content ="(;FF[4]GM[40]CA[UTF-8]AP[PointsIQ]SZ[20]RU[Punish=0,Holes=1,AddTurn=0,MustSurr=1,MinArea=1,Pass=0,Stop=0,LastSafe=0,ScoreTerr=0,InstantWin=0]PB[blue]PW[red]";
				String str=question.startPos;
				String move;
				MoveType moveType;
				while(str.length()>1){
					move=str.substring(str.indexOf("[")+1,str.indexOf("]"));
					str=str.substring(str.indexOf("]")+1);
					int x=new Integer(move.substring(0,move.indexOf(",")));move=move.substring(move.indexOf(",")+1);
					int y=new Integer(move.substring(0,move.indexOf(",")));move=move.substring(move.indexOf(",")+1);
					if(move.equals("R")){moveType=MoveType.RED;}else {moveType=MoveType.BLUE;}
					content += ";" + ((moveType == MoveType.RED) ? "W" : "B");
					content += "[" + getSgfCoord(x) + "" + getSgfCoord(20 + 1 - y) + "]";
				}
				content += ")";
				String[] extensions = {".sgf", ".sgftochki"};
				try {
					ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
					FileSaveService fss = (FileSaveService) ServiceManager.lookup("javax.jnlp.FileSaveService");
					fss.saveFileDialog(	"", extensions,	is, "");
				} catch (Exception exc) {System.err.println("Error: " + exc.getMessage());System.out.println(content);}
			}
		});
	}
	
public PointsIQ(int level){
	this.setLayout(new C_Layout(595,505));
	base=io.getBase(level);
		
	paper=new Paper() {
		public void paperClick(int x,int y,MouseEvent evt) {}
		public void paperMouseMove(int x,int y,MouseEvent evt) {}
	};
	newGame();
	
	jPanel_Paper=paper;
	jPanel_Paper.setBounds(0, 10, 340, 340);
	this.add(jPanel_Paper);
	jPanel_Paper.addMouseMotionListener(new MouseMotionListener() {
		public void mouseMoved(MouseEvent me) {
			int x=getMouseClickX(me);int y=getMouseClickY(me);	
			if(x>0&y<(20+1)&y>0&y<(20+1))labelCoordinates.setText("<HTML><font color=gray>"+x+":"+y);
		}
		public void mouseDragged(MouseEvent e) {}
	});
	jPanel_Paper.addMouseListener(new MouseListener() {
		public void mouseReleased(MouseEvent arg0) {}
		public void mousePressed(MouseEvent me) {
			int x=getMouseClickX(me);int y=getMouseClickY(me);
				
			if(!isComplete){
				if(makeMove(x,y, false)!=MoveResult.ERROR){
					if(isExistsLevelMove(x,y)){
						if(makeMove(AIAnswer.x,AIAnswer.y, true)==MoveResult.ERROR){
							isComplete=true;
							if(!isRePlay){qTrue++;qThis++;}
							butText.setText("<html><font color=green>Задание "+qThis+" выполнено правильно!");
							butNext.setEnabled(true);
						}
					}
				}
			}
		}
		public void mouseExited(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseClicked(MouseEvent arg0) {}
	});
	
	butNext.setEnabled(false);butRePlay.setEnabled(false);nextQuestion();
}

private void nextQuestion(){
	curLevel=0;preLevels="";AIAnswer=null;
	butNext.setEnabled(false);butRePlay.setEnabled(false);
	
	for(int i=curQuestion;i<=base.length;i++){
		if(i==base.length){
			new JOptionPane().showMessageDialog(null,"<html>Тест пройден.<br>" +
					"Ваш результат "+qTrue+" правильных ответов из "+qThis);
			break;
		}
		curQuestion=i+1;question=io.getQuestion(base[i].index);
		newGame();isComplete=false;isRePlay=false;showQuestion();break;
	}
};

private void preQuestion(){
	curLevel=0;preLevels="";AIAnswer=null;
	butNext.setEnabled(false);butRePlay.setEnabled(false);
	question=io.getQuestion(base[curQuestion-1].index);
	newGame();isComplete=false;isRePlay=true;showQuestion();
	
};

private boolean isExistsLevelMove(int x,int y){
	Question.Makros.MakrosLevelMove moves[]=question.makros.getMoves();
	for(int i=0;i<moves.length;i++){
		if(moves[i].levelNumber==(curLevel+1)){
			if((moves[i].humanPoint.x)==x&(moves[i].humanPoint.y)==y&preLevels.equals(moves[i].preLevels)){
				AIAnswer=new Point(moves[i].AIPoint.x,moves[i].AIPoint.y);
				preLevels=moves[i].preLevels+moves[i].levelLetter;
				curLevel++;return true;
			}
		}
	}
	isComplete=true;if(!isRePlay){qThis++;}
	butText.setText("<html><font color=red>Задание "+qThis+" выполнено неверно!<br><font color=black>Как правильно выполнить задание:<br><font color=blue>"+question.comment);
	butNext.setEnabled(true);butRePlay.setEnabled(true);
	return false;
}

private void showQuestion(){
	butText.setText("<html>Текст задания "+(qThis+1)+":<br><font color=blue>"+question.text);
	String str=question.startPos;
	String move;
	while(str.length()>1){
		move=str.substring(str.indexOf("[")+1,str.indexOf("]"));
		str=str.substring(str.indexOf("]")+1);
		int x=new Integer(move.substring(0,move.indexOf(",")));move=move.substring(move.indexOf(",")+1);
		int y=new Integer(move.substring(0,move.indexOf(",")));move=move.substring(move.indexOf(",")+1);
		if(move.equals("R")){makeMove(x,y, true);}
		else {makeMove(x,y, false);}
	}
}

private JButton getButton(int x,int y,int width,int height,String strText,EventListener listener){
	JButton button=new JButton(strText);
	button.addActionListener((ActionListener)listener);
	button.setBounds(x, y, width, height);
	this.add(button);
	return button;
}

private class C_Layout implements LayoutManager {
		int width;
		int height;
	public C_Layout(int width,int height){this.width=width;this.height=height;}
	public void addLayoutComponent(String name, Component comp) {}
	public void removeLayoutComponent(Component comp) {}
	public Dimension preferredLayoutSize(Container parent) {return new Dimension(width, height);}
	public Dimension minimumLayoutSize(Container parent) {return new Dimension(width, height);}
	public void layoutContainer(Container parent) {}
}

private String getSgfCoord(int i) {
	if (i <= 26) {return Character.toString((char) ((int) 'a' + i - 1));} 
	else {return Character.toString((char) ((int) 'A' + i - 26 - 1));}
}

int getMouseClickX(MouseEvent me){return (int)(((double)me.getX()-4-(double)((offsetX-1)*squareSize))/(double)squareSize);};
int getMouseClickY(MouseEvent me){return 21-(int)(((double)me.getY()-8-(double)((offsetY-1)*squareSize))/(double)squareSize);};
void newGame(){paper.initPaper(20, 20);}	
MoveResult makeMove(int x, int y,boolean isRed){return paper.makeMove(x,y,isRed);}
}