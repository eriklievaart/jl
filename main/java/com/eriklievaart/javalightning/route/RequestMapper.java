package com.eriklievaart.javalightning.route;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.javalightning.api.aspect.AspectController;
import com.eriklievaart.javalightning.api.exception.ExternalRedirectException;
import com.eriklievaart.javalightning.api.exception.InternalRedirectException;
import com.eriklievaart.javalightning.aspect.AspectMapper;
import com.eriklievaart.javalightning.control.RequestController;
import com.eriklievaart.toolkit.lang.api.FormattedException;
import com.eriklievaart.toolkit.logging.api.LogTemplate;
import com.eriklievaart.toolkit.lang.api.ThrowableTool;

public class RequestMapper {
	private final LogTemplate log = new LogTemplate(getClass());

	private Router router;
	private AspectMapper aspects;

	public RequestMapper(Router router, AspectMapper aspects) {
		this.router = router;
		this.aspects = aspects;
	}

	public void invoke(FilterContext context) throws IOException, ServletException {
		createResponse(UrlConfig.httpRequestToRoute(context.getRequest()), context);
	}

	private void createResponse(Route route, FilterContext context) throws IOException, ServletException {
		try {
			RequestController request = router.getController(route);
			log.debug("Controller for request % => %", route, request);
			advise(request, aspects.listInterceptors(route)).invoke(context);

		} catch (Exception e) {
			handleException(context, e);
		}
	}

	private void handleException(FilterContext context, Exception e) throws IOException, ServletException {
		Throwable root = ThrowableTool.getRootCause(e);

		if (root instanceof ExternalRedirectException) {
			ExternalRedirectException redirect = (ExternalRedirectException) root;
			context.getResponse().sendRedirect(redirect.getRedirect().getUrl());
			return;
		}

		if (root instanceof InternalRedirectException) {
			context.reset();
			InternalRedirectException redirect = (InternalRedirectException) root;
			createResponse(new Route(RouteType.GET, redirect.getRedirect().getUrl()), context);
			return;
		}
		throw new FormattedException("% invocation failed; %", e, context.getRequest().getRequestURI(), e.getMessage());
	}

	static RequestController advise(RequestController request, List<AspectController> interceptors) {
		if (interceptors.isEmpty()) {
			return request;
		}
		for (int i = 0; i < interceptors.size() - 1; i++) {
			interceptors.get(i).setDelegate(interceptors.get(i + 1));
		}
		interceptors.get(interceptors.size() - 1).setDelegate(request);
		return interceptors.get(0);
	}

}
