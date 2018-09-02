package com.eriklievaart.javalightning.control;

import org.junit.Test;

import com.eriklievaart.javalightning.api.Bean;
import com.eriklievaart.javalightning.api.FilterContextBuilder;
import com.eriklievaart.javalightning.api.ResponseBuilder;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class InOutJectorU {

	@Test
	public void injectNavigator() throws NoSuchMethodException, SecurityException {
		InOutJector ioj = new InOutJector(FilterContextBuilder.newInstance());

		Object[] arguments = ioj.createArguments(getClass().getMethod("injectNavigator", ResponseBuilder.class));
		Check.isEqual(arguments.length, 1);
		Check.notNull(arguments[0]);
		Check.isEqual(arguments[0].getClass(), ResponseBuilder.class);

		ioj.close();
	}

	@Test
	public void injectField() {
		class Dummy {
			@Bean
			private ResponseBuilder navigator;
		}
		Dummy dummy = new Dummy();
		InOutJector ioj = new InOutJector(FilterContextBuilder.newInstance());
		ioj.injectAnnotatedFields(dummy);
		Check.isInstance(ResponseBuilder.class, dummy.navigator);

		ioj.close();
	}

	public void injectNavigator(ResponseBuilder navigator) {
	}

}
