package com.eriklievaart.jl.bundle.api.page;

import java.util.EnumSet;

import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class RouteAddress {
	private final String path;
	private final EnumSet<RouteType> types;

	public RouteAddress(String path, EnumSet<RouteType> types) {
		Check.notNull(path, types);
		this.path = UrlTool.removeLeadingSlashes(UrlTool.removeTrailingSlash(path));
		this.types = types;
	}

	public String getPath() {
		return path;
	}

	public EnumSet<RouteType> getTypes() {
		return types;
	}
}