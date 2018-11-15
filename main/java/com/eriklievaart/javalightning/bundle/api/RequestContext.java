package com.eriklievaart.javalightning.bundle.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.framework.BundleContext;

import com.eriklievaart.javalightning.bundle.api.render.ServletReponseRenderer;
import com.eriklievaart.osgi.toolkit.api.BundleWrapper;
import com.eriklievaart.osgi.toolkit.api.ServiceCollection;

public class RequestContext {

	private ResponseBuilder responseBuilder = new ResponseBuilder();
	private HttpServletResponse response;
	private HttpServletRequest request;
	private BundleContext bundleContext;

	public RequestContext(BundleContext ctx, HttpServletRequest req, HttpServletResponse res) {
		this.bundleContext = ctx;
		this.request = req;
		this.response = res;
	}

	public <E> ServiceCollection<E> getServiceCollection(Class<E> type) {
		return new BundleWrapper(bundleContext).getServiceCollection(type);
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
	}
}
