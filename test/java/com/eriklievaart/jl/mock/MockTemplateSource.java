package com.eriklievaart.jl.mock;

import java.io.InputStream;
import java.util.Map;

import com.eriklievaart.jl.bundle.api.template.TemplateSource;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class MockTemplateSource implements TemplateSource {

	private Map<String, String> templates = NewCollection.map();
	private String prefix;

	public MockTemplateSource(String prefix) {
		this.prefix = prefix;
	}

	public void put(String path, String template) {
		Check.isEqual(UrlTool.getHead(path), prefix);
		Check.isTrue(path.startsWith("/"));
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