package com.eriklievaart.javalightning.bundle.api.page;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.render.TemplateRenderer;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class NoDataTemplateController implements PageController {

	private final String template;

	public NoDataTemplateController(String template) {
		Check.notBlank(template);
		this.template = template;
	}

	@Override
	public void invoke(ResponseBuilder response) throws Exception {
		response.setRenderer(new TemplateRenderer(template));
	}
}
