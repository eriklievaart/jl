package com.eriklievaart.jl.zdemo.controller;

import java.io.File;

import com.eriklievaart.jl.bundle.api.ResponseBuilder;
import com.eriklievaart.jl.bundle.api.page.PageController;
import com.eriklievaart.jl.bundle.api.render.DownloadRenderer;

public class DownloadController implements PageController {

	@Override
	public void invoke(ResponseBuilder builder) throws Exception {
		builder.setRenderer(new DownloadRenderer(new File("/tmp/dummy.txt")));
	}
}