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

import ru.narod.vn91.pointsop.ai.KeijKvantttAi;
import ru.narod.vn91.pointsop.ai.RandomAi;
import ru.narod.vn91.pointsop.server.AiVirtualServer;
import ru.narod.vn91.pointsop.server.MockServerForGui;
import ru.narod.vn91.pointsop.utils.Memory;

public class SelfishGuiStarter {

	public static void main(String[] args) {
		// try {
		// for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		// if ("Nimbus".equals(info.getName())) {
		// UIManager.setLookAndFeel(info.getClassName());
		// break;
		// }
		// }
		// } catch (Exception e) {
		// // If Nimbus is not available, you can set the GUI to another look and
		// // feel.
		// }

		if (Memory.getVersion() <= 1
				&& Memory.getKeijKvantttAiPath().equals("keijkvantttai")) {
			Memory.setKeijKvantttAiPath("");
		}
		if (Memory.getVersion() < 4) {
			Memory.resetColors();
		}
		Memory.setNewestVersion();
		final JFrame frame = new JFrame("Точки - MultiPoints 1.9.0");
		URL url = SelfishGuiStarter.class.getClassLoader().
				getResource("ru/narod/vn91/pointsop/gui/vp.jpg");
		frame.setIconImage(new ImageIcon(url).getImage());
		frame.setSize(925, 670);

		{
			if (Memory.getFrameWidth() > 0
					&& Memory.getFrameHeight() > 0) {
				//					&& Memory.getFrameX() > 0
				//					&& Memory.getFrameY() > 0
				frame.setBounds(
						Memory.getFrameX(),
						Memory.getFrameY(),
						Memory.getFrameWidth(),
						Memory.getFrameHeight()
				);
				//				frame.setSize(
				//						Memory.getFrameWidth(),
				//						Memory.getFrameHeight());
			}
			//			frame.setLocationRelativeTo(frame.getRootPane());

			frame.addComponentListener(
					new ComponentListener() {

						public void componentShown(ComponentEvent e) {
						}

						public void componentResized(ComponentEvent e) {
							Memory.setFrameWidth(frame.getWidth());
							Memory.setFrameHeight(frame.getHeight());
							Memory.setFrameX(frame.getX());
							Memory.setFrameY(frame.getY());
						}

						public void componentMoved(ComponentEvent e) {
							Memory.setFrameX(frame.getX());
							Memory.setFrameY(frame.getY());
						}

						public void componentHidden(ComponentEvent e) {
						}
					}
			);
		}

		{
			int x = frame.getX(), y = frame.getY();
			x = Math.max(x, 0);
			y = Math.max(y, 0);
			frame.setLocation(x, y);
		}

		final JTabbedPaneMod tabbedPane = new JTabbedPaneMod();
		frame.add(tabbedPane.getComponent());

		final GuiController guiController = new GuiController(tabbedPane);

		WelcomePanel roomWelcome = new WelcomePanel(guiController);
		tabbedPane.addTab("Привет!", roomWelcome, false);
		roomWelcome.guiController = guiController;
		guiController.serverOutput = roomWelcome.jTextPane_ServerOutput;

		if (Memory.isDebug()) {
			tabbedPane.addTab("комната", new LangRoom(new MockServerForGui(), "", guiController), true);
		//		tabbedPane.addTab("game room", new GameRoom(null, "", guiController, "",
		//				"", 1, 1, "", false, "", true, true));
		//		tabbedPane.addTab(
		//				"priv chat", new PrivateChat(null, guiController, ""),
		//				false
		//		);
		//		tabbedPane.addTab("Ai test", new AiRoom().panel1, true);
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
													Memory.getKeijKvantttAiPath()
											)
									);
									aiWrapper.init();
								}
							}
					);
					jMenuItem_KeijkvantttaiExecute.setEnabled(
							! Memory.getKeijKvantttAiPath().equals("")
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
											Memory.getKeijKvantttAiPath()
									);
									if (command != null) {
										if (command.endsWith("\\")
												|| command.endsWith("/")
												|| command.endsWith(File.pathSeparator)) {
											command = command + "keijkvantttai.exe";
										}
										Memory.setKeijKvantttAiPath(command);
										JOptionPane.showMessageDialog(
												null,
												"Путь изменён на " + command + " \n" +
														"Попробуйте теперь запустить ИИ. :)"
										);
										jMenuItem_KeijkvantttaiExecute.setEnabled(
												! Memory.getKeijKvantttAiPath().equals("")
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
								"Priymak AI - о программе",
								"http://pointsgame.net/site/pointsai"));
				}

				{
					final JMenuItem jMenuItem =
							new JMenuItem("Priymak AI - запуск 1.10");
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
