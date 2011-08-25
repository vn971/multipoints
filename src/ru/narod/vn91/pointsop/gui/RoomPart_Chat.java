package ru.narod.vn91.pointsop.gui;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import ru.narod.vn91.pointsop.data.Player;
import ru.narod.vn91.pointsop.sounds.Sounds;
import ru.narod.vn91.pointsop.utils.Memory;

@SuppressWarnings("serial")
public class RoomPart_Chat extends javax.swing.JPanel {

	String userFirst, userSecond;
	RoomInterface roomInterface;
	private StyledDocument document = new DefaultStyledDocument();

	void scrollDown() {
		if (jCheckBoxMenuItem_Autoscroll.isSelected()) {
			jTextPane_Chat.setSelectionStart(jTextPane_Chat.getText().length());
			jTextPane_Chat.setSelectionEnd(jTextPane_Chat.getText().length());
		}
	}

	void addChat(String user,
			String message, Long time) {
		Date date = (time!=null) ? new Date(time) : new Date();
		String formattedDate = GlobalGuiSettings.myTimeFormat.format(date);
		try {
			if (user.equals(userFirst)) {
				StyleConstants.setForeground(GlobalGuiSettings.playerNameRed,
						Memory.getPlayer1Color());

				document.insertString(document.getLength(),
						"" + formattedDate + " ",
						GlobalGuiSettings.chatIncoming);
				document.insertString(document.getLength(), user
						+ ":", GlobalGuiSettings.playerNameRed);
				document.insertString(document.getLength(),
						" " + message + "\n",
						GlobalGuiSettings.chatIncoming);
			} else if (user.equals(userSecond)) {
				StyleConstants.setForeground(GlobalGuiSettings.playerNameBlue,
						Memory.getPlayer2Color());

				document.insertString(document.getLength(),
						"" + formattedDate + " ",
						GlobalGuiSettings.chatIncoming);
				document.insertString(document.getLength(), user
						+ ":", GlobalGuiSettings.playerNameBlue);
				document.insertString(
						document.getLength(),
						" " + message + "\n",
						GlobalGuiSettings.chatIncoming);
			} else if (user.equals(roomInterface.getServer().getMyName())) {
				document.insertString(document.getLength(),
						"" + formattedDate + " ",
						GlobalGuiSettings.chatOutgoing);
				document.insertString(document.getLength(), user
						+ ":", GlobalGuiSettings.playerNameOutgoing);
				document.insertString(document.getLength(), " " + message
						+ "\n", GlobalGuiSettings.chatOutgoing);
			} else {
				document.insertString(document.getLength(),
						"" + formattedDate + " ",
						GlobalGuiSettings.chatIncoming);
				document.insertString(document.getLength(), user
						+ ":", GlobalGuiSettings.playerNameIncoming);
				document.insertString(document.getLength(), " " + message
						+ "\n", GlobalGuiSettings.chatIncoming);
			}
		} catch (Exception ignored) {
		}
		scrollDown();

		if (message.toLowerCase().contains(
				roomInterface.getServer().getMyName().
				replaceAll("\\^", "").toLowerCase())) {
			Sounds.playNameMentioned();
		}
	}

	void addServerNotice(String message) {
		try {
			document.insertString(document.getLength(),
					"" + GlobalGuiSettings.myTimeFormat.format(new Date()) + " ",
					GlobalGuiSettings.serverNotice);
			document.insertString(document.getLength(), " " + message
					+ "\n", GlobalGuiSettings.serverNotice);
		} catch (BadLocationException ignored) {
		}
		scrollDown();
	}

	void addUserJoinedNotice(Player player) {
		if (jCheckBoxMenuItem_ShowUserJoin.isSelected()) {
			addServerNotice("в комнату вошёл(а) " + player.guiName + "");
		}
	}

	void addUserLeftNotice(String user, String reason) {
		if (jCheckBoxMenuItem_ShowUserJoin.isSelected()) {
			String messageToAdd = user + " вышел(а) из комнаты";
			if (reason != null && "".equals(reason) == false) {
				messageToAdd += " (" + reason + ")";
			}
			addServerNotice(messageToAdd);
		}
	}

	void setReadOnly(boolean isReadOnly) {
		jButton_Help.setVisible(!isReadOnly);
		jTextField_Chat.setVisible(!isReadOnly);
		jButton_ClearChat.setVisible(false);
		jToggleButton_ScrollDown.setVisible(false);
		jToggleButton_ShowJoinLeave.setVisible(false);
	}

	/** Creates new form RoomPart_Chat */
	public RoomPart_Chat() {
		initComponents();
		jTextField_Chat.setText("test");
		jMenuItem_FontIncrease.setVisible(false);

		jButton_ClearChat.setVisible(false);
		jToggleButton_ScrollDown.setVisible(false);
		jToggleButton_ShowJoinLeave.setVisible(false);
		jCheckBoxMenuItem_ShowUserJoin.setSelected(true);
	}

	public void initChat(
			RoomInterface roomInterface,
			String userFirst,
			String userSecond) {
		this.userFirst = userFirst;
		this.userSecond = userSecond;
		this.roomInterface = roomInterface;

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
        jCheckBoxMenuItem_Autoscroll = new javax.swing.JCheckBoxMenuItem();
        jMenuItem_EraseChat = new javax.swing.JMenuItem();
        jCheckBoxMenuItem_ShowUserJoin = new javax.swing.JCheckBoxMenuItem();
        jMenuItem_FontIncrease = new javax.swing.JMenuItem();
        jScrollPane_Chat = new javax.swing.JScrollPane();
        jTextPane_Chat = new javax.swing.JTextPane();
        jTextPane_Chat.setDocument(document);
        jTextField_Chat = new javax.swing.JTextField();
        jButton_Help = new javax.swing.JButton();
        jToggleButton_ScrollDown = new javax.swing.JToggleButton();
        jToggleButton_ShowJoinLeave = new javax.swing.JToggleButton();
        jButton_ClearChat = new javax.swing.JButton();

        jCheckBoxMenuItem_Autoscroll.setSelected(true);
        jCheckBoxMenuItem_Autoscroll.setText("↓ автопрокрутка чата вниз");
        jCheckBoxMenuItem_Autoscroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem_AutoscrollActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jCheckBoxMenuItem_Autoscroll);

        jMenuItem_EraseChat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/narod/vn91/pointsop/gui/eraser.png"))); // NOI18N
        jMenuItem_EraseChat.setText("очистить окно чата");
        jMenuItem_EraseChat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_EraseChatActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem_EraseChat);

        jCheckBoxMenuItem_ShowUserJoin.setSelected(true);
        jCheckBoxMenuItem_ShowUserJoin.setText("показывать вход-выход игроков в чат");
        jPopupMenu1.add(jCheckBoxMenuItem_ShowUserJoin);

        jMenuItem_FontIncrease.setText("увеличить шрифт (тест)");
        jMenuItem_FontIncrease.setEnabled(false);
        jMenuItem_FontIncrease.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem_FontIncreaseActionPerformed(evt);
            }
        });
        jPopupMenu1.add(jMenuItem_FontIncrease);

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

        jTextField_Chat.setFont(new java.awt.Font("Ubuntu", 0, 16));
        jTextField_Chat.setComponentPopupMenu(jPopupMenu1);
        jTextField_Chat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_ChatKeyPressed(evt);
            }
        });

        jButton_Help.setText("?");
        jButton_Help.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_HelpActionPerformed(evt);
            }
        });

        jToggleButton_ScrollDown.setText("↓");

        jToggleButton_ShowJoinLeave.setText("ᕤ");

        jButton_ClearChat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/narod/vn91/pointsop/gui/eraser.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton_ClearChat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton_ShowJoinLeave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToggleButton_ScrollDown, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton_Help)
                        .addGap(2, 2, 2))
                    .addComponent(jScrollPane_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 468, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jScrollPane_Chat, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Chat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton_Help)))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToggleButton_ScrollDown)
                .addGap(18, 18, 18)
                .addComponent(jToggleButton_ShowJoinLeave)
                .addGap(18, 18, 18)
                .addComponent(jButton_ClearChat)
                .addGap(225, 225, 225))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jTextField_ChatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_ChatKeyPressed
		if ((evt.getKeyCode() == KeyEvent.VK_ENTER)
				&& (jTextField_Chat.getText().trim().isEmpty() == false)) {
			String message = jTextField_Chat.getText();
			if (message.startsWith("/me")) {
				String actionFormattedMessage = "ACTION " + message.substring(3);
//				String messageWOActionStamp =
//						message.substring(3);
				roomInterface.getServer().
						sendChat(roomInterface.getRoomNameOnServer(),
						actionFormattedMessage);
			} else {
				roomInterface.getServer().
						sendChat(roomInterface.getRoomNameOnServer(),
						message);
			}
			jTextField_Chat.setText("");
		} else if ((evt.isControlDown() == true) && ((evt.getKeyCode() == KeyEvent.VK_UP)
				|| (evt.getKeyCode() == KeyEvent.VK_KP_UP)
				|| (evt.getKeyCode() == KeyEvent.VK_DOWN)
				|| (evt.getKeyCode() == KeyEvent.VK_KP_DOWN))) {
			evt.setKeyCode(0); // ignoring these keys
		}
}//GEN-LAST:event_jTextField_ChatKeyPressed

	private void jCheckBoxMenuItem_AutoscrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxMenuItem_AutoscrollActionPerformed
		if (jCheckBoxMenuItem_Autoscroll.isSelected() == true) {
			int l = jTextPane_Chat.getText().length();
			jTextPane_Chat.setSelectionStart(l);
			jTextPane_Chat.setSelectionEnd(l);
		}
	}//GEN-LAST:event_jCheckBoxMenuItem_AutoscrollActionPerformed

	private void jMenuItem_EraseChatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem_EraseChatActionPerformed
		int userConfirm = JOptionPane.showConfirmDialog(null,
				"Действительно очистить окошко с чатом? (Операция необратима.)");
		if (userConfirm == JOptionPane.YES_OPTION) {
			jTextPane_Chat.setText("you cleared the chat...\n");
		}
	}//GEN-LAST:event_jMenuItem_EraseChatActionPerformed

	private void jButton_HelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_HelpActionPerformed
		JOptionPane.showMessageDialog(null, "Для дополнительных действий с чатом щелкните по нему правой кнопкой мыши.\n"
				+ "Для отправки сообщения надо нажать Enter :)");
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_ClearChat;
    private javax.swing.JButton jButton_Help;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_Autoscroll;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem_ShowUserJoin;
    private javax.swing.JMenuItem jMenuItem_EraseChat;
    private javax.swing.JMenuItem jMenuItem_FontIncrease;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane_Chat;
    javax.swing.JTextField jTextField_Chat;
    private javax.swing.JTextPane jTextPane_Chat;
    private javax.swing.JToggleButton jToggleButton_ScrollDown;
    private javax.swing.JToggleButton jToggleButton_ShowJoinLeave;
    // End of variables declaration//GEN-END:variables
}
