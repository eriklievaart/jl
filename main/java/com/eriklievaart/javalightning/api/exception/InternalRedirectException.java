package com.eriklievaart.javalightning.api.exception;

import com.eriklievaart.javalightning.api.UrlMapping;

public class InternalRedirectException extends RuntimeException {

	private final UrlMapping redirect;

	public InternalRedirectException(UrlMapping redirect) {
		this.redirect = redirect;
	}

	public UrlMapping getRedirect() {
		return redirect;
	}
}
