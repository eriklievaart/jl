package com.eriklievaart.jl.core.api.page;

import java.util.Hashtable;
import java.util.Map;

import com.eriklievaart.jl.core.api.ResponseBuilder;
import com.eriklievaart.jl.core.api.render.TemplateRenderer;
import com.eriklievaart.toolkit.lang.api.check.CheckStr;
import com.eriklievaart.toolkit.lang.api.str.Str;

public abstract class AbstractTemplateController implements PageController {

	private ResponseBuilder response;
	protected final Map<String, Object> model = new Hashtable<>();
	private String template;

	@Override
	@SuppressWarnings("hiding")
	public final void invoke(ResponseBuilder response) throws Exception {
		this.response = response;
		invoke();
		renderTemplate();
	}

	public abstract void invoke() throws Exception;

	public boolean isTemplateSet() {
		return Str.notBlank(template);
	}

	public void setTemplate(String path) {
		template = path;
	}

	public void setTemplateIfMissing(String path) {
		if (Str.isBlank(template)) {
			template = path;
		}
	}

	private void renderTemplate() {
		CheckStr.notBlank(template, "No template set for controller $", getClass());
		TemplateRenderer renderer = createRenderer();
		model.forEach((k, v) -> renderer.put(k, v));
		response.setRenderer(renderer);
	}

	protected TemplateRenderer createRenderer() {
		return new TemplateRenderer(template);
	}
}