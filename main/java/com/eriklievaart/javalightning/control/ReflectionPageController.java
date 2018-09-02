package com.eriklievaart.javalightning.control;

import java.io.IOException;

import javax.servlet.ServletException;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.toolkit.lang.api.ToString;

public class ReflectionPageController implements RequestController {

	private ControllerInvocation im;

	public ReflectionPageController(String literal, String method) {
		im = new ControllerInvocation(literal, method);
	}

	@Override
	public void invoke(FilterContext context) throws IOException, ServletException {
		im.injectAndInvoke(context);
		context.getRenderer().render(context);
	}

	@Override
	public String toString() {
		return ToString.simple(this, "$[$]", im);
	}
}
