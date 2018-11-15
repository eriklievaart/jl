package com.eriklievaart.javalightning.bundle.route;

import java.util.EnumSet;
import java.util.Optional;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.toolkit.lang.api.AssertionException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.test.api.BombSquad;

public class RouteIndexU {

	@Test
	public void registerInvalidPrefix() {
		RouteIndex index = new RouteIndex();

		DummyPageController controller = new DummyPageController();
		Route route = new Route("suffix", EnumSet.of(RouteType.GET), () -> controller);
		index.register(new DummyPageService("/service", route));

		BombSquad.diffuse(AssertionException.class, "Missing service", () -> {
			index.resolve(RouteType.GET, "/mvc/service/suffix/");
		});
	}

	@Test
	public void resolveExactMatch() throws Exception {
		RouteIndex index = new RouteIndex();

		DummyPageController controller = new DummyPageController();
		Route route = new Route("suffix", EnumSet.of(RouteType.GET), () -> controller);
		index.register(new DummyPageService("service", route));

		Optional<PageController> resolved = index.resolve(RouteType.GET, "/mvc/service/suffix/");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveEmptyPathAfterService() throws Exception {
		RouteIndex index = new RouteIndex();

		DummyPageController controller = new DummyPageController();
		Route route = new Route("", EnumSet.of(RouteType.GET), () -> controller);
		index.register(new DummyPageService("service", route));

		Optional<PageController> resolved = index.resolve(RouteType.GET, "/mvc/service/");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveServiceDoesNotExist() {
		RouteIndex index = new RouteIndex();
		BombSquad.diffuse(AssertionException.class, "Missing service", () -> {
			index.resolve(RouteType.GET, "/mvc/service/");
		});
	}

	@Test
	public void resolvePathNotRegistered() {
		RouteIndex index = new RouteIndex();

		DummyPageController controller = new DummyPageController();
		Route route = new Route("suffix", EnumSet.of(RouteType.GET), () -> controller);
		index.register(new DummyPageService("service", route));

		Optional<PageController> resolved = index.resolve(RouteType.GET, "/mvc/service/other/");
		Check.isFalse(resolved.isPresent());
	}

	@Test
	public void resolveWildcard() throws Exception {
		RouteIndex index = new RouteIndex();

		DummyPageController controller = new DummyPageController();
		Route route = new Route("path/*", EnumSet.of(RouteType.GET), () -> controller);
		index.register(new DummyPageService("wildcard", route));

		Optional<PageController> resolved = index.resolve(RouteType.GET, "/mvc/wildcard/path/foo");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveHttpPost() {
		RouteIndex index = new RouteIndex();

		DummyPageController controller = new DummyPageController();
		Route route = new Route("suffix", EnumSet.of(RouteType.POST), () -> controller);
		index.register(new DummyPageService("service", route));

		Optional<PageController> resolved = index.resolve(RouteType.POST, "/mvc/service/suffix/");
		Check.isTrue(resolved.isPresent());
	}

	@Test
	public void resolveWrongHttpMethod() {
		RouteIndex index = new RouteIndex();

		DummyPageController controller = new DummyPageController();
		Route route = new Route("suffix", EnumSet.of(RouteType.POST), () -> controller);
		index.register(new DummyPageService("service", route));

		Optional<PageController> resolved = index.resolve(RouteType.GET, "/mvc/service/suffix/");
		Check.isFalse(resolved.isPresent());
	}

	@Test
	public void resolveMultipleHttpMethod() {
		RouteIndex index = new RouteIndex();

		DummyPageController controller = new DummyPageController();
		Route route = new Route("suffix", EnumSet.of(RouteType.POST, RouteType.GET), () -> controller);
		index.register(new DummyPageService("service", route));

		Optional<PageController> resolved = index.resolve(RouteType.GET, "/mvc/service/suffix/");
		Check.isTrue(resolved.isPresent());
	}
}
