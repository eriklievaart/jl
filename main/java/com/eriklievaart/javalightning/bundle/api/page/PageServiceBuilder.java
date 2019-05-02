package com.eriklievaart.javalightning.bundle.api.page;

import java.util.List;

import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class PageServiceBuilder {

	private final List<Route> routes = NewCollection.list();
	private PageSecurity security;

	public PageServiceBuilder() {
	}

	public PageServiceBuilder(List<Route> list) {
		for (Route route : list) {
			routes.add(route);
		}
	}

	public RouteBuilder newRoute(String id) {
		return new RouteBuilder(id, routes);
	}

	public PageService createPageService(String name) {
		Check.notNull(security, "setSecurity not called");

		return new PageService() {
			@Override
			public String getPrefix() {
				return name;
			}

			@Override
			public Route[] getRoutes() {
				return routes.toArray(new Route[] {});
			}

			@Override
			public PageSecurity getSecurity() {
				return security;
			}
		};
	}

	public PageServiceBuilder setSecurity(PageSecurity secure) {
		this.security = secure;
		return this;
	}
}
