package ru.narod.vn91.pointsop.data;

import ru.narod.vn91.pointsop.server.ServerInterface;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;

public class Player {

	public final ServerInterface server;
	public final String id;

	public String guiName = "";
	public Integer rating = 0;
	public ImageIcon imageIcon;
	public String status = "";
	public String userInfo = "";

//	Collection<WeakReference<PlayerChangeListener>> weakListeners = new LinkedList<WeakReference<PlayerChangeListener>>();
final Collection<PlayerChangeListener> listeners = new LinkedList<>();

	public Player(final ServerInterface server, final String id) {
		super();
		this.server = server;
		this.id = id;
		this.guiName = id;
		// others = null, 0
	}

	public Player(ServerInterface server, String id, String guiName,
			Integer rating, Integer winCount, Integer lossCount, Integer drawCount,
			ImageIcon imageIcon, String status, String userInfo) {
		super();
		this.server = server;
		this.id = id;
		this.guiName = guiName;
		this.rating = rating;
		this.imageIcon = imageIcon;
		this.status = status;
		this.userInfo = userInfo;
	}

	public void updateFrom(Player p) {
		for (Field field : p.getClass().getFields()) {
			try {
				if (field.get(p) != null) {
					field.set(this, field.get(p));
				}
			} catch (Exception ignored) {
			}
		}

//		Iterator<WeakReference<PlayerChangeListener>> i = weak.iterator();
//		while (i.hasNext()) {
//			PlayerChangeListener changeListener = i.next().get();
//			if (changeListener != null) {
//				changeListener.onChange(this);
//			} else {
//				i.remove();
//			}
//		}

		for (PlayerChangeListener changeListener : listeners) {
			changeListener.onChange(this);
		}
	}

	public void addChangeListener(PlayerChangeListener changeListener) {
//		weak.add(new WeakReference<PlayerChangeListener>(changeListener));
		listeners.add(changeListener);
	}

	public static int compare(Player p1, Player p2) {
		if (p1.rating - p2.rating != 0) {
			return p1.rating - p2.rating;
		} else {
			String p1Short = p1.guiName.
					replaceFirst("\\*", "").replaceFirst("\\^", "");
			String p2Short = p2.guiName.
					replaceFirst("\\*", "").replaceFirst("\\^", "");
			return 0 - p1Short.compareToIgnoreCase(p2Short);
		}
	}

	@Override
	public String toString() {
		return (guiName != null && guiName.equals("") == false)
			? "[id="+id+",gui="+guiName+"]"
			: "[id=" + id+"]";
	}

}
