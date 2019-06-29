package com.eriklievaart.javalightning.bundle.api.page;

import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.exception.RouteUnavailableException;

public interface RouteService {

	public String getRemoteAddress();

	public String getHost();

	public boolean isAccessible(String service, String route, RequestContext context) throws RouteUnavailableException;

	public Route getRoute(String service, String route) throws RouteUnavailableException;

	public String getRemotePath(String service, String route) throws RouteUnavailableException;

	public String getRemotePath(String service, String route, Map<String, String> params)
			throws RouteUnavailableException;
}
