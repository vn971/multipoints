package ru.narod.vn91.pointsop.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import ru.narod.vn91.pointsop.data.PersistentMemory;

/**
 *
 * @author vasya
 */
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
		frame.setLocationRelativeTo(frame.getRootPane());
		
		//<new code - componentResized & PersistentMemory>
		if(PersistentMemory.getFrameWidth()<=0&PersistentMemory.getFrameHeight()<=0){
			frame.setSize(PersistentMemory.getFrameWidth(), PersistentMemory.getFrameHeight());
		}
		frame.addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				PersistentMemory.setFrameWidth(frame.getWidth());
				PersistentMemory.setFrameHeight(frame.getHeight());
			}
			public void componentMoved(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
		});
		//</new code - componentResized & PersistentMemory>
		
		{
			int x = frame.getBounds().x, y = frame.getBounds().y;
			x = Math.max(x, 0);
			y = Math.max(y, 0);
			frame.setLocation(x, y);
		}

		final JTabbedPaneMod tabbedPane = new JTabbedPaneMod();
		frame.add(tabbedPane);
		tabbedPane.setFocusable(false);

		GuiController guiController = new GuiController(tabbedPane);

		WelcomePanel roomWelcome = new WelcomePanel(guiController);
		tabbedPane.addTab("Привет!", roomWelcome, false);
		roomWelcome.guiController = guiController;
		guiController.serverOutput = roomWelcome.jTextPane_ServerOutput;

//		tabbedPane.addTab("game room", new GameRoom(null, "", guiController, "", "", 1, 1, "", false, "", true, true));
//		tabbedPane.addTab("settings", new ContainerRoom_Settings());
//		tabbedPane.addTab("priv chat", new PrivateChat(null, guiController, ""), false);
//

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
					JMenuItem jMenuItem = new JMenuItem("создать поле для игры");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							Paper paper = new Paper() {

								@Override
								public void paperClick(
										int x,
										int y,
										MouseEvent evt) {
									makeMove(x, y);
								}

								@Override
								public void paperMouseMove(
										int x,
										int y,
										MouseEvent evt) {
								}
							};
							paper.initPaper(39, 32);
							tabbedPane.addTab("game", paper, true);
							tabbedPane.setSelectedComponent(paper);
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
					JMenuItem jMenuItem = new JMenuItem("online помощь");
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
					JMenuItem jMenuItem = new JMenuItem("полезные ссылки");
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
