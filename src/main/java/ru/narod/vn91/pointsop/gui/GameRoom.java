package ru.narod.vn91.pointsop.gui;

import java.awt.HeadlessException;
import ru.narod.vn91.pointsop.data.GameInfoListener;
import ru.narod.vn91.pointsop.data.GameOuterInfo;
import ru.narod.vn91.pointsop.data.Player;
import ru.narod.vn91.pointsop.data.Sgf;
import ru.narod.vn91.pointsop.data.TimeLeft;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveInfoAbstract;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;
import ru.narod.vn91.pointsop.model.GuiController;
import ru.narod.vn91.pointsop.model.GuiForServerInterface;
import ru.narod.vn91.pointsop.server.irc.BackupToServer;
import ru.narod.vn91.pointsop.server.ServerInterface;
import ru.narod.vn91.pointsop.sounds.Sounds;
import ru.narod.vn91.pointsop.utils.Function2;
import ru.narod.vn91.pointsop.utils.Settings;
import ru.narod.vn91.pointsop.utils.ObjectKeeper;
import ru.narod.vn91.pointsop.utils.TimedAction;

import javax.jnlp.FileSaveService;
import javax.jnlp.ServiceManager;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import ru.narod.vn91.pointsop.data.PlayerChangeListener;

@SuppressWarnings("serial")
public class GameRoom extends javax.swing.JPanel implements RoomInterface {

	GuiForServerInterface centralGuiController;
	final GameOuterInfo gameOuterInfo;
	boolean redStopped = false, blueStopped = false;
	ArrayList<MoveInfoAbstract> moveList = new ArrayList<MoveInfoAbstract>();
	int mousePosX, mousePosY, redScore, blueScore;
	boolean isRedTurnNow = true;
	Sgf.GameResult gameResult = Sgf.GameResult.UNFINISHED;
	Paper paper;
	TimerLabel timerLabel1;
	TimerLabel timerLabel2;
	boolean timer1Freezed = true;
	boolean timer2Freezed = true;
	Object synchronizationMakeMoveWhereMouse = new Object();
	final ObjectKeeper<TimedAction> timedAction = new ObjectKeeper<TimedAction>();
//	long timeLastTurnHappend;

	public JPanel getMainJPanel() {
		return this;
	}

	public RoomPart_Chat getRoomPart_Chat() {
		return roomPart_Chat;
	}

	public RoomPart_Userlist getRoomPart_UserList() {
		return roomPart_UserList;
	}

	public RoomPart_GameList getRoomPart_GameList() {
		return null;
	}

	public Paper getRoomPart_Paper() {
		return (Paper)jPanel_Paper;
	}

	public PrivateChat getPrivateChat() {
		return null;
	}

	public ServerInterface getServer() {
		return gameOuterInfo.server;
	}

	public String getRoomNameOnServer() {
		return gameOuterInfo.id;
	}

	public boolean userAsksClose() {
		if (gameOuterInfo.server != null) {
			gameOuterInfo.server.unsubscribeRoom(gameOuterInfo.id);
			return false;
		} else {
			return true;
		}
	}

	public void makeMove(
			boolean silent,
			int x,
			int y,
			boolean isRed,
			boolean isNowRed
			) {
		int yInGui = gameOuterInfo.server.isIncomingYInverted()
			? gameOuterInfo.sizeY + 1 - y
			: y;
		timedAction.value = null;
		MoveResult moveResult = paper.makeMove(silent, x, yInGui, isRed);
		if (moveResult != MoveResult.ERROR) {
			MoveInfoAbstract moveInfoAbstract = new MoveInfoAbstract();
			moveInfoAbstract.coordX = x;
			moveInfoAbstract.coordY = y;
			moveInfoAbstract.moveType = (isRed) ? MoveType.RED : MoveType.BLUE;
			moveList.add(moveInfoAbstract);
			if (silent == false) {
				updateScore();
				Sounds.playMakeMove(gameOuterInfo.amIPlaying());

				// make move to current mouse position
				if (isOnlineGame()
						&& Settings.isDebug() == true
						&& gameOuterInfo.amIPlaying() == true
						&& isRed != gameOuterInfo.amIRed()) {
//					final int moveListSize = moveList.size();
					timedAction.value = new TimedAction() {

						@Override
						public boolean isAlive() {
							return timedAction.value == this;
						}

						@Override
						public void run() {
//							if (paper.getEngine().getDotType(mousePosX, mousePosY).isEmpty()
//									&&moveListSize == moveList.size()) {
//								gameOuterInfo.server.makeMove(gameOuterInfo.id, mousePosX, mousePosY);
//							}
						}

					};
//					int secondsRemaining = gameOuterInfo.amIRed() ? remainingTimeRed : remainingTimeBlue;
//					timedAction.value.executeWhen(new Date().getTime()
//							+ secondsRemaining * 1000L - 200);
				}
			}
		}
	}

	public void updateTime(TimeLeft timeLeft) {
		if (timeLeft.countsDown1 != null) {
			this.timer1Freezed = !timeLeft.countsDown1;
		}
		if (timeLeft.countsDown2 != null) {
			this.timer2Freezed = !timeLeft.countsDown2;
		}
		if (timeLeft.timeLeft1!=null) {
		timerLabel1.setRemainingTime(timeLeft.timeLeft1, timer1Freezed);
		}
		if (timeLeft.timeLeft2!=null) {
			timerLabel2.setRemainingTime(timeLeft.timeLeft2, timer2Freezed);
		}
	}

	public void stopGame(boolean isRedPlayer) {
		String whoPressedStop = (isRedPlayer)
			? gameOuterInfo.firstGuiNameFailsafe()
			: gameOuterInfo.secondGuiNameFailsafe();
		if (isRedPlayer) {
			redStopped = true;
		} else {
			blueStopped = true;
		}
		if (redStopped && blueStopped) {
			if (redScore > blueScore) {
				gameResult = Sgf.GameResult.RED_WON_END_OF_GAME;
			} else if (redScore > blueScore) {
				gameResult = Sgf.GameResult.BLUE_WON_END_OF_GAME;
			} else if (redScore == blueScore) {
				gameResult = Sgf.GameResult.DRAW_END_OF_GAME;
			}
			roomPart_Chat.addServerNotice(
					"Оба игрока нажали кнопку СТОП. Игра закончена...");
		} else {
			roomPart_Chat.addServerNotice(whoPressedStop + " нажал кнопку СТОП");
		}
	}

	public void gameLost(final boolean isRedLooser,
			final boolean wantToSave) {
		gameResult = (isRedLooser) ? Sgf.GameResult.BLUE_WON_BY_RESIGN : Sgf.GameResult.RED_WON_BY_RESIGN;
		final String whoWon = (isRedLooser) 
			? gameOuterInfo.secondGuiNameFailsafe()
			: gameOuterInfo.firstGuiNameFailsafe();
		String whoLost = (isRedLooser) 
			? gameOuterInfo.firstGuiNameFailsafe()
			: gameOuterInfo.secondGuiNameFailsafe();
		roomPart_Chat.addServerNotice(
				"" + whoLost + " сдался... Победитель - " + whoWon + ".");

		new Thread() {
			@Override
			public void run() {
				String eidokropkiLink = "";
				if ((gameOuterInfo.isRated == true) && (wantToSave == true)) {
					eidokropkiLink = BackupToServer.sendToEidokropki(
						gameOuterInfo.first.guiName, gameOuterInfo.second.guiName,
						gameOuterInfo.first.rating, gameOuterInfo.second.rating,
						gameOuterInfo.sizeX, gameOuterInfo.sizeY,
						gameOuterInfo.getTimeAsString(), gameResult, moveList);
					getServer().sendChat(gameOuterInfo.server.getMainRoom(),
							"Закончена игра " + gameOuterInfo.first.guiName + "-" + gameOuterInfo.second.guiName
							+ " ( " + eidokropkiLink + " ), победитель - " + whoWon + ". Поздравляем!");
				}
			}
		}.start();

	}

	public void disconnectedFromServer() {
		jButton_Actions.setEnabled(false);
		jLabel_Avatar1.setEnabled(false);
		jLabel_Avatar2.setEnabled(false);
		jLabel_MouseCoords.setEnabled(false);
		jLabel_Name1.setEnabled(false);
		jLabel_Name2.setEnabled(false);
		jLabel_Score1.setEnabled(false);
		jLabel_Score2.setEnabled(false);
		jLabel_Time1.setEnabled(false);
		jLabel_Time2.setEnabled(false);
		jPanel_Bottom.setEnabled(false);
		jPanel_ForGame.setEnabled(false);
		jPanel_Paper.setEnabled(false);
		jPanel_Players.setEnabled(false);
		roomPart_UserList.setEnabled(false);
		roomPart_Chat.setReadOnly(true);
		roomPart_Chat.addChat("", "disconnected from server", 0L);
	}

	public void paperClick(
			int x,
			int y,
			MouseEvent evt) {
		if (gameOuterInfo.amIPlaying() && evt.getButton() == MouseEvent.BUTTON1) {
			if (isOnlineGame()) {
				gameOuterInfo.server.makeMove(gameOuterInfo.id, x, y);
			} else {
				makeMove(false, x, y, isRedTurnNow, !isRedTurnNow);
				isRedTurnNow = !isRedTurnNow;
			}
		}
	}

	public void paperMouseMove(int x,
			int y,
			MouseEvent evt) {
		mousePosX = x;
		mousePosY = y;
		updateCoordinates();
	}

	private boolean isOnlineGame() {
		return gameOuterInfo.server!=null;
	}

	private void updateScore() {
		jLabel_Score1.setText(String.format(
				"<html><font color=%s>●%s●</font></html>",
				GuiCommon.getHtmlColor(gameOuterInfo.player1Color()),
				gameOuterInfo.isRedFirst ? paper.getRedScore() : paper.getBlueScore()
					));
		jLabel_Score2.setText(String.format(
				"<html><font color=%s>●%s●</font></html>",
				GuiCommon.getHtmlColor(gameOuterInfo.player2Color()),
				gameOuterInfo.isRedFirst ? paper.getBlueScore() : paper.getRedScore()
				));
	}
	
	private void updateCoordinates() {
		if (mousePosX != -1) {
			int xGui = mousePosX;
			int yGui = gameOuterInfo.server.isGuiYInverted()
				? gameOuterInfo.sizeY + 1 - mousePosY
				: mousePosY;
		jLabel_MouseCoords.setText(
				"["+
				gameOuterInfo.server.coordinatesToString(xGui, yGui) +
				"]");
		} else {
			jLabel_MouseCoords.setText("");
		}
	}
	
//	private void showScoreAndCursor() {
//		String result = "<html>";
//		if (mousePosX != -1) {
//			int xGui = mousePosX;
//			int yGui = gameOuterInfo.server.isGuiYInverted()
//				? gameOuterInfo.sizeY + 1 - mousePosY
//				: mousePosY;
//			result += "<b>[";
//			result += gameOuterInfo.server.coordinatesToString(xGui, yGui);
////			result += gameOuterInfo.server.coordinateToString(xGui, yGui);
//			result += "]</b> ";
//		}
//		result += String.format("счёт <b><font color=red>%s</font>"
//				+ "-<font color=blue>%s</font></b>", paper.getRedScore(),
//				paper.getBlueScore());
//		result += ", ход " + (moveList.size() + 1);
//		result += "</html>";
//		jLabel_MouseCoords.setText(result);
//
//	}

	/** Creates new form ContainerRoom_Game */
	public GameRoom(
			final GameOuterInfo gameOuterInfo,
			GuiController centralGuiController) {
		this.centralGuiController = centralGuiController;
		this.gameOuterInfo = gameOuterInfo;
		paper = new Paper() {

			@Override
			public void paperClick(int x,
					int y,
					MouseEvent evt) {
				int yInGui = gameOuterInfo.server.isIncomingYInverted()
					? gameOuterInfo.sizeY + 1 - y
					: y;
				GameRoom.this.paperClick(x, yInGui, evt);
			}

			@Override
			public void paperMouseMove(int x,
					int y,
					MouseEvent evt) {
//				int yInGui = gameOuterInfo.yAxisInverted
//				? gameOuterInfo.sizeY + 1 - y
//					: y;
				GameRoom.this.paperMouseMove(x, y, evt);
			}
		};
		paper.setCoordinatesFormatter(new Function2<Integer, Integer, String>() {
			@Override
			public String call(Integer x, Integer y) {
//				int xGui = x;
//				int yGui = gameOuterInfo.server.isGuiYInverted()
//						? gameOuterInfo.sizeY + 1 - y
//						: y;
				return gameOuterInfo.server.coordinatesToString(x, y);
			}
		});
		paper.initPaper(gameOuterInfo.sizeX, gameOuterInfo.sizeY, gameOuterInfo.server.isGuiYInverted());
		timerLabel1 = new TimerLabel();
		timerLabel2 = new TimerLabel();

		initComponents();

		if (gameOuterInfo.first != null && gameOuterInfo.first.id != null) {
			PlayerChangeListener player1ChangeListener = new PlayerChangeListener() {
				public void onChange(Player player) {
					jLabel_Name1.setText(String.format(
						"<html><font color=%s>%s</font></html>",
						GuiCommon.getHtmlColor(gameOuterInfo.player1Color()),
						gameOuterInfo.first.guiName));
					updateScore();
					jLabel_Avatar1.setIcon(gameOuterInfo.first.imageIcon);
				}
			};
			player1ChangeListener.onChange(gameOuterInfo.first);
			gameOuterInfo.first.addChangeListener(player1ChangeListener);
			gameOuterInfo.server.getUserpic(gameOuterInfo.first.id);
		}

		if (gameOuterInfo.second != null && gameOuterInfo.second.id != null) {
			PlayerChangeListener player2ChangeListener = new PlayerChangeListener() {
				public void onChange(Player player) {
					jLabel_Name2.setText(String.format(
						"<html><font color=%s>%s</font></html>",
						GuiCommon.getHtmlColor(gameOuterInfo.player2Color()),
						gameOuterInfo.second.guiName));
					updateScore();
					jLabel_Avatar2.setIcon(gameOuterInfo.second.imageIcon);
				}
			};
			player2ChangeListener.onChange(gameOuterInfo.second);
			gameOuterInfo.second.addChangeListener(player2ChangeListener);
			gameOuterInfo.server.getUserpic(gameOuterInfo.second.id);
		}

		roomPart_Chat.setReadOnly(false);
		roomPart_Chat.initChat(this,gameOuterInfo);
		// (gameOuterInfo.first==null) ? "" : gameOuterInfo.first.guiName,
		// (gameOuterInfo.second==null) ? "" : gameOuterInfo.second.guiName);
		// if (amIPlaying == false) {
		// jButton_AdditionalActions.setVisible(false);
		// }
//		Integer i = 
//		jSplitPane_Global.setDividerLocation(this.getWidth() - (int)jSplitPane_ChatUsers.getPreferredSize().getWidth());

		GameRoom this_copy = this; // netbeans debug
		roomPart_UserList.initRoomPart(this_copy, centralGuiController);
		
		GameInfoListener gameInfoListener = new GameInfoListener() {
			@Override
			public void onChange(GameOuterInfo gameOuterInfo) {
				String instantWin =
						gameOuterInfo.instantWin == 0
							? ""
							: "немедленный выигрыш при разнице в " + gameOuterInfo.instantWin + " очков, ";
				String message = String.format(
					"Правила игры: " +
						"основное время %s сек, " +
						"добавочное за ход %s сек, " +
						"%s" +
						"%s, " +
						"размер поля %s*%s, " +
						"СТОП %s.",
					gameOuterInfo.startingTime,
					gameOuterInfo.additionalAccumulatingTime,
					instantWin,
					gameOuterInfo.isRated ? "рейтинговая" : "нерейтинговая",
					gameOuterInfo.sizeX, gameOuterInfo.sizeY,
					gameOuterInfo.stopEnabled ? "разрешён" : "запрещён"
						);
				roomPart_Chat.addServerNotice(message);
			}
		};
		gameOuterInfo.addChangeListener(gameInfoListener);
		gameInfoListener.onChange(gameOuterInfo); // invoke change
//		disconnectedFromServer();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu_Actions = new javax.swing.JPopupMenu();
        jMenuItem_Surrender = new javax.swing.JMenuItem();
        jMenuItem_Stop = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem_AskNewGame = new javax.swing.JMenuItem();
        jMenuItem_CancelAskingNewGame = new javax.swing.JMenuItem();
        jMenuItem_AcceptNewGame = new javax.swing.JMenuItem();
        jMenuItem_RejectNewGame = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem_AskEndGameAndScore = new javax.swing.JMenuItem();
        jMenuItem_CancelAskingEndGameAndScore = new javax.swing.JMenuItem();
        jMenuItem_AcceptEndGameAndScore = new javax.swing.JMenuItem();
        jMenuItem_RejectEndGameAndScore = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jMenuItem_AskUndo = new javax.swing.JMenuItem();
        jMenuItem_CancelAskingUndo = new javax.swing.JMenuItem();
        jMenuItem_AcceptUndo = new javax.swing.JMenuItem();
        jMenuItem_RejectUndo = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        jMenuItem_AskDraw = new javax.swing.JMenuItem();
        jMenuItem_CancelAskingDraw = new javax.swing.JMenuItem();
        jMenuItem_AcceptDraw = new javax.swing.JMenuItem();
        jMenuItem_RejectDraw = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem_PauseOpponentTime = new javax.swing.JMenuItem();
        jMenuItem_UnpauseOpponentTime = new javax.swing.JMenuItem();
        jMenuItem_AddOpponentTime = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem_SaveGame = new javax.swing.JMenuItem();
        jSplitPane_Global = new javax.swing.JSplitPane();
        jPanel_ForGame = new javax.swing.JPanel();
        jPanel_Paper = paper;
        jPanel_ChatUsers = new javax.swing.JPanel();
        jSplitPane_ChatUsers = new javax.swing.JSplitPane();
        roomPart_Chat = new ru.narod.vn91.pointsop.gui.RoomPart_Chat();
        jPanel_PlayersAndUserList = new javax.swing.JPanel();
        roomPart_UserList = new ru.narod.vn91.pointsop.gui.RoomPart_Userlist();
        jPanel_Players = new javax.swing.JPanel();
        jPanel_Player1A = new javax.swing.JPanel();
        jLabel_Name1 = new javax.swing.JLabel();
        jPanel_Player1B = new javax.swing.JPanel();
        jLabel_Time1 = timerLabel1;
        jLabel_Score1 = new javax.swing.JLabel();
        jLabel_Avatar1 = new javax.swing.JLabel();
        jPanel_Player2A = new javax.swing.JPanel();
        jLabel_Name2 = new javax.swing.JLabel();
        jPanel_Player2B = new javax.swing.JPanel();
        jLabel_Time2 = timerLabel2;
        jLabel_Score2 = new javax.swing.JLabel();
        jLabel_Avatar2 = new javax.swing.JLabel();
        jPanel_Bottom = new javax.swing.JPanel();
        jLabel_MouseCoords = new javax.swing.JLabel();
        jButton_Actions = new javax.swing.JButton();

        jMenuItem_Surrender.setText("Сдаться");
        jMenuItem_Surrender.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_SurrenderActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_Surrender);

        jMenuItem_Stop.setText("СТОП (отказаться от дальнейшей постановки точек)");
        jMenuItem_Stop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_StopActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_Stop);
        jPopupMenu_Actions.add(jSeparator1);

        jMenuItem_AskNewGame.setText("Предложить начать новую игру");
        jMenuItem_AskNewGame.setEnabled(false);
        jMenuItem_AskNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AskNewGameActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_AskNewGame);

        jMenuItem_CancelAskingNewGame.setText("Отменить предложение новой игры");
        jMenuItem_CancelAskingNewGame.setEnabled(false);
        jMenuItem_CancelAskingNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_CancelAskingNewGameActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_CancelAskingNewGame);

        jMenuItem_AcceptNewGame.setText("Согласиться начать новую игру");
        jMenuItem_AcceptNewGame.setEnabled(false);
        jMenuItem_AcceptNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AcceptNewGameActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_AcceptNewGame);

        jMenuItem_RejectNewGame.setText("Отклонить предложение новой игры");
        jMenuItem_RejectNewGame.setEnabled(false);
        jMenuItem_RejectNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_RejectNewGameActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_RejectNewGame);
        jPopupMenu_Actions.add(jSeparator2);

        jMenuItem_AskEndGameAndScore.setText("Предложить закончить игру с подсчётом точек");
        jMenuItem_AskEndGameAndScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AskEndGameAndScoreActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_AskEndGameAndScore);

        jMenuItem_CancelAskingEndGameAndScore.setText("Отменить предложение конца игры и подсчёта точек");
        jMenuItem_CancelAskingEndGameAndScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_CancelAskingEndGameAndScoreActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_CancelAskingEndGameAndScore);

        jMenuItem_AcceptEndGameAndScore.setText("Согласиться на конец игры и подсчёт точек");
        jMenuItem_AcceptEndGameAndScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AcceptEndGameAndScoreActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_AcceptEndGameAndScore);

        jMenuItem_RejectEndGameAndScore.setText("Отказаться от оканчивания игры и подсчёта точек");
        jMenuItem_RejectEndGameAndScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_RejectEndGameAndScoreActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_RejectEndGameAndScore);
        jPopupMenu_Actions.add(jSeparator3);

        jMenuItem_AskUndo.setText("Попросить взять ход назад");
        jMenuItem_AskUndo.setEnabled(false);
        jMenuItem_AskUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AskUndoActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_AskUndo);

        jMenuItem_CancelAskingUndo.setText("Отменить запрос на возврат хода");
        jMenuItem_CancelAskingUndo.setEnabled(false);
        jMenuItem_CancelAskingUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_CancelAskingUndoActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_CancelAskingUndo);

        jMenuItem_AcceptUndo.setText("Согласиться на возврат хода");
        jMenuItem_AcceptUndo.setEnabled(false);
        jMenuItem_AcceptUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AcceptUndoActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_AcceptUndo);

        jMenuItem_RejectUndo.setText("Отклонить запрос на возврат хода");
        jMenuItem_RejectUndo.setEnabled(false);
        jMenuItem_RejectUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_RejectUndoActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_RejectUndo);
        jPopupMenu_Actions.add(jSeparator4);

        jMenuItem_AskDraw.setText("Предложить ничью");
        jMenuItem_AskDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AskDrawActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_AskDraw);

        jMenuItem_CancelAskingDraw.setText("Отменить предложение на ничью");
        jMenuItem_CancelAskingDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_CancelAskingDrawActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_CancelAskingDraw);

        jMenuItem_AcceptDraw.setText("Согласиться на ничью");
        jMenuItem_AcceptDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AcceptDrawActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_AcceptDraw);

        jMenuItem_RejectDraw.setText("Отклонить предложение на ничью");
        jMenuItem_RejectDraw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_RejectDrawActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_RejectDraw);
        jPopupMenu_Actions.add(jSeparator5);

        jMenuItem_PauseOpponentTime.setText("Остановить время оппонента");
        jMenuItem_PauseOpponentTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_PauseOpponentTimeActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_PauseOpponentTime);

        jMenuItem_UnpauseOpponentTime.setText("Продолжить течение времени оппонента");
        jMenuItem_UnpauseOpponentTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_UnpauseOpponentTimeActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_UnpauseOpponentTime);

        jMenuItem_AddOpponentTime.setText("Добавить оппоненту времени (ввод)");
        jMenuItem_AddOpponentTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_AddOpponentTimeActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_AddOpponentTime);
        jPopupMenu_Actions.add(jSeparator6);

        jMenuItem_SaveGame.setText("Сохранить игру");
        jMenuItem_SaveGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_SaveGameActionPerformed(evt);
            }
        });
        jPopupMenu_Actions.add(jMenuItem_SaveGame);

        jSplitPane_Global.setResizeWeight(1.0);
        jSplitPane_Global.setContinuousLayout(true);
        jSplitPane_Global.setOneTouchExpandable(true);

        jPanel_ForGame.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        javax.swing.GroupLayout jPanel_PaperLayout = new javax.swing.GroupLayout(jPanel_Paper);
        jPanel_Paper.setLayout(jPanel_PaperLayout);
        jPanel_PaperLayout.setHorizontalGroup(
            jPanel_PaperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 386, Short.MAX_VALUE)
        );
        jPanel_PaperLayout.setVerticalGroup(
            jPanel_PaperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel_ForGameLayout = new javax.swing.GroupLayout(jPanel_ForGame);
        jPanel_ForGame.setLayout(jPanel_ForGameLayout);
        jPanel_ForGameLayout.setHorizontalGroup(
            jPanel_ForGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Paper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel_ForGameLayout.setVerticalGroup(
            jPanel_ForGameLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Paper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSplitPane_Global.setLeftComponent(jPanel_ForGame);

        jSplitPane_ChatUsers.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane_ChatUsers.setResizeWeight(0.5);
        jSplitPane_ChatUsers.setContinuousLayout(true);
        jSplitPane_ChatUsers.setLastDividerLocation(599);
        jSplitPane_ChatUsers.setRightComponent(roomPart_Chat);

        jPanel_Player1A.setPreferredSize(new java.awt.Dimension(110, 357));

        jLabel_Name1.setText("игрок1");
        jLabel_Name1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel_Name1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);

        jLabel_Time1.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 15));
        jLabel_Time1.setText("00:00");

        jLabel_Score1.setText("●11●");

        jLabel_Avatar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/narod/vn91/pointsop/gui/zagram-avatar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel_Player1BLayout = new javax.swing.GroupLayout(jPanel_Player1B);
        jPanel_Player1B.setLayout(jPanel_Player1BLayout);
        jPanel_Player1BLayout.setHorizontalGroup(
            jPanel_Player1BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Player1BLayout.createSequentialGroup()
                .addComponent(jLabel_Avatar1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel_Player1BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_Score1)
                    .addComponent(jLabel_Time1)))
        );
        jPanel_Player1BLayout.setVerticalGroup(
            jPanel_Player1BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_Avatar1)
            .addGroup(jPanel_Player1BLayout.createSequentialGroup()
                .addComponent(jLabel_Score1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_Time1))
        );

        javax.swing.GroupLayout jPanel_Player1ALayout = new javax.swing.GroupLayout(jPanel_Player1A);
        jPanel_Player1A.setLayout(jPanel_Player1ALayout);
        jPanel_Player1ALayout.setHorizontalGroup(
            jPanel_Player1ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_Name1, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
            .addComponent(jPanel_Player1B, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel_Player1ALayout.setVerticalGroup(
            jPanel_Player1ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Player1ALayout.createSequentialGroup()
                .addComponent(jLabel_Name1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Player1B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel_Player2A.setPreferredSize(new java.awt.Dimension(110, 357));

        jLabel_Name2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel_Name2.setText("игрок2");
        jLabel_Name2.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        jLabel_Time2.setFont(new java.awt.Font("DejaVu Sans Mono", 0, 15));
        jLabel_Time2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel_Time2.setText("00:00");

        jLabel_Score2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel_Score2.setText("●22●");

        jLabel_Avatar2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/narod/vn91/pointsop/gui/zagram-avatar.png"))); // NOI18N

        javax.swing.GroupLayout jPanel_Player2BLayout = new javax.swing.GroupLayout(jPanel_Player2B);
        jPanel_Player2B.setLayout(jPanel_Player2BLayout);
        jPanel_Player2BLayout.setHorizontalGroup(
            jPanel_Player2BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_Player2BLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel_Player2BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel_Score2)
                    .addComponent(jLabel_Time2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_Avatar2))
        );
        jPanel_Player2BLayout.setVerticalGroup(
            jPanel_Player2BLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_Avatar2)
            .addGroup(jPanel_Player2BLayout.createSequentialGroup()
                .addComponent(jLabel_Score2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel_Time2))
        );

        javax.swing.GroupLayout jPanel_Player2ALayout = new javax.swing.GroupLayout(jPanel_Player2A);
        jPanel_Player2A.setLayout(jPanel_Player2ALayout);
        jPanel_Player2ALayout.setHorizontalGroup(
            jPanel_Player2ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_Name2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
            .addComponent(jPanel_Player2B, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel_Player2ALayout.setVerticalGroup(
            jPanel_Player2ALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_Player2ALayout.createSequentialGroup()
                .addComponent(jLabel_Name2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Player2B, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel_PlayersLayout = new javax.swing.GroupLayout(jPanel_Players);
        jPanel_Players.setLayout(jPanel_PlayersLayout);
        jPanel_PlayersLayout.setHorizontalGroup(
            jPanel_PlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_PlayersLayout.createSequentialGroup()
                .addComponent(jPanel_Player1A, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Player2A, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
        );
        jPanel_PlayersLayout.setVerticalGroup(
            jPanel_PlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_PlayersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel_Player2A, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jPanel_Player1A, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel_MouseCoords.setText("[10:10]");

        jButton_Actions.setText("доп.действия в игре");
        jButton_Actions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton_ActionsMousePressed(evt);
            }
        });
        jButton_Actions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ActionsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_BottomLayout = new javax.swing.GroupLayout(jPanel_Bottom);
        jPanel_Bottom.setLayout(jPanel_BottomLayout);
        jPanel_BottomLayout.setHorizontalGroup(
            jPanel_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_BottomLayout.createSequentialGroup()
                .addComponent(jLabel_MouseCoords)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jButton_Actions))
        );
        jPanel_BottomLayout.setVerticalGroup(
            jPanel_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_BottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jButton_Actions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel_MouseCoords, javax.swing.GroupLayout.DEFAULT_SIZE, 18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel_PlayersAndUserListLayout = new javax.swing.GroupLayout(jPanel_PlayersAndUserList);
        jPanel_PlayersAndUserList.setLayout(jPanel_PlayersAndUserListLayout);
        jPanel_PlayersAndUserListLayout.setHorizontalGroup(
            jPanel_PlayersAndUserListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel_Bottom, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(roomPart_UserList, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
            .addGroup(jPanel_PlayersAndUserListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel_Players, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_PlayersAndUserListLayout.setVerticalGroup(
            jPanel_PlayersAndUserListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_PlayersAndUserListLayout.createSequentialGroup()
                .addComponent(roomPart_UserList, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Bottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(111, 111, 111))
            .addGroup(jPanel_PlayersAndUserListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel_PlayersAndUserListLayout.createSequentialGroup()
                    .addContainerGap(153, Short.MAX_VALUE)
                    .addComponent(jPanel_Players, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jSplitPane_ChatUsers.setLeftComponent(jPanel_PlayersAndUserList);

        javax.swing.GroupLayout jPanel_ChatUsersLayout = new javax.swing.GroupLayout(jPanel_ChatUsers);
        jPanel_ChatUsers.setLayout(jPanel_ChatUsersLayout);
        jPanel_ChatUsersLayout.setHorizontalGroup(
            jPanel_ChatUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 282, Short.MAX_VALUE)
            .addGroup(jPanel_ChatUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel_ChatUsersLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jSplitPane_ChatUsers, javax.swing.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );
        jPanel_ChatUsersLayout.setVerticalGroup(
            jPanel_ChatUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 413, Short.MAX_VALUE)
            .addGroup(jPanel_ChatUsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel_ChatUsersLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(jSplitPane_ChatUsers, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
                    .addGap(0, 0, 0)))
        );

        jSplitPane_Global.setRightComponent(jPanel_ChatUsers);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane_Global, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane_Global, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jMenuItem_SurrenderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_SurrenderActionPerformed
		gameOuterInfo.server.surrender(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_SurrenderActionPerformed

	private void jMenuItem_StopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_StopActionPerformed
		gameOuterInfo.server.stop(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_StopActionPerformed

	private void jMenuItem_AskNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AskNewGameActionPerformed
		gameOuterInfo.server.askNewGame(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_AskNewGameActionPerformed

	private void jMenuItem_CancelAskingNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_CancelAskingNewGameActionPerformed
		gameOuterInfo.server.cancelAskingNewGame(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_CancelAskingNewGameActionPerformed

	private void jMenuItem_AcceptNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AcceptNewGameActionPerformed
		gameOuterInfo.server.acceptNewGame(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_AcceptNewGameActionPerformed

	private void jMenuItem_RejectNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_RejectNewGameActionPerformed
		gameOuterInfo.server.rejectNewGame(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_RejectNewGameActionPerformed

	private void jMenuItem_AskEndGameAndScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AskEndGameAndScoreActionPerformed
		gameOuterInfo.server.askEndGameAndScore(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_AskEndGameAndScoreActionPerformed

	private void jMenuItem_CancelAskingEndGameAndScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_CancelAskingEndGameAndScoreActionPerformed
		gameOuterInfo.server.cancelAskingEndGameAndScore(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_CancelAskingEndGameAndScoreActionPerformed

	private void jMenuItem_AcceptEndGameAndScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AcceptEndGameAndScoreActionPerformed
		gameOuterInfo.server.acceptEndGameAndScore(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_AcceptEndGameAndScoreActionPerformed

	private void jMenuItem_RejectEndGameAndScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_RejectEndGameAndScoreActionPerformed
		gameOuterInfo.server.rejectEndGameAndScore(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_RejectEndGameAndScoreActionPerformed

	private void jMenuItem_AskUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AskUndoActionPerformed
		gameOuterInfo.server.askUndo(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_AskUndoActionPerformed

	private void jMenuItem_CancelAskingUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_CancelAskingUndoActionPerformed
		gameOuterInfo.server.cancelAskingUndo(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_CancelAskingUndoActionPerformed

	private void jMenuItem_AcceptUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AcceptUndoActionPerformed
		gameOuterInfo.server.acceptUndo(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_AcceptUndoActionPerformed

	private void jMenuItem_RejectUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_RejectUndoActionPerformed
		gameOuterInfo.server.rejectUndo(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_RejectUndoActionPerformed

	private void jMenuItem_AskDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AskDrawActionPerformed
		gameOuterInfo.server.askDraw(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_AskDrawActionPerformed

	private void jMenuItem_CancelAskingDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_CancelAskingDrawActionPerformed
		gameOuterInfo.server.cancelAskingDraw(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_CancelAskingDrawActionPerformed

	private void jMenuItem_AcceptDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AcceptDrawActionPerformed
		gameOuterInfo.server.acceptDraw(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_AcceptDrawActionPerformed

	private void jMenuItem_RejectDrawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_RejectDrawActionPerformed
		gameOuterInfo.server.rejectDraw(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_RejectDrawActionPerformed

	private void jMenuItem_PauseOpponentTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_PauseOpponentTimeActionPerformed
		gameOuterInfo.server.pauseOpponentTime(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_PauseOpponentTimeActionPerformed

	private void jMenuItem_UnpauseOpponentTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_UnpauseOpponentTimeActionPerformed
		gameOuterInfo.server.unpauseOpponentTime(gameOuterInfo.id);
	}//GEN-LAST:event_jMenuItem_UnpauseOpponentTimeActionPerformed

	private void jMenuItem_AddOpponentTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_AddOpponentTimeActionPerformed
		try {
			String result = JOptionPane.showInputDialog("Время для добавления оппоненту, секунд:");
			if (result != null && !result.equals("")) {
				int i = Integer.parseInt(result);
				gameOuterInfo.server.addOpponentTime(gameOuterInfo.id, i);
			}
		} catch (NumberFormatException ex) {
		} catch (HeadlessException ex) {
		}
	}//GEN-LAST:event_jMenuItem_AddOpponentTimeActionPerformed

	private void jMenuItem_SaveGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_SaveGameActionPerformed
		String[] extensions = {"sgf", "sgftochki"};
		String sgfData = Sgf.constructSgf(
				gameOuterInfo.firstGuiNameFailsafe(), gameOuterInfo.secondGuiNameFailsafe(),
				gameOuterInfo.first.rating, gameOuterInfo.second.rating, 39, 32,
				gameOuterInfo.getTimeAsString(), gameResult, 0,
				moveList, true);
		try {
			ByteArrayInputStream is = new ByteArrayInputStream(
					sgfData.getBytes());
			FileSaveService fss = (FileSaveService) ServiceManager.lookup("javax.jnlp.FileSaveService");

			fss.saveFileDialog(
					"Vasya - Frosya 2011-04-01", extensions,
					is, "Vasya - Frosya 2011-04-01");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}//GEN-LAST:event_jMenuItem_SaveGameActionPerformed

	private void jButton_ActionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ActionsActionPerformed
//		jPopupMenu_Actions.show(null, evt.ge, WIDTH);
	}//GEN-LAST:event_jButton_ActionsActionPerformed

	private void jButton_ActionsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton_ActionsMousePressed
		jPopupMenu_Actions.show(evt.getComponent(), evt.getX(), evt.getY());
	}//GEN-LAST:event_jButton_ActionsMousePressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Actions;
    private javax.swing.JLabel jLabel_Avatar1;
    private javax.swing.JLabel jLabel_Avatar2;
    private javax.swing.JLabel jLabel_MouseCoords;
    private javax.swing.JLabel jLabel_Name1;
    private javax.swing.JLabel jLabel_Name2;
    private javax.swing.JLabel jLabel_Score1;
    private javax.swing.JLabel jLabel_Score2;
    private javax.swing.JLabel jLabel_Time1;
    private javax.swing.JLabel jLabel_Time2;
    private javax.swing.JMenuItem jMenuItem_AcceptDraw;
    private javax.swing.JMenuItem jMenuItem_AcceptEndGameAndScore;
    private javax.swing.JMenuItem jMenuItem_AcceptNewGame;
    private javax.swing.JMenuItem jMenuItem_AcceptUndo;
    private javax.swing.JMenuItem jMenuItem_AddOpponentTime;
    private javax.swing.JMenuItem jMenuItem_AskDraw;
    private javax.swing.JMenuItem jMenuItem_AskEndGameAndScore;
    private javax.swing.JMenuItem jMenuItem_AskNewGame;
    private javax.swing.JMenuItem jMenuItem_AskUndo;
    private javax.swing.JMenuItem jMenuItem_CancelAskingDraw;
    private javax.swing.JMenuItem jMenuItem_CancelAskingEndGameAndScore;
    private javax.swing.JMenuItem jMenuItem_CancelAskingNewGame;
    private javax.swing.JMenuItem jMenuItem_CancelAskingUndo;
    private javax.swing.JMenuItem jMenuItem_PauseOpponentTime;
    private javax.swing.JMenuItem jMenuItem_RejectDraw;
    private javax.swing.JMenuItem jMenuItem_RejectEndGameAndScore;
    private javax.swing.JMenuItem jMenuItem_RejectNewGame;
    private javax.swing.JMenuItem jMenuItem_RejectUndo;
    private javax.swing.JMenuItem jMenuItem_SaveGame;
    private javax.swing.JMenuItem jMenuItem_Stop;
    private javax.swing.JMenuItem jMenuItem_Surrender;
    private javax.swing.JMenuItem jMenuItem_UnpauseOpponentTime;
    private javax.swing.JPanel jPanel_Bottom;
    private javax.swing.JPanel jPanel_ChatUsers;
    private javax.swing.JPanel jPanel_ForGame;
    private javax.swing.JPanel jPanel_Paper;
    private javax.swing.JPanel jPanel_Player1A;
    private javax.swing.JPanel jPanel_Player1B;
    private javax.swing.JPanel jPanel_Player2A;
    private javax.swing.JPanel jPanel_Player2B;
    private javax.swing.JPanel jPanel_Players;
    private javax.swing.JPanel jPanel_PlayersAndUserList;
    private javax.swing.JPopupMenu jPopupMenu_Actions;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JSplitPane jSplitPane_ChatUsers;
    private javax.swing.JSplitPane jSplitPane_Global;
    private ru.narod.vn91.pointsop.gui.RoomPart_Chat roomPart_Chat;
    private ru.narod.vn91.pointsop.gui.RoomPart_Userlist roomPart_UserList;
    // End of variables declaration//GEN-END:variables
}
