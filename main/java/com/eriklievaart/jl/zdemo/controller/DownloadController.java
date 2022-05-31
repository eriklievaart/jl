package com.eriklievaart.jl.zdemo.controller;

import java.io.File;

import com.eriklievaart.jl.core.api.ResponseBuilder;
import com.eriklievaart.jl.core.api.page.PageController;
import com.eriklievaart.jl.core.api.render.DownloadRenderer;

public class DownloadController implements PageController {

	@Override
	public void invoke(ResponseBuilder builder) throws Exception {
		builder.setRenderer(new DownloadRenderer(new File("/tmp/dummy.txt")));
	}
}