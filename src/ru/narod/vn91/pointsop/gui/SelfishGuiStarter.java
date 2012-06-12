package ru.narod.vn91.pointsop.gui;

import java.awt.event.*;
import java.io.File;
import java.net.URI;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ru.narod.vn91.pointsop.ai.KeijKvantttAi;
import ru.narod.vn91.pointsop.ai.RandomAi;
import ru.narod.vn91.pointsop.model.GuiController;
import ru.narod.vn91.pointsop.server.AiVirtualServer;
import ru.narod.vn91.pointsop.utils.Settings;

public class SelfishGuiStarter {

	public static void main(String[] args) {

		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
		} catch (ClassNotFoundException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		}


		for (String argument : args) {
			// System.out.println("argument = " + argument);
			if (argument.matches("--version|-v|-h|--help")) {
				System.out.println("MultiPoints. Sorry, no info is aviable here...");
				System.exit(0);
			}
			else if (argument.matches("debug=.*")) {
				Settings.setDebug(argument.matches("debug=(true|debug|on)"));
				System.out.println("debug level set to 'true'");
			}
			else if (argument.matches("ircAddress=.*")) {
				String address = argument.replaceFirst(".*=", "");
				Settings.setIrcAddress(address);
				System.out.println(argument);
			}
		}

//	//Create a file chooser
//		final JFileChooser fc = new JFileChooser();
//
//		int returnVal = fc.showOpenDialog(null);
//
//		System.out.println("start file tests...");
//		if (returnVal == JFileChooser.APPROVE_OPTION) {
//			try {
//				File file = fc.getSelectedFile();
//				// This is where a real application would open the file.
//				System.out.println(file.getAbsolutePath());
//				System.out.println("file.isDirectory() = " + file.isDirectory());
//				System.out.println("file.canExecute() = " + file.canExecute());
//				System.out.println("file.canRead() = " + file.canRead());
//				System.out.println("file.canWrite() = " + file.canWrite());
//				
//
//				FileInputStream fileInputStream;
//				fileInputStream = new FileInputStream(file);
//				DataInputStream dataInputStream = new DataInputStream(fileInputStream);
//				InputStreamReader inputStreamReader =
//						new InputStreamReader(dataInputStream, "UTF-8") {
//						};
//				BufferedReader br = new BufferedReader(inputStreamReader);
//
//				String strLine;
//				while ((strLine = br.readLine()) != null) {
//					System.out.println("strLine = " + strLine);
//				}
//				dataInputStream.close();
//			} catch (FileNotFoundException ex) {
//				ex.printStackTrace();
//			} catch (UnsupportedEncodingException ex) {
//				ex.printStackTrace();
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		} else {
//			System.out.println("error");
//		}
////		while("".equals("")) {}
//		System.exit(0);

		if (Settings.getVersion() <= 1 &&
				Settings.getKeijKvantttAiPath().equals("keijkvantttai"))
		{
			Settings.setKeijKvantttAiPath("");
		}
		if (Settings.getVersion() < 4) {
			Settings.resetColors();
		}
		Settings.setNewestVersion();
		final JFrame frame = new JFrame("Точки - MultiPoints 2.1.8");
		URL url = SelfishGuiStarter.class.getClassLoader().
				getResource("ru/narod/vn91/pointsop/gui/vp.jpg");
		frame.setIconImage(new ImageIcon(url).getImage());

		if (Settings.isRestoreSize() &&
				Settings.getFrameWidth() > 0 &&
				Settings.getFrameHeight() > 0) {
			// && Memory.getFrameX() > 0
			// && Memory.getFrameY() > 0
			frame.setSize(Settings.getFrameWidth(), Settings.getFrameHeight());
//				frame.setBounds(
//						Settings.getFrameX(),
//						Settings.getFrameY(),
//						Settings.getFrameWidth(),
//						Settings.getFrameHeight()
//				);
		} else {
			frame.setSize(925, 670);
		}

		if (Settings.isRestorePosition()) {
			frame.setLocation(Settings.getFrameX(), Settings.getFrameY());
		} else {
			frame.setLocationRelativeTo(null);
//				GraphicsDevice device =
//						GraphicsEnvironment.
//								getLocalGraphicsEnvironment().
//								getDefaultScreenDevice();
//				if (device.isFullScreenSupported()) {
//					device.setFullScreenWindow(frame);
//				}
//				else {
//					System.err.println("Full screen not supported");
//				}
		}

		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		// frame.setUndecorated(true);

		int x = frame.getX(), y = frame.getY();
		x = Math.max(x, 0);
		y = Math.max(y, 0);
		frame.setLocation(x, y);

		frame.addComponentListener(
				new ComponentListener() {

					public void componentShown(ComponentEvent e) {
					}

					public void componentResized(ComponentEvent e) {
						Settings.setFrameWidth(frame.getWidth());
						Settings.setFrameHeight(frame.getHeight());
						Settings.setFrameX(frame.getX());
						Settings.setFrameY(frame.getY());
					}

					public void componentMoved(ComponentEvent e) {
						Settings.setFrameX(frame.getX());
						Settings.setFrameY(frame.getY());
					}

					public void componentHidden(ComponentEvent e) {
					}
				}
		);

		final JTabbedPaneMod tabbedPane = new JTabbedPaneMod();

		final GuiController guiController = new GuiController(tabbedPane);

		WelcomePanel roomWelcome = new WelcomePanel(guiController);
		tabbedPane.addTab("Привет!", roomWelcome, false);
		roomWelcome.guiController = guiController;
		guiController.serverOutput = roomWelcome.jTextPane_ServerOutput;

		if (Settings.isDebug()) {
//			ServerInterface mockServer = new MockServerForGui();
//			guiController.updateGameInfo(mockServer, "game", "lang", "user", "user2", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
//			guiController.updateUserInfo(mockServer, "user", "user-gui-name", null, null, null, null, null, "status", "userinfo");
//
//			guiController.privateMessageReceived(mockServer, "user", "message");
//			
//
//			{
//				// hack into the GuiController
//				tabbedPane.addTab("комната", new LangRoom(mockServer, "lang-hack", guiController), true);
//				tabbedPane.addTab("игра", new GameRoom(new GameOuterInfo(mockServer, "game-hack"), guiController), true);
//				tabbedPane.addTab("ИИ", new AiPanel(), true);
//			}
//			{
//				// non-closeable
//				// guiController.subscribedLangRoom("lang", mockServer, "lang", true); // non-closeable
//				// guiController.subscribedGame(mockServer, "game");
//			}
		}

		{
			JMenuBar jMenuBar = new JMenuBar();

			{
				JMenu jMenu = new JMenu("Обучение");

				{
					jMenu.add(new JMenuItemWithLink(
							"Правила игры",
					"http://pointsgame.net/site/rules"));
				}
				{
					jMenu.add(new JMenuItemWithLink(
							"Разборы партий",
					"http://pointsgame.net/site/analysis"));
				}
				{
					jMenu.add(new JMenuItemWithLink(
							"Архив игр",
							"http://pointsgame.net/site/games"));
				}

				jMenu.addSeparator();

				{
					JMenuItem jMenuItem = new JMenuItem("Тест - уровень 1");
					jMenuItem.addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									try {
										javax.swing.JPanel pointsIQ=new com.google.sites.priymakpoints.pointsiq.PointsIQ(1);
										tabbedPane.addTab("PointsIQ level 1", pointsIQ, true);
										tabbedPane.setSelectedComponent(pointsIQ);
									} catch (Exception ignored) {	}
								}
							}
					);
					jMenu.add(jMenuItem);
				}

				{
					JMenuItem jMenuItem = new JMenuItem("Тест - уровень 2");
					jMenuItem.addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									try {
										javax.swing.JPanel pointsIQ=new com.google.sites.priymakpoints.pointsiq.PointsIQ(2);
										tabbedPane.addTab("PointsIQ level 2", pointsIQ, true);
										tabbedPane.setSelectedComponent(pointsIQ);
									} catch (Exception ignored) {	}
								}
							}
					);
					jMenu.add(jMenuItem);
				}

				{
					JMenuItem jMenuItem = new JMenuItem("Тест - уровень 3");
					jMenuItem.addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									try {
										javax.swing.JPanel pointsIQ=new com.google.sites.priymakpoints.pointsiq.PointsIQ(3);
										tabbedPane.addTab("PointsIQ level 3", pointsIQ, true);
										tabbedPane.setSelectedComponent(pointsIQ);
									} catch (Exception ignored) {	}
								}
							}
					);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem("Тест - уровень 4");
					jMenuItem.addActionListener(
							new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									try {
										javax.swing.JPanel pointsIQ=new com.google.sites.priymakpoints.pointsiq.PointsIQ(4);
										tabbedPane.addTab("PointsIQ level 4", pointsIQ, true);
										tabbedPane.setSelectedComponent(pointsIQ);
									} catch (Exception ignored) {	}
								}
							}
					);
					jMenu.add(jMenuItem);
				}

				{
					jMenu.add(new JMenuItemWithLink(
							"О программе PointsIQ",
							"http://pointsgame.net/site/pointsiq"));
				}

				jMenuBar.add(jMenu);
			}

			{
				JMenu jMenu_AI = new JMenu("Играть с ИИ");

				{
					jMenu_AI.add(new JMenuItemWithLink(
							"Keij&Kvanttt AI - описание работы с этим ИИ",
					"http://vkontakte.ru/topic-21455903_24989187"));
				}

				JMenu jMenu_KeijDownloads = new JMenu("Keij&Kvanttt AI - скачать необходимые файлы");
				{
					jMenu_KeijDownloads.add(new JMenuItemWithLink(
								"exe-файл для винды",
							"http://code.google.com/p/pointsgame/"));

					jMenu_KeijDownloads.add(new JMenuItemWithLink(
									"исполняемый файл для линукса и мака",
									"http://pointsgame.net/kekvai/keijkvantttai.linux"));

					jMenu_AI.add(jMenu_KeijDownloads);
				}

				final JMenuItem jMenuItem_KeijkvantttaiExecute;
				{
					jMenuItem_KeijkvantttaiExecute = new JMenuItem(
							"Keij&Kvanttt AI - запуск"
					);
					jMenuItem_KeijkvantttaiExecute.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									AiVirtualServer aiWrapper =
											new AiVirtualServer(
													guiController
											);
									aiWrapper.setAi(
											new KeijKvantttAi(
													aiWrapper,
													39, 32,
													Settings.getKeijKvantttAiPath()
											)
									);
									aiWrapper.init();
								}
							}
					);
					jMenuItem_KeijkvantttaiExecute.setEnabled(
							! Settings.getKeijKvantttAiPath().equals("")
					);
				}

				{
					final JMenuItem jMenuItem = new JMenuItem(
							"Keij&Kvanttt AI - указать путь скачанного файла"
					);
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									String command = JOptionPane.showInputDialog(
											"Для работы с данным ИИ скачайте, пожалуйста, \n" +
													"файл с искусственным интеллектом (ссылка выше).\n\n" +
													"" +
													"После этого введите сюда путь к скачанному файлу. \n" +
													"Например, если вы скачали keijkvantttai.exe в директорию c:/Downloads/  то введите \n" +
													"c:/Downloads/keijkvantttai.exe\n\n" +
													"Чтобы узнать путь к скачанному файлу можно, например, \n" +
													"заглянуть в свойства файла и скопировать строчку 'размещение'.\n\n" +
													"Или запустить файлик и скопировать инфу которую он предоставляет (new). \n\n" +
													"P.S. К сожалению, " +
													"по политике безопасности java, " +
													"выбрать файл более привычным способом не получается.",
											Settings.getKeijKvantttAiPath()
									);
									if (command != null) {
										if (command.endsWith("\\")
												|| command.endsWith("/")
												|| command.endsWith(File.pathSeparator)) {
											command = command + "keijkvantttai.exe";
										}
										Settings.setKeijKvantttAiPath(command);
										JOptionPane.showMessageDialog(
												null,
												"Путь изменён на " + command + " \n" +
														"Попробуйте теперь запустить ИИ. :)"
										);
										jMenuItem_KeijkvantttaiExecute.setEnabled(
												! Settings.getKeijKvantttAiPath().equals("")
										);
									}
								}
							}
					);
					jMenu_AI.add(jMenuItem);
					jMenu_AI.add(jMenuItem_KeijkvantttaiExecute);
				}

				jMenu_AI.add(new JSeparator());

				{
					jMenu_AI.add(new JMenuItemWithLink(
								"PointsAI (от Priymak Alexey) - о программе",
								"http://pointsgame.net/site/pointsai"));
				}

				{
					final JMenuItem jMenuItem = new JMenuItem("PointsAI - запуск 1.12");
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									new Thread() {
										@Override
										public void run() {
											AiVirtualServer aiWrapper =
													new AiVirtualServer(
															guiController
													);
											aiWrapper
													.setAi(
													new com.google.sites.priymakpoints.pointsai.pointsAI_1_10.PointsAIEngine(
															aiWrapper, 39, 32
													)
													);
											aiWrapper.init();
										}
									}.start();
								}
							}
					);
					jMenu_AI.add(jMenuItem);
				}

				jMenu_AI.add(new JSeparator());

				{
					final JMenuItem jMenuItem = new JMenuItem("Рандомный ИИ - запуск");
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									// ai as server
									AiVirtualServer aiWrapper =
											new AiVirtualServer(
//													(GuiForServerInterface)guiController
													guiController
											);
									aiWrapper.setAi(new RandomAi(aiWrapper, 39, 32));
									aiWrapper.init();
								}
							}
					);
					jMenu_AI.add(jMenuItem);
				}

				jMenuBar.add(jMenu_AI);
			}

			{
				JMenu jMenu = new JMenu("Настройки");
				{
					JMenuItem jMenuItem = new JMenuItem("Настройки");
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									SettingsPanel settings = new SettingsPanel();
									tabbedPane.addTab("Настройки", settings, true);
									tabbedPane.setSelectedComponent(settings);
								}
							}
					);
					jMenu.add(jMenuItem);
				}
				jMenuBar.add(jMenu);
			}
			{
				JMenu jMenu = new JMenu("Помощь");

				jMenu.add(new JMenuItemWithLink(
							"О программе MultiPoints",
							"http://pointsgame.net/site/mp"));
				jMenu.add(new JMenuItemWithLink(
							"Обсуждение MultiPoints",
						"http://vkontakte.ru/pointsgame"));
				jMenu.add(new JMenuItemWithLink(
							"Полезные ссылки",
							"http://pointsgame.net/site/links"));
				jMenuBar.add(jMenu);
			}
			frame.setJMenuBar(jMenuBar);
		}

		frame.add(tabbedPane.getComponent());
		frame.setVisible(true);
		roomWelcome.jTextField_Username.requestFocusInWindow();
		frame.addWindowListener(
				new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent we) {
						System.exit(0);
					}
				}
		);
	}
}

/**
 * @see {@link WelcomePanel.LinkedLabel}
 */
@SuppressWarnings("serial")
class JMenuItemWithLink extends JMenuItem {

	@Override
	public void setText(String text) {
		text = text.replaceAll("<html>|<a href=.*>|</a>|</html>", "");
		super.setText(
				"<html><a href=\"\""
						+ ">"
						+ text + "" + "</a></html>"
				);
	}

	public JMenuItemWithLink(String text, final String url) {
		super(text);
		super.setToolTipText(url);

		this.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean doOpen = true;
				{
					int userConfirmation = JOptionPane.showConfirmDialog(
						null,
						"Открываем ссылку: \n" + url,
						"MultiPoints - открытие ссылки",
						JOptionPane.OK_CANCEL_OPTION);
					if (userConfirmation != JOptionPane.OK_OPTION) {
						doOpen = false;
					}
				}
				if (doOpen) {
					try {
						java.awt.Desktop.getDesktop().browse(new URI(url));
					} catch (Exception ignored) {
					}
				}
			}
		});
	}
}
