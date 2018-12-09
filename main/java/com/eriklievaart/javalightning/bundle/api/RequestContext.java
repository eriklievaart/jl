package com.eriklievaart.javalightning.bundle.api;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;

import com.eriklievaart.javalightning.bundle.api.render.ServletReponseRenderer;
import com.eriklievaart.javalightning.bundle.control.ParametersSupplier;
import com.eriklievaart.javalightning.bundle.route.ContentTypes;
import com.eriklievaart.osgi.toolkit.api.ContextWrapper;
import com.eriklievaart.osgi.toolkit.api.ServiceCollection;

public class RequestContext {

	private ResponseBuilder responseBuilder;
	private HttpServletResponse response;
	private HttpServletRequest request;
	private BundleContext bundleContext;
	private AtomicReference<ParametersSupplier> parameterSupplier = new AtomicReference<>();

	public RequestContext(BundleContext ctx, HttpServletRequest req, HttpServletResponse res) {
		this.bundleContext = ctx;
		this.request = req;
		this.response = res;
		reset();
	}

	public void setParameterSupplier(ParametersSupplier supplier) {
		parameterSupplier.set(supplier);
	}

	public Supplier<Parameters> getParameterSupplier() {
		return parameterSupplier.get();
	}

	public <E> ServiceCollection<E> getServiceCollection(Class<E> type) {
		return new ContextWrapper(bundleContext).getServiceCollection(type);
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public ResponseBuilder getResponseBuilder() {
		return responseBuilder;
	}

	public ServletReponseRenderer getRenderer() {
		return responseBuilder.getRenderer();
	}

	public void reset() {
		responseBuilder = new ResponseBuilder();
		responseBuilder.setContentType(ContentTypes.getDefaultContentType(request.getRequestURI()));
	}
}
