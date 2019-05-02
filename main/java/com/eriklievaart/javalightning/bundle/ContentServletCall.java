package com.eriklievaart.javalightning.bundle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.exception.RedirectException;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.javalightning.bundle.api.render.ServletReponseRenderer;
import com.eriklievaart.javalightning.bundle.control.ParametersSupplier;
import com.eriklievaart.javalightning.bundle.route.PageServiceIndex;
import com.eriklievaart.javalightning.bundle.route.RouteNotAccessibleException;
import com.eriklievaart.javalightning.bundle.route.SecureRoute;
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

	public void render(RouteType method, String path) throws IOException {
		RequestContext context = new RequestContext(beans.getContext(), req, res);

		try {
			invoke(method, path, context);
			ServletReponseRenderer renderer = context.getRenderer();
			if (renderer == null) {
				log.warn("$:$ missing renderer", method, path);
			} else {
				renderer.render(context);
			}

		} catch (Exception e) {
			handleException(method + ":" + path, e);
		}
	}

	private void handleException(String original, Exception e) throws IOException {
		Throwable root = ThrowableTool.getRootCause(e);

		if (root instanceof RouteNotAccessibleException) {
			log.debug("access denied for % on %", req.getRemoteHost(), req.getRequestURL());
			res.setStatus(404);
			return;
		}
		if (root instanceof RedirectException) {
			redirect(original, (RedirectException) root);
			return;
		}
		log.error("Uncaught $: $", e, root.getClass().getSimpleName(), root.getMessage());
		throw new FormattedException("% invocation failed; $", e, req.getRequestURI(), e.getMessage());
	}

	private void redirect(String original, RedirectException redirect) throws IOException {
		String url = redirect.getRedirect();

		if (redirect.isInternal()) {
			log.debug("internal redirect % to %", original, url);
			render(RouteType.GET, url);
		} else {
			log.debug("external redirect % to %", original, url);
			res.sendRedirect(url);
		}
	}

	public void invoke(RouteType method, String path, RequestContext context) throws Exception {
		PageServiceIndex index = beans.getPageServiceIndex();
		Optional<SecureRoute> optional = index.resolve(method, path);
		if (!optional.isPresent()) {
			throw new FileNotFoundException(Str.sub("no controller for uri $:$ $", method, path, index.listServices()));
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