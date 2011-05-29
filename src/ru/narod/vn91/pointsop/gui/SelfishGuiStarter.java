package ru.narod.vn91.pointsop.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URI;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import com.google.sites.priymakpoints.pointsai.p_PointsAI.PanelAI;
import ru.narod.vn91.pointsop.data.PersistentMemory;

public class SelfishGuiStarter {

	public static void main(String[] args) {
		if (PersistentMemory.getVersion() <= 0) {
			PersistentMemory.resetColors();
		}
		PersistentMemory.setVersion(1);
		final JFrame frame = new JFrame("Точки");
		URL url = SelfishGuiStarter.class.getClassLoader().
				getResource("ru/narod/vn91/pointsop/data/vp.jpg");
		frame.setIconImage(new ImageIcon(url).getImage());
		frame.setSize(925, 670);

		{
			if (PersistentMemory.getFrameWidth() > 0
					&& PersistentMemory.getFrameHeight() > 0) {
				frame.setSize(PersistentMemory.getFrameWidth(),
						PersistentMemory.getFrameHeight());
			}
			frame.setLocationRelativeTo(frame.getRootPane());

			frame.addComponentListener(new ComponentListener() {

				public void componentShown(ComponentEvent e) {
				}

				public void componentResized(ComponentEvent e) {
					PersistentMemory.setFrameWidth(frame.getWidth());
					PersistentMemory.setFrameHeight(frame.getHeight());
				}

				public void componentMoved(ComponentEvent e) {
				}

				public void componentHidden(ComponentEvent e) {
				}
			});
		}

		{
			int x = frame.getBounds().x, y = frame.getBounds().y;
			x = Math.max(x, 0);
			y = Math.max(y, 0);
			frame.setLocation(x, y);
		}

		final JTabbedPaneMod tabbedPane = new JTabbedPaneMod();
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frame.add(tabbedPane);
		tabbedPane.setFocusable(false);

		GuiController guiController = new GuiController(tabbedPane);

		WelcomePanel roomWelcome = new WelcomePanel(guiController);
		tabbedPane.addTab("Привет!", roomWelcome, false);
		roomWelcome.guiController = guiController;
		guiController.serverOutput = roomWelcome.jTextPane_ServerOutput;

		//tabbedPane.addTab("Играть с ИИ", new PanelAI(frame.getWidth(),frame.getHeight()).getAIPanel(), false);
		
//		tabbedPane.addTab("game room", new GameRoom(null, "", guiController, "", "", 1, 1, "", false, "", true, true));
//		tabbedPane.addTab("priv chat", new PrivateChat(null, guiController, ""), false);

		{
			JMenuBar jMenuBar = new JMenuBar();

			{
				JMenu jMenu = new JMenu("Файл");
				{
					JMenuItem jMenuItem = new JMenuItem("открыть");
					jMenuItem.setEnabled(false);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem("сохранить");
					jMenuItem.setEnabled(false);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem("создать тестовую игру");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {

//							Paper paper = new Paper() {
//
//								@Override
//								public void paperClick(
//										int x,
//										int y,
//										MouseEvent evt) {
//									makeMove(x, y);
//								}
//
//								@Override
//								public void paperMouseMove(
//										int x,
//										int y,
//										MouseEvent evt) {
//								}
//							};
//							paper.initPaper(39, 32);
//
//							ru.narod.vn91.pointsop.gui.RoomPart_Userlist roomPart_UserList = new RoomPart_Userlist();
//							ru.narod.vn91.pointsop.gui.RoomPart_Chat roomPart_Chat=new RoomPart_Chat();
//							javax.swing.JPanel panel=new javax.swing.JPanel();
//
//							panel.setLayout(new java.awt.LayoutManager() {
//								public void removeLayoutComponent(java.awt.Component comp) {
//								}
//								public java.awt.Dimension preferredLayoutSize(java.awt.Container parent) {
//									 return new java.awt.Dimension(frame.getWidth()-12, frame.getHeight()-57);
//								}
//								public java.awt.Dimension minimumLayoutSize(java.awt.Container parent) {
//									 return new java.awt.Dimension(frame.getWidth()-12, frame.getHeight()-57);
//								}
//								public void layoutContainer(java.awt.Container parent) {
//								}
//								public void addLayoutComponent(String name, java.awt.Component comp) {
//								}
//							});
//
//							panel.setSize(frame.getWidth()-12, frame.getHeight()-57);
//
//							paper.setBounds(0, 30, panel.getWidth()-252, panel.getHeight()-68);
//							roomPart_UserList.setBounds(panel.getWidth()-252, 0, 245, (int)(panel.getHeight()*2/5)-20);
//							roomPart_Chat.setBounds(panel.getWidth()-252,(int)(panel.getHeight()*2/5),245,(int)(panel.getHeight()*3/5)-40);
//
//							panel.add(paper);
//							panel.add(roomPart_UserList);
//							panel.add(roomPart_Chat);

//							tabbedPane.addTab("test game", panel, true);
//							tabbedPane.setSelectedComponent(panel);
							GameRoom gameRoom = new GameRoom(null, null, null,
									null, null, 0, 0, null, false, null,
									true, true);
							tabbedPane.addTab("тестовая игра", gameRoom, true);
							tabbedPane.setSelectedComponent(gameRoom);
						}
					});
					jMenu.add(jMenuItem);
				}
				jMenuBar.add(jMenu);
			}

			{
				JMenu jMenu = new JMenu("Играть с ИИ");
				{
					final JMenuItem jMenuItem = new JMenuItem("PointsAI 1.056");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							jMenuItem.setEnabled(false);
							PanelAI panelAI=new PanelAI(frame);
							tabbedPane.addTab("PointsAI 1.056",panelAI,false);
							tabbedPane.setSelectedComponent(panelAI);
						}
					});
					jMenu.add(jMenuItem);
				}
							
				jMenuBar.add(jMenu);
			}

			{
				JMenu jMenu = new JMenu("Настройки");
				{
					JMenuItem jMenuItem = new JMenuItem("общие настройки");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							SettingsPanel settings = new SettingsPanel();
							tabbedPane.addTab("настройки", settings, true);
							tabbedPane.setSelectedComponent(settings);
						}
					});
					jMenu.add(jMenuItem);
				}
				jMenuBar.add(jMenu);
			}
			{
				JMenu jMenu = new JMenu("Помощь");
				
				{
					JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=\"\">правила игры</a></html>");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							try {
								java.awt.Desktop.getDesktop().browse(new URI(
										"http://ru.wikipedia.org/wiki/%D0%A2%D0%BE%D1%87%D0%BA%D0%B8"));
							} catch (Exception e1) {
							}
						}
					});
					jMenu.add(jMenuItem);
				}
				
				{
					JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=\"\">о программе</a></html>");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							try {
								java.awt.Desktop.getDesktop().browse(new URI(
										"http://vkontakte.ru/club21455903"));
							} catch (Exception e1) {
							}
						}
					});
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=\"\">полезные ссылки</a></html>");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							try {
								java.awt.Desktop.getDesktop().browse(
										new URI(
										"http://sites.google.com/site/oscarpoints/links"));
							} catch (Exception e1) {
							}
						}
					});
					jMenu.add(jMenuItem);
				}
				jMenuBar.add(jMenu);
			}
			frame.setJMenuBar(jMenuBar);
		}

		frame.setVisible(true);
		roomWelcome.jTextField_Username.requestFocusInWindow();
		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}
}
