package com.eriklievaart.javalightning.filter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.eriklievaart.toolkit.io.api.FileTool;
import com.eriklievaart.toolkit.lang.api.check.CheckStr;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;

public class MvcInitConfigI {

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
	public void templateFromClasspath() throws Exception {
		File config = new File(SANDBOX, "config.ini");
		File template = new File(SANDBOX, "freemarker/template.tpl");
		template.getParentFile().mkdirs();

		List<String> freemarker = NewCollection.list();
		freemarker.add("config[freemarker]");
		freemarker.add("\trefresh=0");
		FileTool.writeLines(config, freemarker);

		MvcGeneralConfig mic = new MvcGeneralConfig(SANDBOX);
		FreemarkerSettings settings = mic.getFreemarkerSettings();
		Configuration configuration = getConfiguration(settings);

		FileTool.writeStringToFile("original", template);
		CheckStr.isEqual(readTemplate(configuration, "template.tpl"), "classpath");
	}

	@Test
	public void overrideFromTemplateDir() throws Exception {
		File config = new File(SANDBOX, "config.ini");
		File template = new File(SANDBOX, "freemarker/template.tpl");
		template.getParentFile().mkdirs();

		List<String> freemarker = NewCollection.list();
		freemarker.add("config[freemarker]");
		freemarker.add("\trefresh=0");
		freemarker.add("\ttemplate.dir=" + template.getParentFile().getAbsolutePath());
		FileTool.writeLines(config, freemarker);

		MvcGeneralConfig mic = new MvcGeneralConfig(SANDBOX);
		FreemarkerSettings settings = mic.getFreemarkerSettings();
		Configuration configuration = getConfiguration(settings);

		FileTool.writeStringToFile("override", template);
		CheckStr.isEqual(readTemplate(configuration, "template.tpl"), "override");
	}

	@Test
	public void useCache() throws Exception {
		File config = new File(SANDBOX, "config.ini");
		File template = new File(SANDBOX, "freemarker/template.tpl");
		template.getParentFile().mkdirs();

		List<String> freemarker = NewCollection.list();
		freemarker.add("config[freemarker]");
		freemarker.add("\ttemplate.dir=" + template.getParentFile().getAbsolutePath());
		FileTool.writeLines(config, freemarker);

		MvcGeneralConfig mic = new MvcGeneralConfig(SANDBOX);
		FreemarkerSettings settings = mic.getFreemarkerSettings();
		Configuration configuration = getConfiguration(settings);

		FileTool.writeStringToFile("original", template);
		CheckStr.isEqual(readTemplate(configuration, "template.tpl"), "original");

		FileTool.writeStringToFile("changed", template);
		CheckStr.isEqual(readTemplate(configuration, "template.tpl"), "original");
	}

	@Test
	public void autoRefresh() throws Exception {
		File config = new File(SANDBOX, "config.ini");
		File template = new File(SANDBOX, "freemarker/template.tpl");
		template.getParentFile().mkdirs();

		List<String> freemarker = NewCollection.list();
		freemarker.add("config[freemarker]");
		freemarker.add("\ttemplate.dir=" + template.getParentFile().getAbsolutePath());
		freemarker.add("\trefresh=0");
		FileTool.writeLines(config, freemarker);

		MvcGeneralConfig mic = new MvcGeneralConfig(SANDBOX);
		FreemarkerSettings settings = mic.getFreemarkerSettings();
		Configuration configuration = getConfiguration(settings);

		FileTool.writeStringToFile("original", template);
		CheckStr.isEqual(readTemplate(configuration, "template.tpl"), "original");

		Thread.sleep(1000);
		FileTool.writeStringToFile("changed", template);
		CheckStr.isEqual(readTemplate(configuration, "template.tpl"), "changed");
	}

	private Configuration getConfiguration(FreemarkerSettings settings) throws IOException {
		return FreemarkerConfigurationFactory.createNewConfiguration(settings);
	}

	private String readTemplate(Configuration configuration, String file) throws TemplateNotFoundException,
			MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Template tpl = configuration.getTemplate(file);
		StringWriter writer = new StringWriter();
		tpl.process(new HashMap<String, Object>(), writer);
		return writer.getBuffer().toString();
	}

}
