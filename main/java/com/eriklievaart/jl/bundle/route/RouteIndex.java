package com.eriklievaart.jl.bundle.route;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.eriklievaart.jl.bundle.api.RequestContext;
import com.eriklievaart.jl.bundle.api.exception.NotFound404Exception;
import com.eriklievaart.jl.bundle.api.page.PageSecurity;
import com.eriklievaart.jl.bundle.api.page.PageService;
import com.eriklievaart.jl.bundle.api.page.Route;
import com.eriklievaart.jl.bundle.api.page.RouteType;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.AssertionException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.pattern.WildcardTool;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class RouteIndex {
	private LogTemplate log = new LogTemplate(getClass());

	private final String serviceId;
	private final PageSecurity security;
	private Map<String, Route> pathToRoute = NewCollection.concurrentHashMap();
	private Map<String, Route> idToRoute = NewCollection.concurrentHashMap();
	private List<Route> wildcardMappings = NewCollection.concurrentList();

	public RouteIndex(PageService service) {
		Check.notNull(service.getPrefix(), service.getSecurity());

		this.serviceId = service.getPrefix();
		this.security = service.getSecurity();

		for (Route route : service.getRoutes()) {
			installRoute(route);
		}
	}

	private void installRoute(Route route) {
		String path = route.getPath();
		AssertionException.on(idToRoute.containsKey(route.getId()), "duplicate id %", path);
		AssertionException.on(pathToRoute.containsKey(path), "duplicate path %", path);
		idToRoute.put(route.getId(), route);

		if (route.getPath().contains("*")) {
			wildcardMappings.add(route);

		} else {
			for (RouteType method : route.getTypes()) {
				log.trace("installing route $:/$/$", method, serviceId, path);
				String key = createKey(method, path);
				pathToRoute.put(key, route);
			}
		}
	}

	public Optional<SecureRoute> resolve(RouteType method, String path) {
		String key = createKey(method, UrlTool.removeTrailingSlash(path));
		if (pathToRoute.containsKey(key)) {
			return Optional.of(new SecureRoute(pathToRoute.get(key), security));
		}
		for (Route route : wildcardMappings) {
			if (route.getTypes().contains(method) && WildcardTool.match(route.getPath(), path == null ? "" : path)) {
				return Optional.of(new SecureRoute(route, security));
			}
		}
		log.debug("no controller for path $:$ $", method, path, pathToRoute.keySet());
		return Optional.empty();
	}

	private String createKey(RouteType method, String path) {
		Check.notNull(method);
		return Str.sub("$:$", method, path == null ? "" : path);
	}

	public Route getRoute(String routeId) throws NotFound404Exception {
		Route route = idToRoute.get(routeId);
		if (route == null) {
			throw new NotFound404Exception(getUnavailableMessage(routeId));
		}
		return route;
	}

	boolean isAccessible(String routeId, RequestContext context) throws NotFound404Exception {
		if (!idToRoute.containsKey(routeId)) {
			throw new NotFound404Exception(getUnavailableMessage(routeId));
		}
		return security.test(idToRoute.get(routeId), context);
	}

	private String getUnavailableMessage(String id) {
		return Str.sub("No route with id % in service %", id, serviceId);
	}
}