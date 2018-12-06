package com.eriklievaart.javalightning.bundle.api.page;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;

public interface PageController {

	public void invoke(ResponseBuilder response) throws Exception;
}
