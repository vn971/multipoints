package ru.narod.vn91.pointsop.server.irc;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class IrcNicknameManager {

	protected final Map<String, String> fromId = new ConcurrentHashMap<>();
	protected final Map<String, String> fromIrc = new ConcurrentHashMap<>();

	public String irc2id(String ircNick) {
		if (fromIrc.get(ircNick) != null) {
			// we already had him
			return fromIrc.get(ircNick);
		} else {
			String shortBasic = ircNick.replaceAll(IrcRegexp.pointsxtTail_RegExp, "");
			String shortResult;
			if (fromId.containsKey(shortBasic)) {
				int i = 2;
				while (fromId.containsKey(shortBasic + "(" + i + ")")) {
					i += 1;
				}
				shortResult = shortBasic + "(" + i + ")";
			} else {
				shortResult = shortBasic;
			}
			fromId.put(shortResult, ircNick);
			fromIrc.put(ircNick, shortResult);
			return shortResult;
		}
	}

	public String id2irc(String id) {
		String result = fromId.get(id);
		return (result == null) ? "" : result;
	}

	public void changeIrcNick(
			String oldIrcNick,
			String newIrcNick) {

		String id = fromIrc.get(oldIrcNick);

		fromIrc.remove(oldIrcNick); // we point both irc nicks to the Id - no, we don't
		fromIrc.put(newIrcNick, id);

		fromId.remove(id);
		fromId.put(id, newIrcNick); // overwrite the old
	}

	public void removeIrcNick(String ircNick) {
		String shortNick = fromIrc.get(ircNick);
		fromId.remove(shortNick);
		for (Entry<String,String> mapEntry : fromIrc.entrySet()) {
			if (mapEntry.getValue().equals(shortNick)) {
				fromIrc.remove(mapEntry.getKey());
			}
		}
	}

}
