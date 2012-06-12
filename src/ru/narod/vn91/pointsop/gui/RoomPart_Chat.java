package ru.narod.vn91.pointsop.gui;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyledDocument;

import ru.narod.vn91.pointsop.data.GameOuterInfo;
import ru.narod.vn91.pointsop.data.Player;
import ru.narod.vn91.pointsop.sounds.Sounds;

@SuppressWarnings("serial")
public class RoomPart_Chat extends javax.swing.JPanel {

//	String userFirst, userSecond;
	GameOuterInfo gameInfo;
	RoomInterface roomInterface;
	private StyledDocument document = new DefaultStyledDocument();
	volatile boolean autoscroll = true;
	boolean showUserJoinLeave = true;
	// ↓↧⍗
	// ᐥ↔⇙⇆↢

	void setAutoscroll(final boolean newValue){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				autoscroll = newValue;
				jCheckBoxMenuItem_Autoscroll.setSelected(autoscroll);
				scrollDown();
			}
		});
	}

	void setShowUserJoinLeave(final boolean newValue) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				showUserJoinLeave = newValue;
				jCheckBoxMenuItem_ShowUserJoin.setSelected(showUserJoinLeave);
			}
		});
	}

	void tryEraseChat() {
		int userConfirm = JOptionPane.showConfirmDialog(null,
				"Действительно очистить окошко с чатом? (Операция необратима.)");
		if (userConfirm == JOptionPane.YES_OPTION) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					jTextPane_Chat.setText("");
					addServerNotice("you cleared the chat...\n");
				}
			});
		}
	}

	void sendMessageInChatbox() {
		if (jTextField_Chat.getText().trim().isEmpty() == false) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					String message = jTextField_Chat.getText();
					roomInterface.getServer().
					sendChat(roomInterface.getRoomNameOnServer(),
						message);
					jTextField_Chat.setText("");
				}
			});
		}
	}

	void scrollDown() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (autoscroll) {
					int l = jTextPane_Chat.getText().length();
					jTextPane_Chat.setSelectionStart(l);
					jTextPane_Chat.setSelectionEnd(l);
				}
			}
		});
	}

	public void addChat(
			final String user,
			final String message, 
			Long time) {
		Date date = (time != null && time != 0) ? new Date(time) : new Date();
		final String formattedDate = GuiCommon.myTimeFormat.format(date);
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					AttributeSet playerAttributes;
					if (gameInfo != null && gameInfo.firstGuiNameFailsafe().equals(user)) {
						playerAttributes = GuiCommon.getAttributeSet(true, gameInfo.player1Color());
					} else if (gameInfo != null && gameInfo.secondGuiNameFailsafe().equals(user)) {
						playerAttributes = GuiCommon.getAttributeSet(true, gameInfo.player2Color());
					} else if (user.equals(roomInterface.getServer().getMyName())) {
						playerAttributes = GuiCommon.playerNameOutgoing;
					} else {
						playerAttributes = GuiCommon.playerNameIncoming;
					}
					document.insertString(document.getLength(), "" + formattedDate + " ",
						GuiCommon.chatIncoming);
					document.insertString(document.getLength(), user + ":", playerAttributes);
					document.insertString(document.getLength(), " " + message + "\n",
						GuiCommon.chatIncoming);
				} catch (BadLocationException ignored) {
				}
			}
		});
		scrollDown();

		if (message.toLowerCase().contains(
				roomInterface.getServer().getMyName().
				replaceAll("\\^", "").toLowerCase())) {
			Sounds.playNameMentioned();
		}
	}

	public void addServerNotice(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					document.insertString(document.getLength(),
						"" + GuiCommon.myTimeFormat.format(new Date()) + " ",
						GuiCommon.serverNotice);
					document.insertString(document.getLength(), " " + message
						+ "\n", GuiCommon.serverNotice);
				} catch (BadLocationException ignored) {
				}
				scrollDown();
			}
		});
	}

	public void addUserJoinedNotice(final Player player) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (showUserJoinLeave) {
					addServerNotice("в комнату вошёл(а) " + player.guiName + "");
				}
			}
		});
	}

	public void addUserLeftNotice(final String user, final String reason) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				if (showUserJoinLeave) {
					String messageToAdd = user + " вышел(а) из комнаты";
					if (reason != null && reason.equals("") == false) {
						messageToAdd += " (" + reason + ")";
					}
					addServerNotice(messageToAdd);
				}
			}
		});
	}

	void setReadOnly(final boolean isReadOnly) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				jButton_Help.setVisible(!isReadOnly);
				jTextField_Chat.setVisible(!isReadOnly);
			}
		});
	}

	/** Creates new form RoomPart_Chat */
	public RoomPart_Chat() {
		initComponents();
		jTextField_Chat.setText("test");
		jMenuItem_FontIncrease.setVisible(false);

		setShowUserJoinLeave(true);
		setAutoscroll(true);
	}

	public void initChat(
					RoomInterface roomInterface,
					GameOuterInfo gameInfo) {
		this.roomInterface = roomInterface;
		this.gameInfo = gameInfo;

		Document doc = new JTextField_UndoableLimited(
						jTextField_Chat,
						roomInterface.getServer().getMaximumMessageLength());
		jTextField_Chat.setDocument(doc);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem_SendMessage = new javax.swing.JMenuItem();
        jCheckBoxMenuItem_Autoscroll = new javax.swing.JCheckBoxMenuItem();
        jMenuItem_EraseChat = new javax.swing.JMenuItem();
        jCheckBoxMenuItem_ShowUserJoin = new javax.swing.JCheckBoxMenuItem();
        jMenuItem_FontIncrease = new javax.swing.JMenuItem();
        jScrollPane_Chat = new javax.swing.JScrollPane();
        jTextPane_Chat = new javax.swing.JTextPane();
        jTextPane_Chat.setDocument(document);
        jTextField_Chat = new javax.swing.JTextField();
        jButton_Help = new javax.swing.JButton();

        jMenuItem_SendMessage.setText("Отправить сообщение ( ⏎ Enter )");
        jMenuItem_SendMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_SendMessageActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem_SendMessage);

        jCheckBoxMenuItem_Autoscroll.setSelected(true);
        jCheckBoxMenuItem_Autoscroll.setText("↧ крутить чат вниз при новых сообщениях");
        jCheckBoxMenuItem_Autoscroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem_AutoscrollActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jCheckBoxMenuItem_Autoscroll);

        jMenuItem_EraseChat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/narod/vn91/pointsop/gui/eraser.png"))); // NOI18N
        jMenuItem_EraseChat.setText("очистить чат");
        jMenuItem_EraseChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_EraseChatActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem_EraseChat);

        jCheckBoxMenuItem_ShowUserJoin.setSelected(true);
        jCheckBoxMenuItem_ShowUserJoin.setText("⇆ показывать вход-выход игроков");
        jCheckBoxMenuItem_ShowUserJoin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem_ShowUserJoinActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jCheckBoxMenuItem_ShowUserJoin);

        jMenuItem_FontIncrease.setText("увеличить шрифт (тест)");
        jMenuItem_FontIncrease.setEnabled(false);
        jMenuItem_FontIncrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_FontIncreaseActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem_FontIncrease);

        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                formFocusGained(evt);
            }
        });

        jTextPane_Chat.setEditable(false);
        jTextPane_Chat.setComponentPopupMenu(jPopupMenu1);
        jScrollPane_Chat.setViewportView(jTextPane_Chat);

        jTextField_Chat.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        jTextField_Chat.setComponentPopupMenu(jPopupMenu1);
        jTextField_Chat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_ChatKeyPressed(evt);
            }
        });

        jButton_Help.setText("?");
        jButton_Help.setComponentPopupMenu(jPopupMenu1);
        jButton_Help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_HelpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTextField_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_Help))
            .addComponent(jScrollPane_Chat, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 582, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton_Help)
                    .addComponent(jTextField_Chat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jTextField_ChatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_ChatKeyPressed
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			sendMessageInChatbox();
		} else if ((evt.isControlDown() == true) && ((evt.getKeyCode() == KeyEvent.VK_UP)
				|| (evt.getKeyCode() == KeyEvent.VK_KP_UP)
				|| (evt.getKeyCode() == KeyEvent.VK_DOWN)
				|| (evt.getKeyCode() == KeyEvent.VK_KP_DOWN))) {
			evt.setKeyCode(0); // ignoring these keys
		}
}//GEN-LAST:event_jTextField_ChatKeyPressed

	private void jCheckBoxMenuItem_AutoscrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem_AutoscrollActionPerformed
		setAutoscroll(jCheckBoxMenuItem_Autoscroll.isSelected());
	}//GEN-LAST:event_jCheckBoxMenuItem_AutoscrollActionPerformed

	private void jMenuItem_EraseChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_EraseChatActionPerformed
		tryEraseChat();
	}//GEN-LAST:event_jMenuItem_EraseChatActionPerformed

	private void jButton_HelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_HelpActionPerformed
//		JOptionPane.showMessageDialog(null, "Для дополнительных действий с чатом щелкните по нему правой кнопкой мыши.\n"
//				+ "Для отправки сообщения надо нажать Enter :)");
		jPopupMenu1.show(jButton_Help, 0, 0);
//		jPopupMenu1.setVisible(true);
	}//GEN-LAST:event_jButton_HelpActionPerformed

	private void jMenuItem_FontIncreaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_FontIncreaseActionPerformed
		Font prevFont = jTextPane_Chat.getFont();
		int prevSize = prevFont.getSize();
		prevFont = prevFont.deriveFont(prevSize + 10);
		jTextPane_Chat.setFont(prevFont);
	}//GEN-LAST:event_jMenuItem_FontIncreaseActionPerformed

	private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
		jTextField_Chat.requestFocusInWindow();
	}//GEN-LAST:event_formComponentShown

	private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
		jTextField_Chat.requestFocusInWindow();
	}//GEN-LAST:event_formFocusGained

private void jCheckBoxMenuItem_ShowUserJoinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem_ShowUserJoinActionPerformed
	setShowUserJoinLeave(jCheckBoxMenuItem_ShowUserJoin.isSelected());
}//GEN-LAST:event_jCheckBoxMenuItem_ShowUserJoinActionPerformed

	private void jMenuItem_SendMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_SendMessageActionPerformed
		sendMessageInChatbox();
	}//GEN-LAST:event_jMenuItem_SendMessageActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_Help;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_Autoscroll;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_ShowUserJoin;
    private javax.swing.JMenuItem jMenuItem_EraseChat;
    private javax.swing.JMenuItem jMenuItem_FontIncrease;
    private javax.swing.JMenuItem jMenuItem_SendMessage;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane_Chat;
    javax.swing.JTextField jTextField_Chat;
    private javax.swing.JTextPane jTextPane_Chat;
    // End of variables declaration//GEN-END:variables
}
