package com.eriklievaart.javalightning.route;

import com.eriklievaart.toolkit.lang.api.ToString;

public class RouteMapping {

	private final Route route;
	private final String action;

	public RouteMapping(Route route, String action) {
		this.route = route;
		this.action = action;
	}

	public Route getRoute() {
		return route;
	}

	public String getAction() {
		return action;
	}

	@Override
	public String toString() {
		return ToString.simple(this, "$[$ => $]", route, action);
	}
}
