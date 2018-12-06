package com.eriklievaart.javalightning.bundle.route;

import java.util.EnumSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.ContentServletCall;
import com.eriklievaart.javalightning.bundle.MvcBeans;
import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.UrlMapping;
import com.eriklievaart.javalightning.bundle.api.exception.ExternalRedirectException;
import com.eriklievaart.javalightning.bundle.api.exception.InternalRedirectException;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.javalightning.mock.api.MockHttpServletRequest;
import com.eriklievaart.javalightning.mock.api.MockHttpServletResponse;
import com.eriklievaart.javalightning.mock.api.MockRequestContext;
import com.eriklievaart.toolkit.lang.api.FormattedException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.test.api.BombSquad;

public class ContentServletCallU {

	@Test
	public void invokePageController() throws Exception {
		MvcBeans beans = new MvcBeans();
		ContentServletCall invocation = new ContentServletCall(beans, null, null);

		DummyPageController controller = new DummyPageController();
		Route route = new Route("/bar/", EnumSet.of(RouteType.GET), () -> controller);
		beans.getRouteIndex().register(new DummyPageService("foo", route));

		invocation.invoke(RouteType.GET, "/mvc/foo/bar", MockRequestContext.instance());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void invokePageControllerInternalRedirect() throws Exception {
		MvcBeans beans = new MvcBeans();
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		ContentServletCall invocation = new ContentServletCall(beans, req, res);

		DummyPageController destination = new DummyPageController();
		PageController redirect = new PageController() {
			@Override
			public void invoke(ResponseBuilder builder) {
				throw new InternalRedirectException(new UrlMapping("redirect", "/mvc/service/destination"));
			}
		};
		Route redirectRoute = new Route("/internal/", EnumSet.of(RouteType.GET), () -> redirect);
		Route destinationRoute = new Route("/destination/", EnumSet.of(RouteType.GET), () -> destination);
		beans.getRouteIndex().register(new DummyPageService("service", redirectRoute, destinationRoute));

		invocation.render(RouteType.GET, "/mvc/service/internal");
		Check.isTrue(destination.isInvoked());
	}

	@Test
	public void invokePageControllerExternalRedirect() throws Exception {
		MvcBeans beans = new MvcBeans();
		MockHttpServletResponse response = new MockHttpServletResponse();
		ContentServletCall invocation = new ContentServletCall(beans, new MockHttpServletRequest(), response);

		PageController controller = new PageController() {
			@Override
			public void invoke(ResponseBuilder builder) {
				throw new ExternalRedirectException(new UrlMapping("redirect", "http://example.com"));
			}
		};
		Route route = new Route("/bar/", EnumSet.of(RouteType.GET), () -> controller);
		beans.getRouteIndex().register(new DummyPageService("foo", route));

		invocation.render(RouteType.GET, "/mvc/foo/bar");
		response.checkIsRedirectedTo("http://example.com");
	}

	@Test
	public void invokeMissingPageController() throws Exception {
		MvcBeans beans = new MvcBeans();
		HttpServletRequest request = new MockHttpServletRequest();
		ContentServletCall invocation = new ContentServletCall(beans, request, null);

		BombSquad.diffuse(FormattedException.class, "Missing service", () -> {
			invocation.render(RouteType.GET, "/mvc/foo/bar");
		});
	}
}
