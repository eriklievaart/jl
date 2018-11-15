package com.eriklievaart.javalightning.freemarker;

import java.util.Hashtable;

import org.junit.Test;

import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class FreemarkerTemplateServiceU {

	@Test
	public void render() {
		FreemarkerBeans beans = new FreemarkerBeans();
		FreemarkerTemplateService service = new FreemarkerTemplateService(beans);

		beans.getGlobalsListener().register(new DummyTemplateGlobal("date", "today"));

		DummyTemplateSource templates = new DummyTemplateSource("dummy");
		templates.put("/dummy/template.tpl", "${globals.get('date')}");
		beans.getTemplateSourceListener().register(templates);

		String result = StreamTool.toString(service.render("/dummy/template.tpl", new Hashtable<>()));
		Check.isEqual(result, "today");
	}
}