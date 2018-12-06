package com.eriklievaart.javalightning.bundle.api.render;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.template.TemplateService;
import com.eriklievaart.osgi.toolkit.api.ServiceCollection;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class TemplateRenderer implements ServletReponseRenderer {
	private LogTemplate log = new LogTemplate(getClass());

	private String resource;
	private Map<String, Object> model = NewCollection.map();

	public TemplateRenderer(String template) {
		Check.notBlank(template);
		this.resource = template;
	}

	public TemplateRenderer(String template, Map<String, Object> data) {
		Check.notBlank(template);
		Check.notNull(data);
		this.resource = template;
		model.putAll(data);
	}

	@Override
	public void render(RequestContext context) throws IOException {
		log.debug("rendering template %", resource);
		ServiceCollection<TemplateService> service = context.getServiceCollection(TemplateService.class);
		InputStream is = service.oneReturns(s -> s.render(resource, model, context));
		int status = context.getResponseBuilder().getStatusCode();
		log.trace("% status %", resource, status);
		context.getResponse().setStatus(status);
		context.getResponseBuilder().getHeaders().forEach((k, v) -> context.getResponse().addHeader(k, v));
		StreamTool.copyStream(is, context.getResponse().getOutputStream());
	}

	public void put(String key, Object value) {
		model.put(key, value);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + resource;
	}
}
