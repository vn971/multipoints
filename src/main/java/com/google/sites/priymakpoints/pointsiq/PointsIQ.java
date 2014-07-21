package com.google.sites.priymakpoints.pointsiq;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;
import ru.narod.vn91.pointsop.gui.Paper;

import javax.jnlp.FileSaveService;
import javax.jnlp.ServiceManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.util.EventListener;

public class PointsIQ extends javax.swing.JPanel {

	private final JButton butText;
	private final JButton butNext;
	private final JButton butRePlay;
	private final Question[] base;
	private Question question;
	private int curQuestion = 0;
	private int curLevel = 0;
	private final int squareSize = 16;
	private int qTrue = 0;
	private int qThis = 0;
	private String preLevels = "";
	private Point AIAnswer = null;
	private boolean isComplete = true, isRePlay = false;
	private final QuestionIO io = new QuestionIO();
	private final JLabel labelCoordinates = new JLabel();
	private final Paper paper;

	{
		labelCoordinates.setBounds(290, 360, 70, 20);
		this.add(labelCoordinates);
		butText = getButton(350, 10, 240, 340, "", null);
		butText.setEnabled(false);
		butText.setForeground(Color.black);
		butText.setBackground(Color.white);
		butNext = getButton(10, 360, 130, 20, "Продолжить", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				nextQuestion();
			}
		});
		butRePlay = getButton(150, 360, 130, 20, "Повторить", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				preQuestion();
			}
		});
		getButton(350, 360, 240, 20, "Экспорт в .sgf", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String content = "(;FF[4]GM[40]CA[UTF-8]AP[PointsIQ]SZ[20]RU[Punish=0,Holes=1,AddTurn=0,MustSurr=1,MinArea=1,Pass=0,Stop=0,LastSafe=0,ScoreTerr=0,InstantWin=0]PB[blue]PW[red]";
				String str = question.startPos;
				String move;
				MoveType moveType;
				while (str.length() > 1) {
					move = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
					str = str.substring(str.indexOf("]") + 1);
					int x = new Integer(move.substring(0, move.indexOf(",")));
					move = move.substring(move.indexOf(",") + 1);
					int y = new Integer(move.substring(0, move.indexOf(",")));
					move = move.substring(move.indexOf(",") + 1);
					if (move.equals("R")) {
						moveType = MoveType.RED;
					} else {
						moveType = MoveType.BLUE;
					}
					content += ";" + ((moveType == MoveType.RED) ? "W" : "B");
					content += "[" + getSgfCoord(x) + "" + getSgfCoord(20 + 1 - y) + "]";
				}
				content += ")";
				String[] extensions = {".sgf", ".sgftochki"};
				try {
					ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
					FileSaveService fss = (FileSaveService) ServiceManager.lookup("javax.jnlp.FileSaveService");
					fss.saveFileDialog("", extensions, is, "");
				} catch (Exception exc) {
					System.err.println("Error: " + exc.getMessage());
					System.out.println(content);
				}
			}
		});
	}

	public PointsIQ(int level) {
		this.setLayout(new C_Layout(595, 505));
		base = io.getBase(level);

		paper = new Paper() {
			public void paperClick(int x, int y, MouseEvent evt) {
			}

			public void paperMouseMove(int x, int y, MouseEvent evt) {
			}
		};
		newGame();

		JPanel jPanel_Paper = paper;
		jPanel_Paper.setBounds(0, 10, 340, 340);
		this.add(jPanel_Paper);
		jPanel_Paper.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent me) {
				int x = getMouseClickX(me);
				int y = getMouseClickY(me);
				if (x > 0 & y < (20 + 1) & y > 0 & y < (20 + 1))
					labelCoordinates.setText("<HTML><font color=gray>" + x + ":" + y);
			}

			public void mouseDragged(MouseEvent e) {
			}
		});
		jPanel_Paper.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent me) {
				int x = getMouseClickX(me);
				int y = getMouseClickY(me);

				if (!isComplete) {
					if (makeMove(x, y, false) != MoveResult.ERROR) {
						if (isExistsLevelMove(x, y)) {
							if (makeMove(AIAnswer.x, AIAnswer.y, true) == MoveResult.ERROR) {
								isComplete = true;
								if (!isRePlay) {
									qTrue++;
									qThis++;
								}
								butText.setText("<html><font color=green>Задание " + qThis + " выполнено правильно!");
								butNext.setEnabled(true);
							}
						}
					}
				}
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseClicked(MouseEvent arg0) {
			}
		});

		butNext.setEnabled(false);
		butRePlay.setEnabled(false);
		nextQuestion();
	}

	private void nextQuestion() {
		curLevel = 0;
		preLevels = "";
		AIAnswer = null;
		butNext.setEnabled(false);
		butRePlay.setEnabled(false);

		if (curQuestion == base.length) {
			JOptionPane.showMessageDialog(null, "<html>Тест пройден.<br>" +
				"Ваш результат " + qTrue + " правильных ответов из " + qThis);
		} else if (curQuestion < base.length) {
			int i = curQuestion;
			curQuestion = i + 1;
			question = io.getQuestion(base[i].index);
			newGame();
			isComplete = false;
			isRePlay = false;
			showQuestion();
		}
	}

	private void preQuestion() {
		curLevel = 0;
		preLevels = "";
		AIAnswer = null;
		butNext.setEnabled(false);
		butRePlay.setEnabled(false);
		question = io.getQuestion(base[curQuestion - 1].index);
		newGame();
		isComplete = false;
		isRePlay = true;
		showQuestion();
	}

	private boolean isExistsLevelMove(int x, int y) {
		Question.Makros.MakrosLevelMove moves[] = question.makros.getMoves();
		for (Question.Makros.MakrosLevelMove move : moves) {
			if (move.levelNumber == (curLevel + 1)) {
				if ((move.humanPoint.x) == x & (move.humanPoint.y) == y & preLevels.equals(move.preLevels)) {
					AIAnswer = new Point(move.AIPoint.x, move.AIPoint.y);
					preLevels = move.preLevels + move.levelLetter;
					curLevel++;
					return true;
				}
			}
		}
		isComplete = true;
		if (!isRePlay) {
			qThis++;
		}
		butText.setText("<html><font color=red>Задание " + qThis + " выполнено неверно!<br><font color=black>Как правильно выполнить задание:<br><font color=blue>" + question.comment);
		butNext.setEnabled(true);
		butRePlay.setEnabled(true);
		return false;
	}

	private void showQuestion() {
		butText.setText("<html>Текст задания " + (qThis + 1) + ":<br><font color=blue>" + question.text);
		String str = question.startPos;
		String move;
		while (str.length() > 1) {
			move = str.substring(str.indexOf("[") + 1, str.indexOf("]"));
			str = str.substring(str.indexOf("]") + 1);
			int x = new Integer(move.substring(0, move.indexOf(",")));
			move = move.substring(move.indexOf(",") + 1);
			int y = new Integer(move.substring(0, move.indexOf(",")));
			move = move.substring(move.indexOf(",") + 1);
			if (move.equals("R")) {
				makeMove(x, y, true);
			} else {
				makeMove(x, y, false);
			}
		}
	}

	private JButton getButton(int x, int y, int width, int height, String strText, EventListener listener) {
		JButton button = new JButton(strText);
		button.addActionListener((ActionListener) listener);
		button.setBounds(x, y, width, height);
		this.add(button);
		return button;
	}

	private class C_Layout implements LayoutManager {
		final int width;
		final int height;

		public C_Layout(int width, int height) {
			this.width = width;
			this.height = height;
		}

		public void addLayoutComponent(String name, Component comp) {
		}

		public void removeLayoutComponent(Component comp) {
		}

		public Dimension preferredLayoutSize(Container parent) {
			return new Dimension(width, height);
		}

		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(width, height);
		}

		public void layoutContainer(Container parent) {
		}
	}

	private String getSgfCoord(int i) {
		if (i <= 26) {
			return Character.toString((char) ((int) 'a' + i - 1));
		} else {
			return Character.toString((char) ((int) 'A' + i - 26 - 1));
		}
	}

	int getMouseClickX(MouseEvent me) {
		int offsetX = 1;
		return (int) (((double) me.getX() - 4 - (double) ((offsetX - 1) * squareSize)) / (double) squareSize);
	}

	int getMouseClickY(MouseEvent me) {
		int offsetY = 0;
		return 21 - (int) (((double) me.getY() - 8 - (double) ((offsetY - 1) * squareSize)) / (double) squareSize);
	}

	void newGame() {
		paper.initPaper(20, 20, false);
	}

	MoveResult makeMove(int x, int y, boolean isRed) {
		return paper.makeMove(x, y, isRed);
	}
}
