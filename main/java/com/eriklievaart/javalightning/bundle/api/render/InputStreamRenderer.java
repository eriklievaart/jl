package com.eriklievaart.javalightning.bundle.api.render;

import java.io.IOException;
import java.io.InputStream;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class InputStreamRenderer implements ServletReponseRenderer {

	private InputStream is;

	public InputStreamRenderer(InputStream is) {
		Check.notNull(is, "InputStream cannot be null!");
		this.is = is;
	}

	@Override
	public void render(RequestContext context) throws IOException {
		context.getResponse().setStatus(context.getResponseBuilder().getView().getStatusCode());
		context.getResponseBuilder().getHeaders().forEach((k, v) -> context.getResponse().addHeader(k, v));
		StreamTool.copyStream(is, context.getResponse().getOutputStream());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
