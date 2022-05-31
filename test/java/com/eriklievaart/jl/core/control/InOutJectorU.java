package com.eriklievaart.jl.core.control;

import org.junit.Test;

import com.eriklievaart.jl.core.api.Bean;
import com.eriklievaart.jl.core.api.RequestContext;
import com.eriklievaart.jl.core.api.Singleton;
import com.eriklievaart.jl.mock.MockRequestContext;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class InOutJectorU {

	@Test
	public void injectAnnotatedFieldsPredefined() {
		class Dummy {
			@Bean
			private RequestContext context;
		}
		Dummy dummy = new Dummy();
		InOutJector ioj = new InOutJector(new MockRequestContext());
		ioj.injectAnnotatedFields(dummy);
		Check.notNull(dummy.context);
	}

	@Test
	public void injectAnnotatedFieldsManagedBean() {
		class Dummy {
			@Bean
			private ManagedBean bean;
		}
		Dummy dummy = new Dummy();
		InOutJector ioj = new InOutJector(new MockRequestContext());

		Check.isNull(dummy.bean);
		ioj.injectAnnotatedFields(dummy);
		Check.notNull(dummy.bean);
	}

	@Singleton
	public static class ManagedBean {
	}
}