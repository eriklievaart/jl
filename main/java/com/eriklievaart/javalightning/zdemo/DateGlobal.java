package com.eriklievaart.javalightning.zdemo;

import java.util.Date;

import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;

public class DateGlobal implements TemplateGlobal {

	@Override
	public String getName() {
		return "date";
	}

	@Override
	public Object getObject() {
		return new Date();
	}
}
