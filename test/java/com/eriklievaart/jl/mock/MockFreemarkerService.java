package com.eriklievaart.jl.mock;

import java.util.Hashtable;
import java.util.Map;

import com.eriklievaart.jl.core.api.template.ClasspathTemplateSource;
import com.eriklievaart.jl.core.api.template.TemplateGlobal;
import com.eriklievaart.jl.core.api.template.TemplateSource;
import com.eriklievaart.jl.freemarker.FreemarkerBeans;
import com.eriklievaart.jl.freemarker.FreemarkerTemplateService;
import com.eriklievaart.toolkit.io.api.StreamTool;

public class MockFreemarkerService {
	private Hashtable<String, Object> model = new Hashtable<>();

	private final FreemarkerBeans beans = new FreemarkerBeans();
	private final FreemarkerTemplateService service = new FreemarkerTemplateService(beans);
	private final MockHttpServletRequest request = new MockHttpServletRequest();
	private final MockHttpServletResponse response = new MockHttpServletResponse();
	private final MockRequestContext context = new MockRequestContext(request, response);

	public MockFreemarkerService(TemplateSource templates) {
		beans.getTemplateSourceListener().register(templates);
	}

	public MockFreemarkerService(Class<?> loader, String prefix) {
		beans.getTemplateSourceListener().register(new ClasspathTemplateSource(loader, prefix));
	}

	public void register(TemplateGlobal global) {
		beans.getGlobalsIndex().register(global);
	}

	public void put(String key, Object value) {
		model.put(key, value);
	}

	public void putAll(Map<String, Object> data) {
		model.putAll(data);
	}

	public void addParameter(String key, String value) {
		request.addParameter(key, value);
	}

	public String render(String template) {
		model.putIfAbsent("lightning", new MockLightning());
		return StreamTool.toString(service.render(template, model, context));
	}
}