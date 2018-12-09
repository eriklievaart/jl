package com.eriklievaart.javalightning.bundle.api.exception;

public class ExternalRedirectException extends RedirectException {

	public ExternalRedirectException(String url) {
		super(RedirectException.EXTERNAL, url);
	}
}
