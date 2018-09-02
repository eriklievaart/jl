package com.eriklievaart.javalightning.route;

import java.util.EnumSet;

public enum RouteType {

	GET, POST, HEAD, PUT, DELETE, TRACE, OPTIONS, CONNECT;

	public static final EnumSet<RouteType> ALL = EnumSet.allOf(RouteType.class);

}
