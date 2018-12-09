package com.eriklievaart.javalightning.bundle.api.page;

import java.util.List;

import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class PageServiceBuilder {

	private final List<Route> routes = NewCollection.list();

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

		return new PageService() {
			@Override
			public String getPrefix() {
				return name;
			}

			@Override
			public Route[] getRoutes() {
				return routes.toArray(new Route[] {});
			}
		};
	}
}
