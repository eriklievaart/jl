package com.eriklievaart.javalightning.filter;

public class FreemarkerSettings {

	private String templateDir = null;
	private int templateUpdateDelayMillis = 10 * 1000;

	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public int getTemplateUpdateDelayMillis() {
		return templateUpdateDelayMillis;
	}

	public void setTemplateUpdateDelayMillis(int templateUpdateDelayMillis) {
		this.templateUpdateDelayMillis = templateUpdateDelayMillis;
	}

}
