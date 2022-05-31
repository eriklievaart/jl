package com.eriklievaart.jl.core.api.page;

import java.util.Map;

import com.eriklievaart.jl.core.api.RequestContext;
import com.eriklievaart.jl.core.api.exception.NotFound404Exception;

public interface RouteService {

	public String getRemoteAddress();

	public String getHost();

	public boolean isAccessible(String service, String route, RequestContext context) throws NotFound404Exception;

	public Route getRoute(String service, String route) throws NotFound404Exception;

	public String getRemotePath(String service, String route) throws NotFound404Exception;

	public String getRemotePath(String service, String route, Map<String, String> params)
			throws NotFound404Exception;
}