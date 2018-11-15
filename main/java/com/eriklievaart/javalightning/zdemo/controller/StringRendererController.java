package com.eriklievaart.javalightning.zdemo.controller;

import java.util.Date;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.render.StringRenderer;

public class StringRendererController implements PageController {

	@Override
	public void invoke(ResponseBuilder builder) {
		builder.setRenderer(new StringRenderer("hello<br/><br/>" + new Date()));
	}
}