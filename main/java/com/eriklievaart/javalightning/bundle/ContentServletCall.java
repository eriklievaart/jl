package com.eriklievaart.javalightning.bundle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.exception.InternalRedirectException;
import com.eriklievaart.javalightning.bundle.api.exception.RedirectException;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.render.ServletReponseRenderer;
import com.eriklievaart.javalightning.bundle.control.ParametersSupplier;
import com.eriklievaart.javalightning.bundle.route.PageServiceIndex;
import com.eriklievaart.javalightning.bundle.route.RouteNotAccessibleException;
import com.eriklievaart.javalightning.bundle.route.SecureRoute;
import com.eriklievaart.javalightning.bundle.rule.RequestAddress;
import com.eriklievaart.javalightning.bundle.rule.RuleResultType;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.FormattedException;
import com.eriklievaart.toolkit.lang.api.ThrowableTool;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class ContentServletCall {
	private LogTemplate log = new LogTemplate(getClass());

	private MvcBeans beans;
	private HttpServletRequest req;
	private HttpServletResponse res;

	public ContentServletCall(MvcBeans beans, HttpServletRequest req, HttpServletResponse res) {
		Check.notNull(beans);
		this.beans = beans;
		this.req = req;
		this.res = res;
	}

	public void render(RequestAddress address, RuleResultType result) throws IOException {
		switch (result) {

		case BLOCK:
			log.debug("blocking %", req.getRequestURL());
			res.sendError(404);
			return;

		case HTTPS:
			String url = req.getRequestURL().insert(4, "s").toString();
			log.trace("https redirect %", url);
			res.sendRedirect(url);
			return;

		case ALLOW:
			render(address);
			return;

		default:
			throw new FormattedException("invalid result $", result);
		}
	}

	public void render(RequestAddress address) throws IOException {
		RequestContext context = new RequestContext(beans.getContext(), req, res);

		try {
			invoke(address, context);
			ServletReponseRenderer renderer = context.getRenderer();
			if (renderer == null) {
				log.warn("$ missing renderer", address);
			} else {
				renderer.render(context);
			}

		} catch (Exception e) {
			handleException(address, context, e);
		}
	}

	private void handleException(RequestAddress address, RequestContext context, Exception e) throws IOException {
		Throwable root = ThrowableTool.getRootCause(e);
		String exceptionPath = beans.getPageServiceIndex().getExceptionRedirect();

		if (root instanceof RouteNotAccessibleException) {
			log.debug("access denied for % on %", req.getRemoteHost(), req.getRequestURL());
			res.setStatus(404);
			return;

		} else if (root instanceof RedirectException) {
			redirect(address, (RedirectException) root);
			return;

		}
		log.error("Uncaught $: $", e, root.getClass().getSimpleName(), root.getMessage());
		if (Str.notBlank(exceptionPath)) {
			storeExceptionInRequest(context, e);
			redirect(address, new InternalRedirectException(exceptionPath));

		} else {
			throw new FormattedException("% invocation failed; $", e, req.getRequestURI(), e.getMessage());
		}
	}

	private void storeExceptionInRequest(RequestContext context, Exception e) {
		if (context.getException().isPresent()) {
			log.error("Original Exception not properly handled, aborting redirect loop.", e);
			throw new RuntimeException(e);
		}
		context.getRequest().setAttribute(RequestContext.EXCEPTION_ATTRIBUTE, e);
	}

	private void redirect(RequestAddress original, RedirectException redirect) throws IOException {
		String url = redirect.getRedirect();

		if (redirect.isInternal()) {
			log.debug("internal redirect % to %", original.getPath(), url);
			render(new RequestAddress(original.getMethod(), original.getDomain(), UrlTool.getPath(url)));
		} else {
			log.debug("external redirect % to %", original.getPath(), url);
			res.sendRedirect(url);
		}
	}

	public void invoke(RequestAddress address, RequestContext context) throws Exception {
		PageServiceIndex index = beans.getPageServiceIndex();

		Optional<SecureRoute> optional = index.resolve(address.getMethod(), address.getPath());
		if (!optional.isPresent()) {
			throw new FileNotFoundException(Str.sub("no controller for uri $ $", address, index.listServices()));
		}
		SecureRoute route = optional.get();
		route.validate(context);

		PageController controller = route.getController(context);
		log.debug("$ -> controller $", route.getRoute(), controller);
		invoke(context, controller);
	}

	private void invoke(RequestContext context, PageController controller) throws Exception {
		ParametersSupplier parameters = new ParametersSupplier(context.getRequest());
		context.setParameterSupplier(parameters);
		try {
			context.injectBeanAnnotations(controller);
			controller.invoke(context.getResponseBuilder());

		} finally {
			parameters.close();
		}
	}
}
