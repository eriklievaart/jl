package com.eriklievaart.javalightning.bundle.route;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.ContentServletCall;
import com.eriklievaart.javalightning.bundle.MvcBeans;
import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.exception.ExternalRedirectException;
import com.eriklievaart.javalightning.bundle.api.exception.InternalRedirectException;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.page.PageServiceBuilder;
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
		beans.setServletPrefix("mvc");
		ContentServletCall invocation = new ContentServletCall(beans, null, null);

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("bar").mapGet("/bar/", () -> controller);
		beans.getRouteIndex().register(routes.createPageService("foo"));

		invocation.invoke(RouteType.GET, "/mvc/foo/bar", MockRequestContext.instance());
		Check.isTrue(controller.isInvoked());
	}

	@Test
	public void invokePageControllerInternalRedirect() throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setServletPrefix("mvc");
		HttpServletRequest req = new MockHttpServletRequest();
		HttpServletResponse res = new MockHttpServletResponse();
		ContentServletCall invocation = new ContentServletCall(beans, req, res);

		DummyPageController destination = new DummyPageController();
		PageController redirect = new PageController() {
			@Override
			public void invoke(ResponseBuilder builder) {
				throw new InternalRedirectException("/mvc/service/destination");
			}
		};
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("internal").mapGet("/internal/", () -> redirect);
		routes.newRoute("destination").mapGet("/destination/", () -> destination);
		beans.getRouteIndex().register(routes.createPageService("service"));

		invocation.render(RouteType.GET, "/mvc/service/internal");
		Check.isTrue(destination.isInvoked());
	}

	@Test
	public void invokePageControllerExternalRedirect() throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setServletPrefix("mvc");
		MockHttpServletResponse response = new MockHttpServletResponse();
		ContentServletCall invocation = new ContentServletCall(beans, new MockHttpServletRequest(), response);

		PageController controller = new PageController() {
			@Override
			public void invoke(ResponseBuilder builder) {
				throw new ExternalRedirectException("http://example.com");
			}
		};

		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("bar").mapGet("/bar/", () -> controller);
		beans.getRouteIndex().register(routes.createPageService("foo"));

		invocation.render(RouteType.GET, "/mvc/foo/bar");
		response.checkIsRedirectedTo("http://example.com");
	}

	@Test
	public void invokeMissingPageController() throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setServletPrefix("mvc");
		HttpServletRequest request = new MockHttpServletRequest();
		ContentServletCall invocation = new ContentServletCall(beans, request, null);

		BombSquad.diffuse(FormattedException.class, "Missing service", () -> {
			invocation.render(RouteType.GET, "/mvc/foo/bar");
		});
	}
}
