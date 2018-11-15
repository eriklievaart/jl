package com.eriklievaart.javalightning.freemarker;

import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;

public class DummyTemplateGlobal implements TemplateGlobal {

	private String name;
	private Object object;

	public DummyTemplateGlobal(String name, Object object) {
		this.name = name;
		this.object = object;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Object getObject() {
		return object;
	}

}
