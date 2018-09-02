package com.eriklievaart.javalightning.route;

import org.junit.Test;

import com.eriklievaart.toolkit.lang.api.check.Check;

public class RouteU {

	@Test
	public void matchesWildcardYes() {
		Route route = new Route(RouteType.GET, "/static/style.css");
		Route wildcard = new Route(RouteType.GET, "/static/*");
		Check.isTrue(route.matchesWildcard(wildcard));
	}

	@Test
	public void matchesWildcardNo() {
		Route route = new Route(RouteType.GET, "/static/style.css");
		Route wildcard = new Route(RouteType.GET, "/app/*");
		Check.isFalse(route.matchesWildcard(wildcard));
	}

}
