package ru.narod.vn91.pointsop.data;

import ru.narod.vn91.pointsop.server.ServerInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlayerPool {

	final Map<PlayerShort, Player> map = new HashMap<>();

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

	public void remove(ServerInterface server, String id) {
		PlayerShort playerShort = new PlayerShort(server, id);
		map.remove(playerShort);
	}

	class PlayerShort {

		final ServerInterface server;
		final String id;

		public PlayerShort(ServerInterface server, String id) {
			super();
			this.server = server;
			this.id = id;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			PlayerShort that = (PlayerShort) o;
			return Objects.equals(server, that.server) &&
				Objects.equals(id, that.id);
		}

		@Override
		public int hashCode() {
			return Objects.hash(server, id);
		}

		private PlayerPool getOuterType() {
			return PlayerPool.this;
		}
	}
}
