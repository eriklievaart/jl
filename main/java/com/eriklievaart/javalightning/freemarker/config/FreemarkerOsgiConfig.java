package com.eriklievaart.javalightning.freemarker.config;

import com.eriklievaart.javalightning.freemarker.OsgiTemplateLoader;
import com.eriklievaart.osgi.toolkit.api.BundleWrapper;

public class FreemarkerOsgiConfig {
	private static final String TEMPLATE_TIMEOUT_PROPERTY = "com.eriklievaart.javalightning.freemarker.timeout";
	private static final String TEMPLATE_PATH_PROPERTY = "com.eriklievaart.javalightning.freemarker.path";

	private FreemarkerConfigurationFactory factory;
	private OsgiTemplateLoader loader;

	public FreemarkerOsgiConfig(FreemarkerConfigurationFactory factory, OsgiTemplateLoader loader) {
		this.factory = factory;
		this.loader = loader;
	}

	public void config(BundleWrapper wrapper) {
		loader.setTemplatePath(wrapper.getPropertyString(TEMPLATE_PATH_PROPERTY, null));
		factory.setTimeout(wrapper.getPropertyLong(TEMPLATE_TIMEOUT_PROPERTY, 60 * 1000));
	}
}
