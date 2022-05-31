package com.eriklievaart.jl.bundle;

import org.osgi.framework.BundleContext;

import com.eriklievaart.jl.bundle.api.page.RouteService;
import com.eriklievaart.jl.bundle.route.PageServiceIndex;
import com.eriklievaart.jl.bundle.route.RouteServiceImpl;

public class MvcBeans {

	private PageServiceIndex routes = new PageServiceIndex();
	private WebSocketIndex sockets = new WebSocketIndex();
	private BundleContext context;

	public BundleContext getContext() {
		return context;
	}

	public void setContext(BundleContext context) {
		this.context = context;
	}

	public PageServiceIndex getPageServiceIndex() {
		return routes;
	}

	public WebSocketIndex getWebSocketIndex() {
		return sockets;
	}

	public RouteService getRouteService() {
		return new RouteServiceImpl(routes);
	}

	public void setServletPrefix(String prefix) {
		routes.setServletPrefix(prefix);
	}

	public void setHost(String host) {
		routes.setHost(host);
	}

	public void setHttps(boolean value) {
		routes.setHttps(value);
	}

	public void setExceptionRedirect(String redirect) {
		routes.setExceptionRedirect(redirect);
	}
}