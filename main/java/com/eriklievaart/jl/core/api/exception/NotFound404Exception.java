package com.eriklievaart.jl.core.api.exception;

import com.eriklievaart.toolkit.lang.api.str.Str;

public class NotFound404Exception extends Exception {

	public NotFound404Exception() {
		super("not found");
	}

	public NotFound404Exception(String format, Object... args) {
		super(Str.sub(format, args));
	}
}