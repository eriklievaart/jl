package com.eriklievaart.jl.bundle.api.render;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.jl.bundle.api.RequestContext;
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
		HttpServletResponse reponse = context.getResponse();
		reponse.setStatus(context.getResponseBuilder().getStatusCode());
		context.getResponseBuilder().forEachHeader(h -> reponse.addHeader(h.getKey(), h.getValue()));
		StreamTool.copyStream(is, reponse.getOutputStream());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}