package com.eriklievaart.jl.bundle.api.page;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

import com.eriklievaart.toolkit.lang.api.check.Check;

public class RouteBuilder {

	private String id;
	private List<Route> routes;

	public RouteBuilder(String id, List<Route> routes) {
		this.id = id;
		this.routes = routes;
	}

	public void mapGet(String path, Supplier<PageController> supplier) {
		map(path, RouteType.GET, supplier);
	}

	public void mapPost(String path, Supplier<PageController> supplier) {
		map(path, RouteType.POST, supplier);
	}

	public void mapGetAndPost(String path, Supplier<PageController> supplier) {
		map(path, EnumSet.of(RouteType.GET, RouteType.POST), supplier);
	}

	public void mapAll(String path, Supplier<PageController> supplier) {
		map(path, EnumSet.allOf(RouteType.class), supplier);
	}

	public void map(String path, RouteType method, Supplier<PageController> supplier) {
		map(path, EnumSet.of(method), supplier);
	}

	public void map(String path, EnumSet<RouteType> types, Supplier<PageController> supplier) {
		for (Route route : routes) {
			Check.isFalse(route.getId().equals(id), "route with id % already created", id);
		}
		RouteAddress address = new RouteAddress(path, types);
		routes.add(new Route(id, address, supplier));
	}
}