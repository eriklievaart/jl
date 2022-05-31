package com.eriklievaart.jl.core.api.render;

import java.io.File;

public class FileDownload {

	private final String name;
	private final File file;

	public FileDownload(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public File getFile() {
		return file;
	}
}