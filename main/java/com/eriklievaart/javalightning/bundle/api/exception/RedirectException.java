package com.eriklievaart.javalightning.bundle.api.exception;

public class RedirectException extends RuntimeException {
	static final boolean EXTERNAL = false;
	static final boolean INTERNAL = true;

	private final boolean internal;
	private final String redirect;

	protected RedirectException(boolean internal, String redirect) {
		this.internal = internal;
		this.redirect = redirect;
	}

	public boolean isInternal() {
		return internal;
	}

	public String getRedirect() {
		return redirect;
	}
}
