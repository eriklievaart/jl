package com.eriklievaart.javalightning.freemarker;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.Bean;
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

	@Test
	public void renderWithInjectedBean() {
		MockTemplateSource templates = new MockTemplateSource("dummy");
		templates.put("/dummy/template.tpl", "${globals.get('variable').get()}");

		class InjectMe implements Supplier<String> {
			@Bean
			private HttpServletRequest request;

			@Override
			public String get() {
				Check.notNull(request, "HttpServletRequest should be injected");
				return request.getParameter("fruit");
			}
		}
		MockFreemarkerService freemarker = new MockFreemarkerService(templates);
		freemarker.addParameter("fruit", "pomegranate");
		freemarker.register(TemplateGlobal.of("variable", () -> new InjectMe()));
		String result = freemarker.render("/dummy/template.tpl");

		Check.isEqual(result, "pomegranate");
	}
}