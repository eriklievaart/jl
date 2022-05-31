package com.eriklievaart.jl.core.api.render;

import java.io.IOException;

import com.eriklievaart.jl.core.api.RequestContext;

public interface ServletReponseRenderer {

	public void render(RequestContext context) throws IOException;
}