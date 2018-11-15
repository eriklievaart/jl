package com.eriklievaart.javalightning.freemarker;

import com.eriklievaart.javalightning.freemarker.config.FreemarkerConfigurationFactory;
import com.eriklievaart.javalightning.freemarker.config.FreemarkerOsgiConfig;
import com.eriklievaart.javalightning.freemarker.whiteboard.GlobalsIndex;
import com.eriklievaart.javalightning.freemarker.whiteboard.TemplateSourceIndex;

import freemarker.template.Configuration;

public class FreemarkerBeans {

	private TemplateSourceIndex whiteboard = new TemplateSourceIndex();
	private OsgiTemplateLoader loader = new OsgiTemplateLoader(whiteboard);
	private FreemarkerConfigurationFactory factory = new FreemarkerConfigurationFactory(loader);
	private FreemarkerOsgiConfig config = new FreemarkerOsgiConfig(factory, loader);
	private GlobalsIndex globals = new GlobalsIndex();

	public TemplateSourceIndex getTemplateSourceListener() {
		return whiteboard;
	}

	public FreemarkerConfigurationFactory getFactory() {
		return factory;
	}

	public Configuration getCachedConfiguration() {
		return factory.getCachedConfiguration();
	}

	public FreemarkerOsgiConfig getFreemarkerOsgiConfig() {
		return config;
	}

	public OsgiTemplateLoader getOsgiTemplateLoader() {
		return loader;
	}

	public GlobalsIndex getGlobalsListener() {
		return globals;
	}
}
