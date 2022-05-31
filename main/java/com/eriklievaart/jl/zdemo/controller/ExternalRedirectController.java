package com.eriklievaart.jl.zdemo.controller;

import com.eriklievaart.jl.bundle.api.ResponseBuilder;
import com.eriklievaart.jl.bundle.api.exception.ExternalRedirectException;
import com.eriklievaart.jl.bundle.api.page.PageController;

public class ExternalRedirectController implements PageController {

	@Override
	public void invoke(ResponseBuilder builder) {
		throw new ExternalRedirectException("http://www.github.com");
	}
}