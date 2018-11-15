package com.eriklievaart.javalightning.bundle.route;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

	private Map<String, Route> mapping = NewCollection.concurrentHashMap();
	private List<Route> wildcardMappings = NewCollection.concurrentList();

	public ServiceIndex(PageService service) {
		for (Route route : service.getRoutes()) {
			installRoute(route);
		}
	}

	private void installRoute(Route route) {
		String path = route.getPath();
		AssertionException.on(mapping.containsKey(path), "duplicate entry", path);

		if (route.getPath().contains("*")) {
			wildcardMappings.add(route);

		} else {
			for (RouteType method : route.getTypes()) {
				mapping.put(createKey(method, path), route);
			}
		}
	}

	public Optional<PageController> resolve(RouteType method, String path) {
		String key = createKey(method, UrlTool.removeTrailingSlash(path));
		if (mapping.containsKey(key)) {
			return Optional.of(mapping.get(key).createController());
		}
		for (Route route : wildcardMappings) {
			if (route.getTypes().contains(method) && WildcardTool.match(route.getPath(), path)) {
				return Optional.of(route.createController());
			}
		}
		return Optional.empty();
	}

	private String createKey(RouteType method, String path) {
		Check.notNull(method);
		Check.notNull(path);
		return Str.sub("$:$", method, path);
	}
}
