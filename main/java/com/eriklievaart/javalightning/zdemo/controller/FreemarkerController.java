package com.eriklievaart.javalightning.zdemo.controller;

import java.util.Date;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.render.TemplateRenderer;

public class FreemarkerController implements PageController {

	@Override
	public void invoke(ResponseBuilder response) {
		response.getModel().put("date", new Date());
		response.getModel().put("partial", "/zdemo/partial.tpl");
		response.setRenderer(new TemplateRenderer("/zdemo/freemarker.tpl"));
	}
}
