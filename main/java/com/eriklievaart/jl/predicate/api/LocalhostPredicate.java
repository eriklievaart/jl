package com.eriklievaart.jl.predicate.api;

import java.util.function.BiPredicate;

import javax.servlet.http.HttpServletRequest;

import com.eriklievaart.jl.bundle.api.RequestContext;
import com.eriklievaart.jl.bundle.api.page.Route;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class LocalhostPredicate implements BiPredicate<Route, RequestContext> {
	@Override
	public boolean test(Route route, RequestContext context) {
		return isLocalhost(context.getRequest());
	}

	public static boolean isLocalhost(HttpServletRequest request) {
		String domain = UrlTool.getDomain(request.getRequestURL().toString());
		new LogTemplate(LocalhostPredicate.class).trace("domain %", domain);
		return domain.matches("(127.0.0.1|localhost)(:\\d++)?");
	}
}