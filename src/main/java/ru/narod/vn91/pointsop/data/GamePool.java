package ru.narod.vn91.pointsop.data;

import ru.narod.vn91.pointsop.server.ServerInterface;

import java.util.HashMap;
import java.util.Map;

public class GamePool {

	final Map<GameShort, GameOuterInfo> map = new HashMap<>();

	public GameOuterInfo get(ServerInterface server, String id) {
		GameShort gameShort = new GameShort(server, id);
		if (map.get(gameShort) != null) {
			return map.get(gameShort);
		} else {
			GameOuterInfo newGame = new GameOuterInfo(server, id);
			map.put(gameShort, newGame);
			return newGame;
		}
	}

	public void remove(ServerInterface server, String id) {
		GameShort gameShort = new GameShort(server, id);
		map.remove(gameShort);
	}

	class GameShort {
		final ServerInterface server;
		final String id;
		public GameShort(ServerInterface server, String id) {
			super();
			this.server = server;
			this.id = id;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
			result = prime * result
					+ ((this.server == null) ? 0 : this.server.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			GameShort other = (GameShort) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (this.id == null) {
				if (other.id != null)
					return false;
			} else if (!this.id.equals(other.id))
				return false;
			if (this.server == null) {
				if (other.server != null)
					return false;
			} else if (!this.server.equals(other.server))
				return false;
			return true;
		}
		private GamePool getOuterType() {
			return GamePool.this;
		}
	}

}
