package com.eriklievaart.javalightning.bundle.route;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.eriklievaart.javalightning.bundle.api.exception.RouteUnavailableException;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.AssertionException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.pattern.WildcardTool;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class ServiceIndex {

	private final String serviceId;
	private Map<String, Route> pathToRoute = NewCollection.concurrentHashMap();
	private Map<String, Route> idToRoute = NewCollection.concurrentHashMap();
	private List<Route> wildcardMappings = NewCollection.concurrentList();

	public ServiceIndex(PageService service) {
		serviceId = service.getPrefix();
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
				pathToRoute.put(createKey(method, path), route);
			}
		}
	}

	public Optional<PageController> resolve(RouteType method, String path) {
		String key = createKey(method, UrlTool.removeTrailingSlash(path));
		if (pathToRoute.containsKey(key)) {
			return Optional.of(pathToRoute.get(key).createController());
		}
		for (Route route : wildcardMappings) {
			if (route.getTypes().contains(method) && WildcardTool.match(route.getPath(), path == null ? "" : path)) {
				return Optional.of(route.createController());
			}
		}
		return Optional.empty();
	}

	private String createKey(RouteType method, String path) {
		Check.notNull(method);
		return Str.sub("$:$", method, path == null ? "" : path);
	}

	public Route getRoute(String id) throws RouteUnavailableException {
		Route route = idToRoute.get(id);
		if (route == null) {
			throw new RouteUnavailableException("No route with id % in service %", id, serviceId);
		}
		return route;
	}
}
