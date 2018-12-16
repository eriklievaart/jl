package com.eriklievaart.javalightning.bundle.api.page;

import java.util.function.BiPredicate;

import com.eriklievaart.javalightning.bundle.api.RequestContext;

public interface PageService {

	public String getPrefix();

	public Route[] getRoutes();

	public BiPredicate<Route, RequestContext> getAccessible();
}
