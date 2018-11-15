package com.eriklievaart.javalightning.freemarker;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.javalightning.bundle.api.template.TemplateService;
import com.eriklievaart.javalightning.bundle.api.template.TemplateSource;
import com.eriklievaart.osgi.toolkit.api.BundleWrapper;
import com.eriklievaart.osgi.toolkit.api.Whiteboard;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class Activator implements BundleActivator {
	private LogTemplate log = new LogTemplate(getClass());

	private ServiceRegistration<TemplateService> registration;
	private Whiteboard<TemplateSource> templateWhiteboard;
	private Whiteboard<TemplateGlobal> globalWhiteboard;

	@Override
	public void start(BundleContext context) throws Exception {
		log.info("starting freemarker template service");

		FreemarkerBeans beans = new FreemarkerBeans();
		beans.getFreemarkerOsgiConfig().config(new BundleWrapper(context));

		templateWhiteboard = new Whiteboard<>(context, TemplateSource.class);
		templateWhiteboard.addListener(beans.getTemplateSourceListener());
		globalWhiteboard = new Whiteboard<>(context, TemplateGlobal.class);
		globalWhiteboard.addListener(beans.getGlobalsListener());

		FreemarkerTemplateService service = new FreemarkerTemplateService(beans);
		Hashtable<String, String> props = new Hashtable<>();
		registration = context.registerService(TemplateService.class, service, props);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		registration.unregister();
		templateWhiteboard.shutdown();
	}
}
