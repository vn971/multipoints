package ru.narod.vn91.pointsop.data;

import java.util.HashMap;
import java.util.Map;

import ru.narod.vn91.pointsop.server.ServerInterface;

public class PlayerPool {

	Map<PlayerShort, Player> map = new HashMap<PlayerShort, Player>();

	public Player get(ServerInterface server, String id) {
		PlayerShort playerShort = new PlayerShort(server, id);
		if (map.get(playerShort) != null) {
			return map.get(playerShort);
		} else {
			Player newPlayer = new Player(server, id);
			map.put(playerShort, newPlayer);
			return newPlayer;
		}
	}

	class PlayerShort {

		ServerInterface server;
		String id;

		public PlayerShort(ServerInterface server, String id) {
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
			PlayerShort other = (PlayerShort) obj;
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

		private PlayerPool getOuterType() {
			return PlayerPool.this;
		}
	}
}
