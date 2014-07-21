package ru.narod.vn91.pointsop.data;

public class TimeSettings {

	public final int starting, periodAdditional; // zagram style
	public final int periodTransient; // irc style
	public final int periodLength; // = 1  :)

	public TimeSettings(int starting, int periodAdditional, int periodTransient, int periodLength, int timeOuts) {
		this.starting = starting;
		this.periodAdditional = periodAdditional;
		this.periodTransient = periodTransient;
		this.periodLength = periodLength;
	}
}
