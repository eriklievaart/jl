package com.eriklievaart.javalightning.bundle;

import java.util.Dictionary;

import javax.servlet.Servlet;

import org.osgi.framework.BundleContext;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.RouteService;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class Activator extends ActivatorWrapper {
	private static final String SERVLET_PREFIX = "com.eriklievaart.javalightning.bundle.servlet_prefix";
	private static final String REDIRECT_HOME = "com.eriklievaart.javalightning.bundle.home";

	@Override
	public void init(BundleContext context) throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setContext(getBundleContext());

		addWhiteboardWithCleanup(PageService.class, beans.getRouteIndex());
		addServiceWithCleanup(RouteService.class, beans.getRouteService());

		String prefix = getContextWrapper().getPropertyString(SERVLET_PREFIX, "");
		if (Str.notBlank(prefix)) {
			beans.setServletPrefix(prefix);
		}
		String pattern = UrlTool.append(prefix, "*");
		String home = getContextWrapper().getPropertyString(REDIRECT_HOME, "/web/");
		Dictionary<String, ?> props = dictionary(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, pattern);
		addServiceWithCleanup(Servlet.class, new ContentServlet(beans, home), props);
	}
}
