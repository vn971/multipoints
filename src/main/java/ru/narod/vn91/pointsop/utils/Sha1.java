package ru.narod.vn91.pointsop.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha1 {

	public static String getSha1PodbotPassword(String input) {
		return getSha1("podbot" + input);
	}

	public static String getSha1(String input) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			byte[] digest = sha1.digest(input.getBytes());
			String result = "";
			for (byte d : digest) {
				result += Integer.toString((d & 0xff) + 0x100, 16).substring(1);
			}
			return result.toUpperCase();
		} catch (NoSuchAlgorithmException ex) {
			return input; // too lazy to fix this at 2014-03-18
		}
	}

}
