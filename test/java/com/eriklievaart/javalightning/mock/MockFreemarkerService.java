package com.eriklievaart.javalightning.mock;

import java.util.Hashtable;

import com.eriklievaart.javalightning.bundle.api.template.ClasspathTemplateSource;
import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.javalightning.bundle.api.template.TemplateSource;
import com.eriklievaart.javalightning.freemarker.FreemarkerBeans;
import com.eriklievaart.javalightning.freemarker.FreemarkerTemplateService;
import com.eriklievaart.toolkit.io.api.StreamTool;

public class MockFreemarkerService {
	private Hashtable<String, Object> model = new Hashtable<>();

	private final FreemarkerBeans beans = new FreemarkerBeans();
	private final FreemarkerTemplateService service = new FreemarkerTemplateService(beans);

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

	public String render(String template) {
		MockRequestContext context = MockRequestContext.instance();
		model.putIfAbsent("lightning", new MockLightning());
		return StreamTool.toString(service.render(template, model, context));
	}
}
