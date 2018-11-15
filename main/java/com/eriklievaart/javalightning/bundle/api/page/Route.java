package com.eriklievaart.javalightning.bundle.api.page;

import java.util.EnumSet;
import java.util.function.Supplier;

import com.eriklievaart.toolkit.io.api.UrlTool;

public class Route {
	private final String path;
	private final EnumSet<RouteType> types;
	private final Supplier<PageController> supplier;

	public Route(String path, EnumSet<RouteType> types, Supplier<PageController> supplier) {
		this.path = UrlTool.removeLeadingSlashes(UrlTool.removeTrailingSlash(path));
		this.types = types;
		this.supplier = supplier;
	}

	public String getPath() {
		return path;
	}

	public EnumSet<RouteType> getTypes() {
		return types;
	}

	public PageController createController() {
		return supplier.get();
	}
}