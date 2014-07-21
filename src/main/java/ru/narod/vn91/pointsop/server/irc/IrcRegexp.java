package ru.narod.vn91.pointsop.server.irc;

public class IrcRegexp {
	
	// inDI_X220111123511[g101]
	public static final String pointsxtTail_RegExp = "_X[0-9]{12}\\[....\\]";

	public static boolean isGamerNickname(String fullNick) {
		return fullNick.matches(".*" + pointsxtTail_RegExp);
	}

	public static boolean isPointsXTNickname(String ircNick) {
		return (isPointsopNickname(ircNick) == false)
				&& ircNick.matches(".*" + pointsxtTail_RegExp);
	}

	public static boolean isPointsopNickname(String ircNick) {
		return ircNick.startsWith("^") &&
			ircNick.matches(".*" + pointsxtTail_RegExp + ".*");
	}

	public static String cutIrcTail(String irc) {
		return irc.replaceAll(pointsxtTail_RegExp, "");
	}

	public static String cutIrcText(String text) {
		return text.replaceAll(pointsxtTail_RegExp, "");
	}
	
	public static int getPlayerRank(String ircNick) {
		if (isGamerNickname(ircNick)) {
			String rankAsString = ircNick.substring(
				ircNick.length() - 15,
				ircNick.length() - 11
					);
			return Integer.parseInt(rankAsString);
		} else {
			return 0;
		}
	}

	public static String extractUserStatus(String nick) {
		if (IrcRegexp.isGamerNickname(nick)) {
			String stateType = nick.substring(
					nick.length() - 5,
					nick.length() - 1
			);
			if (stateType.equals("free")) {
				return " ";
			} else if (stateType.equals("away")) {
				return "";
			} else {
				return "!";
			}
		} else {
			return "";
		}
	}


}

