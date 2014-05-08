package ru.narod.vn91.pointsop.ai;

public interface ConsoleGui6 {

//	void list_commands(String... commandNames); must be supported by AI layer

	void name(String name);

	void version(String version);

	void play(int x, int y, boolean color);

	void suggest(int x, int y, boolean color);

	void error(String error);
}
