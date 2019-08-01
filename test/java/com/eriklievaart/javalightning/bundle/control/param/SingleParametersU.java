package com.eriklievaart.javalightning.bundle.control.param;

import org.junit.Test;

import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.MultiMap;

public class SingleParametersU {

	@Test
	public void getOptional() {
		MultiMap<String, String> map = new MultiMap<>();
		map.add("present", "value");
		SingleParameters parameters = new SingleParameters(map.toHashtable());

		Check.isFalse(parameters.getOptional("missing").isPresent());
		Check.isTrue(parameters.getOptional("present").isPresent());
		Check.isEqual(parameters.getOptional("present").get(), "value");
	}
}
