package ru.narod.vn91.pointsop.gui;

import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class RoomPart_GameList extends javax.swing.JPanel {

	RoomInterface containerRoom;
	GuiController guiController;
	private ArrayList<GameRoomData> gameList = new ArrayList<GameRoomData>();

	void gameCreated(String roomName,
			String user1,
			String user2,
			String settings,
			boolean placeOnTop) {
		int rowNumb = 0;
		while ((rowNumb < gameList.size()) && (gameList.get(rowNumb).roomName.equals(
				roomName) == false)) {
			++rowNumb;
		}
		if (rowNumb < gameList.size()) {
			// equal found
			gameList.remove(rowNumb);
//			gameList.set(rowNumb, new GameRoomData(roomName, user1, user2, gameList.size()));
			DefaultTableModel tableModel = ((DefaultTableModel)jTable1.getModel());
			tableModel.removeRow(rowNumb);
//			String[] row = {user1, user2, settings};
//			tableModel.insertRow(rowNumb, row);
		} else {
			// creating a new one
//			gameList.add(new GameRoomData(roomName, user1, user2, gameList.size()));
//			DefaultTableModel tableModel = ((DefaultTableModel) jTable1.getModel());
//			String[] row = {user1, user2, settings};
//			tableModel.addRow(row);
		}
		if (placeOnTop) {
			rowNumb = 0;
		}
		gameList.add(
				rowNumb,
				new GameRoomData(roomName, user1, user2, rowNumb));
		DefaultTableModel tableModel = ((DefaultTableModel)jTable1.getModel());
		String[] row = {user1, user2, settings};
		tableModel.insertRow(rowNumb, row);
	}

	void gameDestroyed(String roomName) {
		int rowNumb = 0;
		while ((rowNumb < gameList.size()) && (gameList.get(rowNumb).roomName.equals(
				roomName) == false)) {
			++rowNumb;
		}
		if (rowNumb < gameList.size()) {
			DefaultTableModel tableModel = ((DefaultTableModel)jTable1.getModel());
			tableModel.removeRow(rowNumb);
			gameList.remove(rowNumb);
		}
	}

	public void initRoomPart(RoomInterface containerRoom,
			GuiController guiController) {
		this.guiController = guiController;
		this.containerRoom = containerRoom;
	}

	/** Creates new form RoomPart_GameList */
	public RoomPart_GameList() {
		initComponents();
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setFocusable(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Игрок 1 (красный)", "Игрок 2 (синий)", "настройки"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setFocusable(false);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
		if (evt.getClickCount() == 2) {
			int row = jTable1.getSelectedRow();
			if (row >= 0) {
				String roomName = gameList.get(row).roomName;
				if (("".equals(gameList.get(row).user1) || "".equals(gameList.get(
						row).user2))
						&& (!containerRoom.getServer().getMyName().equals(gameList.get(
						row).user1))
						&& (!containerRoom.getServer().getMyName().equals(gameList.get(
						row).user2))) {
					containerRoom.getServer().requestPlay(roomName);
				} else {
					guiController.activateGameRoom(containerRoom.getServer(),
							roomName);
					containerRoom.getServer().subscribeRoom(roomName);
				}
			}
		}
	}//GEN-LAST:event_jTable1MouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

class GameRoomData {

	String roomName, user1, user2;
	int rowNumber;

	public GameRoomData(String roomName,
			String user1,
			String user2,
			int rowNumber) {
		this.roomName = roomName;
		this.user1 = user1;
		this.user2 = user2;
	}
}
