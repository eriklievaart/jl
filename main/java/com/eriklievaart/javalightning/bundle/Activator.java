package com.eriklievaart.javalightning.bundle;

import java.io.File;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.servlet.Servlet;

import org.osgi.framework.BundleContext;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.RouteService;
import com.eriklievaart.javalightning.bundle.rule.RuleEngine;
import com.eriklievaart.javalightning.bundle.rule.RuleEngineParser;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;
import com.eriklievaart.osgi.toolkit.api.ContextWrapper;
import com.eriklievaart.toolkit.io.api.ResourceTool;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.io.api.ini.IniNodeIO;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class Activator extends ActivatorWrapper {
	private static final String HOST = "com.eriklievaart.javalightning.bundle.host";
	private static final String HTTPS = "com.eriklievaart.javalightning.bundle.https";
	private static final String SERVLET_PREFIX = "com.eriklievaart.javalightning.bundle.servlet_prefix";
	private static final String EXCEPTION_REDIRECT = "com.eriklievaart.javalightning.bundle.exception.redirect";
	private static final String RULES = "com.eriklievaart.javalightning.bundle.rules";

	private LogTemplate log = new LogTemplate(getClass());

	@Override
	public void init(BundleContext context) throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setContext(getBundleContext());

		String prefix = getContextWrapper().getPropertyString(SERVLET_PREFIX, "");
		beans.setServletPrefix(prefix);
		beans.setHost(getContextWrapper().getPropertyString(HOST, "localhost:8000"));
		beans.setExceptionRedirect(getContextWrapper().getPropertyString(EXCEPTION_REDIRECT, ""));
		beans.setHttps(getContextWrapper().getPropertyBoolean(HTTPS, false));
		ContentServlet servlet = new ContentServlet(beans, createRulesEngine(getContextWrapper()));

		addServiceWithCleanup(Servlet.class, servlet, getOsgiPropertiesServlet(prefix));
		addServiceWithCleanup(RouteService.class, beans.getRouteService());
		addWhiteboardWithCleanup(PageService.class, beans.getPageServiceIndex());
	}

	private Dictionary<String, Object> getOsgiPropertiesServlet(String prefix) {
		Dictionary<String, Object> props = new Hashtable<>();
		props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, UrlTool.append(prefix, "*"));
		props.put("osgi.http.whiteboard.servlet.multipart.enabled", true);
		return props;
	}

	private RuleEngine createRulesEngine(ContextWrapper wrapper) {
		String config = wrapper.getPropertyString(RULES, "");
		if (Str.isBlank(config)) {
			log.info("using default rules, override with osgi property %", RULES);
			String path = "/bundle/rules-defaults.ini";
			return RuleEngineParser.parse(IniNodeIO.read(ResourceTool.getInputStream(getClass(), path)));
		} else {
			log.info("loading rules from: $", config);
			return RuleEngineParser.parse(IniNodeIO.read(new File(config)));
		}
	}
}