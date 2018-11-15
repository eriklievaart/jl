package com.eriklievaart.javalightning.bundle.api.exception;

import com.eriklievaart.javalightning.bundle.api.UrlMapping;

public class ExternalRedirectException extends RedirectException {

	public ExternalRedirectException(UrlMapping redirect) {
		super(RedirectException.EXTERNAL, redirect);
	}
}
