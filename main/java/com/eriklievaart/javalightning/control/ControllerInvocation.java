package com.eriklievaart.javalightning.control;

import java.lang.reflect.Method;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.toolkit.lang.api.ToString;
import com.eriklievaart.toolkit.reflect.api.LiteralTool;
import com.eriklievaart.toolkit.reflect.api.MethodTool;
import com.eriklievaart.toolkit.reflect.api.ReflectException;
import com.eriklievaart.toolkit.reflect.api.method.MethodWrapper;

public class ControllerInvocation {

	private final Method m;

	ControllerInvocation(String literal, String method) {
		m = MethodTool.getMethod(literal, method);

		try {
			LiteralTool.newInstance(literal);
		} catch (ReflectException re) {
			throw new ReflectException("Unable to instantiate controller. Does % have a default contructor?", literal);
		}
	}

	public void injectAndInvoke(FilterContext context) {
		try (InOutJector ioj = new InOutJector(context)) {
			Object instance = ioj.createInjected(m.getDeclaringClass());

			new MethodWrapper(m, instance).invoke(ioj.createArguments(m));
		}
	}

	public Class<?> getControllerClass() {
		return m.getDeclaringClass();
	}

	public Method getControllerMethod() {
		return m;
	}

	@Override
	public String toString() {
		return ToString.simple(this, "$[$#$]", m.getDeclaringClass().getSimpleName(), m.getName());
	}
}
