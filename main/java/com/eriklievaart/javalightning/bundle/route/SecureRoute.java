package com.eriklievaart.javalightning.bundle.route;

import java.util.function.BiPredicate;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class SecureRoute {

	private Route route;
	private BiPredicate<Route, RequestContext> predicate;

	public SecureRoute(Route route, BiPredicate<Route, RequestContext> predicate) {
		this.route = route;
		this.predicate = predicate;
	}

	public boolean isAccessible(RequestContext context) {
		return predicate.test(route, context);
	}

	public PageController getController(RequestContext context) {
		Check.isTrue(isAccessible(context), "route not accessible $", route);
		return route.createController();
	}
}
