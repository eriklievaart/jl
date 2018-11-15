package com.eriklievaart.javalightning.bundle.api.render;

import java.io.IOException;

import com.eriklievaart.javalightning.bundle.api.RequestContext;

public class RedirectRenderer implements ServletReponseRenderer {

	@Override
	public void render(RequestContext context) throws IOException {
		context.getResponse().sendRedirect(context.getResponseBuilder().getView().getPath());
	}

}
