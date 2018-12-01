package com.eriklievaart.javalightning.freemarker;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.template.TemplateService;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.FormattedException;

import freemarker.template.Template;

public class FreemarkerTemplateService implements TemplateService {

	private FreemarkerBeans beans;

	public FreemarkerTemplateService(FreemarkerBeans beans) {
		this.beans = beans;
	}

	@Override
	public InputStream render(String view, Map<String, Object> data) {
		data.put("globals", beans.getGlobalsIndex());

		StringWriter writer = new StringWriter();
		try {
			Template template = beans.getCachedConfiguration().getTemplate(view);
			template.process(data, writer);
		} catch (Exception e) {
			throw new FormattedException("Unable to render template %; $", e, view, e.getMessage());
		}
		return StreamTool.toInputStream(writer.getBuffer().toString());
	}
}
