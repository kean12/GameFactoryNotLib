package com.game.util.web;

import java.security.MessageDigest;

public final class TradeMD5 {
	public static String toMD5(String password) {
		byte[] unencodedPassword = password.getBytes();
		MessageDigest md = null;
		try {
			// first create an instance, given the provider
			md = MessageDigest.getInstance("SHA");
		} catch (Exception e) {
			return password;
		}
		md.reset();
		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);

		// now calculate the hash
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		return buf.toString();
	}

}