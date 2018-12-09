package com.eriklievaart.javalightning.bundle.route;

import com.eriklievaart.javalightning.bundle.api.exception.RouteUnavailableException;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.bundle.api.page.RouteService;

public class RouteServiceImpl implements RouteService {

	private RouteIndex routes;

	public RouteServiceImpl(RouteIndex routes) {
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
}
