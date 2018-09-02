package com.eriklievaart.javalightning.api;

import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.javalightning.api.render.ServletReponseRenderer;
import com.eriklievaart.javalightning.filter.ApplicationContext;

public class FilterContext {

	private ResponseBuilder responseBuilder = new ResponseBuilder();
	private HttpServletResponse response;
	private HttpServletRequest request;
	private FilterChain chain;
	private ApplicationContext applicationContext;

	public FilterContext(ApplicationContext ctx, HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
		this.applicationContext = ctx;
		this.request = req;
		this.response = res;
		this.chain = chain;
	}

	public FilterChain getFilterChain() {
		return chain;
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
		ServletReponseRenderer override = responseBuilder.getRenderer();
		return override != null ? override : getDefaultRenderer(responseBuilder.getView(), responseBuilder.getModel());
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public ServletReponseRenderer getDefaultRenderer(UIDefinition view, Map<String, Object> model) {
		return applicationContext.getRendererFactory().createRenderer(view, model);
	}

	public void reset() {
		responseBuilder = new ResponseBuilder();
	}
}
