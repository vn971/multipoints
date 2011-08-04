package ru.narod.vn91.pointsop.ai;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Date;

public class ConsoleAi6Implementation implements ConsoleAi6 {

	ConsoleGui6 gui;

	Process process;

	BufferedWriter writer;
	ListenThread listenThread;

	boolean isDisposed = false;

	public ConsoleAi6Implementation(
			ConsoleGui6 gui,
			String command) {
		this.gui = gui;
		try {
			process = Runtime.getRuntime().exec(command);
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

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(
							process.getInputStream()
					)
					);
			listenThread = new ListenThread(reader);
			listenThread.setDaemon(true);
			listenThread.start();

			writer =
					new BufferedWriter(
							new OutputStreamWriter(
									process.getOutputStream()
							)
					);
		} catch (Exception ex) {
			gui.error(ex.toString());
		}
	}

	// public void init(int sizeX, int sizeY) {
	// this.boardsize(sizeX, sizeY);
	// }

	void writeToProcess(String string) {
		try {
//			System.out.println(new Date()+ " " + string);
			writer.write(string);
			writer.write("\n");
			writer.flush();
		} catch (IOException e) {
			gui.error(e.toString());
		}
	}

	String getColor(boolean color) {
		return color ? "1" : "0";
	}

	boolean getColor(String color) {
		color = color.trim();
		if (color.equals("1")) {
			return true;
		} else if (color.equals("0")) {
			return false;
		} else {
			throw new IllegalArgumentException("boolean value '" + color + "'");
		}
	}

	@Override
	public void boardsize(int x, int y) {
		writeToProcess("1 boardsize " + x + " " + y);
	}

	@Override
	public void genmove(boolean color) {
		// TODO Auto-generated method stub
		writeToProcess("1 genmove " + getColor(color));
	}

	@Override
	public void list_commands() {
		writeToProcess("1 list_commands");
	}

	@Override
	public void name() {
		writeToProcess("1 name");
	}

	@Override
	public void play(int x, int y, boolean color) {
		writeToProcess("1 play " + x + " " + y + " " + getColor(color));
	}

	@Override
	public void quit() {
		isDisposed = true;
		writeToProcess("1 quit");
		process.destroy();
	}

	@Override
	public void reg_genmove(boolean color) {
		writeToProcess("1 reg_genmove " + getColor(color));
	}

	@Override
	public void reg_genmove_with_complexity(boolean color, Complexity complexity) {
		writeToProcess("1 reg_genmove_with_complexity "
				+ getColor(color) + " " + complexity);
	}

	@Override
	public void reg_genmove_with_time(boolean color, long milliseconds) {
		writeToProcess("1 reg_genmove_with_time "
				+ getColor(color) + " " + (int) milliseconds);
	}

	@Override
	public void undo() {
		writeToProcess("1 undo");
	}

	@Override
	public void version() {
		writeToProcess("1 version");
	}

	class ListenThread
			extends Thread {
		BufferedReader reader;

		public ListenThread(BufferedReader reader) {
			this.reader = reader;
		}

		@Override
		public void run() {
			while (isDisposed == false) {
				try {
					String rawLine = reader.readLine();
					if (rawLine == null) {
						break;
						// TODO I shoudn't need that!!
					}
//					System.out.println(new Date()+ " " + rawLine);
					try {
						// String[] aiMoveSplitted = aiMove_Raw.split(" ", 3);
						// int x = Integer.parseInt(aiMoveSplitted[0]) + 1;
						// int y = Integer.parseInt(aiMoveSplitted[1]) + 1;
						// boolean isRed = aiMoveSplitted[2].equals("1");
						// gui.play(x, y, isRed);
						String[] splitted = rawLine.split(" ");
						if (splitted.length < 3) {
							break;
						}
						if (splitted[0].equals("?")) {
							gui.error(rawLine);
						} else if (splitted[0].equals("=")) {
							// String id = splitted[1];
							String command = splitted[2];
							String arg1 = (splitted.length > 3) ? splitted[3] : "";
							String arg2 = (splitted.length > 4) ? splitted[4] : "";
							String arg3 = (splitted.length > 5) ? splitted[5] : "";
							String allArgs =
									(splitted.length <= 3)
											? ""
											: rawLine.split(" ",
													4)[3];

							if (command.equals("list_commands")) {
							} else if (command.equals("quit")) {
							} else if (command.equals("boardsize")) {
							} else if (command.equals("name")) {
								gui.name(allArgs);
							} else if (command.equals("version")) {
								gui.version(allArgs);
							} else if (command.equals("play")) {
								gui.play(
										Integer.parseInt(arg1),
										Integer.parseInt(arg2),
										getColor(arg3));
							} else if (command.equals("genmove")) {
								gui.play(
										Integer.parseInt(arg1),
										Integer.parseInt(arg2),
										getColor(arg3));
							} else if (command.equals("reg_genmove")) {
								gui.suggest(
										Integer.parseInt(arg1),
										Integer.parseInt(arg2),
										getColor(arg3));
							} else if (command.equals("reg_genmove_with_complexity")) {
								gui.suggest(
										Integer.parseInt(arg1),
										Integer.parseInt(arg2),
										getColor(arg3));
							} else if (command.equals("reg_genmove_with_time")) {
								gui.suggest(
										Integer.parseInt(arg1),
										Integer.parseInt(arg2),
										getColor(arg3));
							} else if (command.equals("undo")) {
								gui.error("cannot handle 'undo' message: " + rawLine);
							} else {
								gui.error("unknown message: " + rawLine);
							}

						} else {
							gui.error(rawLine);
						}
					} catch (Exception e) {
						gui.error("received unknown message: " + rawLine + ", exception: "
								+ e.toString());
					}
				} catch (IOException ex) {
					boolean processFinished = true;
					try {
						process.exitValue();
					} catch (Exception e) {
						processFinished = false;
					}
					if (processFinished) {
						// gui.error("process was destroyed");
					}
				}
			}
		}
	}

}
