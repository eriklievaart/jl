package com.eriklievaart.javalightning.bundle.api.render;

import java.io.IOException;
import java.io.InputStream;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.template.TemplateService;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class TemplateRenderer implements ServletReponseRenderer {
	private LogTemplate log = new LogTemplate(getClass());

	private String resource;

	public TemplateRenderer(String template) {
		this.resource = template;
	}

	@Override
	public void render(RequestContext context) throws IOException {
		log.info("rendering template %", resource);
		InputStream is = context.getServiceCollection(TemplateService.class).withOne(s -> {
			return s.render(resource, context.getResponseBuilder().getModel());
		});
		context.getResponse().setStatus(context.getResponseBuilder().getView().getStatusCode());
		context.getResponse().setContentType("text/html");
		StreamTool.copyStream(is, context.getResponse().getOutputStream());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + resource;
	}
}
