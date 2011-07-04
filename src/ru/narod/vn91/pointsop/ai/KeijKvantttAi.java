package ru.narod.vn91.pointsop.ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ru.narod.vn91.pointsop.gameEngine.SingleGameEngineInterface;

public class KeijKvantttAi
		implements Gui2Ai_Interface {

	Ai2Gui_Interface gui;

	Process process;
	BufferedReader reader;
	BufferedWriter writer;
	ListenThread listenThread;
	
	boolean isDisposed = false;
	int sizeX, sizeY;

	public KeijKvantttAi(
			Ai2Gui_Interface gui,
			int sizeX,
			int sizeY,
			String command) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.gui = gui;
		try {
			process = Runtime.getRuntime().exec(command);
			reader =
					new BufferedReader(
							new InputStreamReader(
									process.getInputStream()
							)
					);
			writer =
					new BufferedWriter(
							new OutputStreamWriter(
									process.getOutputStream()
							)
					);
			Runtime.getRuntime().addShutdownHook(
					new Thread() {
						@Override
						public void run() {
							try {
								writer.write("-1\n");
								writer.flush();
							} catch (IOException ignored) {
							}
							process.destroy();
						}
					}
			);
		} catch (Exception ex) {
		}
	}

	public void init() {
		try {
			writer.write("" + 2 + " " + sizeX + " " + sizeY + "\n");
			writer.flush();
			listenThread = new ListenThread();
			listenThread.setDaemon(true);
			listenThread.start();
		} catch (Exception ex) {
			gui.endOfGame();
		}
//		if (process != null) {
//		}
	}

	public void receiveMove(
			int x,
			int y,
			boolean isRed,
			boolean toBeAnswered,
			long timeExpected) {
		String message =
				"" + (x - 1) + " " + (y - 1) + " "
						+ (isRed ? "1" : "0") + " "
						+ (toBeAnswered ? "1" : "0") + "\n";
		try {
			writer.write(message);
			writer.flush();
		} catch (Exception ex) {
			gui.endOfGame();
		}
	}

	public void dispose() {
		isDisposed = true;
//		gui = null;
		try {
			writer.write("-1\n");
			writer.flush();
			process.destroy();
		} catch (Exception ignored) {
		}
	}

	public String getName() {
		return "Keij&Kvanttt AI";
	}

	class ListenThread
			extends Thread {

		@Override
		public void run() {
			while (isDisposed == false) {
				try {
					String aiMove_Raw = reader.readLine();
					try {
						String[] aiMoveSplitted = aiMove_Raw.split(" ", 3);
						int x = Integer.parseInt(aiMoveSplitted[0]) + 1;
						int y = Integer.parseInt(aiMoveSplitted[1]) + 1;
						boolean isRed = aiMoveSplitted[2].equals("1");
						gui.makeMove(x, y, isRed, 0.5, null, 0);
					} catch (Exception e) {
						gui.receiveMessage(aiMove_Raw);
					}
				} catch (IOException ex) {
					boolean processFinished = true;
					try {
						process.exitValue();
					} catch (Exception e) {
						processFinished = false;
					}
					if (processFinished) {
						gui.endOfGame();
					}
				}
			}
		}
	}
}
