package com.eriklievaart.javalightning.control;

import java.io.IOException;

import javax.servlet.ServletException;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.toolkit.lang.api.ToString;

public class PassThroughController implements RequestController {

	@Override
	public void invoke(FilterContext context) throws IOException, ServletException {
		context.getFilterChain().doFilter(context.getRequest(), context.getResponse());
	}

	@Override
	public String toString() {
		return ToString.simple(this, "$[]");
	}
}
