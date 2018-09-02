package com.eriklievaart.javalightning.api.render;

import java.io.IOException;

import com.eriklievaart.javalightning.api.FilterContext;

public interface ServletReponseRenderer {

	public void render(FilterContext context) throws IOException;

}
