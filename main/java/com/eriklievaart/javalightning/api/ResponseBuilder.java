package com.eriklievaart.javalightning.api;

import java.util.Map;

import com.eriklievaart.javalightning.api.render.ServletReponseRenderer;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class ResponseBuilder {

	private ServletReponseRenderer renderer = null;
	private UIDefinition view = new UIDefinition(null, 200);
	private Map<String, Object> model = NewCollection.map();

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

	public void setRenderer(ServletReponseRenderer value) {
		renderer = value;
	}
}
