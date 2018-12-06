package com.eriklievaart.javalightning.bundle;

import java.util.Dictionary;

import javax.servlet.Servlet;

import org.osgi.framework.BundleContext;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;

public class Activator extends ActivatorWrapper {
	private static final String SERVLET_PATTERN = "com.eriklievaart.javalightning.bundle.servlet_pattern";

	@Override
	public void init(BundleContext context) throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setContext(getBundleContext());

		addWhiteboardWithCleanup(PageService.class, beans.getRouteIndex());

		String pattern = getContextWrapper().getPropertyString(SERVLET_PATTERN, "/mvc/*");
		Dictionary<String, ?> props = dictionary(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, pattern);
		addServiceWithCleanup(Servlet.class, new ContentServlet(beans), props);
	}
}
