package com.eriklievaart.jl.freemarker.config;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

public class FreemarkerConfigurationFactory {

	private final TemplateLoader templateLoader;
	private final AtomicLong timeout = new AtomicLong();
	private final AtomicReference<Configuration> configurationReference = new AtomicReference<>();

	public FreemarkerConfigurationFactory(TemplateLoader loader) {
		templateLoader = loader;
	}

	public void setTimeout(long millis) {
		timeout.set(millis);
		configurationReference.set(null);
	}

	public Configuration getCachedConfiguration() {
		Configuration cached = configurationReference.get();
		if (cached != null) {
			return cached;
		}
		configurationReference.set(createNewConfiguration());
		return configurationReference.get();
	}

	public Configuration createNewConfiguration() {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
		configuration.setLocalizedLookup(false);
		configuration.setEncoding(Locale.getDefault(), "UTF-8");
		configuration.setTemplateUpdateDelayMilliseconds(timeout.get());
		configuration.setTemplateLoader(templateLoader);
		return configuration;
	}
}