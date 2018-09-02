package com.eriklievaart.javalightning.route;

import com.eriklievaart.toolkit.lang.api.ToString;
import com.eriklievaart.toolkit.lang.api.pattern.WildcardTool;

public class Route {

	private final RouteType type;
	private final String path;
	private final String key;

	public Route(RouteType type, String path) {
		this.type = type;
		this.path = path;
		key = type + ":" + path;
	}

	public String getPath() {
		return path;
	}

	public RouteType getType() {
		return type;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Route) {
			Route other = (Route) obj;
			return key.equals(other.key);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return key.hashCode();
	}

	@Override
	public String toString() {
		return ToString.simple(this, "${%}", key);
	}

	public boolean matchesWildcard(Route wildcard) {
		return getType() == wildcard.getType() && WildcardTool.match(wildcard.getPath(), getPath());
	}
}
