package com.google.sites.priymakpoints.pointsai.p_PointsAI;

import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ru.narod.vn91.pointsop.gui.Paper;

public class PanelAI extends JPanel{
	
	PointsAI pointsAI=null;
	Variables var=new Variables();
	Paper paper;
	JLabel label0,label1,label2;
	JButton b;
	Rectangle r=null;
	
void init(final int width,final int height){	
	label0=new JLabel(new ImageIcon(PointsAI.class.getResource("pointsAI.png")));
	setSize(width-12,height-57);
	setLayout(new java.awt.LayoutManager() {								
		public void removeLayoutComponent(java.awt.Component comp) {
		}
		public java.awt.Dimension preferredLayoutSize(java.awt.Container parent) {
			 return new java.awt.Dimension(width-12,height-57);
		}
		public java.awt.Dimension minimumLayoutSize(java.awt.Container parent) {
			 return new java.awt.Dimension(width-12,height-57);
		}
		public void layoutContainer(java.awt.Container parent) {
		}
		public void addLayoutComponent(String name, java.awt.Component comp) {
		}
	});
	
	label0.setBounds(getWidth()-240, 30, 216, 223);
	add(label0);

	label1=new JLabel(var.text);
	label1.setBounds(getWidth()-240,260,230,90);
	add(label1);
	
	label2=new LinkedLabel();
	label2.setText("<html>Официальная страница</html>");
	label2.setToolTipText(var.site);
	label2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	label2.setBounds(getWidth()-240,355,145,20);
	add(label2);
	
	b=new JButton("Новая игра");
	b.setCursor(new Cursor(Cursor.HAND_CURSOR));
	b.setBounds(getWidth()-240,390,130,20);
	add(b);
	
	b.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			newGame();
		}
	});
}

void onResize(final int width,final int height){
	label0.setBounds(getWidth()-240, 30, 216, 223);
	label1.setBounds(getWidth()-240,260,230,90);
	label2.setBounds(getWidth()-240,355,145,20);
	b.setBounds(getWidth()-240,390,130,20);
	paper.setBounds(0, 30, getWidth()-252, getHeight()-38);
}
	
void activate(){
	paper = new Paper(){
		public void paperClick(int x,int y,MouseEvent evt) {
			if(pointsAI.game.isCanMakeMove(x, y)){
				pointsAI.makeMove(x,y,false);
				java.awt.Point point=pointsAI.getAIMove();
				makeMove(point.x, point.y, true);
			}
		}
		public void paperMouseMove(int x,int y,MouseEvent evt){}
	};

	paper.initPaper(39, 32);
	pointsAI.setPaper(paper);
	
	pointsAI.makeMove(19,17,true);
	pointsAI.makeMove(18,16,true);
	pointsAI.makeMove(20,17,true);
	pointsAI.makeMove(21,16,true);
	pointsAI.makeMove(18,17,false);
	pointsAI.makeMove(19,16,false);
	pointsAI.makeMove(20,16,false);
	pointsAI.makeMove(21,17,false);

	int variant=new java.util.Random().nextInt(6);
	int x=0,y=0;
	switch(variant){
		case 0:{x=17;y=16;break;}
		case 1:{x=21;y=15;break;}
		case 2:{x=18;y=15;break;}
		case 3:{x=19;y=19;break;}
		case 4:{x=20;y=19;break;}
		case 5:{x=22;y=16;break;}
	}
	pointsAI.makeMove(x,y,true);
	
	if(r!=null)paper.setBounds(r);else paper.setBounds(0, 30, getWidth()-252, getHeight()-68);
	add(paper);
}	
	
void newGame(){	
	r=paper.getBounds();
	remove(paper);
	pointsAI.newGame();
	activate();
	repaint();
}

public PanelAI(final JFrame frame){
	
	frame.addComponentListener(new ComponentListener() {
		public void componentShown(ComponentEvent e) {}
		public void componentResized(ComponentEvent e) {onResize(frame.getWidth(),frame.getHeight());}
		public void componentMoved(ComponentEvent e) {}
		public void componentHidden(ComponentEvent e) {}
	});
	
	if(pointsAI==null){
		try{
			pointsAI=new com.google.sites.priymakpoints.pointsai.p_PointsAI.PointsAI();
			init(frame.getWidth(),frame.getHeight());
			activate();
		}catch(Exception e1){
			new JOptionPane().showMessageDialog(null, "Невозможно запустить ИИ!", "Ошибка", 0);
		}		
	}
}

class LinkedLabel extends JLabel {
	public LinkedLabel() {super();}
	public void setToolTipText(final String link) {
		super.setToolTipText(link);
		super.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					try {java.awt.Desktop.getDesktop().browse(new URI(link));
					} catch (Exception e1) {}
				} 
			}
		});
	}
	public void setText(String text) {
		text = text.replaceAll("<html>|<a href=.*>|</a>|</html>", "");
		super.setText("<html><a href=\"\""+ ">"+ text + "</a></html>");
	}
}

}
