package com.eriklievaart.jl.bundle.api.template;

import java.util.function.Supplier;

public interface TemplateGlobal {

	public String getName();

	public Object getObject();

	public static TemplateGlobal of(String name, Object object) {
		if (object instanceof TemplateGlobal) {
			throw new RuntimeException("do not nest TemplateGlobals");
		}
		return of(name, () -> object);
	}

	public static TemplateGlobal of(String name, Supplier<Object> supplier) {
		return new TemplateGlobal() {
			@Override
			public String getName() {
				return name;
			}

			@Override
			public Object getObject() {
				return supplier.get();
			}
		};
	}
}