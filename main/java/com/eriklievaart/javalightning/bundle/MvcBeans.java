package com.eriklievaart.javalightning.bundle;

import org.osgi.framework.BundleContext;

import com.eriklievaart.javalightning.bundle.route.RouteIndex;

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

}
