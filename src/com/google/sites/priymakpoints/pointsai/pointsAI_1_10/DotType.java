package com.google.sites.priymakpoints.pointsai.pointsAI_1_10;

public enum DotType {
	
	GLOBAL,ANY,NULL,OUT,LAND,BLUE,RED,RED_EMPTY,BLUE_EMPTY,BLUE_TARGET,
	RED_NORMAL,RED_ATTACK,RED_PROTECTION,RED_GROUND,RED_CAPTURE,RED_DEFENCE,
	BLUE_NORMAL,MAKROS_BLUE,MAKROS_RED;

	public String toString() {
		switch (this) {
			case GLOBAL:return "G";
			case ANY:return "A";
			case NULL:return "N";
			case OUT:return "O";
			case LAND:return "L";
			case BLUE:return "B";
			case RED:return "R";
			case RED_EMPTY:return "P";
			case BLUE_EMPTY:return "C";
			case BLUE_TARGET:return "E";
			case RED_NORMAL:return "0";
			case RED_ATTACK:return "1";
			case RED_PROTECTION:return "2";
			case RED_CAPTURE:return "3";
			case RED_GROUND:return "4";
			case RED_DEFENCE:return "5";
			case BLUE_NORMAL:return "6";
			case MAKROS_BLUE:return "7";
			case MAKROS_RED:return "8";
			default:return "$";
		}
	}
	
}
