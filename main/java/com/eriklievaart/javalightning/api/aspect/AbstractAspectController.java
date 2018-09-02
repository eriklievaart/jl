package com.eriklievaart.javalightning.api.aspect;

import java.io.IOException;

import javax.servlet.ServletException;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.javalightning.control.RequestController;

public abstract class AbstractAspectController implements AspectController {

	private RequestController delegate;

	@Override
	public final void setDelegate(RequestController delegate) {
		this.delegate = delegate;
	}

	protected final void delegate(FilterContext context) throws IOException, ServletException {
		if (delegate != null) {
			delegate.invoke(context);
		}
	}

}
