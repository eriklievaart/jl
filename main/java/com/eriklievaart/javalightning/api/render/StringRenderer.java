package com.eriklievaart.javalightning.api.render;

import java.io.IOException;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class StringRenderer implements ServletReponseRenderer {

	private String data;

	public StringRenderer(String data) {
		Check.notNull(data, "Data cannot be null, use an empty String instead!");
		this.data = data;
	}

	@Override
	public void render(FilterContext context) throws IOException {
		context.getResponse().setStatus(context.getResponseBuilder().getView().getStatusCode());
		context.getResponse().setContentType("text/html");
		StreamTool.writeString(data, context.getResponse().getOutputStream());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + data;
	}

}
