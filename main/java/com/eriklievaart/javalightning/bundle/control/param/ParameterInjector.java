package com.eriklievaart.javalightning.bundle.control.param;

import com.eriklievaart.javalightning.bundle.api.Parameters;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.reflect.api.FieldTool;
import com.eriklievaart.toolkit.reflect.api.InstanceTool;

public class ParameterInjector {

	private Parameters parameters;

	public ParameterInjector(Parameters parameters) {
		this.parameters = parameters;
	}

	public void inject(Object object) {
		Check.notNull(object);

		Class<? extends Object> type = object.getClass();
		for (String name : FieldTool.getFieldNames(type)) {
			if (parameters.contains(name)) {
				InstanceTool.injectField(object, FieldTool.getField(type, name), parameters.getString(name));
			}
		}
	}
}
