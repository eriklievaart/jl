package com.eriklievaart.javalightning.api.render;

import java.io.IOException;

import com.eriklievaart.javalightning.api.FilterContext;

public class RedirectRenderer implements ServletReponseRenderer {

	@Override
	public void render(FilterContext context) throws IOException {
		context.getResponse().sendRedirect(context.getResponseBuilder().getView().getPath());
	}

}
