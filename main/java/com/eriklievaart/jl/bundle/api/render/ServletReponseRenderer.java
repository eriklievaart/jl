package com.eriklievaart.jl.bundle.api.render;

import java.io.IOException;

import com.eriklievaart.jl.bundle.api.RequestContext;

public interface ServletReponseRenderer {

	public void render(RequestContext context) throws IOException;
}