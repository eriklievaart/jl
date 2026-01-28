package com.eriklievaart.jl.core.api.render;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.jl.core.api.RequestContext;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class StringRenderer implements ServletReponseRenderer {

	private String data;

	public StringRenderer(String data) {
		Check.notNull(data, "Data cannot be null, use an empty String instead!");
		this.data = data;
	}

	public StringRenderer(String format, Object... args) {
		Check.notBlank(format);
		this.data = Str.sub(format, args);
	}

	@Override
	public void render(RequestContext context) throws IOException {
		HttpServletResponse reponse = context.getResponse();
		int status = context.getResponseBuilder().getStatusCode();
		if (status != 200) { // don't override errors
			reponse.setStatus(status);
		}
		context.getResponseBuilder().forEachHeader(h -> reponse.addHeader(h.getKey(), h.getValue()));
		StreamTool.writeString(data, reponse.getOutputStream());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + data;
	}
}