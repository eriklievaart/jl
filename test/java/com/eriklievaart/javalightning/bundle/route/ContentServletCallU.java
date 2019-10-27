package com.eriklievaart.javalightning.bundle.route;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.ContentServletCall;
import com.eriklievaart.javalightning.bundle.MvcBeans;
import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.exception.ExternalRedirectException;
import com.eriklievaart.javalightning.bundle.api.exception.InternalRedirectException;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.page.PageSecurity;
import com.eriklievaart.javalightning.bundle.api.page.PageServiceBuilder;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.javalightning.bundle.rule.RequestAddress;
import com.eriklievaart.javalightning.bundle.rule.RuleResultType;
import com.eriklievaart.javalightning.mock.MockHttpServletRequest;
import com.eriklievaart.javalightning.mock.MockHttpServletResponse;
import com.eriklievaart.javalightning.mock.MockRequestContext;
import com.eriklievaart.toolkit.lang.api.FormattedException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.mock.BombSquad;

public class ContentServletCallU {

	@Test
	public void renderBlockRequest() throws IOException {
		MvcBeans beans = new MvcBeans();
		beans.setServletPrefix("mvc");

		MockHttpServletRequest req = new MockHttpServletRequest();
		MockHttpServletResponse res = new MockHttpServletResponse();
		ContentServletCall invocation = new ContentServletCall(beans, req, res);

		invocation.render(new RequestAddress(RouteType.GET, "/mvc/foo/bar"), RuleResultType.BLOCK);
		res.getOutputStream().checkNoDataWritten();
		res.checkSendError(404);
	}

	@Test
	public void renderHttpsRedirect() throws IOException {
		MvcBeans beans = new MvcBeans();
		beans.setServletPrefix("mvc");

		MockHttpServletResponse res = new MockHttpServletResponse();
		MockHttpServletRequest req = new MockHttpServletRequest();
		ContentServletCall invocation = new ContentServletCall(beans, req, res);

		req.setUrl("http://secure.com/path");
		invocation.render(new RequestAddress(RouteType.GET, "secure.com", "path"), RuleResultType.HTTPS);
		res.checkIsRedirectedTo("https://secure.com/path");
	}

	@Test
	public void invokePageController() throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setServletPrefix("mvc");
		ContentServletCall invocation = new ContentServletCall(beans, null, null);

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("bar").mapGet("/bar/", () -> controller);
		routes.setSecurity(new PageSecurity((a, b) -> true));
		beans.getPageServiceIndex().register(routes.createPageService("foo"));

		invocation.invoke(new RequestAddress(RouteType.GET, "/mvc/foo/bar"), new MockRequestContext());
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
		routes.setSecurity(new PageSecurity((a, b) -> true));
		beans.getPageServiceIndex().register(routes.createPageService("service"));

		invocation.render(new RequestAddress(RouteType.GET, "/mvc/service/internal"));
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
		routes.setSecurity(new PageSecurity((a, b) -> true));
		beans.getPageServiceIndex().register(routes.createPageService("foo"));

		invocation.render(new RequestAddress(RouteType.GET, "/mvc/foo/bar"));
		response.checkIsRedirectedTo("http://example.com");
	}

	@Test
	public void resolveNotAccessible() throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setServletPrefix("mvc");
		ContentServletCall invocation = new ContentServletCall(beans, null, null);

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("bar").mapGet("/bar/", () -> controller);
		routes.setSecurity(new PageSecurity((a, b) -> false));
		beans.getPageServiceIndex().register(routes.createPageService("foo"));

		BombSquad.diffuse(RouteNotAccessibleException.class, "not accessible", () -> {
			invocation.invoke(new RequestAddress(RouteType.GET, "/mvc/foo/bar"), new MockRequestContext());
		});
	}

	@Test
	public void resolveNotAccessibleCustomException() throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setServletPrefix("mvc");
		ContentServletCall invocation = new ContentServletCall(beans, null, null);

		DummyPageController controller = new DummyPageController();
		PageServiceBuilder routes = new PageServiceBuilder();
		routes.newRoute("bar").mapGet("/bar/", () -> controller);
		routes.setSecurity(new PageSecurity((a, b) -> false, () -> {
			throw new RuntimeException("appel");
		}));
		beans.getPageServiceIndex().register(routes.createPageService("foo"));

		BombSquad.diffuse(RuntimeException.class, "appel", () -> {
			invocation.invoke(new RequestAddress(RouteType.GET, "/mvc/foo/bar"), new MockRequestContext());
		});
	}

	@Test
	public void invokeMissingPageController() throws Exception {
		MvcBeans beans = new MvcBeans();
		beans.setServletPrefix("mvc");
		HttpServletRequest request = new MockHttpServletRequest();
		ContentServletCall invocation = new ContentServletCall(beans, request, null);

		BombSquad.diffuse(FormattedException.class, "Missing service", () -> {
			invocation.render(new RequestAddress(RouteType.GET, "/mvc/foo/bar"));
		});
	}
}
