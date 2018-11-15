package com.eriklievaart.javalightning.zdemo.controller;

import java.io.File;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.render.DownloadRenderer;

public class DownloadController implements PageController {

	@Override
	public void invoke(ResponseBuilder builder) throws Exception {
		builder.setRenderer(new DownloadRenderer(new File("/tmp/dummy.txt")));
	}
}
