package com.eriklievaart.javalightning.bundle.control.param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.Parameters;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class ParameterInjectorU {

	@Test
	public void injectStrings() {
		class Injectme {
			public String a;
			public String b;
		}
		Injectme injectme = new Injectme();

		Map<String, List<String>> map = NewCollection.map();
		map.put("a", Arrays.asList("alpha"));
		map.put("b", Arrays.asList("beta"));
		Parameters parameters = new SingleParameters(map);

		new ParameterInjector(parameters).inject(injectme);
		Check.isEqual(injectme.a, "alpha");
		Check.isEqual(injectme.b, "beta");
	}
}
