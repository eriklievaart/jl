package com.eriklievaart.javalightning.freemarker.config;

import com.eriklievaart.javalightning.freemarker.OsgiTemplateLoader;
import com.eriklievaart.osgi.toolkit.api.ContextWrapper;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class FreemarkerOsgiConfig {
	private static final String TEMPLATE_TIMEOUT_PROPERTY = "com.eriklievaart.javalightning.freemarker.timeout";
	private static final String TEMPLATE_PATH_PROPERTY = "com.eriklievaart.javalightning.freemarker.path";

	private LogTemplate log = new LogTemplate(getClass());
	private FreemarkerConfigurationFactory factory;
	private OsgiTemplateLoader loader;

	public FreemarkerOsgiConfig(FreemarkerConfigurationFactory factory, OsgiTemplateLoader loader) {
		this.factory = factory;
		this.loader = loader;
	}

	public void config(ContextWrapper wrapper) {
		String path = wrapper.getPropertyString(TEMPLATE_PATH_PROPERTY, null);
		long timeout = wrapper.getPropertyLong(TEMPLATE_TIMEOUT_PROPERTY, 60 * 1000);
		logHotDeploymentSettings(path, timeout);
		loader.setTemplatePath(path);
		factory.setTimeout(timeout);
	}

	private void logHotDeploymentSettings(String path, long timeout) {
		if (Str.isBlank(path)) {
			log.debug("hot deployment is off");
		} else {
			log.info("hot deployment path % timeout $", path, timeout);
		}
	}
}
