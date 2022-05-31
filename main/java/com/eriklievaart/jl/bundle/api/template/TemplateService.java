package com.eriklievaart.jl.bundle.api.template;

import java.io.InputStream;
import java.util.Map;

import com.eriklievaart.jl.bundle.api.RequestContext;

public interface TemplateService {

	public InputStream render(String view, Map<String, Object> data, RequestContext context);
}