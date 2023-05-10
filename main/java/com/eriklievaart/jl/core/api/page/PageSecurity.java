package com.eriklievaart.jl.core.api.page;

import java.util.function.BiPredicate;

import com.eriklievaart.jl.core.api.RequestContext;
import com.eriklievaart.jl.core.api.exception.NotFound404Exception;
import com.eriklievaart.toolkit.lang.api.function.TryRunnable;

public class PageSecurity {
	public static PageSecurity allowAll = new PageSecurity((r, c) -> true);

	private final BiPredicate<Route, RequestContext> accessible;
	private final TryRunnable<?> onDeny;

	public PageSecurity(BiPredicate<Route, RequestContext> accessible) {
		this(accessible, () -> {
			throw new NotFound404Exception("access denied for");
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