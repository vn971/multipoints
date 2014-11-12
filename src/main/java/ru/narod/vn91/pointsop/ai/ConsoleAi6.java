package ru.narod.vn91.pointsop.ai;

public interface ConsoleAi6 {

	void list_commands();

	void quit();

	@Deprecated void boardsize(int x, int y);

	void init(int x, int y, int randomSeed);

	void name();

	void version();

	void play(int x, int y, boolean color);

	@Deprecated void genmove(boolean color);
	@Deprecated void reg_genmove(boolean color);
	void gen_move(boolean color);

	@Deprecated void reg_genmove_with_complexity(boolean color, Complexity complexity);
	void gen_move_with_complexity(boolean color, Complexity complexity);

	@Deprecated void reg_genmove_with_time(boolean color, long milliseconds);
	void gen_move_with_time(boolean color, long milliseconds);

	void set_random_seed(int seed);

	void undo();

	/**
	 * an integer from 0 to 100
	 */
	public class Complexity {

		int complexity;

		/**
		 * @param complexity
		 *          an integer from 0 to 100
		 */
		public Complexity(int complexity) {
			super();
			setComplexity(complexity);
		}

		/**
		 * @param complexity
		 *          an integer from 0 to 100
		 */
		void setComplexity(int complexity) {
			if (complexity > 100) {
				complexity = 100;
			} else if (complexity < 0) {
				complexity = 0;
			}
			this.complexity = complexity;
		}

		/**
		 * an integer from 0 to 100
		 */
		public int getComplexity() {
			return this.complexity;
		}

		@Override
		public String toString() {
			return ""+complexity;
		}

	}

}
