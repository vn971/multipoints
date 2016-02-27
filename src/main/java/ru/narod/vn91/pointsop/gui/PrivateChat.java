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
import ru.narod.vn91.pointsop.sounds.Sounds;

@SuppressWarnings("serial")
public class PrivateChat extends javax.swing.JPanel {

	private final Player companion;
	private StyledDocument document = new DefaultStyledDocument();
	private Date lastPingSent = new Date();
	private Paper paper;
	private boolean lastMoveWasMine;
	private static String prefixPointsop = "OpCmd ";
	private static String prefixNewGame = "NewGame ";
	private static String prefixMakeMove = "MakeMove ";

	public boolean close() {
		return true;
	}

	public void personalInviteReceived(GameOuterInfo gameInfo) {
		this.jButton_AcceptInvite.setEnabled(true);
		this.jButton_RejectInvite.setEnabled(true);
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
		jButton_AcceptInvite.setEnabled(false);
		jButton_RejectInvite.setEnabled(false);
		this.addChat("server", "Заявка на игру закрыта.", false);
	}

	public void yourPersonalInviteCancelled() {
		jButton_CancelInvite.setEnabled(false);
		jButton_Invite.setEnabled(true);
		this.addChat("server", "Вы отменили своё приглашение на игру.", false);
	}

	public void yourPersonalInviteRejected() {
		jButton_CancelInvite.setEnabled(false);
		jButton_Invite.setEnabled(true);
		this.addChat("server", "Собеседник отклонил ваше приглашение на игру.", false);
	}

	public void yourPersonalInviteSent() {
		jButton_CancelInvite.setEnabled(true);
		jButton_Invite.setEnabled(false);
		this.addChat("server", "Заявка на игру отправлена.", false);
	}

	public void addChat(String user,
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
							"" + GuiCommon.myTimeFormat.format(
							new Date()) + " ",
							GuiCommon.chatOutgoing);
					document.insertString(document.getLength(), user
							+ ":", GuiCommon.playerNameOutgoing);
					document.insertString(document.getLength(), " " + message
							+ "\n", GuiCommon.chatOutgoing);
				} else {
					document.insertString(document.getLength(),
							"" + GuiCommon.myTimeFormat.format(
							new Date()) + " ",
							GuiCommon.chatIncoming);
					document.insertString(document.getLength(), user
							+ ":", GuiCommon.playerNameIncoming);
					document.insertString(document.getLength(), " " + message
							+ "\n", GuiCommon.chatIncoming);
				}
			} catch (Exception e) {
			}
			jTextPane_Chat.setSelectionStart(jTextPane_Chat.getText().length());
			jTextPane_Chat.setSelectionEnd(jTextPane_Chat.getText().length());
			if (silent == false && user != null && !(user.equals(companion.server.getMyName()))) {
				Sounds.playReceiveChat();
			}
		}
	}

	/** Creates new form PrivateChat */
	public PrivateChat(final Player me) {
		this.companion = me;
		initComponents();
		jScrollPane1.setVisible(false);
		if (me.server.isPrivateChatEnabled()) {
			jTextField_Chat.requestFocusInWindow();
			jTextField_Chat.setDocument(
				new JTextField_UndoableLimited(
				jTextField_Chat,
				me.server.getMaximumMessageLength()));
		} else {
			jTextField_Chat.setEnabled(false);
			jTextPane_Chat.setEnabled(false);
			jTextPane_Chat.setText("чат на этом сервере недоступен\n");
		}
		jButton_Invite.setEnabled(me.server.isPrivateGameInviteAllowed());
		jButton_Ping.setEnabled(me.server.isPingEnabled());
		jButton_Sound.setEnabled(me.server.isSoundNotifyEnabled());
		jTextArea1.setBackground(this.getBackground());
		{
			PlayerChangeListener changeListener = new PlayerChangeListener() {
				@Override
				public void onChange(Player player) {
					jLabel_Userpic.setIcon(me.imageIcon);
					if (me.userInfo != null) {
					jTextArea1.setText(player.userInfo);
//					jTextArea1.setEnabled(true);
					jScrollPane1.setVisible(true);
					}
				}
			};
			changeListener.onChange(me);
			me.addChangeListener(changeListener);
		}
		me.server.getUserInfoText(me.id);
		me.server.getUserpic(me.id);
		jButton_AcceptInvite.setEnabled(false);
		jButton_RejectInvite.setEnabled(false);
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
        jButton_AcceptInvite = new javax.swing.JButton();
        jButton_Invite = new javax.swing.JButton();
        jButton_CancelInvite = new javax.swing.JButton();
        jButton_RejectInvite = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

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

        jLabel_Userpic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jButton_AcceptInvite.setText("принять заявку на игру");
        jButton_AcceptInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AcceptInviteActionPerformed(evt);
            }
        });

        jButton_Invite.setText("вызвать на игру");
        jButton_Invite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_InviteActionPerformed(evt);
            }
        });

        jButton_CancelInvite.setText("отозвать свою заявку");
        jButton_CancelInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelInviteActionPerformed(evt);
            }
        });

        jButton_RejectInvite.setText("отклонить заявку");
        jButton_RejectInvite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_RejectInviteActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jTextArea1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextArea1.setDisabledTextColor(new java.awt.Color(60, 60, 60));
        jTextArea1.setEnabled(false);
        jTextArea1.setFocusable(false);
        jTextArea1.setHighlighter(null);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel_InfoLayout = new javax.swing.GroupLayout(jPanel_Info);
        jPanel_Info.setLayout(jPanel_InfoLayout);
        jPanel_InfoLayout.setHorizontalGroup(
            jPanel_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel_Userpic, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
            .addComponent(jButton_Ping, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
            .addComponent(jButton_Sound, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
            .addComponent(jButton_AcceptInvite, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
            .addComponent(jButton_RejectInvite, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
            .addComponent(jButton_Invite, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
            .addComponent(jButton_CancelInvite, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
        );
        jPanel_InfoLayout.setVerticalGroup(
            jPanel_InfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_InfoLayout.createSequentialGroup()
                .addComponent(jLabel_Userpic)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_Ping)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_Sound)
                .addGap(18, 18, 18)
                .addComponent(jButton_AcceptInvite)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_RejectInvite)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton_Invite)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_CancelInvite)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                    .addComponent(jScrollPane_Chat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE))
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
			Sounds.playSendChat();
			jTextField_Chat.setText("");
		}
}//GEN-LAST:event_jTextField_ChatKeyPressed

	private void jButton_SoundActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SoundActionPerformed
	// companion.server.sendPrivateMsg(companion.id, "/SendSOUND");
	}//GEN-LAST:event_jButton_SoundActionPerformed

	private void jButton_PingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_PingActionPerformed
	// lastPingSent = new Date();
	// companion.server.sendPrivateMsg(companion.id, "/Ping");
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

	private void jButton_InviteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_InviteActionPerformed
		new GameInvitePersonal(null, true, companion).setVisible(true);
	}//GEN-LAST:event_jButton_InviteActionPerformed

	private void jButton_AcceptInviteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AcceptInviteActionPerformed
		companion.server.acceptPersonalGameInvite(companion.id);
	}//GEN-LAST:event_jButton_AcceptInviteActionPerformed

	private void jButton_CancelInviteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_CancelInviteActionPerformed
		companion.server.cancelPersonalGameInvite(companion.id);
	}//GEN-LAST:event_jButton_CancelInviteActionPerformed

	private void jButton_RejectInviteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_RejectInviteActionPerformed
		companion.server.rejectPersonalGameInvite(companion.id);
	}//GEN-LAST:event_jButton_RejectInviteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_AcceptInvite;
    private javax.swing.JButton jButton_CancelInvite;
    private javax.swing.JButton jButton_Invite;
    private javax.swing.JButton jButton_Ping;
    private javax.swing.JButton jButton_RejectInvite;
    private javax.swing.JButton jButton_Sound;
    private javax.swing.JLabel jLabel_Userpic;
    private javax.swing.JPanel jPanel_Info;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane_Chat;
    private javax.swing.JTextArea jTextArea1;
    javax.swing.JTextField jTextField_Chat;
    private javax.swing.JTextPane jTextPane_Chat;
    // End of variables declaration//GEN-END:variables
}
