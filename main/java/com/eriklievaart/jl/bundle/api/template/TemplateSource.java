package com.eriklievaart.jl.bundle.api.template;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public interface TemplateSource {

	public String getPrefix();

	public InputStream getTemplate(String path);

	public long getLastModified();

	public default List<TemplateGlobal> getGlobals() {
		return new ArrayList<>();
	}
}