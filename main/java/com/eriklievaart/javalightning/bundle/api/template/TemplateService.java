package com.eriklievaart.javalightning.bundle.api.template;

import java.io.InputStream;
import java.util.Map;

public interface TemplateService {

	public InputStream render(String view, Map<String, Object> data);
}
