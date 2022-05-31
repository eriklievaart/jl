package com.eriklievaart.jl.zdemo.controller;

import java.util.Date;

import com.eriklievaart.jl.bundle.api.ResponseBuilder;
import com.eriklievaart.jl.bundle.api.page.PageController;
import com.eriklievaart.jl.bundle.api.render.StringRenderer;

public class StringRendererController implements PageController {

	@Override
	public void invoke(ResponseBuilder builder) {
		builder.setRenderer(new StringRenderer("hello<br/><br/>" + new Date()));
	}
}