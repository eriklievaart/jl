package com.eriklievaart.javalightning.aspect;

import java.util.List;
import java.util.stream.Collectors;

import com.eriklievaart.javalightning.api.aspect.AspectController;
import com.eriklievaart.javalightning.route.Route;
import com.eriklievaart.javalightning.route.RouteMapping;
import com.eriklievaart.toolkit.lang.api.FormattedException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.logging.api.LogTemplate;
import com.eriklievaart.toolkit.reflect.api.LiteralTool;
import com.eriklievaart.toolkit.reflect.api.ReflectException;

public class AspectMapper {
	private LogTemplate log = new LogTemplate(getClass());

	private List<RouteMapping> mappings;

	public AspectMapper(List<RouteMapping> mappings) {
		this.mappings = mappings;
		checkMappings();
	}

	private void checkMappings() {
		for (RouteMapping mapping : mappings) {
			String action = mapping.getAction();

			try {
				Object instance = LiteralTool.newInstance(action);
				Check.isInstance(AspectController.class, instance, "$ does not implement AspectController", action);
				log.debug("Valid aspect % on $:$", action, mapping.getRoute().getType(), mapping.getRoute().getPath());

			} catch (ReflectException e) {
				throw new FormattedException("Not a valid class literal for aspect: %", e, action);
			}
		}
	}

	public List<AspectController> listInterceptors(Route route) {
		return mappings.stream().filter(m -> isOnRoute(route, m)).map(AspectMapper::createAspect)
				.collect(Collectors.toList());
	}

	private boolean isOnRoute(Route route, RouteMapping mapping) {
		if (mapping.getRoute().getType() != route.getType()) {
			return false;
		}
		return route.matchesWildcard(mapping.getRoute());
	}

	static AspectController createAspect(RouteMapping mapping) {
		return LiteralTool.newInstance(mapping.getAction());
	}

}
