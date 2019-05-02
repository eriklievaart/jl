package com.eriklievaart.javalightning.bundle.api.page;

import java.util.function.BiPredicate;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.route.RouteNotAccessibleException;
import com.eriklievaart.toolkit.lang.api.function.TryRunnable;

public class PageSecurity {

	private final BiPredicate<Route, RequestContext> accessible;
	private final TryRunnable<?> onDeny;

	public PageSecurity(BiPredicate<Route, RequestContext> accessible) {
		this(accessible, () -> {
			throw new RouteNotAccessibleException();
		});
	}

	public PageSecurity(BiPredicate<Route, RequestContext> accessible, TryRunnable<?> onDeny) {
		this.accessible = accessible;
		this.onDeny = onDeny;
	}

	public BiPredicate<Route, RequestContext> getAccessible() {
		return accessible;
	}

	public TryRunnable<?> getOnDeny() {
		return onDeny;
	}

	public boolean test(Route route, RequestContext context) {
		return accessible.test(route, context);
	}
}