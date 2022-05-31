package com.eriklievaart.jl.bundle.api.page;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Supplier;

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

	/**
	 * Create a new route with the specified id.
	 *
	 * @return used to configure the specifics of the route (HTTP method & PageController)
	 */
	public RouteBuilder newRoute(String id) {
		return new RouteBuilder(id, routes);
	}

	/**
	 * shorthand for creating a route where the path matches the route id for all HTTP methods.
	 */
	public void newIdentityRouteAll(String id, Supplier<PageController> supplier) {
		newRoute(id).map(id, EnumSet.allOf(RouteType.class), supplier);
	}

	/**
	 * shorthand for creating a HTTP GET route where the path matches the route id.
	 */
	public void newIdentityRouteGet(String id, Supplier<PageController> supplier) {
		newRoute(id).mapGet(id, supplier);
	}

	/**
	 * shorthand for creating a HTTP POST route where the path matches the route id.
	 */
	public void newIdentityRoutePost(String id, Supplier<PageController> supplier) {
		newRoute(id).mapPost(id, supplier);
	}

	/**
	 * shorthand for creating a HTTP GET & POST route where the path matches the route id.
	 */
	public void newIdentityRouteGetAndPost(String id, Supplier<PageController> supplier) {
		newRoute(id).mapGetAndPost(id, supplier);
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