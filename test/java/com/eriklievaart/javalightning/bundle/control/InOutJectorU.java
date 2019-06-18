package com.eriklievaart.javalightning.bundle.control;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.Bean;
import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.mock.MockRequestContext;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class InOutJectorU {

	@Test
	public void injectField() {
		class Dummy {
			@Bean
			private RequestContext context;
		}
		Dummy dummy = new Dummy();
		InOutJector ioj = new InOutJector(new MockRequestContext());
		ioj.injectAnnotatedFields(dummy);
		Check.notNull(dummy.context);
	}
}
