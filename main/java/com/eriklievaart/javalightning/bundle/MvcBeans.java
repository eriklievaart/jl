package com.eriklievaart.javalightning.bundle;

import org.osgi.framework.BundleContext;

import com.eriklievaart.javalightning.bundle.api.page.RouteService;
import com.eriklievaart.javalightning.bundle.route.RouteIndex;
import com.eriklievaart.javalightning.bundle.route.RouteServiceImpl;

public class MvcBeans {

	private RouteIndex routes = new RouteIndex();
	private BundleContext context;

	public BundleContext getContext() {
		return context;
	}

	public void setContext(BundleContext context) {
		this.context = context;
	}

	public RouteIndex getRouteIndex() {
		return routes;
	}

	public RouteService getRouteService() {
		return new RouteServiceImpl(routes);
	}

	public void setServletPrefix(String prefix) {
		routes.setServletPrefix(prefix);
	}
}
