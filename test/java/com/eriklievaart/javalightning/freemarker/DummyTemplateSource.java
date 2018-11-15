package com.eriklievaart.javalightning.freemarker;

import java.io.InputStream;
import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.template.TemplateSource;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class DummyTemplateSource implements TemplateSource {

	private String prefix;
	private Map<String, String> templates = NewCollection.map();

	public DummyTemplateSource(String id) {
		this.prefix = id;
	}

	public void put(String path, String template) {
		templates.put(path, template);
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public InputStream getTemplate(String path) {
		return StreamTool.toInputStream(templates.get(path));
	}

	@Override
	public long getLastModified() {
		return 0;
	}

}
