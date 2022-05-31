package com.eriklievaart.jl.core.route;

import java.util.EnumSet;
import java.util.Optional;

import org.junit.Test;

import com.eriklievaart.jl.core.api.ResponseBuilder;
import com.eriklievaart.jl.core.api.exception.NotFound404Exception;
import com.eriklievaart.jl.core.api.page.PageSecurity;
import com.eriklievaart.jl.core.api.page.PageServiceBuilder;
import com.eriklievaart.jl.core.api.page.RouteType;
import com.eriklievaart.jl.core.page.AccessiblePageServiceBuilder;
import com.eriklievaart.jl.mock.MockRequestContext;
import com.eriklievaart.toolkit.lang.api.AssertionException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.mock.BombSquad;

public class PageServiceIndexU {

	@Test
	public void getRemotePathDefault() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> new DummyPageController());
		index.register(routes.createPageService("service"));

		String path = index.getRemotePath("service", "suffix");
		Check.isEqual(path, "/service/suffix");
	}

	@Test
	public void getRemotePathWithPrefix() throws Exception {
		PageServiceIndex index = new PageServiceIndex();
		index.setServletPrefix("mvc");

		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> new DummyPageController());
		index.register(routes.createPageService("service"));

		String path = index.getRemotePath("service", "suffix");
		Check.isEqual(path, "/mvc/service/suffix");
	}

	@Test
	public void getPathMissingService() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> new DummyPageController());
		index.register(routes.createPageService("service"));

		BombSquad.diffuse(NotFound404Exception.class, "Missing service `idonotexist`", () -> {
			index.getRemotePath("idonotexist", "suffix");
		});
	}

	@Test
	public void getPathMissingRoute() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> new DummyPageController());
		index.register(routes.createPageService("service"));

		BombSquad.diffuse(NotFound404Exception.class, "No route with id `idonotexist`", () -> {
			index.getRemotePath("service", "idonotexist");
		});
	}

	@Test
	public void registerInvalidPrefix() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		index.register(routes.createPageService("/service"));

		Optional<SecureRoute> optional = index.resolve(RouteType.GET, "/service/suffix/");
		Check.isFalse(optional.isPresent());
	}

	@Test
	public void resolveExactMatch() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/suffix/");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().getController(new MockRequestContext()).invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveEmptyPathAfterService() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("empty").map("", RouteType.GET, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().getController(new MockRequestContext()).invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveEmptyPathAfterServiceNoSlash() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();

		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("empty").map("", RouteType.GET, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().getController(new MockRequestContext()).invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveServiceDoesNotExist() {
		PageServiceIndex index = new PageServiceIndex();
		Optional<SecureRoute> optional = index.resolve(RouteType.GET, "/service/");
		Check.isFalse(optional.isPresent());
	}

	@Test
	public void resolvePathNotRegistered() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("suffix", RouteType.GET, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> optional = index.resolve(RouteType.GET, "/service/other/");
		Check.isFalse(optional.isPresent());
	}

	@Test
	public void resolveWildcard() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();

		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("star").map("path/*", RouteType.GET, () -> controller);
		index.register(routes.createPageService("wildcard"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/wildcard/path/foo");
		Check.isTrue(resolved.isPresent());

		Check.isFalse(controller.isInvoked());
		resolved.get().getController(new MockRequestContext()).invoke(new ResponseBuilder());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void resolveHttpPost() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();

		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("suffix", RouteType.POST, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.POST, "/service/suffix/");
		Check.isTrue(resolved.isPresent());
	}

	@Test
	public void resolveWrongHttpMethod() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("suffix", RouteType.POST, () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/suffix/");
		Check.isFalse(resolved.isPresent());
	}

	@Test
	public void resolveMultipleHttpMethod() {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();

		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("suffix", EnumSet.of(RouteType.POST, RouteType.GET), () -> controller);
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/suffix/");
		Check.isTrue(resolved.isPresent());
	}

	@Test
	public void resolveNotAccessible() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		routes.setSecurity(new PageSecurity((a, b) -> false));
		index.register(routes.createPageService("service"));

		Optional<SecureRoute> resolved = index.resolve(RouteType.GET, "/service/suffix/");
		Check.isTrue(resolved.isPresent());

		BombSquad.diffuse(AssertionException.class, "route not accessible", () -> {
			resolved.get().getController(new MockRequestContext()).invoke(new ResponseBuilder());
		});
		Check.isFalse(controller.isInvoked());
	}

	@Test
	public void isAccessiblePass() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		routes.setSecurity(new PageSecurity((a, b) -> true));
		index.register(routes.createPageService("service"));

		Check.isTrue(index.isAccessible("service", "suffix", new MockRequestContext()));
	}

	@Test
	public void isAccessibleFail() throws Exception {
		PageServiceIndex index = new PageServiceIndex();

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = AccessiblePageServiceBuilder.instance();
		routes.newRoute("suffix").map("/suffix/", RouteType.GET, () -> controller);
		routes.setSecurity(new PageSecurity((a, b) -> false));
		index.register(routes.createPageService("service"));

		Check.isFalse(index.isAccessible("service", "suffix", new MockRequestContext()));
	}
}