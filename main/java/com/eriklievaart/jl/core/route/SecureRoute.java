package com.eriklievaart.jl.core.route;

import com.eriklievaart.jl.core.api.RequestContext;
import com.eriklievaart.jl.core.api.page.PageController;
import com.eriklievaart.jl.core.api.page.PageSecurity;
import com.eriklievaart.jl.core.api.page.Route;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class SecureRoute {

	private final Route route;
	private final PageSecurity security;

	public SecureRoute(Route route, PageSecurity security) {
		this.route = route;
		this.security = security;
	}

	public boolean isAccessible(RequestContext context) {
		return security.getAccessible().test(route, context);
	}

	public PageController getController(RequestContext context) {
		Check.isTrue(isAccessible(context), "route not accessible $", route);
		return route.createController();
	}

	public Route getRoute() {
		return route;
	}

	public void validate(RequestContext context) throws Exception {
		if (!isAccessible(context)) {
			security.getOnDeny().invoke();
		}
	}
}