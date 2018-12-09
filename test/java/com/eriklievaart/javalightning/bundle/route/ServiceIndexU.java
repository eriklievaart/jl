package com.eriklievaart.javalightning.bundle.route;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.page.PageServiceBuilder;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class ServiceIndexU {

	@Test
	public void resolveExactMatch() {
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("exact").map("bar/exact", RouteType.GET, () -> new DummyPageController());
		ServiceIndex index = new ServiceIndex(routes.createPageService("foo"));
		Check.isTrue(index.resolve(RouteType.GET, "bar/exact").isPresent());
	}

	@Test
	public void resolveNoMatch() {
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("exact").map("bar/exact", RouteType.GET, () -> new DummyPageController());
		ServiceIndex index = new ServiceIndex(routes.createPageService("service"));
		Check.isFalse(index.resolve(RouteType.GET, "bar/elsewhere").isPresent());
	}

	@Test
	public void resolveWildcardMatch() {
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("wildcard").map("bar/*", RouteType.GET, () -> new DummyPageController());
		ServiceIndex index = new ServiceIndex(routes.createPageService("foo"));
		Check.isTrue(index.resolve(RouteType.GET, "bar/elsewhere").isPresent());
	}

	@Test
	public void resolveWildcardNull() {
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("wildcard").map("*", RouteType.GET, () -> new DummyPageController());
		ServiceIndex index = new ServiceIndex(routes.createPageService("foo"));
		Check.isTrue(index.resolve(RouteType.GET, null).isPresent());
	}
}
