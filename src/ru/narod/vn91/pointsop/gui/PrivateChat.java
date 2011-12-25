/*
 * PrivateChat.java
 *
 * Created on Feb 11, 2011, 9:31:17 PM
 */
package ru.narod.vn91.pointsop.gui;

import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import ru.narod.vn91.pointsop.data.GameOuterInfo;
import ru.narod.vn91.pointsop.data.Player;
import ru.narod.vn91.pointsop.data.PlayerChangeListener;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveResult;
import ru.narod.vn91.pointsop.server.ServerInterface;
import ru.narod.vn91.pointsop.sounds.Sounds;

@SuppressWarnings("serial")
public class PrivateChat extends javax.swing.JPanel {

	private final Player companion;
//	GuiForServerInterface guiController;
	private StyledDocument document = new DefaultStyledDocument();
	private Date lastPingSent = new Date();
//	SingleGameEngineInterface engine;
	private Paper paper;
	private boolean lastMoveWasMine;
	private static String prefixPointsop = "OpCmd ";
	private static String prefixNewGame = "NewGame ";
	private static String prefixMakeMove = "MakeMove ";

	public boolean close() {
		return true;
	}

	public void personalInviteReceived(GameOuterInfo gameInfo) {
		this.jButton_AcceptIncomingInvite.setEnabled(true);
		String message = String.format("Собеседник вызывает вас на игру с " +
			"%s секундами основного времени + %s добавочного за ход, %s, размер поля %s*%s",
			gameInfo.startingTime,
			gameInfo.additionalAccumulatingTime,
			gameInfo.isRated ? "рейтинговая" : "нерейтинговая",
			gameInfo.sizeX,
			gameInfo.sizeY);
		this.addChat("server", message, false);
	}

	public void personalInviteCancelled() {
		jButton_AcceptIncomingInvite.setEnabled(false);
	}

	public void yourPersonalInviteCancelled() {
		jButton_CancelInvite.setEnabled(false);
		jButton_GameInvite.setEnabled(true);
		this.addChat("server", "Вы отменили своё приглашение на игру.", false);
	}

	public void yourPersonalInviteRejected() {
		jButton_CancelInvite.setEnabled(false);
		jButton_GameInvite.setEnabled(true);
		this.addChat("server", "Оппонент отклонил ваше приглашение на игру.", false);
	}

	public void yourPersonalInviteSent() {
		jButton_CancelInvite.setEnabled(true);
		jButton_GameInvite.setEnabled(false);
	}

	void addChat(String user,
			String message,
			boolean silent) {
		if (message.equals("/Pong")) {
			int ping = (int)((new Date()).getTime() - lastPingSent.getTime());
			addChat("***",
					"Пинг до пользователя " + companion.id + " = " + ping + " миллисекунд.",
					false);
		} else if (message.startsWith(prefixPointsop + prefixMakeMove)) {
			if (lastMoveWasMine) {
				boolean OpponentColor = companion.server.getMyName().compareTo(
						companion.id) > 0;
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
				paper.initPaper(x, y, false);
				addChat("", "Началась новая игра, размеры поля: " + x + ":" + y,
						false);
				lastMoveWasMine = companion.server.getMyName().compareTo(companion.id) > 0;
			}
			// ignore unknown pointsOp messages
		} else if (message.startsWith(prefixPointsop)) {
			// ignore unknown pointsOp messages
		} else {
			try {
				if (user.equals(companion.server.getMyName())) {
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
	public PrivateChat(final Player me) {
		this.companion = me;
		initComponents();
		if (me.server.isPrivateChatEnabled()) {
			jTextField_Chat.requestFocusInWindow();
			jTextField_Chat.setDocument(
				new JTextField_UndoableLimited(
				jTextField_Chat,
				me.server.getMaximumMessageLength()));
		} else {
			jTextField_Chat.setEnabled(false);
//			jTextField_Chat.setText("чат на этом сервере недоступен");
			jTextPane_Chat.setEnabled(false);
			jTextPane_Chat.setText("чат на этом сервере недоступен");
		}
		jButton_GameInvite.setEnabled(me.server.isPrivateGameInviteAllowed());
		jButton_Ping.setEnabled(me.server.isPingEnabled());
		jButton_Sound.setEnabled(me.server.isSoundNotifyEnabled());
		me.addChangeListener(new PlayerChangeListener() {
			@Override
			public void onChange(Player player) {
				jLabel_Userpic.setIcon(me.imageIcon);
			}
		});
		me.server.getUserInfoText(me.id);
		me.server.getUserpic(me.id);
		jButton_AcceptIncomingInvite.setEnabled(false);
		jButton_CancelInvite.setEnabled(false);
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
        jPanel_Info = new javax.swing.JPanel();
        jButton_Ping = new javax.swing.JButton();
        jButton_Sound = new javax.swing.JButton();
        jLabel_Userpic = new javax.swing.JLabel();
        jButton_AcceptIncomingInvite = new javax.swing.JButton();
        jButton_GameInvite = new javax.swing.JButton();
        jButton_CancelInvite = new javax.swing.JButton();

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

        jButton_Ping.setText("померить ping");
        jButton_Ping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_PingActionPerformed(evt);
            }
        });

        jButton_Sound.setText("отправить звук");
        jButton_Sound.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SoundActionPerformed(evt);
            }
        });

        jButton_AcceptIncomingInvite.setText("принять заявку на игру");
        jButton_AcceptIncomingInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AcceptIncomingInviteActionPerformed(evt);
            }
        });

        jButton_GameInvite.setText("вызвать на игру");
        jButton_GameInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GameInviteActionPerformed(evt);
            }
        });

        jButton_CancelInvite.setText("отозвать свою заявку");
        jButton_CancelInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelInviteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_InfoLayout = new javax.swing.GroupLayout(jPanel_Info);
        jPanel_Info.setLayout(jPanel_InfoLayout);
        jPanel_InfoLayout.setHorizontalGroup(
            jPanel_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_InfoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton_Sound, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                    .addComponent(jLabel_Userpic)
                    .addComponent(jButton_AcceptIncomingInvite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton_Ping, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                    .addComponent(jButton_GameInvite, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
                    .addComponent(jButton_CancelInvite, javax.swing.GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel_InfoLayout.setVerticalGroup(
            jPanel_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_InfoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Userpic)
                .addGap(18, 18, 18)
                .addComponent(jButton_Ping)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_Sound)
                .addGap(18, 18, 18)
                .addComponent(jButton_AcceptIncomingInvite)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_GameInvite)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_CancelInvite)
                .addContainerGap(258, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addComponent(jTextField_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel_Info, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField_Chat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel_Info, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jTextField_ChatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_ChatKeyPressed
		if ((evt.getKeyCode() == KeyEvent.VK_ENTER)
				&& (jTextField_Chat.getText().trim().isEmpty() == false)) {
			String message = jTextField_Chat.getText().trim();

			companion.server.sendPrivateMsg(companion.id, message);
			addChat(companion.server.getMyName(), message, true);
			Sounds.playSendChat();
			jTextField_Chat.setText("");
		}
}//GEN-LAST:event_jTextField_ChatKeyPressed

	private void jButton_SoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SoundActionPerformed
		companion.server.sendPrivateMsg(companion.id, "/SendSOUND");
	}//GEN-LAST:event_jButton_SoundActionPerformed

	private void jButton_PingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_PingActionPerformed
		lastPingSent = new Date();
		companion.server.sendPrivateMsg(companion.id, "/Ping");
	}//GEN-LAST:event_jButton_PingActionPerformed

	private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
		jTextField_Chat.requestFocusInWindow();
	}//GEN-LAST:event_formComponentShown

	private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
		jTextField_Chat.requestFocusInWindow();
	}//GEN-LAST:event_formFocusGained

	private void formComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_formComponentAdded
		jTextField_Chat.requestFocusInWindow();
	}//GEN-LAST:event_formComponentAdded

	private void jButton_GameInviteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GameInviteActionPerformed
		new GameInvitePersonal(null, true, companion).setVisible(true);
	}//GEN-LAST:event_jButton_GameInviteActionPerformed

	private void jButton_AcceptIncomingInviteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AcceptIncomingInviteActionPerformed
		companion.server.acceptPersonalGameInvite(companion.id);
	}//GEN-LAST:event_jButton_AcceptIncomingInviteActionPerformed

	private void jButton_CancelInviteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelInviteActionPerformed
		companion.server.cancelPersonalGameInvite(companion.id);
		// TODO add your handling code here:
	}//GEN-LAST:event_jButton_CancelInviteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_AcceptIncomingInvite;
    private javax.swing.JButton jButton_CancelInvite;
    private javax.swing.JButton jButton_GameInvite;
    private javax.swing.JButton jButton_Ping;
    private javax.swing.JButton jButton_Sound;
    private javax.swing.JLabel jLabel_Userpic;
    private javax.swing.JPanel jPanel_Info;
    private javax.swing.JScrollPane jScrollPane_Chat;
    javax.swing.JTextField jTextField_Chat;
    private javax.swing.JTextPane jTextPane_Chat;
    // End of variables declaration//GEN-END:variables
}
