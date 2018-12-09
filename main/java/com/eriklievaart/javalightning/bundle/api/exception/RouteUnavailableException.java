package com.eriklievaart.javalightning.bundle.api.exception;

import com.eriklievaart.toolkit.lang.api.str.Str;

public class RouteUnavailableException extends Exception {

	public RouteUnavailableException(String format, Object... args) {
		super(Str.sub(format, args));
	}
}
