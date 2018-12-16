package com.eriklievaart.javalightning.bundle.api.page;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.exception.RouteUnavailableException;

public interface RouteService {

	public Route getRoute(String service, String route) throws RouteUnavailableException;

	public String getRemotePath(String service, String route) throws RouteUnavailableException;

	public boolean isAccessible(String service, String route, RequestContext context) throws RouteUnavailableException;
}
