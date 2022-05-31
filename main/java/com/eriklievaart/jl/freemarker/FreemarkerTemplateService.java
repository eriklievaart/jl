package com.eriklievaart.jl.freemarker;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.Map;

import com.eriklievaart.jl.bundle.api.RequestContext;
import com.eriklievaart.jl.bundle.api.template.TemplateService;
import com.eriklievaart.jl.freemarker.model.FreemarkerParameters;
import com.eriklievaart.jl.freemarker.model.Lightning;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.FormattedException;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

import freemarker.template.Template;

public class FreemarkerTemplateService implements TemplateService {
	private LogTemplate log = new LogTemplate(getClass());

	private FreemarkerBeans beans;

	public FreemarkerTemplateService(FreemarkerBeans beans) {
		this.beans = beans;
	}

	@Override
	public InputStream render(String view, Map<String, Object> data, RequestContext context) {
		if (log.isTraceEnabled()) {
			data.forEach((k, v) -> log.trace("template param % -> %", k, v));
		}
		if (context != null) {
			data.putIfAbsent("lightning", new Lightning(context));
			data.putIfAbsent("globals", beans.getInjectedGlobals(context));
			data.putIfAbsent("parameter", new FreemarkerParameters(context.getParameterSupplier()));
		}
		try {
			StringWriter writer = new StringWriter();
			Template template = beans.getCachedConfiguration().getTemplate(view);
			template.process(data, writer);
			return StreamTool.toInputStream(writer.getBuffer().toString());

		} catch (Exception e) {
			throw new FormattedException("Unable to render template %; $", e, view, e.getMessage());
		}
	}
}