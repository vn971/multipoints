package ru.narod.vn91.pointsop.gui;

import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import javax.jnlp.FileContents;
import javax.jnlp.FileOpenService;
import javax.jnlp.ServiceManager;
import javax.jnlp.UnavailableServiceException;
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
		for (String arg : args) {
			System.out.println(arg);
		}
		if (PersistentMemory.getVersion() <= 0) {
			PersistentMemory.resetColors();
		}
		if (PersistentMemory.getVersion() <= 1
				&& PersistentMemory.getKeijKvantttAiPath().equals("keijkvantttai")) {
			PersistentMemory.setKeijKvantttAiPath("");
		}
		PersistentMemory.setVersion(PersistentMemory.version);
		final JFrame frame = new JFrame("Точки - pointsOp 0.9.5");
		URL url = SelfishGuiStarter.class.getClassLoader().
				getResource("ru/narod/vn91/pointsop/gui/vp.jpg");
		frame.setIconImage(new ImageIcon(url).getImage());
		frame.setSize(925, 670);

		{
			if (PersistentMemory.getFrameWidth() > 0
					&& PersistentMemory.getFrameHeight() > 0) {
//					&& PersistentMemory.getFrameX() > 0
//					&& PersistentMemory.getFrameY() > 0
				frame.setBounds(
						PersistentMemory.getFrameX(),
						PersistentMemory.getFrameY(),
						PersistentMemory.getFrameWidth(),
						PersistentMemory.getFrameHeight()
				);
//				frame.setSize(
//						PersistentMemory.getFrameWidth(),
//						PersistentMemory.getFrameHeight());
			}
//			frame.setLocationRelativeTo(frame.getRootPane());

			frame.addComponentListener(
					new ComponentListener() {

						public void componentShown(ComponentEvent e) {
						}

						public void componentResized(ComponentEvent e) {
							PersistentMemory.setFrameWidth(frame.getWidth());
							PersistentMemory.setFrameHeight(frame.getHeight());
							PersistentMemory.setFrameX(frame.getX());
							PersistentMemory.setFrameY(frame.getY());
						}

						public void componentMoved(ComponentEvent e) {
							PersistentMemory.setFrameX(frame.getX());
							PersistentMemory.setFrameY(frame.getY());
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
//		tabbedPane.addTab(
//				"priv chat", new PrivateChat(null, guiController, ""),
//				false
//		);
//		tabbedPane.addTab("Ai test", new AiRoom().panel1, true);

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
					JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=\"\">Priymak PointsAI - о программе</a></html>"
					);
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									try {
										java.awt.Desktop.getDesktop().browse(
												new URI(
														"http://sites.google.com/site/priymakpoints/intelligence"
												)
										);
									} catch (Exception ignored) {
									}
								}
							}
					);
					jMenu.add(jMenuItem);
				}

				{
					final JMenuItem jMenuItem = new JMenuItem("Priymak PointsAI - 1.08");
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									AiVirtualServer aiWrapper =
											new AiVirtualServer(
													guiController
											);
									aiWrapper.setAi(
											new com.google.sites.priymakpoints.pointsai.pointsAI_1_08.PointsAIEngine(
													aiWrapper, 39, 32
											)
									);
									aiWrapper.init();
								}
							}
					);
					jMenu.add(jMenuItem);
				}

				{
					final JMenuItem jMenuItem = new JMenuItem("Priymak PointsAI - 1.07");
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									AiVirtualServer aiWrapper =
											new AiVirtualServer(
													guiController
											);
									aiWrapper.setAi(
											new com.google.sites.priymakpoints.pointsai.pointsAI_1_07.PointsAIEngine(
													aiWrapper, 39, 32
											)
									);
									aiWrapper.init();
								}
							}
					);
					jMenu.add(jMenuItem);
				}

				jMenu.add(new JSeparator());

				{
					final JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=>Keij&Kvanttt AI - помощь в настройке</a></html>"
					);
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									try {
										java.awt.Desktop.getDesktop().browse(
												new URI(
														"http://vkontakte.ru/topic-21455903_24989187"
												)
										);
									} catch (Exception ignored) {
									}
								}
							}
					);
					jMenu.add(jMenuItem);
				}

				{
					final JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=>Keij&Kvanttt AI - ссылка для скачки exe-шника</a></html>"
					);
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									try {
										java.awt.Desktop.getDesktop().browse(
												new URI(
														"http://dl.dropbox.com/u/15765203/keijkvantttai.exe"
												)
										);
									} catch (Exception ignored) {
									}
								}
							}
					);
					jMenu.add(jMenuItem);
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
													PersistentMemory.getKeijKvantttAiPath()
											)
									);
									aiWrapper.init();
								}
							}
					);
					jMenuItem_KeijkvantttaiExecute.setEnabled(
							!PersistentMemory.getKeijKvantttAiPath().equals("")
					);
//					/home/u2/Downloads/kkai/PointsConsole/a.out
//
//					jMenu.add(jMenuItem_KeijkvantttaiExecute);
				}

				{
					final JMenuItem jMenuItem = new JMenuItem(
							"Keij&Kvanttt AI - настройки запуска."
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
											PersistentMemory.getKeijKvantttAiPath()
									);
									if (command != null) {
										if (command.endsWith("\\")
												|| command.endsWith("/")
												|| command.endsWith(File.pathSeparator)) {
											command = command + "keijkvantttai.exe";
										}
										PersistentMemory.setKeijKvantttAiPath(command);
										JOptionPane.showMessageDialog(
												null,
												"Путь изменён на " + command + " \n" +
														"Попробуйте теперь запустить ИИ. :)"
										);
										jMenuItem_KeijkvantttaiExecute.setEnabled(
												!PersistentMemory.getKeijKvantttAiPath().equals("")
										);
									}
								}
							}
					);
					jMenu.add(jMenuItem);
					jMenu.add(jMenuItem_KeijkvantttaiExecute);
				}

				jMenu.add(new JSeparator());

				{
					final JMenuItem jMenuItem = new JMenuItem("Рандомный ИИ");
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									// ai as server
									AiVirtualServer aiWrapper =
											new AiVirtualServer(
													guiController
											);
									aiWrapper.setAi(new RandomAi(aiWrapper, 39, 32));
									aiWrapper.init();
								}
							}
					);
					jMenu.add(jMenuItem);
				}

				jMenuBar.add(jMenu);
			}

			{
				JMenu jMenu = new JMenu("Настройки");
				{
					JMenuItem jMenuItem = new JMenuItem("настройки");
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									SettingsPanel settings = new SettingsPanel();
									tabbedPane.addTab("настройки", settings, true);
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
				{
					JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=\"\">правила игры Точки</a></html>"
					);
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									try {
										java.awt.Desktop.getDesktop().browse(
												new URI(
														"http://pointsgame.net/drupal6/node/4"
												)
										);
									} catch (Exception ignored) {
									}
								}
							}
					);
					jMenu.add(jMenuItem);
				}
				{
					JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=\"\">о программе PointsOP</a></html>"
					);
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									try {
										java.awt.Desktop.getDesktop().browse(
												new URI(
														"http://vkontakte.ru/club21455903"
												)
										);
									} catch (Exception ignored) {
									}
								}
							}
					);
					jMenu.add(jMenuItem);
				}

				{
					JMenuItem jMenuItem = new JMenuItem(
							"<html><a href=\"\">полезные ссылки</a></html>"
					);
					jMenuItem.addActionListener(
							new ActionListener() {

								public void actionPerformed(ActionEvent e) {
									try {
										java.awt.Desktop.getDesktop().browse(
												new URI(
														"http://sites.google.com/site/oscarpoints/links"
												)
										);
									} catch (Exception ignored) {
									}
								}
							}
					);
					jMenu.add(jMenuItem);
				}
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
