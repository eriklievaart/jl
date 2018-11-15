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
import com.eriklievaart.javalightning.bundle.control.InOutJector;
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
			context.getRenderer().render(context);

		} catch (Exception e) {
			handleException(method + ":" + path, e);
		}
	}

	private void handleException(String original, Exception e) throws IOException {
		Throwable root = ThrowableTool.getRootCause(e);

		if (root instanceof RedirectException) {
			RedirectException redirect = (RedirectException) root;
			String url = redirect.getRedirectUrl();

			if (redirect.isInternal()) {
				log.debug("internal redirect % to %", original, url);
				render(RouteType.GET, url);
			} else {
				log.debug("external redirect % to %", original, url);
				res.sendRedirect(url);
			}
			return;
		}
		throw new FormattedException("% invocation failed; $", e, req.getRequestURI(), e.getMessage());
	}

	public void invoke(RouteType method, String path, RequestContext context) throws Exception {
		Optional<PageController> optional = beans.getRouteIndex().resolve(method, path);

		if (optional.isPresent()) {
			PageController controller = optional.get();

			try (InOutJector ioj = new InOutJector(context)) {
				ioj.injectAnnotatedFields(controller);
				controller.invoke(context.getResponseBuilder());
			}

		} else {
			String message = Str.sub("no controller for uri $:$ $", method, path, beans.getRouteIndex().listServices());
			throw new FileNotFoundException(message);
		}
	}
}