package com.eriklievaart.javalightning.route;

import java.util.List;
import java.util.Map;

import com.eriklievaart.javalightning.control.PassThroughController;
import com.eriklievaart.javalightning.control.ReflectionPageController;
import com.eriklievaart.javalightning.control.RequestController;
import com.eriklievaart.toolkit.logging.api.LogTemplate;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.reflect.api.string.ReflectionRepresentation;
import com.eriklievaart.toolkit.reflect.api.string.ReflectionRepresentationTool;

public class RouteConfigReader {

	private final Map<Route, RequestController> routes = NewCollection.hashtable();
	private final Map<Route, RequestController> wildcards = NewCollection.orderedMap();

	private final LogTemplate log = new LogTemplate(getClass());

	public RouteConfigReader(List<RouteMapping> mappings) {
		for (RouteMapping mapping : mappings) {
			addMapping(mapping.getRoute(), mapping.getAction());
		}
	}

	private void addMapping(Route route, String action) {
		if (action.equals("*")) {
			log.info("Adding PassThroughController for % => %", route, action);
			addRoute(route, new PassThroughController());

		} else {
			ReflectionRepresentation invoke = ReflectionRepresentationTool.fromString(action);
			log.info("Adding ReflectionpageController for % => %", route, action);
			addRoute(route, new ReflectionPageController(invoke.getLiteralName(), invoke.getMemberName()));
		}
	}

	private void addRoute(Route route, RequestController controller) {

		if (route.getPath().contains("*")) {
			Check.isFalse(wildcards.containsKey(route), "% duplicate entry", route);
			wildcards.put(route, controller);
		} else {
			Check.isFalse(routes.containsKey(route), "% duplicate entry", route);
			routes.put(route, controller);
		}
	}

	public Map<Route, RequestController> getRoutes() {
		return routes;
	}

	public Map<Route, RequestController> getWildcards() {
		return wildcards;
	}

}
