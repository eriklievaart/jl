package com.eriklievaart.javalightning.aspect;

import java.io.IOException;

import javax.servlet.ServletException;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.javalightning.api.aspect.AbstractAspectController;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class TraceAspect extends AbstractAspectController {
	private LogTemplate log = new LogTemplate(getClass());

	@Override
	public void invoke(FilterContext context) throws IOException, ServletException {
		long start = System.currentTimeMillis();

		delegate(context);

		long spent = System.currentTimeMillis() - start;
		if (spent > 100) {
			log.info("$ms spent in $", spent, context.getRequest().getRequestURI());
		}
	}
}
