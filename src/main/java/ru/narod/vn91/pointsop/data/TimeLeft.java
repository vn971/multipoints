package ru.narod.vn91.pointsop.data;

public class TimeLeft {

	public final Integer timeLeft1;
	public final Integer timeLeft2;

	public final Boolean countsDown1;
	public final Boolean countsDown2;

	public TimeLeft(int timeLeft1, int timeLeft2, Boolean countsDown1,
			Boolean countsDown2) {
		super();
		this.timeLeft1 = timeLeft1;
		this.timeLeft2 = timeLeft2;
		this.countsDown1 = countsDown1;
		this.countsDown2 = countsDown2;
	}

	@Override
	public String toString() {
		return String.format("[%s%s, %s%s]",
			timeLeft1,
			countsDown1 ? "" : "(p)",
			timeLeft2,
			countsDown2 ? "" : "(p)");
	}
}
