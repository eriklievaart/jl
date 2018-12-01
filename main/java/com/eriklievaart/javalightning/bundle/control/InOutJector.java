package com.eriklievaart.javalightning.bundle.control;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eriklievaart.javalightning.bundle.api.Bean;
import com.eriklievaart.javalightning.bundle.api.Parameters;
import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.osgi.Service;
import com.eriklievaart.osgi.toolkit.api.ServiceCollection;
import com.eriklievaart.toolkit.lang.api.FormattedException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.reflect.api.FieldTool;
import com.eriklievaart.toolkit.reflect.api.annotations.AnnotatedField;
import com.eriklievaart.toolkit.reflect.api.annotations.AnnotationTool;

public class InOutJector implements AutoCloseable {

	private Map<Class<?>, Function<Field, ?>> suppliers = NewCollection.map();
	private RequestContext context;
	private List<CloseableSilently> closeables = NewCollection.concurrentList();

	public InOutJector(RequestContext context) {
		this.context = context;
		createSuppliers(context);
	}

	private void createSuppliers(RequestContext requestContext) {
		suppliers.put(RequestContext.class, f -> requestContext);
		suppliers.put(HttpServletRequest.class, f -> requestContext.getRequest());
		suppliers.put(HttpServletResponse.class, f -> requestContext.getResponse());
		suppliers.put(HttpSession.class, f -> requestContext.getRequest().getSession());
		suppliers.put(ServiceCollection.class, this::createServiceCollection);
		suppliers.put(Parameters.class, this::createParameters);
	}

	@SuppressWarnings("unused")
	public Parameters createParameters(Field field) {
		return new ParametersSupplier(context.getRequest(), closeables).get();
	}

	public ServiceCollection<?> createServiceCollection(Field field) {
		return context.getServiceCollection(FieldTool.getGenericLiteral(field));
	}

	public void injectAnnotatedFields(Object instance) {
		Class<?> type = instance.getClass();
		for (AnnotatedField<Bean> field : AnnotationTool.getFieldsAnnotatedWith(type, Bean.class)) {
			field.inject(instance, createArgument(field.getMember()));
		}
		for (AnnotatedField<Service> field : AnnotationTool.getFieldsAnnotatedWith(type, Service.class)) {
			throw new FormattedException("OSGI: $", field.getMember());
		}
	}

	private Object createArgument(Field field) {
		Function<Field, ?> supplier = suppliers.get(field.getType());
		Check.notNull(supplier, "Cannot inject type $", field.getType());
		Object result = supplier.apply(field);
		Check.isInstance(field.getType(), result);
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