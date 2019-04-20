package com.eriklievaart.javalightning.mock;

import com.eriklievaart.toolkit.lang.api.str.Str;

@SuppressWarnings("unused")
public class MockLightning {
	public boolean isRouteAccessible(String service, String route) {
		return true;
	}

	public String getRemotePath(String service, String route) {
		return Str.sub("/$/$", service, route);
	}
}
