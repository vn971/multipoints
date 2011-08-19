package ru.narod.vn91.pointsop.data;

import java.awt.Image;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import ru.narod.vn91.pointsop.server.ServerInterface;

public class Player {

	public final ServerInterface server;
	public final String id;

	public String guiName = "";
	public Integer rating = 0;
	public Integer winCount = 0;
	public Integer lossCount = 0;
	public Integer drawCount = 0;
	public Image image;
	public String status = "";

	Collection<PlayerChangeListener> changeListenerList = new ArrayList<PlayerChangeListener>();

	public Player(final ServerInterface server, final String id) {
		super();
		this.server = server;
		this.id = id;
		this.guiName = id;
		// others = null, 0
	}

	public Player(ServerInterface server, String id, String guiName,
			Integer rating, Integer winCount, Integer lossCount, Integer drawCount,
			Image image, String status) {
		super();
		this.server = server;
		this.id = id;
		this.guiName = guiName;
		this.rating = rating;
		this.winCount = winCount;
		this.lossCount = lossCount;
		this.drawCount = drawCount;
		this.image = image;
		this.status = status;
	}

	public void updateFrom(Player p) {
		for (Field field : p.getClass().getFields()) {
			try {
				if (field.get(p) != null) {
					field.set(this, field.get(p));
				}
			} catch (Exception e) {
			}
		}
		for (PlayerChangeListener changeListener : changeListenerList) {
			changeListener.onChange(this);
		}
	}

	public void addChangeListener(PlayerChangeListener changeListener) {
		changeListenerList.add(changeListener);
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

}
