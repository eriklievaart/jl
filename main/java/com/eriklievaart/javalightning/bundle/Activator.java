package com.eriklievaart.javalightning.bundle;

import java.util.Hashtable;

import javax.servlet.Servlet;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.osgi.toolkit.api.BundleWrapper;
import com.eriklievaart.osgi.toolkit.api.Whiteboard;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class Activator implements BundleActivator {
	private static final String SERVLET_PATTERN = "com.eriklievaart.javalightning.bundle.servlet_pattern";

	private LogTemplate log = new LogTemplate(getClass());

	private ServiceRegistration<Servlet> servlet;
	private Whiteboard<PageService> pageWhiteboard;
	private MvcBeans beans = new MvcBeans();

	@Override
	public void start(BundleContext context) throws Exception {
		log.info("starting bundle javalightning");

		beans.setContext(context);
		pageWhiteboard = new Whiteboard<>(context, PageService.class);
		pageWhiteboard.addListener(beans.getRouteIndex());

		Hashtable<String, String> props = new Hashtable<>();
		String pattern = new BundleWrapper(context).getPropertyString(SERVLET_PATTERN, "/mvc/*");
		props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, pattern);
		servlet = context.registerService(Servlet.class, new ContentServlet(beans), props);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		pageWhiteboard.shutdown();
		servlet.unregister();
	}
}
