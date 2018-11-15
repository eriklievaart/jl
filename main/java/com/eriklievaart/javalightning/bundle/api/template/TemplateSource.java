package com.eriklievaart.javalightning.bundle.api.template;

import java.io.InputStream;

public interface TemplateSource {

	public String getPrefix();

	public InputStream getTemplate(String path);

	public long getLastModified();
}
