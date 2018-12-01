package com.eriklievaart.javalightning.bundle.api.osgi;

import java.util.List;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.bundle.api.template.ClasspathTemplateSource;
import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.javalightning.bundle.api.template.TemplateSource;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;
import com.eriklievaart.toolkit.lang.api.check.Check;

public abstract class LightningActivator extends ActivatorWrapper {

	private String name;

	public LightningActivator(String name) {
		Check.matches(name, "[a-z]++");
		this.name = name;
	}

	public void addTemplateSource() {
		addServiceWithCleanup(TemplateSource.class, new ClasspathTemplateSource(getClass(), name));
	}

	public void addTemplateSource(TemplateGlobal... globals) {
		addServiceWithCleanup(TemplateSource.class, new ClasspathTemplateSource(getClass(), name, globals));
	}

	public void addTemplateSource(TemplateSource source) {
		addServiceWithCleanup(TemplateSource.class, source);
	}

	public void addPageService(List<Route> routes) {
		addPageService(routes.toArray(new Route[] {}));
	}

	public void addPageService(Route... routes) {
		addServiceWithCleanup(PageService.class, new PageService() {
			@Override
			public String getPrefix() {
				return name;
			}

			@Override
			public Route[] getRoutes() {
				return routes;
			}
		});
	}
}
