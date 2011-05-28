package ru.narod.vn91.pointsai.p_PointsAI;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ru.narod.vn91.pointsop.gui.Paper;

public class PanelAI extends JPanel{
	
	PointsAI pointsAI=null;
	JPanel infoPanel;
	Variables var=new Variables();
	Paper paper;
	
void init(final int width,final int height){	
	JLabel lFon=new JLabel(new ImageIcon(PointsAI.class.getResource("fon.png")));
	infoPanel=getInfoPanel();
	
	this.setSize(width-12,height-57);
	this.setLayout(new java.awt.LayoutManager() {								
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
	
	lFon.setBounds(this.getWidth()-252, 0, 250, 260);
	infoPanel.setBounds(this.getWidth()-252,270,infoPanel.getWidth(),infoPanel.getHeight());

	this.add(lFon);
	this.add(infoPanel);
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
	
	paper.setBounds(0, 30, this.getWidth()-252, this.getHeight()-68);
	this.add(paper);
}	
	
JPanel getInfoPanel(){
	final JPanel panel=new JPanel();
	//panel.setBackground(Color.red);
	
	panel.setSize(250,this.getHeight()-280);
	panel.setLayout(new java.awt.LayoutManager() {								
		public void removeLayoutComponent(java.awt.Component comp) {
		}
		public java.awt.Dimension preferredLayoutSize(java.awt.Container parent) {
			 return new java.awt.Dimension(panel.getWidth(),panel.getHeight());
		}
		public java.awt.Dimension minimumLayoutSize(java.awt.Container parent) {
			 return new java.awt.Dimension(panel.getWidth(),panel.getHeight());
		}
		public void layoutContainer(java.awt.Container parent) {
		}
		public void addLayoutComponent(String name, java.awt.Component comp) {
		}
	});
	
	JLabel label1=new JLabel(var.text+"<br><br>автор Алексей Приймак");
	label1.setBounds(10,10,230,110);
	panel.add(label1);
	
	JLabel label2=new LinkedLabel();
	label2.setText("<html>Официальная страница</html>");
	label2.setToolTipText("http://sites.google.com/site/priymakpoints/intelligence");
	label2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	label2.setBounds(10,130,150,20);
	panel.add(label2);
	
	JButton b=new JButton("Новая игра");
	b.setCursor(new Cursor(Cursor.HAND_CURSOR));
	b.setBounds(10,170,130,20);
	panel.add(b);
	
	b.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			newGame();
		}
	});
	
	return panel;
}

void newGame(){	
	this.remove(paper);
	pointsAI.newGame();
	activate();
	this.repaint();
}

public PanelAI(final int width,final int height){
	this.addFocusListener(new FocusListener() {
		public void focusLost(FocusEvent e) {}
		public void focusGained(FocusEvent e) {
			if(pointsAI==null){
				Graphics g=PanelAI.this.getGraphics();
				g.setFont(new Font("Tahoma",30,30));
				g.drawString("Подождите около 10 секунд ...", 100, 100);
				
				try{
					pointsAI=new ru.narod.vn91.pointsai.p_PointsAI.PointsAI();
					init(width,height);
					activate();
				}catch(Exception e1){
					g.clearRect(0, 0, PanelAI.this.getWidth(), PanelAI.this.getHeight());
					g.drawString("Произошла ошибка ...", 100, 100);
					g.setFont(new Font("Tahoma",20,20));
					g.drawString("необходимо подключение к Интернету", 100, 130);
				}
				
		}}
	});
}

public javax.swing.JPanel getAIPanel(){return this;}

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
