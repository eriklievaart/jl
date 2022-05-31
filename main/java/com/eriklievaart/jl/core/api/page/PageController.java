package com.eriklievaart.jl.core.api.page;

import com.eriklievaart.jl.core.api.ResponseBuilder;

public interface PageController {

	public void invoke(ResponseBuilder response) throws Exception;
}