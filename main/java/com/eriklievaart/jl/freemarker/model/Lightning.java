package com.eriklievaart.jl.freemarker.model;

import java.util.Map;

import com.eriklievaart.jl.core.api.RequestContext;
import com.eriklievaart.jl.core.api.exception.NotFound404Exception;
import com.eriklievaart.jl.core.api.page.RouteService;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class Lightning {
	private LogTemplate log = new LogTemplate(getClass());

	private RequestContext context;

	public Lightning(RequestContext context) {
		this.context = context;
	}

	/** returns remote path for current URL. */
	public String getRemotePath() {
		return context.getServiceCollection(RouteService.class).oneReturns(s -> {
			return s.getRemoteAddress();
		});
	}

	public String getRemotePath(String service, String route) {
		log.trace("getting remote path $.$", service, route);
		return context.getServiceCollection(RouteService.class).oneReturns(s -> {
			try {
				return s.getRemotePath(service, route);
			} catch (NotFound404Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		});
	}

	public String getRemotePath(String service, String route, Map<String, String> parameters) {
		log.trace("getting remote path $.$", service, route);
		return context.getServiceCollection(RouteService.class).oneReturns(s -> {
			try {
				return s.getRemotePath(service, route, parameters);
			} catch (NotFound404Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		});
	}

	public boolean isRouteAccessible(String service, String route) {
		return context.getServiceCollection(RouteService.class).oneReturns(s -> {
			try {
				return s.isAccessible(service, route, context);

			} catch (NotFound404Exception e) {
				log.warn("Unable to find route $:$", service, route);
				return false;
			}
		});
	}
}