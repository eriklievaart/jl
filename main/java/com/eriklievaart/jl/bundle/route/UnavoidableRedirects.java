package com.eriklievaart.jl.bundle.route;

public class UnavoidableRedirects {

	private final String home;
	private final String favicon;

	public UnavoidableRedirects(String home, String favicon) {
		this.home = home;
		this.favicon = favicon;
	}

	public String redirect(String input) {
		switch (simplify(input)) {

		case "":
			return home;

		case "favicon.ico":
			return favicon;

		default:
			return input;
		}
	}

	private String simplify(String input) {
		return input.trim().replaceFirst("^/++", "");
	}
}