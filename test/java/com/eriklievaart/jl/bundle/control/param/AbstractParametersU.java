package com.eriklievaart.jl.bundle.control.param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.eriklievaart.jl.bundle.api.Parameters;
import com.eriklievaart.toolkit.bean.api.BeanInjector;
import com.eriklievaart.toolkit.bean.api.BeanValidationException;
import com.eriklievaart.toolkit.bean.api.annotate.Size;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.MultiMap;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.mock.BombSquad;

public class AbstractParametersU {

	@Test
	public void getMap() {
		MultiMap<String, String> map = new MultiMap<>();
		map.add("foo", "bar");
		map.add("foo", "bas");
		map.add("fop", "bat");
		Parameters parameters = new SingleParameters(map.toHashtable());

		Assertions.assertThat(parameters.getMap()).containsEntry("foo", "bar");
		Assertions.assertThat(parameters.getMap()).containsEntry("fop", "bat");

		Assertions.assertThat(parameters.getMap("foo")).containsEntry("foo", "bar");
		Assertions.assertThat(parameters.getMap("foo")).doesNotContainKey("fop");

		Assertions.assertThat(parameters.getMap("foo", "fop")).containsEntry("foo", "bar");
		Assertions.assertThat(parameters.getMap("foo", "fop")).containsEntry("fop", "bat");
	}

	@Test
	public void injectStrings() {
		class Injectme {
			public String a;
			public String b;
		}
		Injectme injectme = new Injectme();

		MultiMap<String, String> map = new MultiMap<>();
		map.add("a", "alpha");
		map.add("b", "beta");
		Parameters parameters = new SingleParameters(map.toHashtable());

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

		BombSquad.diffuse(BeanValidationException.class, "invalid: [inject", () -> {
			new BeanInjector(parameters.getMap()).inject(injectme).validate(injectme);
		});
	}
}