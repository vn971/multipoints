package ru.narod.vn91.pointsop.data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveInfoAbstract;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;

/**
 *
 * @author vasya
 */
public class Sgf {

	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	{
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public enum GameResult {

		RED_WON_BY_RESIGN, BLUE_WON_BY_RESIGN,
		RED_WON_BY_TIME, BLUE_WON_BY_TIME,
		RED_WON_END_OF_GAME, BLUE_WON_END_OF_GAME,
		DRAW_END_OF_GAME, DRAW_BY_AGREEMENT,
		UNFINISHED;

		boolean in(GameResult... set) {
			for (GameResult gameResult : set) {
				if (gameResult == this) {
					return true;
				}
			}
			return false;
		}
	}

	private static String get1SgfCoord(int i) {
		if (i <= 26) {
			return Character.toString((char) ((int) 'a' + i - 1));
		} else {
			return Character.toString((char) ((int) 'A' + i - 26 - 1));
		}
	}

	public static String constructSgfForPhp(
			String redName, String blueName,
			int rank1, int rank2,
			int fieldSizeX, int fieldSizeY,
			String timeLimits, GameResult gameResult, int scoreRedMinusBlue,
			ArrayList<MoveInfoAbstract> moves, boolean upsideDown) {
		String content = "";
		String sizeProperty = (fieldSizeX == fieldSizeY)
				? "" + fieldSizeX
				: "" + fieldSizeX + ":" + fieldSizeY;
		String timeMainProperty, timeAdditionalProperty;
		if (timeLimits.equals("5sec/turn")) {
			timeMainProperty = "5";
			timeAdditionalProperty = "5";
		} else if (timeLimits.equals("180sec/5turns")) {
			timeMainProperty = "180";
			timeAdditionalProperty = "36";
		} else {
			timeMainProperty = "1";
			timeAdditionalProperty = "1";
		}
		String gameResultAsString;
		{
			switch (gameResult) {
				case BLUE_WON_BY_RESIGN:
					gameResultAsString = "RE[B+R]";
				case BLUE_WON_BY_TIME:
					gameResultAsString = "RE[B+T]";
				case BLUE_WON_END_OF_GAME:
					gameResultAsString = "RE[B+" + (-scoreRedMinusBlue) + "]";
				case RED_WON_BY_RESIGN:
					gameResultAsString = "RE[W+R]";
				case RED_WON_BY_TIME:
					gameResultAsString = "RE[W+T]";
				case RED_WON_END_OF_GAME:
					gameResultAsString = "RE[R+" + (-scoreRedMinusBlue) + "]";
				case DRAW_BY_AGREEMENT:
					gameResultAsString = "RE[0]";
				case DRAW_END_OF_GAME:
					gameResultAsString = "RE[0]";
				case UNFINISHED:
					gameResultAsString = "";
				default:
					gameResultAsString = "";
			}
		}

		content = "type=paste&"
				+ "sgf=";
		content = ""
				+ "(;FF[4]GM[40]CA[UTF-8]SZ[" + sizeProperty + "]"
				+ "RU[Punish=0,Holes=1,AddTurn=0,MustSurr=1,MinArea=1,Pass=0,Stop=1,LastSafe=0,ScoreTerr=0,InstantWin=0]"
				+ "AP[pointsOp]"
				+ "PB[" + blueName
				+ "]"
				+ "PW[" + redName
				+ "]"
				+ "TM[" + timeMainProperty + "]"
				+ "OT[" + timeAdditionalProperty + "]"
				+ "\n"
				+ "DT[" + simpleDateFormat.format(new Date()) + "]"
				+ "\n"
				+ gameResultAsString
				+ "\n"
				+ "WR[" + rank1 + "]BR[" + rank2 + "]"
				+ "\n";
		try {
			content = "type=paste&" + "sgf=" + URLEncoder.encode(content, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
		}
		for (int moveNumber = 0; moveNumber < moves.size(); moveNumber++) {
			MoveInfoAbstract move = moves.get(moveNumber);
			content += ";" + ((move.moveType == MoveType.RED) ? "W" : "B");
			if (upsideDown) {
				content += "[" + get1SgfCoord(move.coordX) + "" + get1SgfCoord(fieldSizeY + 1 - move.coordY) + "]\n";
			} else {
				content += "[" + get1SgfCoord(move.coordX) + "" + get1SgfCoord(move.coordY) + "]\n";
			}
		}
		content += ")";

		return content;
	}

	public static String constructSgfSimple(
			String redName, String blueName,
			int rank1, int rank2,
			int fieldSizeX, int fieldSizeY,
			String timeLimits, GameResult gameResult, int scoreRedMinusBlue,
			ArrayList<DotColored> moveList, boolean upsideDown) {
		String content = "";
		String sizeProperty = (fieldSizeX == fieldSizeY)
				? "" + fieldSizeX
				: "" + fieldSizeX + ":" + fieldSizeY;
		String timeMainProperty, timeAdditionalProperty;
		if (timeLimits==null) {
			timeMainProperty = "1";
			timeAdditionalProperty = "1";
		} else if (timeLimits.equals("5sec/turn")) {
			timeMainProperty = "5";
			timeAdditionalProperty = "5";
		} else if (timeLimits.equals("180sec/5turns")) {
			timeMainProperty = "180";
			timeAdditionalProperty = "36";
		} else {
			timeMainProperty = "1";
			timeAdditionalProperty = "1";
		}
		String gameResultAsString;
		{
			switch (gameResult) {
				case BLUE_WON_BY_RESIGN:
					gameResultAsString = "RE[B+R]";
				case BLUE_WON_BY_TIME:
					gameResultAsString = "RE[B+T]";
				case BLUE_WON_END_OF_GAME:
					gameResultAsString = "RE[B+" + (-scoreRedMinusBlue) + "]";
				case RED_WON_BY_RESIGN:
					gameResultAsString = "RE[W+R]";
				case RED_WON_BY_TIME:
					gameResultAsString = "RE[W+T]";
				case RED_WON_END_OF_GAME:
					gameResultAsString = "RE[R+" + (-scoreRedMinusBlue) + "]";
				case DRAW_BY_AGREEMENT:
					gameResultAsString = "RE[0]";
				case DRAW_END_OF_GAME:
					gameResultAsString = "RE[0]";
				case UNFINISHED:
					gameResultAsString = "";
				default:
					gameResultAsString = "";
			}
		}

		content = "type=paste&"
				+ "sgf=";
		content = ""
				+ "(;FF[4]GM[40]CA[UTF-8]SZ[" + sizeProperty + "]"
				+ "RU[Punish=0,Holes=1,AddTurn=0,MustSurr=1,MinArea=1,Pass=0,Stop=1,LastSafe=0,ScoreTerr=0,InstantWin=0]"
				+ "AP[pointsOp]"
				+ "PB[" + blueName
				+ "]"
				+ "PW[" + redName
				+ "]"
				+ "TM[" + timeMainProperty + "]"
				+ "OT[" + timeAdditionalProperty + "]"
				+ "\n"
				+ "DT[" + simpleDateFormat.format(new Date()) + "]"
				+ "\n"
				+ gameResultAsString
				+ "\n"
				+ "WR[" + rank1 + "]BR[" + rank2 + "]"
				+ "\n";
		for (int moveNumber = 0; moveNumber < moveList.size(); moveNumber++) {
			DotColored dot = moveList.get(moveNumber);
			content += ";" + (dot.isRed ? "W" : "B");
			if (upsideDown) {
				content += "[" + get1SgfCoord(dot.x) + "" + get1SgfCoord(fieldSizeY + 1 - dot.y) + "]\n";
			} else {
				content += "[" + get1SgfCoord(dot.x) + "" + get1SgfCoord(dot.y) + "]\n";
			}
		}
		content += ")";

		return content;
	}


	public static String constructSgf(
			String redName, String blueName,
			int rank1, int rank2,
			int fieldSizeX, int fieldSizeY,
			String timeLimits, GameResult gameResult, int scoreRedMinusBlue,
			ArrayList<MoveInfoAbstract> moveList, boolean upsideDown) {

		ArrayList<DotColored> moveListNew = new ArrayList<DotColored>();
		for (MoveInfoAbstract moveInfo : moveList) {
			DotColored dot = new DotColored(
					moveInfo.coordX, moveInfo.coordY,
					moveInfo.moveType == MoveType.RED);
			moveListNew.add(dot);
		}
		return constructSgfSimple(redName, blueName, rank1, rank2, fieldSizeX, fieldSizeY, timeLimits, gameResult, scoreRedMinusBlue, moveListNew, upsideDown);
	}
}
