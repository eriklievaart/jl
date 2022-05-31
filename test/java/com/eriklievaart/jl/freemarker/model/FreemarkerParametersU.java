package com.eriklievaart.jl.freemarker.model;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.eriklievaart.jl.bundle.control.param.SingleParameters;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.MultiMap;

public class FreemarkerParametersU {

	@Test
	public void get() {
		MultiMap<String, String> map = new MultiMap<>();
		map.add("key", "value");

		FreemarkerParameters testable = new FreemarkerParameters(() -> new SingleParameters(map.unmodifiableMap()));
		Check.isEqual(testable.get("key"), "value");
		Check.isEqual(testable.get("key", ""), "value");
		Check.isEqual(testable.get("mising", "foo"), "foo");
	}

	@Test
	public void forward() {
		MultiMap<String, String> map = new MultiMap<>();
		map.add("a", "1");
		map.add("b", "2");

		FreemarkerParameters testable = new FreemarkerParameters(() -> new SingleParameters(map.unmodifiableMap()));
		Check.isEqual(testable.forward("a"), "a=1");
		Check.isEqual(testable.forward("b"), "b=2");
		Check.isEqual(testable.forward("c"), "c=");

		Assertions.assertThat(testable.forward("a", "b")).isIn("a=1&b=2", "b=2&a=1");
	}

	@Test
	public void isString() {
		MultiMap<String, String> map = new MultiMap<>();
		map.add("key", "foo");

		FreemarkerParameters testable = new FreemarkerParameters(() -> new SingleParameters(map.unmodifiableMap()));
		Check.isTrue(testable.is("key", "foo"));
		Check.isFalse(testable.is("key", "bar"));
		Check.isFalse(testable.is("key", (String) null));
		Check.isFalse(testable.is("wrong key", "foo"));
	}

	@Test
	public void isInteger() {
		MultiMap<String, String> map = new MultiMap<>();
		map.add("key", "1234");

		FreemarkerParameters testable = new FreemarkerParameters(() -> new SingleParameters(map.unmodifiableMap()));
		Check.isTrue(testable.is("key", 1234));
		Check.isFalse(testable.is("key", 5678));
		Check.isFalse(testable.is("key", (Integer) null));
		Check.isFalse(testable.is("wrong key", 1234));
	}

	@Test
	public void isLong() {
		MultiMap<String, String> map = new MultiMap<>();
		map.add("key", "1234");

		FreemarkerParameters testable = new FreemarkerParameters(() -> new SingleParameters(map.unmodifiableMap()));
		Check.isTrue(testable.is("key", 1234l));
		Check.isFalse(testable.is("key", 5678l));
		Check.isFalse(testable.is("key", (Long) null));
		Check.isFalse(testable.is("wrong key", 1234l));
	}
}