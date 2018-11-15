package com.eriklievaart.javalightning.bundle.control;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eriklievaart.javalightning.bundle.api.Bean;
import com.eriklievaart.javalightning.bundle.api.Parameters;
import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.reflect.api.annotations.AnnotatedField;
import com.eriklievaart.toolkit.reflect.api.annotations.AnnotationTool;

public class InOutJector implements AutoCloseable {

	private Map<Class<?>, Supplier<?>> suppliers = NewCollection.map();
	private RequestContext context;
	private List<CloseableSilently> closeables = NewCollection.concurrentList();

	public InOutJector(RequestContext context) {
		this.context = context;
		createSuppliers(context);
	}

	private void createSuppliers(RequestContext requestContext) {
		suppliers.put(RequestContext.class, () -> requestContext);
		suppliers.put(HttpServletRequest.class, () -> requestContext.getRequest());
		suppliers.put(HttpServletResponse.class, () -> requestContext.getResponse());
		suppliers.put(HttpSession.class, () -> requestContext.getRequest().getSession());
		suppliers.put(Parameters.class, this::createParameters);
	}

	public Parameters createParameters() {
		return new ParametersSupplier(context.getRequest(), closeables).get();
	}

	public void injectAnnotatedFields(Object instance) {
		for (AnnotatedField<Bean> field : AnnotationTool.getFieldsAnnotatedWith(instance.getClass(), Bean.class)) {
			field.inject(instance, createArgument(field.getType()));
		}
	}

	public Object[] createArguments(Method m) {
		List<Object> parameters = NewCollection.list();
		for (Parameter parameter : m.getParameters()) {
			parameters.add(createArgument(parameter.getType()));
		}
		return parameters.toArray();
	}

	private Object createArgument(Class<?> type) {
		Supplier<?> supplier = suppliers.get(type);
		Check.notNull(supplier, "Cannot inject type $", type);
		Object result = supplier.get();
		Check.isInstance(type, result);
		return result;
	}

	@Override
	public void close() {
		for (CloseableSilently close : closeables) {
			try {
				System.currentTimeMillis(); // dummy statement for check style
			} finally {
				close.close();
			}
		}
	}
}