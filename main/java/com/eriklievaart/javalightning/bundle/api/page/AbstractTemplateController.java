package com.eriklievaart.javalightning.bundle.api.page;

import java.util.Hashtable;
import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.render.TemplateRenderer;

public abstract class AbstractTemplateController implements PageController {

	private ResponseBuilder response;
	protected final Map<String, Object> model = new Hashtable<>();

	@Override
	@SuppressWarnings("hiding")
	public final void invoke(ResponseBuilder response) throws Exception {
		this.response = response;
		invoke();
	}

	public abstract void invoke() throws Exception;

	public void renderTemplate(String template) {
		TemplateRenderer renderer = new TemplateRenderer(template);
		model.forEach((k, v) -> renderer.put(k, v));
		response.setRenderer(renderer);
	}
}
