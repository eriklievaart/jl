package com.eriklievaart.jl.core.api.render;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.jl.core.api.RequestContext;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class StringRenderer implements ServletReponseRenderer {

	private String data;

	public StringRenderer(String data) {
		Check.notNull(data, "Data cannot be null, use an empty String instead!");
		this.data = data;
	}

	@Override
	public void render(RequestContext context) throws IOException {
		HttpServletResponse reponse = context.getResponse();
		reponse.setStatus(context.getResponseBuilder().getStatusCode());
		context.getResponseBuilder().forEachHeader(h -> reponse.addHeader(h.getKey(), h.getValue()));
		StreamTool.writeString(data, reponse.getOutputStream());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + data;
	}
}