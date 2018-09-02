package com.eriklievaart.javalightning.api.render;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.javalightning.filter.FreemarkerConfigurationFactory;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerRenderer implements ServletReponseRenderer {
	private LogTemplate log = new LogTemplate(getClass());

	private String resource;
	private Map<String, Object> data = new HashMap<String, Object>();

	public FreemarkerRenderer(String template) {
		this.resource = template;
	}

	@Override
	public void render(FilterContext context) throws IOException {
		context.getResponse().setStatus(context.getResponseBuilder().getView().getStatusCode());
		context.getResponse().setContentType("text/html");

		try (Writer out = new OutputStreamWriter(context.getResponse().getOutputStream(), Charset.forName("UTF-8"))) {
			render(FreemarkerConfigurationFactory.getCachedConfiguration(), out);
		}
	}

	public void render(Configuration cfg, Writer out) throws IOException {
		log.trace("Rendering template %", resource);
		Template template = cfg.getTemplate(resource);

		try {
			template.process(data, out);
		} catch (TemplateException e) {
			throw new IOException(e);
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + resource;
	}

	public void put(String key, Object value) {
		data.put(key, value);
	}

	public void putAll(Map<String, Object> model) {
		data.putAll(model);
	}
}
