package com.eriklievaart.jl.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.jl.core.api.RequestContext;
import com.eriklievaart.jl.core.api.UseErrorPage;
import com.eriklievaart.jl.core.api.exception.NotFound404Exception;
import com.eriklievaart.jl.core.api.exception.RedirectException;
import com.eriklievaart.jl.core.api.page.PageController;
import com.eriklievaart.jl.core.api.page.RouteType;
import com.eriklievaart.jl.core.api.render.InputStreamRenderer;
import com.eriklievaart.jl.core.api.render.ServletReponseRenderer;
import com.eriklievaart.jl.core.control.ParametersSupplier;
import com.eriklievaart.jl.core.route.PageServiceIndex;
import com.eriklievaart.jl.core.route.SecureRoute;
import com.eriklievaart.jl.core.rule.RequestAddress;
import com.eriklievaart.jl.core.rule.RuleResultType;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.FormattedException;
import com.eriklievaart.toolkit.lang.api.ThrowableTool;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;
import com.eriklievaart.toolkit.reflect.api.annotations.AnnotationTool;

public class ContentServletCall {
	private LogTemplate log = new LogTemplate(getClass());
	private Supplier<InputStream> favicon = () -> getClass().getResourceAsStream("/core/favicon.ico");

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
			sendError(403, "forbidden");
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
		render(address, new RequestContext(beans.getContext(), req, res));
	}

	private void render(RequestAddress address, RequestContext context) throws IOException {
		try {
			invoke(address, context);
			ServletReponseRenderer renderer = context.getRenderer();
			if (renderer == null) {
				log.warn("$ missing renderer", address);
			} else {
				renderer.render(context);
			}

		} catch (Exception e) {
			Boolean manualOverride = context.isUsingErrorPage();
			if (manualOverride == null) {
				context.setUseErrorPage(true);
			}
			if (!ignoreException(e)) {
				handleException(address, context, e);
			}
		}
	}

	private boolean ignoreException(Exception e) {
		Throwable root = ThrowableTool.getRootCause(e);
		if (root instanceof IOException && Str.isEqualIgnoreCase(root.getMessage(), "broken pipe")) {
			return true;
		}
		return false;
	}

	private void handleException(RequestAddress address, RequestContext context, Exception e) throws IOException {
		Throwable root = ThrowableTool.getRootCause(e);
		String exceptionPath = beans.getPageServiceIndex().getExceptionRedirect();

		if (root instanceof RedirectException) {
			redirect(address, (RedirectException) root);
			return;
		}
		boolean usingErrorPage = context.isUsingErrorPage() && Str.notBlank(exceptionPath);
		if (usingErrorPage) {
			customErrorPage(context, e, exceptionPath);
			return;
		}
		if (root instanceof NotFound404Exception) {
			log.debug("$:$ $ %", address.getMethod(), req.getRequestURL(), root.getMessage(), req.getRemoteHost());
			sendError(404, "not found");
			return;
		}
		log.error("Uncaught $: $", e, root.getClass().getSimpleName(), root.getMessage());
		throw new FormattedException("% invocation failed; $", e, req.getRequestURI(), e.getMessage());
	}

	private void customErrorPage(RequestContext context, Exception e, String exceptionPath) throws IOException {
		context.setUseErrorPage(false); // prevents errors in the error page causing a redirect loop
		storeExceptionInRequest(context, e);
		render(new RequestAddress(RouteType.GET, exceptionPath), context);
	}

	private void sendError(int status, String message) {
		try {
			res.sendError(status, message);
		} catch (IOException e) {
			log.debug(e);
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
			if (address.isFavicon()) {
				log.debug("favicon not configured, using framework fallback");
				invoke(context, b -> b.setRenderer(new InputStreamRenderer(favicon.get())));
				return;
			} else {
				throw new NotFound404Exception("cannot resolve $:$", address.getMethod(), address.getPath());
			}
		}
		SecureRoute route = optional.get();
		route.validate(context);

		PageController controller = route.getController(context);
		useErrorPage(controller, context);
		log.debug("$ -> controller $", route.getRoute(), controller.getClass());
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

	private void useErrorPage(PageController controller, RequestContext context) {
		Boolean overridden = context.isUsingErrorPage();
		UseErrorPage annotation = AnnotationTool.getLiteralAnnotation(controller.getClass(), UseErrorPage.class);
		if (overridden == null && annotation != null) {
			context.setUseErrorPage(annotation.value());
		}
	}
}
