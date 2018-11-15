package com.eriklievaart.javalightning.bundle.route;

import java.util.EnumSet;
import java.util.Optional;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class ServiceIndexU {

	@Test
	public void resolveNoMatch() {
		Route route = new Route("bar/exact", EnumSet.of(RouteType.GET), () -> new DummyPageController());
		ServiceIndex index = new ServiceIndex(new DummyPageService("foo", route));
		Optional<PageController> optional = index.resolve(RouteType.GET, "bar/elsewhere");
		Check.isFalse(optional.isPresent());
	}

	@Test
	public void resolveExactMatch() {
		Route route = new Route("bar/exact", EnumSet.of(RouteType.GET), () -> new DummyPageController());
		ServiceIndex index = new ServiceIndex(new DummyPageService("foo", route));
		Optional<PageController> optional = index.resolve(RouteType.GET, "bar/exact");
		Check.isTrue(optional.isPresent());
	}

	@Test
	public void resolveWildcardMatch() {
		Route route = new Route("bar/*", EnumSet.of(RouteType.GET), () -> new DummyPageController());
		ServiceIndex index = new ServiceIndex(new DummyPageService("foo", route));
		Optional<PageController> optional = index.resolve(RouteType.GET, "bar/elsewhere");
		Check.isTrue(optional.isPresent());
	}
}
