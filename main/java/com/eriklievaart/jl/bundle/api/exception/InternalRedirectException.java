package com.eriklievaart.jl.bundle.api.exception;

public class InternalRedirectException extends RedirectException {

	public InternalRedirectException(String redirect) {
		super(RedirectException.INTERNAL, redirect);
	}
}