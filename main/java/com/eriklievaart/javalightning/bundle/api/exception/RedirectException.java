package com.eriklievaart.javalightning.bundle.api.exception;

import com.eriklievaart.javalightning.bundle.api.UrlMapping;

public class RedirectException extends RuntimeException {
	static final boolean EXTERNAL = false;
	static final boolean INTERNAL = true;

	private final boolean internal;
	private final UrlMapping redirect;

	protected RedirectException(boolean internal, UrlMapping redirect) {
		this.internal = internal;
		this.redirect = redirect;
	}

	public boolean isInternal() {
		return internal;
	}

	public UrlMapping getRedirect() {
		return redirect;
	}

	public String getRedirectUrl() {
		return redirect.getUrl();
	}
}
