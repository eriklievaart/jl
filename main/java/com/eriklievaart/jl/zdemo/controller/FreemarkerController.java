package com.eriklievaart.jl.zdemo.controller;

import java.util.Date;

import com.eriklievaart.jl.core.api.ResponseBuilder;
import com.eriklievaart.jl.core.api.page.PageController;
import com.eriklievaart.jl.core.api.render.TemplateRenderer;

public class FreemarkerController implements PageController {

	@Override
	public void invoke(ResponseBuilder response) {
		TemplateRenderer renderer = new TemplateRenderer("/zdemo/freemarker.ftlh");
		renderer.put("date", new Date());
		renderer.put("partial", "/zdemo/partial.ftlh");
		response.setRenderer(renderer);
	}
}