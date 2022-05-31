package com.eriklievaart.jl.core.api.exception;

public class ExternalRedirectException extends RedirectException {

	public ExternalRedirectException(String url) {
		super(RedirectException.EXTERNAL, url);
	}
}