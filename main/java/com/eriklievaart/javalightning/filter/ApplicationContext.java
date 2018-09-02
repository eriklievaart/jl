package com.eriklievaart.javalightning.filter;

import java.io.File;

import com.eriklievaart.javalightning.api.render.ServletReponseRendererFactory;

public class ApplicationContext {

	// used only as an override for resources packed in the application
	private File configDir = new File("/data");
	private ServletReponseRendererFactory rendererFactory;

	ApplicationContext(ServletReponseRendererFactory factory) {
		this.rendererFactory = factory;
	}

	public ServletReponseRendererFactory getRendererFactory() {
		return rendererFactory;
	}

	void setConfigDir(File file) {
		configDir = file;
	}

	public File getConfigDir() {
		return configDir;
	}

}
