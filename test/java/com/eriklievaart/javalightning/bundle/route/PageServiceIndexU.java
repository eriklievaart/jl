package com.eriklievaart.javalightning.bundle.route;

import java.util.EnumSet;
import java.util.Optional;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.exception.RouteUnavailableException;
import com.eriklievaart.javalightning.bundle.api.page.PageServiceBuilder;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.javalightning.mock.MockRequestContext;
import com.eriklievaart.toolkit.lang.api.AssertionException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.mock.BombSquad;

public class PageServiceIndexU {

	@Test
	public void getRemotePathDefault() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> new DummyPageController());
		index.register(routes.createPageService("service"));

		String path = index.getRemotePath("service", "suffix");
		Check.isEqual(path, "/service/suffix");
	}

	@Test
	public void getRemotePathWithPrefix() throws Exception {
		PageServiceIndex index = new PageServiceIndex();
		index.setServletPrefix("mvc");

		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> new DummyPageController());
		index.register(routes.createPageService("service"));

		String path = index.getRemotePath("service", "suffix");
		Check.isEqual(path, "/mvc/service/suffix");
	}

	@Test
	public void getPathMissingService() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> new DummyPageController());
		index.register(routes.createPageService("service"));

		BombSquad.diffuse(RouteUnavailableException.class, "Missing service `idonotexist`", () -> {
			index.getRemotePath("idonotexist", "suffix");
		});
	}

	@Test
	public void getPathMissingRoute() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> new DummyPageController());
		index.register(routes.createPageService("service"));

		BombSquad.diffuse(RouteUnavailableException.class, "No route with id `idonotexist`", () -> {
			index.getRemotePath("service", "idonotexist");
		});
	}

	@Test
	public void registerInvalidPrefix() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		index.register(routes.createPageService("/service"));

		BombSquad.diffuse(AssertionException.class, "Missing service", () -> {
			index.resolve(RouteType.GET, "/service/suffix/");
		});
	}

	@Test
	public void resolveExactMatch() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/suffix/");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().getController(MockRequestContext.instance()).invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveEmptyPathAfterService() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("empty").map("", RouteType.GET, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().getController(MockRequestContext.instance()).invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveEmptyPathAfterServiceNoSlash() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();

		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("empty").map("", RouteType.GET, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().getController(MockRequestContext.instance()).invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveServiceDoesNotExist() {
		PageServiceIndex index = new PageServiceIndex();
		BombSquad.diffuse(AssertionException.class, "Missing service", () -> {
			index.resolve(RouteType.GET, "/service/");
		});
	}

	@Test
	public void resolvePathNotRegistered() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("suffix", RouteType.GET, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/other/");
		Check.isFalse(resolved.isPresent());
	}

	@Test
	public void resolveWildcard() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();

		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("star").map("path/*", RouteType.GET, () -> controller);
		index.register(routes.createPageService("wildcard"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/wildcard/path/foo");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().getController(MockRequestContext.instance()).invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveHttpPost() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();

		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("suffix", RouteType.POST, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.POST, "/service/suffix/");
		Check.isTrue(resolved.isPresent());
	}

	@Test
	public void resolveWrongHttpMethod() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("suffix", RouteType.POST, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/suffix/");
		Check.isFalse(resolved.isPresent());
	}

	@Test
	public void resolveMultipleHttpMethod() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();

		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("suffix", EnumSet.of(RouteType.POST, RouteType.GET), () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/suffix/");
		Check.isTrue(resolved.isPresent());
	}

	@Test
	public void resolveNotAccessible() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		routes.setAccessible((route, context) -> false);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/suffix/");
		Check.isTrue(resolved.isPresent());

		BombSquad.diffuse(AssertionException.class, "route not accessible", () -> {
			resolved.get().getController(MockRequestContext.instance()).invoke(new ResponseBuilder());
		});
		Check.isFalse(controller.isInvoked());
	}

	@Test
	public void isAccessiblePass() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		routes.setAccessible((route, context) -> true);
		index.register(routes.createPageService("service"));

		Check.isTrue(index.isAccessible("service", "suffix", MockRequestContext.instance()));
	}

	@Test
	public void isAccessibleFail() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		routes.setAccessible((route, context) -> false);
		index.register(routes.createPageService("service"));

		Check.isFalse(index.isAccessible("service", "suffix", MockRequestContext.instance()));
	}
}
