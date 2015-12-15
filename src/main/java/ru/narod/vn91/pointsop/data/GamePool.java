package ru.narod.vn91.pointsop.data;

import ru.narod.vn91.pointsop.server.ServerInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			GameShort gameShort = (GameShort) o;
			return Objects.equals(server, gameShort.server) &&
				Objects.equals(id, gameShort.id);
		}
		@Override
		public int hashCode() {
			return Objects.hash(server, id);
		}
		private GamePool getOuterType() {
			return GamePool.this;
		}
	}

}
