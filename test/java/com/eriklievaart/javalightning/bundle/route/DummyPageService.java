package com.eriklievaart.javalightning.bundle.route;

import java.util.List;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class DummyPageService implements PageService {

	private final String prefix;
	private final List<Route> routes = NewCollection.list();

	public DummyPageService(String prefix, Route... r) {
		this.prefix = prefix;

		for (Route route : r) {
			routes.add(route);
		}
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public Route[] getRoutes() {
		return routes.toArray(new Route[] {});
	}

}
