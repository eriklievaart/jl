package com.eriklievaart.javalightning.bundle.api.exception;

import com.eriklievaart.javalightning.bundle.api.UrlMapping;

public class InternalRedirectException extends RedirectException {

	public InternalRedirectException(UrlMapping redirect) {
		super(RedirectException.INTERNAL, redirect);
	}
}
