package ru.narod.vn91.pointsop.gui;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import ru.narod.vn91.pointsop.server.ServerInterface;

public class LangRoom extends javax.swing.JPanel implements RoomInterface {

	ServerInterface server;
	String nameOnServer;
	GuiForServerInterface centralGuiController;

	@Override
	public JPanel getMainJPanel() {
		return this;
	}

	@Override
	public RoomPart_Chat getRoomPart_Chat() {
		return roomPart_Chat1;
	}

	@Override
	public RoomPart_Userlist getRoomPart_UserList() {
		return roomPart_UserList1;
	}

	@Override
	public RoomPart_GameList getRoomPart_GameList() {
		return roomPart_GameList1;
	}

	@Override
	public Paper getRoomPart_Paper() {
		return null;
	}

	@Override
	public PrivateChat getPrivateChat() {
		return null;
	}

	@Override
	public ServerInterface getServer() {
		return server;
	}

	@Override
	public String getRoomNameOnServer() {
		return nameOnServer;
	}

	@Override
	public boolean userAsksClose() {
		getServer().unsubscribeRoom(nameOnServer);
		return false;
	}

	/** Creates new form ContainerRoom_Lang */
	public LangRoom(
			ServerInterface server,
			String nameOnServer,
			GuiController centralGuiController) {
		this.nameOnServer = nameOnServer;
		this.centralGuiController = centralGuiController;
		initComponents();
		jToggleButton_LeaveZayavkaActionPerformed(null);
		this.server = server;

		roomPart_Chat1.initChat(this, null);
		roomPart_UserList1.initRoomPart(this, centralGuiController);
		roomPart_GameList1.initRoomPart(this, centralGuiController);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jButton1 = new javax.swing.JButton();
    roomPart_Chat1 = new ru.narod.vn91.pointsop.gui.RoomPart_Chat();
    roomPart_GameList1 = new ru.narod.vn91.pointsop.gui.RoomPart_GameList();
    roomPart_UserList1 = new ru.narod.vn91.pointsop.gui.RoomPart_Userlist();
    jPanel1 = new javax.swing.JPanel();
    jToggleButton_LeaveZayavka = new javax.swing.JToggleButton();
    jButton2 = new javax.swing.JButton();

    jButton1.setText("!топ");
    jButton1.setFocusable(false);
    jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

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

    roomPart_Chat1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
    roomPart_Chat1.setMinimumSize(null);
    roomPart_Chat1.setPreferredSize(null);

    jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

    jToggleButton_LeaveZayavka.setText("[] заявка на игру");
    jToggleButton_LeaveZayavka.setFocusable(false);
    jToggleButton_LeaveZayavka.addChangeListener(new javax.swing.event.ChangeListener() {
      public void stateChanged(javax.swing.event.ChangeEvent evt) {
        jToggleButton_LeaveZayavkaStateChanged(evt);
      }
    });
    jToggleButton_LeaveZayavka.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jToggleButton_LeaveZayavkaActionPerformed(evt);
      }
    });

    jButton2.setText(".?");
    jButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton2ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
        .addComponent(jButton2)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 499, Short.MAX_VALUE)
        .addComponent(jToggleButton_LeaveZayavka))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jToggleButton_LeaveZayavka, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
    this.setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
          .addComponent(roomPart_Chat1, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
          .addComponent(roomPart_GameList1, javax.swing.GroupLayout.DEFAULT_SIZE, 677, Short.MAX_VALUE)
          .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addComponent(roomPart_UserList1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addComponent(roomPart_GameList1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(roomPart_Chat1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
      .addComponent(roomPart_UserList1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
    );
  }// </editor-fold>//GEN-END:initComponents

	private void formFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_formFocusGained
		roomPart_Chat1.requestFocusInWindow();
	}//GEN-LAST:event_formFocusGained

	private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
		roomPart_Chat1.requestFocusInWindow();
	}//GEN-LAST:event_formComponentShown

private void jToggleButton_LeaveZayavkaStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jToggleButton_LeaveZayavkaStateChanged

}//GEN-LAST:event_jToggleButton_LeaveZayavkaStateChanged

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
	JOptionPane.showMessageDialog(null, 
					"Ваши предложения, что сюда добавить?\n"
					+ "\n"
					+ "Для PointsXT - может быть команды?\n"
					+ "Для zagram - может быть фильтрация заявок на игру (автоприём, автоотказ) ?\n"
					+ "\n"
					+ "http://pointsgame.net/forum/");
}//GEN-LAST:event_jButton2ActionPerformed

private void jToggleButton_LeaveZayavkaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton_LeaveZayavkaActionPerformed
	if (jToggleButton_LeaveZayavka.isSelected()) {
		jToggleButton_LeaveZayavka.setText("убрать заявку на игру");
		jToggleButton_LeaveZayavka.setIcon(new ImageIcon());
		if (evt != null && server != null) {
			server.searchOpponent();
		}
	} else {
		jToggleButton_LeaveZayavka.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ru/narod/vn91/pointsop/gui/new.png")));
		jToggleButton_LeaveZayavka.setText("оставить заявку на игру");
		if (server != null) {
			server.stopSearchingOpponent();
		}
	}
}//GEN-LAST:event_jToggleButton_LeaveZayavkaActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JButton jButton2;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JToggleButton jToggleButton_LeaveZayavka;
  private ru.narod.vn91.pointsop.gui.RoomPart_Chat roomPart_Chat1;
  private ru.narod.vn91.pointsop.gui.RoomPart_GameList roomPart_GameList1;
  private ru.narod.vn91.pointsop.gui.RoomPart_Userlist roomPart_UserList1;
  // End of variables declaration//GEN-END:variables
}
