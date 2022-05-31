package com.eriklievaart.jl.core.api.page;

import java.util.EnumSet;

public enum RouteType {

	GET, POST, HEAD, PUT, DELETE, TRACE, OPTIONS, CONNECT;

	public static final EnumSet<RouteType> ALL = EnumSet.allOf(RouteType.class);

	public static RouteType parse(String raw) {
		return valueOf(raw.trim().toUpperCase());
	}
}