package com.eriklievaart.javalightning.bundle.api;

import java.util.function.Consumer;

import com.eriklievaart.javalightning.bundle.api.render.ServletReponseRenderer;
import com.eriklievaart.toolkit.lang.api.collection.Box2;
import com.eriklievaart.toolkit.lang.api.collection.MultiMap;

public class ResponseBuilder {

	private ServletReponseRenderer renderer = null;
	private MultiMap<String, String> headers = new MultiMap<>();
	private int statusCode = 200;

	public ServletReponseRenderer getRenderer() {
		return renderer;
	}

	public MultiMap<String, String> getHeaders() {
		return headers;
	}

	public void forEachHeader(Consumer<Box2<String, String>> consumer) {
		headers.forEach((key, list) -> list.forEach(value -> consumer.accept(new Box2<>(key, value))));
	}

	public void setContentType(String type) {
		headers.setSingleValue("Content-type", type);
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
