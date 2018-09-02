package com.eriklievaart.javalightning.api.render;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.toolkit.io.api.StreamTool;

public class DownloadRenderer implements ServletReponseRenderer {

	private final FileDownload download;

	public DownloadRenderer(File file) throws FileNotFoundException {
		download = new FileDownload(file.getName(), file);
	}

	public DownloadRenderer(FileDownload download) {
		this.download = download;
	}

	@Override
	public void render(FilterContext context) throws IOException {
		context.getResponse().setContentLength((int) download.getFile().length());
		context.getResponse().setContentType("application/octet-stream");
		context.getResponse().setHeader("Content-Disposition", "attachment; filename=\"" + download.getName() + "\"");
		StreamTool.copyStream(new FileInputStream(download.getFile()), context.getResponse().getOutputStream());
	}

}
