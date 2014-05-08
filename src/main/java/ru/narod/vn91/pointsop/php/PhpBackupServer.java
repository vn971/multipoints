/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.narod.vn91.pointsop.php;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;
import ru.narod.vn91.pointsop.data.Sgf;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveInfoAbstract;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveType;

public class PhpBackupServer {

	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	{
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private static String sendToPhpServer(String urlAsString, String content) {
		URL url;
		URLConnection urlConn;
		DataOutputStream output;
		BufferedReader input;
		String result = "";
		try {
			url = new URL(urlAsString);
			urlConn = url.openConnection();
			urlConn.setDoInput(true);
			urlConn.setDoOutput(true);
			urlConn.setUseCaches(false);
			urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			output = new DataOutputStream(urlConn.getOutputStream());
			output.writeBytes(content);
			output.flush();
			output.close();
			DataInputStream in = new DataInputStream(urlConn.getInputStream());
			input = new BufferedReader(new InputStreamReader(in));
			String str;
			while ((str = input.readLine()) != null) {
				result = result + str + "\n";
			}
			input.close();
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		}
		return result;
	}

	/**
	 * sends a game to the pointsgt server
	 * @param redName
	 * @param blueName
	 * @param isRated
	 * @param timeLimits
	 * @param isRedLooser
	 * @param moves
	 * @param comments comments to send to the pointsgt server
	 * @return number of pgt game if succeeded, -1 if not.
	 */
	public static String sendToPointsgt(String redName, String blueName, boolean isRated, String timeLimits, boolean isRedLooser, ArrayList<MoveInfoAbstract> moves, String comments) {
		String content = "";
		String redNameEncoded = redName;
		String blueNameEncoded = blueName;
		try {
			redNameEncoded = URLEncoder.encode(redName, "UTF-8");
			blueNameEncoded = URLEncoder.encode(blueName, "UTF-8");
			comments = URLEncoder.encode(comments, "UTF-8");
		} catch (UnsupportedEncodingException ignore) {
		}
		content = "pointsop_game=1&"
				+ "op_ver=1&"
				+ "op_pass=dt8hf34s&"
				+ "op_nick1=" + redNameEncoded + "&"
				+ "op_nick2=" + blueNameEncoded + "&"
				+ "op_blits=" + timeLimits.equals("5sec/turn") + "&"
				+ "op_rated=" + ((isRated) ? "1" : "0") + "&"
				+ "op_time=" + simpleDateFormat.format(new Date()) + "&"
				+ "op_win=" + ((isRedLooser) ? "2" : "1") + "&"
				+ "op_comment='" + comments + "'&"
				+ "op_parent_id=0&"
				+ "op_nmoves=" + moves.size() + "&"
				+ "op_game_seq=";
		if (moves.size() < 4) {
			// ignoring this game
		} else {
			for (int moveNumber = 0; moveNumber < moves.size(); moveNumber++) {
				if ((moveNumber <= 3) && (moves.get(0).moveType != MoveType.RED)) {
					MoveInfoAbstract moveInfoAbstract = moves.get(3 - moveNumber);
					content += (moveInfoAbstract.coordX - 1) + " " + (moveInfoAbstract.coordY - 1) + " ";
				} else {
					MoveInfoAbstract moveInfoAbstract = moves.get(moveNumber);
					content += (moveInfoAbstract.coordX - 1) + " " + (moveInfoAbstract.coordY - 1) + " ";
				}
			}
		}
		content = content.trim();

		String phpReply = sendToPhpServer("http://p875.h1.ru/PointsGT/PointsGT.php", content);
//		System.out.println("pgt request=\n"+content+"\n");
//		System.out.println("\n" + phpReply);
		if (phpReply.matches(".*pgt-version=1.*")) {
			try {
				int numberStarts = phpReply.indexOf("pgt-number=") + "pgt-number=".length();
				int numberEnds = phpReply.indexOf(";", numberStarts);
				String numberOfTheGame = phpReply.substring(numberStarts, numberEnds);
				return numberOfTheGame;
//				System.out.println("pointsGT number=" + Integer.parseInt(numberOfTheGame));
			} catch (Exception ignore) {
			}
		}
		return "";
	}

//	private static String get1SgfCoord(int i) {
//		if (i <= 26) {
//			return Character.toString((char) ((int) 'a' + i - 1));
//		} else {
//			return Character.toString((char) ((int) 'A' + i - 26 - 1));
//		}
//	}

	public static String sendToEidokropki(
			String redName, String blueName,
			int rank1, int rank2,
			int fieldSizeX, int fieldSizeY,
			String timeLimits, Sgf.GameResult gameResult,
			ArrayList<MoveInfoAbstract> moves) {
		String content = Sgf.constructSgfForPhp(
				redName, blueName,
				rank1, rank2,
				fieldSizeX, fieldSizeY,
				timeLimits,
				gameResult,
				fieldSizeY, moves, true);
		String phpReply = sendToPhpServer("http://eidokropki.reaktywni.pl/backend/add_pxt_game.php", content);
		int eidokropkiBrStamp = phpReply.indexOf(" <br>");
		int gameNumber = -1;
		try {
			gameNumber = Integer.parseInt(phpReply.substring(1, eidokropkiBrStamp));
		} catch (Exception ignore) {
		}
		if (gameNumber == -1) {
			return "";
		} else {
			return "http://eidokropki.reaktywni.pl/#url:pointsxt" + gameNumber;
		}
	}
}
