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
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import ru.narod.vn91.pointsop.ai.KeijKvantttAi;
import ru.narod.vn91.pointsop.ai.RandomAi;
import ru.narod.vn91.pointsop.data.PersistentMemory;
import ru.narod.vn91.pointsop.server.AiVirtualServer;

public class SelfishGuiStarter {

	public static void main(String[] args) {
//		try {
//			Process r = Runtime.getRuntime().exec("echo test");
////			r.getInputStream();
//			BufferedWriter writer =
//					new BufferedWriter(
//					new OutputStreamWriter(
//					r.getOutputStream()));
//			writer.append('t');
//			writer.append('y');
//			writer.newLine();
//			BufferedReader reader =
//					new BufferedReader(
//					new InputStreamReader(
//					r.getInputStream()));
//			System.out.println(reader.readLine());
//			System.out.println(reader.readLine());
//			System.exit(0);
//		} catch (IOException ex) {
//			Logger.getLogger(SelfishGuiStarter.class.getName()).log(Level.SEVERE,
//					null, ex);
//		}
//		System.exit(0);

		if (PersistentMemory.getVersion() <= 0) {
			PersistentMemory.resetColors();
		}
		PersistentMemory.setVersion(1);
		final JFrame frame = new JFrame("Точки");
		URL url = SelfishGuiStarter.class.getClassLoader().
				getResource("ru/narod/vn91/pointsop/gui/vp.jpg");
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

		final GuiController guiController = new GuiController(tabbedPane);

		WelcomePanel roomWelcome = new WelcomePanel(guiController);
		tabbedPane.addTab("Привет!", roomWelcome, false);
		roomWelcome.guiController = guiController;
		guiController.serverOutput = roomWelcome.jTextPane_ServerOutput;

//		tabbedPane.addTab("game room", new GameRoom(null, "", guiController, "",
//				"", 1, 1, "", false, "", true, true));
//		tabbedPane.addTab("priv chat", new PrivateChat(null, guiController, ""),
//				false);

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
//				{
//					JMenuItem jMenuItem = new JMenuItem("создать тестовую игру");
//					jMenuItem.addActionListener(new ActionListener() {
//
//						public void actionPerformed(ActionEvent e) {
//
//							GameRoom gameRoom = new GameRoom(null, null, null,
//									null, null, 0, 0, null, false, null,
//									true, true);
//							tabbedPane.addTab("тестовая игра", gameRoom, true);
//							tabbedPane.setSelectedComponent(gameRoom);
//						}
//					});
//					jMenu.add(jMenuItem);
//				}
				jMenuBar.add(jMenu);
			}

			{
				JMenu jMenu = new JMenu("Играть с ИИ");

				{
					final JMenuItem jMenuItem = new JMenuItem("PointsAI 1.07");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							AiVirtualServer aiWrapper =
									new AiVirtualServer(
									guiController,
									"PointsAI 1.07");
							aiWrapper.setAi(new com.google.sites.priymakpoints.pointsai.pointsAI_1_07.PointsAIEngine(
									aiWrapper, 39, 32));
							aiWrapper.init();
						}
					});
					jMenu.add(jMenuItem);
				}

				{
					final JMenuItem jMenuItem = new JMenuItem("PointsAI 1.056");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							//jMenuItem.setEnabled(false);
							AiVirtualServer aiWrapper =
									new AiVirtualServer(
									guiController,
									"PointsAI 1.056");
							aiWrapper.setAi(new com.google.sites.priymakpoints.pointsai.pointsAI_1_056.PointsAIEngine(
									aiWrapper, 39, 32));
							aiWrapper.init();
						}
					});
					jMenu.add(jMenuItem);
				}

				jMenu.add(new JSeparator());

				{
					final JMenuItem jMenuItem = new JMenuItem(
							"Бетта-версия запуска Keij&Kvanttt AI");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							JOptionPane.showMessageDialog(
									frame,
									"Для запуска данного ИИ вам нужна "
									+ "установленная консольная версия от Keij&KvanTTT"
									+ ":\nhttp://dl.dropbox.com/u/15765203/keijkvantttai.exe"
									+ "\n\n"
									+ "Если у вас не получается запустить ИИ, "
									+ "попросите помощи на канале или \n"
									+ "дождитесь "
									+ "более позднего (и думаю более удобного) релиза.");
							// ai as server
							AiVirtualServer aiWrapper =
									new AiVirtualServer(
									guiController,
									"KeijKvantttAi");
							String command = JOptionPane.showInputDialog(
									"введите имя файла - консольного клиента ИИ:");
							aiWrapper.setAi(new KeijKvantttAi(aiWrapper, 39, 32,
									command));
							aiWrapper.init();
						}
					});
					jMenu.add(jMenuItem);
				}

				jMenu.add(new JSeparator());

				{
					final JMenuItem jMenuItem = new JMenuItem("Рандомный ИИ");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							// ai as server
							AiVirtualServer aiWrapper =
									new AiVirtualServer(
									guiController,
									"RandomAI");
							aiWrapper.setAi(new RandomAi(aiWrapper, 39, 32));
							aiWrapper.init();
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
							"<html><a href=\"\">правила игры Точки</a></html>");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							try {
								java.awt.Desktop.getDesktop().browse(
										new URI(
										"http://ru.wikipedia.org/wiki/%D0%A2%D0%BE%D1%87%D0%BA%D0%B8"));
							} catch (Exception e1) {
							}
						}
					});
					jMenu.add(jMenuItem);
				}

				{
					JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=\"\">о программе PointsOP</a></html>");
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
							"<html><a href=\"\">о программе PointsAI</a></html>");
					jMenuItem.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent e) {
							try {
								java.awt.Desktop.getDesktop().browse(
										new URI(
										"http://sites.google.com/site/priymakpoints/intelligence"));
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
