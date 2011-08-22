/*
 * PrivateChat.java
 *
 * Created on Feb 11, 2011, 9:31:17 PM
 */
package ru.narod.vn91.pointsop.gui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import ru.narod.vn91.pointsop.data.Player;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;
import ru.narod.vn91.pointsop.server.ServerInterface;
import ru.narod.vn91.pointsop.sounds.Sounds;

/**
 *
 * @author vasya
 */
public class PrivateChat extends javax.swing.JPanel {

	final Player player;
//	ServerInterface server;
//	GuiForServerInterface guiController;
//	private String companionNick;
	private StyledDocument document = new DefaultStyledDocument();
	boolean lastMessageWasHighlighted = true;
	Date lastPingSent = new Date();
//	SingleGameEngineInterface engine;
	Paper paper;
	boolean lastMoveWasMine;
	static String prefixPointsop = "OpCmd ";
	static String prefixNewGame = "NewGame ";
	static String prefixMakeMove = "MakeMove ";

	public boolean close() {
		return true;
	}

	void addChat(String user,
			String message,
			boolean silent) {
		if (message.equals("/Pong")) {
			int ping = (int)((new Date()).getTime() - lastPingSent.getTime());
			addChat("***",
					"Пинг до пользователя " + player.id + " = " + ping + " миллисекунд.",
					false);
		} else if (message.startsWith(prefixPointsop + prefixMakeMove)) {
			if (lastMoveWasMine) {
				boolean OpponentColor = player.server.getMyName().compareTo(
						player.id) > 0;
				message = message.replaceAll(prefixPointsop + prefixMakeMove, "");
				int x = -1, y = -1;
				try {
					String[] coordinatesAsString = message.split(" ");
					x = Integer.parseInt(coordinatesAsString[0]);
					y = Integer.parseInt(coordinatesAsString[1]);
				} catch (Exception e) {
				}
				MoveResult moveResult = paper.makeMove(false, x, y,
						OpponentColor);
				if (moveResult != MoveResult.ERROR) {
					lastMoveWasMine = false;
				}
			} else {
				// it is my move now.
			}
		} else if (message.startsWith(prefixPointsop + prefixNewGame)) {
			message = message.replaceAll(prefixPointsop + prefixNewGame, "");
			int x = -1, y = -1;
			try {
				String[] coordinatesAsString = message.split(" ");
				x = Integer.parseInt(coordinatesAsString[0]);
				y = Integer.parseInt(coordinatesAsString[1]);
			} catch (Exception e) {
			}
			if ((x > 0) && (y > 0) && (x < 50) && (y < 50)) {
				paper.initPaper(x, y);
				addChat("", "Началась новая игра, размеры поля: " + x + ":" + y,
						false);
				lastMoveWasMine = player.server.getMyName().compareTo(player.id) > 0;
			}
			// ignore unknown pointsOp messages
		} else if (message.startsWith(prefixPointsop)) {
			// ignore unknown pointsOp messages
		} else {
			try {
				if (user.equals(player.server.getMyName())) {
					document.insertString(document.getLength(),
							"" + GlobalGuiSettings.myTimeFormat.format(
							new Date()) + " ",
							GlobalGuiSettings.chatOutgoing);
					document.insertString(document.getLength(), user
							+ ":", GlobalGuiSettings.playerNameOutgoing);
					document.insertString(document.getLength(), " " + message
							+ "\n", GlobalGuiSettings.chatOutgoing);
				} else {
					document.insertString(document.getLength(),
							"" + GlobalGuiSettings.myTimeFormat.format(
							new Date()) + " ",
							GlobalGuiSettings.chatIncoming);
					document.insertString(document.getLength(), user
							+ ":", GlobalGuiSettings.playerNameIncoming);
					document.insertString(document.getLength(), " " + message
							+ "\n", GlobalGuiSettings.chatIncoming);
				}
			} catch (Exception e) {
			}
			jTextPane_Chat.setSelectionStart(jTextPane_Chat.getText().length());
			jTextPane_Chat.setSelectionEnd(jTextPane_Chat.getText().length());
			if (silent == false) {
				Sounds.playReceiveChat();
			}
		}
	}

	/** Creates new form PrivateChat */
	public PrivateChat(
			Player p
//			final ServerInterface server,
//			GuiForServerInterface guiController,
//			final String companionId
			) {
		this.player = p;
//		this.server = server;
//		this.companionNick = companionId;
		if (player.server != null) {
			lastMoveWasMine = player.server.getMyName().compareTo(player.id) > 0;
		} else {
			lastMoveWasMine = false;
		}
//		this.guiController = guiController;
		paper = new Paper() {

			@Override
			public void paperClick(int x,
					int y,
					MouseEvent evt) {
				if (player.id.startsWith("^")) {
					if (lastMoveWasMine) {
						addChat("", "сейчас ход оппонента.", true);
					} else {
						boolean myColor = !(player.server.getMyName().compareTo(
								player.id) > 0);
						MoveResult moveResult = paper.makeMove(false, x, y,
								myColor);
						if (moveResult != MoveResult.ERROR) {
							player.server.sendPrivateMsg(player.id,
									prefixPointsop + prefixMakeMove + x + " " + y);
							lastMoveWasMine = true;
						}
					}
				} else {
					addChat("server",
							"Играть пока-что можно только с другими игроками зашедшими через Op.",
							false);
				}
			}

			@Override
			public void paperMouseMove(int x,
					int y,
					MouseEvent evt) {
			}
		};
		paper.initPaper(30, 30);
		initComponents();
//		if (companionNick.startsWith("^") == false) {
//		}
		jButton_StartNewGame.setVisible(false);
		jPanel_Paper.setVisible(false);
		jTextField_Chat.requestFocusInWindow();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane_Chat = new javax.swing.JScrollPane();
        jTextPane_Chat = new javax.swing.JTextPane();
        jTextPane_Chat.setDocument(document);
        jTextField_Chat = new javax.swing.JTextField();
        jButton_Sound = new javax.swing.JButton();
        jButton_Userinfo = new javax.swing.JButton();
        jButton_Ping = new javax.swing.JButton();
        jPanel_Paper = paper;//(companionNick.startsWith("^")) : new JPanel();
        jButton_StartNewGame = new javax.swing.JButton();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                formComponentAdded(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jTextPane_Chat.setEditable(false);
        jScrollPane_Chat.setViewportView(jTextPane_Chat);

        jTextField_Chat.setFont(new java.awt.Font("Ubuntu", 0, 16));
        jTextField_Chat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_ChatKeyPressed(evt);
            }
        });

        jButton_Sound.setText("отправить звук");
        jButton_Sound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SoundActionPerformed(evt);
            }
        });

        jButton_Userinfo.setText("юзеринфо");
        jButton_Userinfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_UserinfoActionPerformed(evt);
            }
        });

        jButton_Ping.setText("померять Ping");
        jButton_Ping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_PingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_PaperLayout = new javax.swing.GroupLayout(jPanel_Paper);
        jPanel_Paper.setLayout(jPanel_PaperLayout);
        jPanel_PaperLayout.setHorizontalGroup(
            jPanel_PaperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 43, Short.MAX_VALUE)
        );
        jPanel_PaperLayout.setVerticalGroup(
            jPanel_PaperLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        jButton_StartNewGame.setText("начать новую игру");
        jButton_StartNewGame.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_StartNewGameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                    .addComponent(jTextField_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_StartNewGame, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel_Paper, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Userinfo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton_Ping, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton_Sound, javax.swing.GroupLayout.Alignment.TRAILING)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButton_Userinfo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Ping)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Sound)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_StartNewGame)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel_Paper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField_Chat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jTextField_ChatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_ChatKeyPressed
		if ((evt.getKeyCode() == KeyEvent.VK_ENTER)
				&& (jTextField_Chat.getText().trim().isEmpty() == false)) {
			String message = jTextField_Chat.getText().trim();

			player.server.sendPrivateMsg(player.id, message);
			addChat(player.server.getMyName(), message, true);
			Sounds.playSendChat();
			jTextField_Chat.setText("");
		}
}//GEN-LAST:event_jTextField_ChatKeyPressed

	private void jButton_SoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SoundActionPerformed
		player.server.sendPrivateMsg(player.id, "/SendSOUND");
	}//GEN-LAST:event_jButton_SoundActionPerformed

	private void jButton_PingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_PingActionPerformed
		lastPingSent = new Date();
		player.server.sendPrivateMsg(player.id, "/Ping");
	}//GEN-LAST:event_jButton_PingActionPerformed

	private void jButton_UserinfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_UserinfoActionPerformed
		player.server.sendPrivateMsg("Podbot", "!info " + player.id.replaceAll(
				"\\(.*\\)", ""));
		addChat("", "ответ (информацию юзеринфо) Вы получите от имени 'PodBot' "
				+ "-- так уж всё устроено тут на канале...:)", false);
	}//GEN-LAST:event_jButton_UserinfoActionPerformed

	private void jButton_StartNewGameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_StartNewGameActionPerformed
		String userOutput = JOptionPane.showInputDialog(
				"введите размеры поля по X и по Y через пробел.\n"
				+ "Размеры не должны быть больше 50-ти.\n"
				+ "Например, 30 25");
		int x = -1, y = -1;
		try {
			String[] splitted = userOutput.split(" ");
			x = Integer.parseInt(splitted[0]);
			y = Integer.parseInt(splitted[1]);
		} catch (Exception e) {
		}
		if ((x > 0) && (y > 0) && (x < 50) && (y < 50)) {
			paper.initPaper(x, y);
			addChat("", "Началась новая игра, размеры поля: " + x + ":" + y,
					true);
			lastMoveWasMine = player.server.getMyName().compareTo(player.id) > 0;
			player.server.sendPrivateMsg(player.id,
					prefixPointsop + prefixNewGame + x + " " + y);
		}
	}//GEN-LAST:event_jButton_StartNewGameActionPerformed

	private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
		jTextField_Chat.requestFocusInWindow();
	}//GEN-LAST:event_formComponentShown

	private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
		jTextField_Chat.requestFocusInWindow();
	}//GEN-LAST:event_formFocusGained

	private void formComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_formComponentAdded
		jTextField_Chat.requestFocusInWindow();
	}//GEN-LAST:event_formComponentAdded

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Ping;
    private javax.swing.JButton jButton_Sound;
    private javax.swing.JButton jButton_StartNewGame;
    private javax.swing.JButton jButton_Userinfo;
    private javax.swing.JPanel jPanel_Paper;
    private javax.swing.JScrollPane jScrollPane_Chat;
    javax.swing.JTextField jTextField_Chat;
    private javax.swing.JTextPane jTextPane_Chat;
    // End of variables declaration//GEN-END:variables
}
