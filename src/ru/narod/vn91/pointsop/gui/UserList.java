package ru.narod.vn91.pointsop.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import ru.narod.vn91.pointsop.data.Player;

public class UserList {
	
	DefaultListModel model = new DefaultListModel();
	JList jList = new JList(model);
	List<Player> playerList = new ArrayList<Player>();

	void add(Player p) {
		playerList.add(p);
		model.addElement(new JLabel(p.guiName));
	}

	void remove(Player p) {
		for (int i = 0; i < playerList.size(); i++) {
//			JLabel jlabel = JLabel.class.cast(model.getElementAt(i));
			if (playerList.get(i).equals(p)) {
				playerList.remove(i);
				model.remove(i);
				break;
			}
		}
	}

}
