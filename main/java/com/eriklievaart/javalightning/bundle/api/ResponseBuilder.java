package com.eriklievaart.javalightning.bundle.api;

import java.util.Hashtable;
import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.render.ServletReponseRenderer;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class ResponseBuilder {

	private ServletReponseRenderer renderer = null;
	private UIDefinition view = new UIDefinition(null, 200);
	private Map<String, Object> model = NewCollection.map();
	private Map<String, String> headers = new Hashtable<>();

	public UIDefinition setView(String name) {
		UIDefinition uid = new UIDefinition(name, 200);
		view = uid;
		return uid;
	}

	public UIDefinition getView() {
		return view;
	}

	public ServletReponseRenderer getRenderer() {
		return renderer;
	}

	public Map<String, Object> getModel() {
		return model;
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
}
