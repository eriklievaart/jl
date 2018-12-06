package com.eriklievaart.javalightning.zdemo.controller;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.UrlMapping;
import com.eriklievaart.javalightning.bundle.api.exception.InternalRedirectException;
import com.eriklievaart.javalightning.bundle.api.page.PageController;

public class InternalRedirectController implements PageController {

	@Override
	public void invoke(ResponseBuilder builder) {
		throw new InternalRedirectException(new UrlMapping("internal", "/mvc/demo/exact"));
	}
}
