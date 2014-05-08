package ru.narod.vn91.pointsop.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

public class Sha1 {
	
	public static String getSha1PodbotPassword(String input) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			byte[] digest = sha1.digest(("podbot" + input).getBytes());
			return HexBin.encode(digest);
		} catch (NoSuchAlgorithmException ex) {
			return input;
		}
	}

	public static String getSha1(String input) {
		try {
			MessageDigest sha1 = MessageDigest.getInstance("SHA1");
			byte[] digest = sha1.digest(input.getBytes());
			return HexBin.encode(digest);
		} catch (NoSuchAlgorithmException ex) {
			return input;
		}
	}

}
