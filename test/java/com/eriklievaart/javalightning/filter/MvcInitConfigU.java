package com.eriklievaart.javalightning.filter;

import java.io.File;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eriklievaart.toolkit.io.api.FileTool;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class MvcInitConfigU {

	private static final File SANDBOX = new File("/tmp/sandbox/root");

	@Before
	public void setup() {
		if (SANDBOX.exists()) {
			FileTool.delete(SANDBOX);
		}
	}

	@After
	public void cleanup() {
		if (SANDBOX.exists()) {
			FileTool.delete(SANDBOX);
		}
	}

	@Test
	public void configureNothing() throws Exception {
		File config = new File(SANDBOX, "config.ini");
		File template = new File(SANDBOX, "freemarker/template.tpl");
		template.getParentFile().mkdirs();

		List<String> freemarker = NewCollection.list();
		freemarker.add("config[freemarker]");
		FileTool.writeLines(config, freemarker);

		MvcGeneralConfig mic = new MvcGeneralConfig(SANDBOX);
		FreemarkerSettings settings = mic.getFreemarkerSettings();
		Check.isNull(settings.getTemplateDir());
		Check.isEqual(settings.getTemplateUpdateDelayMillis(), 10000);
	}

	@Test
	public void overrideTemplateDir() throws Exception {
		File config = new File(SANDBOX, "config.ini");
		File template = new File(SANDBOX, "freemarker/template.tpl");
		template.getParentFile().mkdirs();
		String templateDir = template.getParentFile().getAbsolutePath();

		List<String> freemarker = NewCollection.list();
		freemarker.add("config[freemarker]");
		freemarker.add("\ttemplate.dir=" + templateDir);
		FileTool.writeLines(config, freemarker);

		MvcGeneralConfig mic = new MvcGeneralConfig(SANDBOX);
		FreemarkerSettings settings = mic.getFreemarkerSettings();
		Check.isEqual(settings.getTemplateDir(), templateDir);
		Check.isEqual(settings.getTemplateUpdateDelayMillis(), 10000);
	}

	@Test
	public void overrideRefresh() throws Exception {
		File config = new File(SANDBOX, "config.ini");
		File template = new File(SANDBOX, "freemarker/template.tpl");
		template.getParentFile().mkdirs();

		List<String> freemarker = NewCollection.list();
		freemarker.add("config[freemarker]");
		freemarker.add("\trefresh=1000");
		FileTool.writeLines(config, freemarker);

		MvcGeneralConfig mic = new MvcGeneralConfig(SANDBOX);
		FreemarkerSettings settings = mic.getFreemarkerSettings();
		Check.isNull(settings.getTemplateDir());
		Check.isEqual(settings.getTemplateUpdateDelayMillis(), 1000);
	}

}
