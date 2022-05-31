package com.eriklievaart.jl.zdemo.controller;

import com.eriklievaart.jl.core.api.ResponseBuilder;
import com.eriklievaart.jl.core.api.exception.InternalRedirectException;
import com.eriklievaart.jl.core.api.page.PageController;

public class InternalRedirectController implements PageController {

	@Override
	public void invoke(ResponseBuilder builder) {
		throw new InternalRedirectException("/mvc/demo/exact");
	}
}