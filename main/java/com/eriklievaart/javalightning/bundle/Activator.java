package com.eriklievaart.javalightning.bundle;

import java.util.Dictionary;

import javax.servlet.Servlet;

import org.osgi.framework.BundleContext;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.RouteService;
import com.eriklievaart.javalightning.bundle.route.UnavoidableRedirects;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;
import com.eriklievaart.osgi.toolkit.api.ContextWrapper;
import com.eriklievaart.toolkit.io.api.UrlTool;

public class Activator extends ActivatorWrapper {
	private static final String HOST = "com.eriklievaart.javalightning.bundle.host";
	private static final String SERVLET_PREFIX = "com.eriklievaart.javalightning.bundle.servlet_prefix";
	private static final String REDIRECT_HOME = "com.eriklievaart.javalightning.bundle.home";
	private static final String REDIRECT_FAVICON = "com.eriklievaart.javalightning.bundle.favicon";

	@Override
	public void init(BundleContext context) throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setContext(getBundleContext());

		String prefix = getContextWrapper().getPropertyString(SERVLET_PREFIX, "");
		beans.setServletPrefix(prefix);
		beans.setHost(getContextWrapper().getPropertyString(HOST, "localhost:8000"));
		ContentServlet servlet = new ContentServlet(beans, getRedirects(getContextWrapper()));

		String pattern = UrlTool.append(prefix, "*");
		Dictionary<String, ?> props = dictionary(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, pattern);

		addServiceWithCleanup(Servlet.class, servlet, props);
		addServiceWithCleanup(RouteService.class, beans.getRouteService());
		addWhiteboardWithCleanup(PageService.class, beans.getRouteIndex());
	}

	private UnavoidableRedirects getRedirects(ContextWrapper wrapper) {
		String home = wrapper.getPropertyString(REDIRECT_HOME, "/web/");
		String favicon = wrapper.getPropertyString(REDIRECT_FAVICON, "/web/static/favicon.ico");
		return new UnavoidableRedirects(home, favicon);
	}
}
