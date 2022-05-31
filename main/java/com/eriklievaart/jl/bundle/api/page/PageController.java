package com.eriklievaart.jl.bundle.api.page;

import com.eriklievaart.jl.bundle.api.ResponseBuilder;

public interface PageController {

	public void invoke(ResponseBuilder response) throws Exception;
}