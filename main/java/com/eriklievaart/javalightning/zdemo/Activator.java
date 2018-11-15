package com.eriklievaart.javalightning.zdemo;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.template.ClasspathTemplateSource;
import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.javalightning.bundle.api.template.TemplateSource;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class Activator implements BundleActivator {
	private LogTemplate log = new LogTemplate(getClass());

	private ServiceRegistration<PageService> pages;
	private ServiceRegistration<TemplateGlobal> global;
	private ServiceRegistration<TemplateSource> templates;

	@Override
	public void start(BundleContext context) throws Exception {
		log.info("starting bundle zdemo");
		new Hashtable<>().put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, "/mvc/*");

		ClasspathTemplateSource templateSource = new ClasspathTemplateSource(getClass(), "zdemo");
		templates = context.registerService(TemplateSource.class, templateSource, new Hashtable<>());
		global = context.registerService(TemplateGlobal.class, new DateGlobal(), new Hashtable<>());
		pages = context.registerService(PageService.class, new ExamplePageService(), new Hashtable<>());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		pages.unregister();
		global.unregister();
		templates.unregister();
	}
}