/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.narod.vn91.pointsop.server.irc;

import ru.narod.vn91.pointsop.data.Sgf;
import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface.MoveInfoAbstract;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

public class BackupToServer {

	static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	static {
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	private static String httpPost(String urlAsString, String content) {
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
		} catch (IOException ignored) {
		}
		return result;
	}


	public static String sendToEidokropki(
			String redName, String blueName,
			int rank1, int rank2,
			int fieldSizeX, int fieldSizeY,
			String timeLimits, Sgf.GameResult gameResult,
			ArrayList<MoveInfoAbstract> moves) {
		String content = Sgf.constructSgfForHttp(
			redName, blueName,
			rank1, rank2,
			fieldSizeX, fieldSizeY,
			timeLimits,
			gameResult,
			fieldSizeY, moves, true);
		String httpReply = httpPost("http://eidokropki.reaktywni.pl/backend/add_pxt_game.php", content);
		int eidokropkiBrStamp = httpReply.indexOf(" <br>");
		int gameNumber = -1;
		try {
			gameNumber = Integer.parseInt(httpReply.substring(1, eidokropkiBrStamp));
		} catch (Exception ignore) {
		}
		if (gameNumber == -1) {
			return "";
		} else {
			return "http://eidokropki.reaktywni.pl/#url:pointsxt" + gameNumber;
		}
	}
}
