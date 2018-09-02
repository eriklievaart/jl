package com.eriklievaart.javalightning.route;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.eriklievaart.javalightning.api.UrlMapping;
import com.eriklievaart.toolkit.io.api.FileTool;
import com.eriklievaart.toolkit.io.api.ResourceTool;
import com.eriklievaart.toolkit.io.api.RuntimeIOException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;
import com.eriklievaart.toolkit.reflect.api.FieldTool;
import com.eriklievaart.toolkit.reflect.api.InstanceTool;
import com.eriklievaart.toolkit.reflect.api.Modifiers;

public class UrlConfig {
	private static LogTemplate log = new LogTemplate(UrlConfig.class);

	private static final String LINE_FORMAT = "[HTTP_METHOD] [PATH] [CONTROLLER_METHOD]";

	public static List<RouteMapping> parseRoutes() {
		return parse(ResourceTool.readLines("/WEB-INF/mvc/route.txt"));
	}

	public static List<RouteMapping> parseAspects() {
		String path = "/WEB-INF/mvc/aspects.txt";
		Optional<File> optional = ResourceTool.getOptionalFile(path);

		if (optional.isPresent()) {
			try {
				return parse(FileTool.readLines(optional.get()));
			} catch (RuntimeIOException e) {
				log.warn("Unable to read File %", e, optional.get());
			}

		} else {
			log.warn("% not found, no aspects available", path);
		}
		return NewCollection.list();
	}

	public static Map<String, String> createUrlMapForConstants(Class<?> type) {
		Set<String> knownUrls = parseRoutes().stream().map(rm -> rm.getRoute().getPath()).collect(Collectors.toSet());

		Map<String, String> map = NewCollection.map();

		for (Field field : getUrlMappingConstants(type)) {
			UrlMapping instance = (UrlMapping) InstanceTool.readStaticField(field);
			CheckCollection.isPresent(knownUrls, instance.getUrl());
			map.put(instance.getName(), instance.getUrl());
		}
		CheckCollection.notEmpty(map, "no UrlMapping found on $", type);
		return map;
	}

	private static Collection<Field> getUrlMappingConstants(Class<?> type) {
		Check.notNull(type);

		List<Field> list = new ArrayList<>();
		for (Field field : FieldTool.getFields(type)) {
			if (field.getType() == UrlMapping.class) {
				Check.isTrue(Modifiers.of(field).isStatic(), "$ must be static", field);
				Check.isTrue(Modifiers.of(field).isFinal(), "$ must be final", field);
				list.add(field);
			}
		}
		return list;
	}

	public static Route httpRequestToRoute(HttpServletRequest request) {
		String path = request.getServletPath();
		String method = request.getMethod();
		return new Route(RouteType.valueOf(method), path);
	}

	public static List<RouteMapping> parse(List<String> lines) {
		List<RouteMapping> result = NewCollection.list();

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (Str.isBlank(line) || line.trim().startsWith("#")) {
				continue;
			}
			String[] split = line.trim().split("\\s+");
			RuntimeIOException.on(split.length != 3, "invalid line:$ % expected " + LINE_FORMAT, i, line);

			String method = split[0];
			String path = split[1];
			String action = split[2];

			for (RouteType type : getRouteTypes(method)) {
				result.add(new RouteMapping(new Route(type, path), action));
			}
		}
		return result;
	}

	private static EnumSet<RouteType> getRouteTypes(String method) {
		return method.equals("*") ? RouteType.ALL : EnumSet.of(RouteType.valueOf(method));
	}

}
