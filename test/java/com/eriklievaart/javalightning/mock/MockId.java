package com.eriklievaart.javalightning.mock;

import java.util.Random;

public class MockId {

	private static final String chars = "abcdefghijklmnopqrstuvwxyz";
	private static final Random random = new Random();

	public String generate() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			builder.append(chars.charAt(random.nextInt(26)));
		}
		return builder.toString();
	}
}
