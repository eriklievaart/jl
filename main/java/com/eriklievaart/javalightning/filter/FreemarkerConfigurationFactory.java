package com.eriklievaart.javalightning.filter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;

public class FreemarkerConfigurationFactory {
	private static final LogTemplate log = new LogTemplate(FreemarkerConfigurationFactory.class);

	private static FreemarkerSettings settings;
	private static final AtomicReference<Configuration> CONFIGURATION_REFERENCE = new AtomicReference<Configuration>();

	static void setFreemarkerSettings(FreemarkerSettings defaults) {
		settings = defaults;
	}

	public static Configuration getCachedConfiguration() throws IOException {
		Configuration cached = CONFIGURATION_REFERENCE.get();
		if (cached != null) {
			return cached;
		}
		Configuration configuration = FreemarkerConfigurationFactory.createNewConfiguration(settings);
		CONFIGURATION_REFERENCE.set(configuration);
		return configuration;
	}

	public static Configuration createNewConfiguration(FreemarkerSettings settings) throws IOException {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_26);
		configuration.setEncoding(Locale.getDefault(), "UTF-8");
		configuration.setTemplateUpdateDelayMilliseconds(settings.getTemplateUpdateDelayMillis());
		configuration.setTemplateLoader(getTemplateLoader(settings));
		return configuration;
	}

	private static TemplateLoader getTemplateLoader(FreemarkerSettings settings) throws IOException {
		String templatePath = settings.getTemplateDir();
		String classpathDir = "/WEB-INF/freemarker/";

		ClassTemplateLoader ctl = new ClassTemplateLoader(FreemarkerConfigurationFactory.class, classpathDir);
		if (Str.isBlank(templatePath)) {
			log.info("loading freemarker templates from classpath $", classpathDir);
			return ctl;
		}
		log.info("loading freemarker templates directory %, then from classpath $", templatePath, classpathDir);
		FileTemplateLoader ftl = new FileTemplateLoader(new File(templatePath));
		return new MultiTemplateLoader(new TemplateLoader[] { ftl, ctl });
	}

}
