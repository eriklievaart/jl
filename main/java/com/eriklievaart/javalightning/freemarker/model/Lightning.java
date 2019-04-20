package com.eriklievaart.javalightning.freemarker.model;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.exception.RouteUnavailableException;
import com.eriklievaart.javalightning.bundle.api.page.RouteService;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class Lightning {
	private LogTemplate log = new LogTemplate(getClass());

	private RequestContext context;

	public Lightning(RequestContext context) {
		this.context = context;
	}

	public String getRemotePath(String service, String route) {
		log.trace("getting remote path $.$", service, route);
		return context.getServiceCollection(RouteService.class).oneReturns(s -> {
			try {
				return s.getRemotePath(service, route);
			} catch (RouteUnavailableException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		});
	}

	public boolean isRouteAccessible(String service, String route) {
		return context.getServiceCollection(RouteService.class).oneReturns(s -> {
			try {
				return s.isAccessible(service, route, context);

			} catch (RouteUnavailableException e) {
				log.warn("Unable to find route $:$", service, route);
				return false;
			}
		});
	}
}
