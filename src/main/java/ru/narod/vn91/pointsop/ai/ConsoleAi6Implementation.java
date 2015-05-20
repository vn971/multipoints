package ru.narod.vn91.pointsop.ai;

import java.io.*;
import java.util.Date;

public class ConsoleAi6Implementation implements ConsoleAi6 {

	final ConsoleGui6 gui;

	Process process;

	BufferedWriter processWriter;
	ListenThread listenThread;

	volatile boolean isDisposed = false;
	volatile int messageNumber = 0;

	final StringBuffer fullInputOutputHistory = new StringBuffer();
	final StringBuilder outgoingCommandHistory = new StringBuilder();

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
								processWriter.write("-1\n");
								processWriter.flush();
							} catch (IOException ex) {
								ex.printStackTrace();
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

			processWriter =
					new BufferedWriter(
							new OutputStreamWriter(
									process.getOutputStream()
							)
					);
		} catch (Exception ex) {
			ex.printStackTrace();
			gui.error(ex.toString());
		}
	}

	void writeToProcess(String string) {
		try {
			fullInputOutputHistory.append(new Date()).append(" ").append(string).append("\n");
			outgoingCommandHistory.append(string).append("\n");

			processWriter.write(string);
			processWriter.write("\n");
			processWriter.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
			error();
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
	public String wrapperOutgoingHistory() {
		return outgoingCommandHistory.toString();
	}

	@Override
	public void boardsize(int x, int y) {
		writeToProcess((messageNumber += 1) + " boardsize " + x + " " + y);
	}

	@Override
	public void init(int x, int y, int randomSeed) {
		writeToProcess((messageNumber += 1) + " init " + x + " " + y + " " + randomSeed);
	}

	@Override
	public void genmove(boolean color) {
		writeToProcess((messageNumber += 1) + " genmove " + getColor(color));
	}
	@Override
	public void reg_genmove(boolean color) {
		writeToProcess((messageNumber += 1) + " reg_genmove " + getColor(color));
	}
	@Override
	public void gen_move(boolean color) {
		writeToProcess((messageNumber += 1) + " gen_move " + getColor(color));
	}

	@Override
	public void list_commands() {
		writeToProcess((messageNumber += 1) + " list_commands");
	}

	@Override
	public void name() {
		writeToProcess((messageNumber += 1) + " name");
	}

	@Override
	public void play(int x, int y, boolean color) {
		writeToProcess((messageNumber += 1) + " play " + x + " " + y + " " + getColor(color));
	}

	@Override
	public void quit() {
		isDisposed = true;
		writeToProcess((messageNumber += 1) + " quit");
		process.destroy();
	}

	@Override
	public void reg_genmove_with_complexity(boolean color, Complexity complexity) {
		writeToProcess((messageNumber += 1) + " reg_genmove_with_complexity "
				+ getColor(color) + " " + complexity);
	}
	@Override
	public void gen_move_with_complexity(boolean color, Complexity complexity) {
		writeToProcess((messageNumber += 1) + " gen_move_with_complexity "
				+ getColor(color) + " " + complexity);
	}

	@Override
	public void reg_genmove_with_time(boolean color, long milliseconds) {
		writeToProcess((messageNumber += 1) + " reg_genmove_with_time "
				+ getColor(color) + " " + (int) milliseconds);
	}
	@Override
	public void gen_move_with_time(boolean color, long milliseconds) {
		writeToProcess((messageNumber += 1) + " gen_move_with_time " + getColor(color) + " " + (int) milliseconds);
	}

	@Override
	public void set_random_seed(int seed) {
		writeToProcess((messageNumber += 1) + " set_random_seed " + seed);
	}

	@Override
	public void undo() {
		writeToProcess((messageNumber += 1) + " undo");
	}

	@Override
	public void version() {
		writeToProcess((messageNumber += 1) + " version");
	}

	private void error() {
		gui.error(fullInputOutputHistory.toString());
	}

	class ListenThread
			extends Thread {
		final BufferedReader reader;

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
						// dunno why this is needed
					}
					fullInputOutputHistory.append(new Date()).append(" ").append(rawLine).append("\n");
					try {
						String[] splitted = rawLine.split(" ");
						if (splitted.length < 3) {
							break;
						}
						if (splitted[0].equals("?")) {
							if (splitted.length>=3 && splitted[2].equals("play")) {
								// do not log it, because if IRC bugs
							} else {
								error();
							}
						} else if (splitted[0].equals("=")) {
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
							} else if (command.equals("init")) {
							} else if (command.equals("name")) {
								gui.name(allArgs);
							} else if (command.equals("version")) {
								gui.version(allArgs);
							} else if (command.equals("play")) {
								gui.play(
										Integer.parseInt(arg1),
										Integer.parseInt(arg2),
										getColor(arg3));
							} else if (command.equals("genmove")
									|| command.equals("reg_genmove")
									|| command.equals("gen_move")) {
								gui.play(
										Integer.parseInt(arg1),
										Integer.parseInt(arg2),
										getColor(arg3));
							} else if (command.equals("reg_genmove_with_complexity")
									|| command.equals("gen_move_with_complexity")) {
								gui.suggest(
										Integer.parseInt(arg1),
										Integer.parseInt(arg2),
										getColor(arg3));
							} else if (command.equals("reg_genmove_with_time")
									|| command.equals("gen_move_with_time")) {
								gui.suggest(
										Integer.parseInt(arg1),
										Integer.parseInt(arg2),
										getColor(arg3));
							} else if (command.equals("undo")) {
								error();
								// gui.error("cannot handle 'undo' message: " + rawLine);
							} else {
								error();
								// gui.error("unknown message: " + rawLine);
							}

						} else {
							error();
						}
					} catch (Exception e) {
						e.printStackTrace();
						error();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
					boolean processFinished = true;
					try {
						process.exitValue();
					} catch (Exception e) {
						ex.printStackTrace();
						processFinished = false;
					}
					if (processFinished) {
					}
				}
			}
		}
	}

}
