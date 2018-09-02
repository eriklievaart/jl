package com.eriklievaart.javalightning.api.exception;

import com.eriklievaart.javalightning.api.UrlMapping;

public class ExternalRedirectException extends RuntimeException {

	private final UrlMapping redirect;

	public ExternalRedirectException(UrlMapping redirect) {
		this.redirect = redirect;
	}

	public UrlMapping getRedirect() {
		return redirect;
	}
}
