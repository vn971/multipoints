package ru.narod.vn91.pointsop.gui;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import javax.swing.JLabel;

import ru.narod.vn91.pointsop.data.Memory;
import ru.narod.vn91.pointsop.server.ServerPointsxt;

public class WelcomePanel extends javax.swing.JPanel {

	public GuiController guiController;

	private void userWantsPointsxtConnection() {
		String nick = jTextField_Username.getText();
		nick = ServerPointsxt.getAllowedNick(nick, true);
		jTextField_Username.setText(nick);
		if (nick.equals("") == false) {
			Memory.setUserName(nick);
//			if (nick.equals("pp")) {
//				guiController.pointsopServer = new ServerPointsop(guiController);
//				guiController.pointsopServer.connect();
//				return; // do not connect to tochki.org and ircworld.ru
//			}
			if (guiController.pointsxt_vn91_server == null) {
				guiController.pointsxt_vn91_server = new ServerPointsxt(
						"pointsgame.net", guiController, nick, "none", "201120", true
				);
				guiController.pointsxt_vn91_server.connect();
			}
			//			if (guiController.pointsxt_tochkiorg_server == null) {
			//				guiController.pointsxt_tochkiorg_server = new ServerPointsxt(
			//						"tochki.org", guiController, nick, null, "1ppass1", true);
			//				guiController.pointsxt_tochkiorg_server.connect();
			//			}

			//			if (guiController.pointsxt_ircworldru_server == null) {
			//				guiController.pointsxt_ircworldru_server = new ServerPointsxt(
			//						"ircworld.ru", guiController, nick, null, "201120", true);
			//				guiController.pointsxt_ircworldru_server.connect();
			//			}
		}
	}

	/**
	 * Creates new form WelcomePanel
	 */
	public WelcomePanel(GuiController actionDistributor) {
		this.guiController = actionDistributor;
		initComponents();
		jPanel_Invisible.setVisible(false);
		jTextField_City.setVisible(false);
		jTextField_Email.setVisible(false);
		jTextField_Icq.setVisible(false);
		jPasswordField.setVisible(false);
		jTextField_Username.setText(Memory.getUserName());
		jTextField_Username.select(
				jTextField_Username.getText().length(),
				jTextField_Username.getText().length()
		);
	}

	/**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jTextField_Username = new javax.swing.JTextField();
        jButton_Connect = new javax.swing.JButton();
        jPasswordField = new javax.swing.JPasswordField();
        jTextField_Email = new javax.swing.JTextField();
        jTextField_Icq = new javax.swing.JTextField();
        jTextField_City = new javax.swing.JTextField();
        jPanel_Invisible = new javax.swing.JPanel();
        jPanel_Tochkiorg = new javax.swing.JPanel();
        jButton_ConnectTochkiorg = new javax.swing.JButton();
        jButton_DisconnectTochkiorg = new javax.swing.JButton();
        jButton_GuestConnectTochkiorg = new javax.swing.JButton();
        jPanel_Ircworld = new javax.swing.JPanel();
        jButton_ConnectIrcworld = new javax.swing.JButton();
        jButton_DisconnectIrcworld = new javax.swing.JButton();
        jButton_GuestConnectIrcworld = new javax.swing.JButton();
        jPanel_AllServers = new javax.swing.JPanel();
        jButton_ConnectAllServers = new javax.swing.JButton();
        jButton_GuestConnectAllServers = new javax.swing.JButton();
        jButton_DisconnectAllServers = new javax.swing.JButton();
        jPanel_Right = new javax.swing.JPanel();
        jLabel_Logo = new javax.swing.JLabel();
        jLabel_IncontactGroup = new LinkedLabel();
        jLabel_Qestions = new LinkedLabel();
        jLabel_GameWishes = new LinkedLabel();
        jLabel_ChatWishes = new LinkedLabel();
        jLabel_Links = new LinkedLabel();
        jScrollPane_ServerOutput = new javax.swing.JScrollPane();
        jTextPane_ServerOutput = new javax.swing.JTextPane();

        jTextField_Username.setBorder(javax.swing.BorderFactory.createTitledBorder("введите имя"));
        jTextField_Username.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField_Username.setNextFocusableComponent(jPasswordField);
        jTextField_Username.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTextField_UsernameKeyPressed(evt);
            }
        });

        jButton_Connect.setText("подключиться");
        jButton_Connect.setNextFocusableComponent(jTextField_Username);
        jButton_Connect.setRequestFocusEnabled(false);
        jButton_Connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ConnectActionPerformed(evt);
            }
        });

        jPasswordField.setBorder(javax.swing.BorderFactory.createTitledBorder("пароль"));
        jPasswordField.setEnabled(false);
        jPasswordField.setNextFocusableComponent(jTextField_Email);
        jPasswordField.setRequestFocusEnabled(false);

        jTextField_Email.setBorder(javax.swing.BorderFactory.createTitledBorder("email"));
        jTextField_Email.setEnabled(false);
        jTextField_Email.setNextFocusableComponent(jTextField_City);
        jTextField_Email.setRequestFocusEnabled(false);

        jTextField_Icq.setBorder(javax.swing.BorderFactory.createTitledBorder("icq"));
        jTextField_Icq.setEnabled(false);
        jTextField_Icq.setNextFocusableComponent(jButton_Connect);
        jTextField_Icq.setRequestFocusEnabled(false);

        jTextField_City.setBorder(javax.swing.BorderFactory.createTitledBorder("город"));
        jTextField_City.setEnabled(false);
        jTextField_City.setNextFocusableComponent(jTextField_Icq);
        jTextField_City.setRequestFocusEnabled(false);

        jPanel_Tochkiorg.setBorder(javax.swing.BorderFactory.createTitledBorder("tochki.org"));
        jPanel_Tochkiorg.setEnabled(false);

        jButton_ConnectTochkiorg.setText("войти");
        jButton_ConnectTochkiorg.setEnabled(false);

        jButton_DisconnectTochkiorg.setText("выйти");
        jButton_DisconnectTochkiorg.setEnabled(false);

        jButton_GuestConnectTochkiorg.setText("войти гостём");
        jButton_GuestConnectTochkiorg.setEnabled(false);
        jButton_GuestConnectTochkiorg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GuestConnectTochkiorgActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_TochkiorgLayout = new javax.swing.GroupLayout(jPanel_Tochkiorg);
        jPanel_Tochkiorg.setLayout(jPanel_TochkiorgLayout);
        jPanel_TochkiorgLayout.setHorizontalGroup(
            jPanel_TochkiorgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TochkiorgLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_ConnectTochkiorg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_GuestConnectTochkiorg)
                .addGap(18, 18, 18)
                .addComponent(jButton_DisconnectTochkiorg)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_TochkiorgLayout.setVerticalGroup(
            jPanel_TochkiorgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_TochkiorgLayout.createSequentialGroup()
                .addGroup(jPanel_TochkiorgLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_ConnectTochkiorg)
                    .addComponent(jButton_GuestConnectTochkiorg)
                    .addComponent(jButton_DisconnectTochkiorg))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel_Ircworld.setBorder(javax.swing.BorderFactory.createTitledBorder("ircworld.ru"));
        jPanel_Ircworld.setEnabled(false);

        jButton_ConnectIrcworld.setText("войти");
        jButton_ConnectIrcworld.setEnabled(false);

        jButton_DisconnectIrcworld.setText("выйти");
        jButton_DisconnectIrcworld.setEnabled(false);

        jButton_GuestConnectIrcworld.setText("войти гостём");
        jButton_GuestConnectIrcworld.setEnabled(false);
        jButton_GuestConnectIrcworld.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GuestConnectIrcworldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel_IrcworldLayout = new javax.swing.GroupLayout(jPanel_Ircworld);
        jPanel_Ircworld.setLayout(jPanel_IrcworldLayout);
        jPanel_IrcworldLayout.setHorizontalGroup(
            jPanel_IrcworldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_IrcworldLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_ConnectIrcworld)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_GuestConnectIrcworld)
                .addGap(18, 18, 18)
                .addComponent(jButton_DisconnectIrcworld)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_IrcworldLayout.setVerticalGroup(
            jPanel_IrcworldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_IrcworldLayout.createSequentialGroup()
                .addGroup(jPanel_IrcworldLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_ConnectIrcworld)
                    .addComponent(jButton_GuestConnectIrcworld)
                    .addComponent(jButton_DisconnectIrcworld))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel_AllServers.setBorder(javax.swing.BorderFactory.createTitledBorder("все сервера"));
        jPanel_AllServers.setEnabled(false);

        jButton_ConnectAllServers.setText("войти");
        jButton_ConnectAllServers.setEnabled(false);

        jButton_GuestConnectAllServers.setText("войти гостём");
        jButton_GuestConnectAllServers.setEnabled(false);
        jButton_GuestConnectAllServers.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GuestConnectAllServersActionPerformed(evt);
            }
        });

        jButton_DisconnectAllServers.setText("выйти");
        jButton_DisconnectAllServers.setEnabled(false);

        javax.swing.GroupLayout jPanel_AllServersLayout = new javax.swing.GroupLayout(jPanel_AllServers);
        jPanel_AllServers.setLayout(jPanel_AllServersLayout);
        jPanel_AllServersLayout.setHorizontalGroup(
            jPanel_AllServersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_AllServersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton_ConnectAllServers)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton_GuestConnectAllServers)
                .addGap(18, 18, 18)
                .addComponent(jButton_DisconnectAllServers)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel_AllServersLayout.setVerticalGroup(
            jPanel_AllServersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_AllServersLayout.createSequentialGroup()
                .addGroup(jPanel_AllServersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton_ConnectAllServers)
                    .addComponent(jButton_GuestConnectAllServers)
                    .addComponent(jButton_DisconnectAllServers))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel_InvisibleLayout = new javax.swing.GroupLayout(jPanel_Invisible);
        jPanel_Invisible.setLayout(jPanel_InvisibleLayout);
        jPanel_InvisibleLayout.setHorizontalGroup(
            jPanel_InvisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 706, Short.MAX_VALUE)
            .addGroup(jPanel_InvisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel_InvisibleLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel_InvisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel_InvisibleLayout.createSequentialGroup()
                            .addComponent(jPanel_AllServers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jPanel_Tochkiorg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel_Ircworld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(120, Short.MAX_VALUE)))
        );
        jPanel_InvisibleLayout.setVerticalGroup(
            jPanel_InvisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
            .addGroup(jPanel_InvisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel_InvisibleLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel_InvisibleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel_InvisibleLayout.createSequentialGroup()
                            .addComponent(jPanel_Tochkiorg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jPanel_Ircworld, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel_AllServers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(29, Short.MAX_VALUE)))
        );

        jPanel_Right.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel_Logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/narod/vn91/pointsop/gui/pointsOP.png"))); // NOI18N

        jLabel_IncontactGroup.setText("<html>О программе PointsOP</html>");
        jLabel_IncontactGroup.setToolTipText("http://pointsgame.net/site/pointsop");
        jLabel_IncontactGroup.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel_Qestions.setText("<html>Обсуждение PointsOP</html>");
        jLabel_Qestions.setToolTipText("http://vkontakte.ru/pointsgame");
        jLabel_Qestions.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel_GameWishes.setText("<html>Информация про ЯроБот</html>");
        jLabel_GameWishes.setToolTipText("http://vkontakte.ru/topic-13325_25000527");
        jLabel_GameWishes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel_ChatWishes.setText("<html>Сайт о игре Точки</html>");
        jLabel_ChatWishes.setToolTipText("http://pointsgame.net");
        jLabel_ChatWishes.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel_Links.setText("<html>Полезные ссылки</html>");
        jLabel_Links.setToolTipText("http://pointsgame.net/site/links");
        jLabel_Links.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jTextPane_ServerOutput.setEditable(false);
        jTextPane_ServerOutput.setText("Cоединение:\n----------------------------------------------------------------------------\n");
        jScrollPane_ServerOutput.setViewportView(jTextPane_ServerOutput);

        javax.swing.GroupLayout jPanel_RightLayout = new javax.swing.GroupLayout(jPanel_Right);
        jPanel_Right.setLayout(jPanel_RightLayout);
        jPanel_RightLayout.setHorizontalGroup(
            jPanel_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_Logo, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE)
                    .addComponent(jLabel_IncontactGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Qestions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_GameWishes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_Links, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_ChatWishes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane_ServerOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 786, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel_RightLayout.setVerticalGroup(
            jPanel_RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel_RightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel_Logo, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_IncontactGroup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_Qestions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_GameWishes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_ChatWishes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel_Links, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane_ServerOutput, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTextField_Username, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addComponent(jPasswordField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
                            .addComponent(jTextField_Email, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jTextField_City, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jTextField_Icq, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jButton_Connect, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel_Right, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jPanel_Invisible, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(309, 309, 309)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel_Right, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField_Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_City, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_Icq, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton_Connect)
                        .addGap(45, 45, 45)
                        .addComponent(jPanel_Invisible, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jButton_ConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ConnectActionPerformed
		userWantsPointsxtConnection();
	}//GEN-LAST:event_jButton_ConnectActionPerformed

	private void jTextField_UsernameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField_UsernameKeyPressed
		if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
			userWantsPointsxtConnection();
		}
	}//GEN-LAST:event_jTextField_UsernameKeyPressed

	private void jButton_GuestConnectTochkiorgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GuestConnectTochkiorgActionPerformed
		jButton_ConnectTochkiorg.setEnabled(false);
		jButton_GuestConnectTochkiorg.setEnabled(false);
		jButton_DisconnectTochkiorg.setEnabled(true);
	}//GEN-LAST:event_jButton_GuestConnectTochkiorgActionPerformed

	private void jButton_GuestConnectIrcworldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GuestConnectIrcworldActionPerformed
	}//GEN-LAST:event_jButton_GuestConnectIrcworldActionPerformed

	private void jButton_GuestConnectAllServersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GuestConnectAllServersActionPerformed
	}//GEN-LAST:event_jButton_GuestConnectAllServersActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton_Connect;
    private javax.swing.JButton jButton_ConnectAllServers;
    private javax.swing.JButton jButton_ConnectIrcworld;
    private javax.swing.JButton jButton_ConnectTochkiorg;
    private javax.swing.JButton jButton_DisconnectAllServers;
    private javax.swing.JButton jButton_DisconnectIrcworld;
    private javax.swing.JButton jButton_DisconnectTochkiorg;
    private javax.swing.JButton jButton_GuestConnectAllServers;
    private javax.swing.JButton jButton_GuestConnectIrcworld;
    private javax.swing.JButton jButton_GuestConnectTochkiorg;
    private javax.swing.JLabel jLabel_ChatWishes;
    private javax.swing.JLabel jLabel_GameWishes;
    private javax.swing.JLabel jLabel_IncontactGroup;
    private javax.swing.JLabel jLabel_Links;
    private javax.swing.JLabel jLabel_Logo;
    private javax.swing.JLabel jLabel_Qestions;
    private javax.swing.JPanel jPanel_AllServers;
    private javax.swing.JPanel jPanel_Invisible;
    private javax.swing.JPanel jPanel_Ircworld;
    private javax.swing.JPanel jPanel_Right;
    private javax.swing.JPanel jPanel_Tochkiorg;
    private javax.swing.JPasswordField jPasswordField;
    private javax.swing.JScrollPane jScrollPane_ServerOutput;
    private javax.swing.JTextField jTextField_City;
    private javax.swing.JTextField jTextField_Email;
    private javax.swing.JTextField jTextField_Icq;
    javax.swing.JTextField jTextField_Username;
    public javax.swing.JTextPane jTextPane_ServerOutput;
    // End of variables declaration//GEN-END:variables
}

class LinkedLabel extends JLabel {

	public LinkedLabel() {
		super();
	}

	@Override
	public void setToolTipText(final String link) {
		super.setToolTipText(link);
		super.addMouseListener(
				new MouseListener() {

					public void mouseReleased(MouseEvent e) {
					}

					public void mousePressed(MouseEvent e) {
					}

					public void mouseExited(MouseEvent e) {
					}

					public void mouseEntered(MouseEvent e) {
					}

					public void mouseClicked(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1) {
							try {
								java.awt.Desktop.getDesktop().browse(new URI(link));
							} catch (Exception e1) {
							}
						} else {
						}
					}
				}
		);
	}

	@Override
	public void setText(String text) {
		// blue color and underlining
		text = text.replaceAll("<html>|<a href=.*>|</a>|</html>", "");
		super.setText(
				"<html><a href=\"\""
						+ ">"
						+ text + "</a></html>"
		);
	}
}
