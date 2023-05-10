package com.eriklievaart.jl.core;

import java.io.File;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.Servlet;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.eriklievaart.jl.core.api.page.PageService;
import com.eriklievaart.jl.core.api.page.RouteService;
import com.eriklievaart.jl.core.api.websocket.WebSocketService;
import com.eriklievaart.jl.core.rule.RuleEngine;
import com.eriklievaart.jl.core.rule.RuleEngineParser;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;
import com.eriklievaart.osgi.toolkit.api.ContextWrapper;
import com.eriklievaart.toolkit.io.api.ResourceTool;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.io.api.ini.IniNodeIO;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class Activator extends ActivatorWrapper {
	private static final String HOST = "com.eriklievaart.jl.core.host";
	private static final String HTTPS = "com.eriklievaart.jl.core.https";
	private static final String SERVLET_PREFIX = "com.eriklievaart.jl.core.servlet_prefix";
	private static final String EXCEPTION_REDIRECT = "com.eriklievaart.jl.core.exception.redirect";
	private static final String RULES = "com.eriklievaart.jl.core.rules";

	private LogTemplate log = new LogTemplate(getClass());

	@Override
	public void init(BundleContext context) throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setContext(getBundleContext());

		beans.setServletPrefix(getServletPrefix());
		beans.setHost(getContextWrapper().getPropertyString(HOST, "localhost:8000"));
		beans.setExceptionRedirect(getContextWrapper().getPropertyString(EXCEPTION_REDIRECT, ""));
		beans.setHttps(getContextWrapper().getPropertyBoolean(HTTPS, false));

		addServiceWithCleanup(RouteService.class, beans.getRouteService());
		addWhiteboardWithCleanup(PageService.class, beans.getPageServiceIndex());
		addWhiteboardWithCleanup(WebSocketService.class, beans.getWebSocketIndex());
		addWebSocketServlet(context, beans);
	}

	private void addWebSocketServlet(BundleContext context, MvcBeans beans) throws Exception {
		ClassLoader original = Thread.currentThread().getContextClassLoader();
		ClassLoader jettyClassLoader = getJettyBundle(context).adapt(BundleWiring.class).getClassLoader();
		Thread.currentThread().setContextClassLoader(jettyClassLoader);

		try {
			ContentServlet servlet = new ContentServlet(beans, createRulesEngine(getContextWrapper()));
			addServiceWithCleanup(Servlet.class, servlet, getOsgiPropertiesServlet(getServletPrefix()));

		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		} finally {
			Thread.currentThread().setContextClassLoader(original);
		}
	}

	private String getServletPrefix() {
		return getContextWrapper().getPropertyString(SERVLET_PREFIX, "");
	}

	private Map<String, Object> getOsgiPropertiesServlet(String prefix) {
		Map<String, Object> props = new Hashtable<>();
		props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_SERVLET_PATTERN, UrlTool.append(prefix, "*"));
		props.put("osgi.http.whiteboard.servlet.multipart.enabled", true);
		return props;
	}

	private RuleEngine createRulesEngine(ContextWrapper wrapper) {
		String config = wrapper.getPropertyString(RULES, "");

		if (Str.notBlank(config)) {
			log.info("loading rules from $ as configured per osgi property %", config, RULES);
			return RuleEngineParser.parse(IniNodeIO.read(new File(config)));

		} else if (wrapper.getProjectFile("rules.ini").isFile()) {
			File file = wrapper.getProjectFile("rules.ini");
			log.info("Local rules.ini found! Loading rules from $", file);
			return RuleEngineParser.parse(IniNodeIO.read(file));

		} else {
			log.info("Using default rules, override with osgi property %", RULES);
			String path = "/core/rules-defaults.ini";
			return RuleEngineParser.parse(IniNodeIO.read(ResourceTool.getInputStream(getClass(), path)));
		}
	}

	private Bundle getJettyBundle(BundleContext context) {
		for (Bundle bundle : context.getBundles()) {
			if (bundle.getSymbolicName().equals("org.apache.felix.http.jetty")) {
				return bundle;
			}
		}
		throw new RuntimeException("Jetty not found");
	}
}