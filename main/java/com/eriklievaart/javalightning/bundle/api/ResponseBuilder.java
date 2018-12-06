package com.eriklievaart.javalightning.bundle.api;

import java.util.Hashtable;
import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.render.ServletReponseRenderer;

public class ResponseBuilder {

	private ServletReponseRenderer renderer = null;
	private Map<String, String> headers = new Hashtable<>();
	private int statusCode = 200;

	public ServletReponseRenderer getRenderer() {
		return renderer;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setContentType(String type) {
		headers.put("Content-type", type);
	}

	public void setRenderer(ServletReponseRenderer value) {
		renderer = value;
	}

	public void setStatusCode(int value) {
		statusCode = value;
	}

	public int getStatusCode() {
		return statusCode;
	}
}
