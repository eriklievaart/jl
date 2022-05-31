package com.eriklievaart.jl.mock;

import java.io.InputStream;
import java.util.Map;

import com.eriklievaart.jl.core.api.RequestContext;
import com.eriklievaart.jl.core.api.osgi.JavalightningId;
import com.eriklievaart.jl.core.api.template.TemplateService;
import com.eriklievaart.toolkit.io.api.StreamTool;

public class MockTemplateService implements TemplateService {

	private Class<?> loader;
	private String service;

	public MockTemplateService(String service, Class<?> loader) {
		JavalightningId.validateSyntax(service);
		this.service = service;
		this.loader = loader;
	}

	@Override
	public InputStream render(String view, Map<String, Object> data, RequestContext context) {
		MockFreemarkerService delegate = new MockFreemarkerService(loader, service);
		delegate.putAll(data);
		return StreamTool.toInputStream(delegate.render(view));
	}
}