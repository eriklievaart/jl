package com.eriklievaart.javalightning.freemarker;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.javalightning.mock.MockFreemarkerService;
import com.eriklievaart.javalightning.mock.MockTemplateSource;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class FreemarkerTemplateServiceU {

	@Test
	public void render() {
		MockTemplateSource templates = new MockTemplateSource("dummy");
		templates.put("/dummy/template.tpl", "${globals.get('date')}");

		MockFreemarkerService freemarker = new MockFreemarkerService(templates);
		freemarker.register(TemplateGlobal.of("date", "today"));
		String result = freemarker.render("/dummy/template.tpl");

		Check.isEqual(result, "today");
	}
}