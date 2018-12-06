package com.eriklievaart.javalightning.freemarker;

import java.util.Hashtable;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.javalightning.mock.api.MockRequestContext;
import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class FreemarkerTemplateServiceU {

	@Test
	public void render() {
		FreemarkerBeans beans = new FreemarkerBeans();
		FreemarkerTemplateService service = new FreemarkerTemplateService(beans);

		beans.getGlobalsIndex().register(TemplateGlobal.of("date", "today"));

		DummyTemplateSource templates = new DummyTemplateSource("dummy");
		templates.put("/dummy/template.tpl", "${globals.get('date')}");
		beans.getTemplateSourceListener().register(templates);

		MockRequestContext context = MockRequestContext.instance();
		String result = StreamTool.toString(service.render("/dummy/template.tpl", new Hashtable<>(), context));
		Check.isEqual(result, "today");
	}
}