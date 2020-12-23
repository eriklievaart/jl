package com.eriklievaart.javalightning.bundle.route;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.page.PageServiceBuilder;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.javalightning.bundle.page.AccessiblePageServiceBuilder;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class RouteIndexU {

	@Test
	public void resolveExactMatch() {
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("exact").map("bar/exact", RouteType.GET, () -> new DummyPageController());
		RouteIndex index = new RouteIndex(routes.createPageService("foo"));
		Check.isTrue(index.resolve(RouteType.GET, "bar/exact").isPresent());
	}

	@Test
	public void resolveGetTrailingSlash() {
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("exact").map("bar/exact", RouteType.GET, () -> new DummyPageController());
		RouteIndex index = new RouteIndex(routes.createPageService("foo"));
		Check.isTrue(index.resolve(RouteType.GET, "bar/exact/").isPresent());
	}

	@Test
	public void resolveRegisterTrailingSlash() {
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("exact").map("bar/exact", RouteType.GET, () -> new DummyPageController());
		RouteIndex index = new RouteIndex(routes.createPageService("foo"));
		Check.isTrue(index.resolve(RouteType.GET, "bar/exact/").isPresent());
	}

	@Test
	public void resolveNoMatch() {
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("exact").map("bar/exact", RouteType.GET, () -> new DummyPageController());
		RouteIndex index = new RouteIndex(routes.createPageService("service"));
		Check.isFalse(index.resolve(RouteType.GET, "bar/elsewhere").isPresent());
	}

	@Test
	public void resolveWildcardMatch() {
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("wildcard").map("bar/*", RouteType.GET, () -> new DummyPageController());
		RouteIndex index = new RouteIndex(routes.createPageService("foo"));
		Check.isTrue(index.resolve(RouteType.GET, "bar/elsewhere").isPresent());
	}

	@Test
	public void resolveWildcardNull() {
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("wildcard").map("*", RouteType.GET, () -> new DummyPageController());
		RouteIndex index = new RouteIndex(routes.createPageService("foo"));
		Check.isTrue(index.resolve(RouteType.GET, null).isPresent());
	}
}
