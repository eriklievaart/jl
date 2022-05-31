package com.eriklievaart.jl.bundle.api.exception;

public class ExternalRedirectException extends RedirectException {

	public ExternalRedirectException(String url) {
		super(RedirectException.EXTERNAL, url);
	}
}