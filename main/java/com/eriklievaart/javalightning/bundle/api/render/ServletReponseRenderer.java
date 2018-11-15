package com.eriklievaart.javalightning.bundle.api.render;

import java.io.IOException;

import com.eriklievaart.javalightning.bundle.api.RequestContext;

public interface ServletReponseRenderer {

	public void render(RequestContext context) throws IOException;

}
