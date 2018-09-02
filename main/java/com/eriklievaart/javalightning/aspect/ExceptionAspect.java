package com.eriklievaart.javalightning.aspect;

import java.io.IOException;

import javax.servlet.ServletException;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.javalightning.api.aspect.AbstractAspectController;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class ExceptionAspect extends AbstractAspectController {
	private LogTemplate log = new LogTemplate(getClass());

	@Override
	public void invoke(FilterContext context) throws IOException, ServletException {
		try {
			delegate(context);

		} catch (Exception e) {
			log.error("$ => $", e, e.getMessage(), context.getRequest().getRequestURI());
			throw new ServletException("Uncaught Exception", e);
		}
	}
}
