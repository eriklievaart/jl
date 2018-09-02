package com.eriklievaart.javalightning.route;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.eriklievaart.javalightning.control.RequestController;
import com.eriklievaart.toolkit.lang.api.FormattedException;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class Router {

	private final Map<Route, RequestController> routes;
	private final Map<Route, RequestController> wildcards;
	private final List<Route> wildcardOrder;

	/**
	 * Initialise the Router, should only be called once!
	 */
	public Router(List<RouteMapping> mappings) {
		RouteConfigReader config = new RouteConfigReader(mappings);

		routes = config.getRoutes();

		Map<Route, RequestController> w = config.getWildcards();
		wildcards = new Hashtable<>(w);
		wildcardOrder = new CopyOnWriteArrayList<>(w.keySet());
	}

	public RequestController getController(Route route) {
		Check.notNull(route, "Route cannot be null!");

		RequestController provider = routes.get(route);
		if (provider != null) {
			return provider;
		}
		for (Route wildcard : wildcardOrder) {
			if (route.matchesWildcard(wildcard)) {
				return wildcards.get(wildcard);
			}
		}
		throw new FormattedException("Controller unavailable for %, add catch all route!", route);
	}

	public boolean isRoute(Route route) {
		return routes.containsKey(route);
	}
}
