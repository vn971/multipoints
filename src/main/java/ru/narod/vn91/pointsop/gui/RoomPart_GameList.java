package ru.narod.vn91.pointsop.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import ru.narod.vn91.pointsop.data.GameOuterInfo;
import ru.narod.vn91.pointsop.data.GameInfoListener;
import ru.narod.vn91.pointsop.data.GameOuterInfo.GameState;
import ru.narod.vn91.pointsop.model.GuiController;

public class RoomPart_GameList extends javax.swing.JPanel {

	RoomInterface room;
	GuiController guiController;
	private final List<GameOuterInfo> gameList = new ArrayList<>(10);

	static Object[] createRow(GameOuterInfo gameOuterInfo) {
		String row1, row2, row3;

		String firstAsString;
		if (gameOuterInfo.first == null || gameOuterInfo.first.guiName == null) {
			firstAsString = "";
		} else if (gameOuterInfo.first.rating <= 0) {
			firstAsString = gameOuterInfo.first.guiName;
		} else {
			firstAsString = gameOuterInfo.first.guiName + " (" + gameOuterInfo.first.rating + ")";
		}

		String secondAsString;
		if (gameOuterInfo.second == null || gameOuterInfo.second.guiName == null) {
			secondAsString = "";
		} else if (gameOuterInfo.second.rating <= 0) {
			secondAsString = gameOuterInfo.second.guiName;
		} else {
			secondAsString = gameOuterInfo.second.guiName + " (" + gameOuterInfo.second.rating + ")";
		}

//		String firstAsString = (gameOuterInfo.first == null || gameOuterInfo.first.guiName == null)
//				? "" : gameOuterInfo.first.guiName;
//		String secondAsString = (gameOuterInfo.second == null || gameOuterInfo.second.guiName == null)
//				? "" : gameOuterInfo.second.guiName;

		boolean isSearching = (gameOuterInfo.state == GameState.SearchingOpponent);
		row1 = String.format("%s%s%s",
				isSearching ? "<html><b>" : "",
						firstAsString,
						isSearching ? "</b></html>" : "");
		row2 = String.format("%s%s%s",
				isSearching ? "<html><b>" : "",
				secondAsString,
				isSearching ? "</b></html>" : "");
		row3 = String.format("%s%s, %s%s",
				isSearching ? "<html><b>" : "",
				gameOuterInfo.isRated ? "R" : "F",
				gameOuterInfo.getTimeAsString(),
				isSearching ? "</b></html>" : "");
		Object[] result = { row1, row2, row3 };
		return result;
	}

	public void gameCreated(
			GameOuterInfo gameOuterInfo) {
		synchronized (gameList) {
			if (gameList.contains(gameOuterInfo)) {
				return;
			}
			if (gameOuterInfo.state == GameState.SearchingOpponent) {
				gameList.add(0, gameOuterInfo);
			} else {
				gameList.add(gameOuterInfo); // end of list
			}

			final DefaultTableModel tableModel = ((DefaultTableModel) jTable1
					.getModel());
			Object[] row = createRow(gameOuterInfo);
			if (gameOuterInfo.state==GameState.SearchingOpponent) {
				tableModel.insertRow(0, row);
			} else {
				tableModel.addRow(row); // end of list
			}

			gameOuterInfo.addChangeListener(new GameInfoListener() {

				@Override
				public void onChange(GameOuterInfo gameOuterInfo) {
					int position = 0;
					for (GameOuterInfo info2 : gameList) {
						if (info2 == gameOuterInfo) {
							Object[] row = createRow(gameOuterInfo);
							int columnNumber = 0;
							for (Object object : row) {
								tableModel.setValueAt(object, position, columnNumber);
								columnNumber += 1;
							}
							break;
						} else {
							position += 1;
						}
					}
				}
			});
		}

		// int rowNumb = 0;
		// while ((rowNumb < gameList.size()) &&
		// (gameList.get(rowNumb).roomName.equals(
		// Gam) == false)) {
		// ++rowNumb;
		// }
		// if (rowNumb < gameList.size()) {
		// // equal found
		// gameList.remove(rowNumb);
		// // gameList.set(rowNumb, new GameRoomData(roomName, user1, user2,
		// gameList.size()));
		// DefaultTableModel tableModel = ((DefaultTableModel)jTable1.getModel());
		// tableModel.removeRow(rowNumb);
		// // String[] row = {user1, user2, settings};
		// // tableModel.insertRow(rowNumb, row);
		// } else {
		// // creating a new one
		// // gameList.add(new GameRoomData(roomName, user1, user2,
		// gameList.size()));
		// // DefaultTableModel tableModel = ((DefaultTableModel)
		// jTable1.getModel());
		// // String[] row = {user1, user2, settings};
		// // tableModel.addRow(row);
		// }
		// if (placeOnTop) {
		// rowNumb = 0;
		// }
		// gameList.add(
		// rowNumb,
		// new GameRoomData(roomName, user1, user2, rowNumb));
		// DefaultTableModel tableModel = ((DefaultTableModel)jTable1.getModel());
		// String[] row = {user1, user2, settings};
		// tableModel.insertRow(rowNumb, row);
	}

	public void gameDestroyed(
			GameOuterInfo gameOuterInfo) {
		synchronized (gameList) {
			int position = 0;
			for (GameOuterInfo info2 : gameList) {
				if (gameOuterInfo==info2) {
					gameList.remove(position);
					DefaultTableModel tableModel = ((DefaultTableModel)jTable1.getModel());
					tableModel.removeRow(position);
					break;
				} else {
					position+=1;
				}
			}
		}

//		int rowNumb = 0;
//		while ((rowNumb < gameList.size()) && (gameList.get(rowNumb).roomName.equals(
//				roomName) == false)) {
//			++rowNumb;
//		}
//		if (rowNumb < gameList.size()) {
//			DefaultTableModel tableModel = ((DefaultTableModel)jTable1.getModel());
//			tableModel.removeRow(rowNumb);
//			gameList.remove(rowNumb);
//		}
	}

	public void initRoomPart(
			RoomInterface containerRoom,
			GuiController guiController) {
		this.guiController = guiController;
		this.room = containerRoom;
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
			synchronized (gameList) {
				int row = jTable1.getSelectedRow();
				if (row >= 0) {
					GameOuterInfo gameInfo = gameList.get(row);
					if (gameInfo.state == GameState.SearchingOpponent) {
						gameInfo.server.askGameVacancyPlay(gameInfo.id);
					} else {
						guiController.activateGameRoom(gameInfo.server, gameInfo.id);
						gameInfo.server.subscribeRoom(gameInfo.id);
					}
				}
			}
		}
		if (jTable1.getRowCount() >= 1) {
			// java seems to be buggy
			jTable1.setRowSelectionInterval(0, 0);
		}
		jTable1.clearSelection(); // java seems to be buggy
	}//GEN-LAST:event_jTable1MouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

//class GameRoomData {
//
//	String roomName, user1, user2;
//	int rowNumber;
//
//	public GameRoomData(String roomName,
//			String user1,
//			String user2,
//			int rowNumber) {
//		this.roomName = roomName;
//		this.user1 = user1;
//		this.user2 = user2;
//	}
//}
