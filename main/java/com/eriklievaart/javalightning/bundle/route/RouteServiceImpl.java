package com.eriklievaart.javalightning.bundle.route;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.exception.RouteUnavailableException;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.bundle.api.page.RouteService;
import com.eriklievaart.toolkit.io.api.UrlTool;

public class RouteServiceImpl implements RouteService {

	private PageServiceIndex routes;

	public RouteServiceImpl(PageServiceIndex routes) {
		this.routes = routes;
	}

	@Override
	public Route getRoute(String service, String route) throws RouteUnavailableException {
		return routes.getRoute(service, route);
	}

	@Override
	public String getRemotePath(String service, String route) throws RouteUnavailableException {
		return routes.getRemotePath(service, route);
	}

	@Override
	public boolean isAccessible(String service, String route, RequestContext context) throws RouteUnavailableException {
		return routes.isAccessible(service, route, context);
	}

	@Override
	public String getRemoteAddress() {
		return UrlTool.append(routes.getHost(), routes.getServletPrefix());
	}

	@Override
	public String getHost() {
		return routes.getHost();
	}
}
