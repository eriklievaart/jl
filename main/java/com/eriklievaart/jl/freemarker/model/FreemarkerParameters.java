package com.eriklievaart.jl.freemarker.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

import com.eriklievaart.jl.core.api.Parameters;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.collection.MapTool;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class FreemarkerParameters {

	private Supplier<Parameters> supplier;

	public FreemarkerParameters(Supplier<Parameters> supplier) {
		this.supplier = supplier;
	}

	public String get(String key) {
		return supplier.get().getString(key, "");
	}

	public String get(String key, String fallback) {
		return supplier.get().getString(key, fallback);
	}

	public boolean is(String key, String expected) {
		if (!supplier.get().contains(key)) {
			return false;
		}
		return Str.isEqual(supplier.get().getString(key), expected);
	}

	public boolean is(String key, Integer expected) {
		if (!supplier.get().contains(key)) {
			return false;
		}
		Integer value = Integer.valueOf(supplier.get().getString(key));
		return Objects.equals(value, expected);
	}

	public boolean is(String key, Long expected) {
		if (!supplier.get().contains(key)) {
			return false;
		}
		Long value = Long.valueOf(supplier.get().getString(key));
		return Objects.equals(value, expected);
	}

	public String forward(String... keys) {
		return UrlTool.getQueryString(MapTool.toMap(Arrays.asList(keys), key -> get(key)));
	}
}