package com.eriklievaart.javalightning.bundle.control;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eriklievaart.javalightning.bundle.api.Bean;
import com.eriklievaart.javalightning.bundle.api.Parameters;
import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.osgi.toolkit.api.ServiceCollection;
import com.eriklievaart.toolkit.bean.api.BeanInjector;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.reflect.api.FieldTool;
import com.eriklievaart.toolkit.reflect.api.annotations.AnnotatedField;
import com.eriklievaart.toolkit.reflect.api.annotations.AnnotationTool;

public class InOutJector {

	private Map<Class<?>, Function<Field, ?>> suppliers = NewCollection.map();
	private RequestContext context;

	public InOutJector(RequestContext context) {
		this.context = context;
		createSuppliers(context);
	}

	private void createSuppliers(RequestContext requestContext) {
		suppliers.put(RequestContext.class, f -> requestContext);
		suppliers.put(HttpServletRequest.class, f -> requestContext.getRequest());
		suppliers.put(HttpServletResponse.class, f -> requestContext.getResponse());
		suppliers.put(HttpSession.class, f -> requestContext.getRequest().getSession());
		suppliers.put(Parameters.class, f -> requestContext.getParameterSupplier().get());
		suppliers.put(BeanInjector.class, f -> new BeanInjector(requestContext.getParameterSupplier().get().getMap()));
		suppliers.put(ServiceCollection.class, this::createServiceCollection);
	}

	public ServiceCollection<?> createServiceCollection(Field field) {
		return context.getServiceCollection(FieldTool.getGenericLiteral(field));
	}

	public void injectAnnotatedFields(Object instance) {
		Class<?> type = instance.getClass();
		for (AnnotatedField<Bean> field : AnnotationTool.getFieldsAnnotatedWith(type, Bean.class)) {
			field.inject(instance, createArgument(field.getMember()));
		}
	}

	private Object createArgument(Field field) {
		Function<Field, ?> supplier = suppliers.get(field.getType());
		Check.notNull(supplier, "Cannot inject type $ into $", field.getType(), field.getDeclaringClass());
		Object result = supplier.apply(field);
		Check.isInstance(field.getType(), result);
		return result;
	}
}
