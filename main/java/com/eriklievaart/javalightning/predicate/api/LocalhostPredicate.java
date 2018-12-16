package com.eriklievaart.javalightning.predicate.api;

import java.util.function.BiPredicate;

import javax.servlet.http.HttpServletRequest;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.page.Route;

public class LocalhostPredicate implements BiPredicate<Route, RequestContext> {
	@Override
	public boolean test(Route route, RequestContext context) {
		HttpServletRequest request = context.getRequest();
		return request.getRemoteHost().equals("127.0.0.1");
	}
}
