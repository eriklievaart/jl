package com.eriklievaart.javalightning.zdemo;

import java.util.Date;

import org.osgi.framework.BundleContext;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.template.ClasspathTemplateSource;
import com.eriklievaart.javalightning.bundle.api.template.TemplateSource;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;

public class Activator extends ActivatorWrapper {

	@Override
	public void init(BundleContext context) throws Exception {
		ClasspathTemplateSource cts = new ClasspathTemplateSource(getClass(), "zdemo");
		cts.addGlobal("date", () -> new Date());

		addServiceWithCleanup(TemplateSource.class, cts);
		addServiceWithCleanup(PageService.class, new ExamplePageService());
	}
}