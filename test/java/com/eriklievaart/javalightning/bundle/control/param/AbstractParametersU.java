package com.eriklievaart.javalightning.bundle.control.param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.Parameters;
import com.eriklievaart.toolkit.bean.api.BeanValidationException;
import com.eriklievaart.toolkit.bean.api.annotate.Size;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.mock.BombSquad;

public class AbstractParametersU {

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

	@Test
	public void injectValidate() {
		class Injectme {
			@Size(min = 1, max = 5)
			public String inject;
		}
		Injectme injectme = new Injectme();

		Map<String, List<String>> map = NewCollection.map();
		map.put("inject", Arrays.asList("1234567890"));
		Parameters parameters = new SingleParameters(map);

		BombSquad.diffuse(BeanValidationException.class, "invalid: [inject]", () -> {
			parameters.getParamInjector().inject(injectme).validate(injectme);
		});
	}
}
