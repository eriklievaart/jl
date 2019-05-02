package com.eriklievaart.javalightning.predicate.api;

import java.util.function.BiPredicate;

import javax.servlet.http.HttpServletRequest;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.page.Route;

public class LocalhostPredicate implements BiPredicate<Route, RequestContext> {
	@Override
	public boolean test(Route route, RequestContext context) {
		return isLocalhost(context.getRequest());
	}

	public static boolean isLocalhost(HttpServletRequest request) {
		return request.getRemoteHost().equals("127.0.0.1");
	}
}
