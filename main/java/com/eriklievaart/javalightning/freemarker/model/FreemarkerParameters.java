package com.eriklievaart.javalightning.freemarker.model;

import java.util.function.Supplier;

import com.eriklievaart.javalightning.bundle.api.Parameters;

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
}
