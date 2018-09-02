package com.eriklievaart.javalightning.api;

import java.util.Collections;
import java.util.Map;

import com.eriklievaart.toolkit.lang.api.ToString;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class UIDefinition {

	private String path;
	private int statusCode;
	private Map<String, Object> attributes = NewCollection.concurrentMap();

	UIDefinition(String path, int status) {
		this.path = path;
		this.statusCode = status;
	}

	public static UIDefinition view(String path) {
		return new UIDefinition(path, 200);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String value) {
		path = value;
	}

	public void setStatusCode(int value) {
		statusCode = value;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void put(String key, Object value) {
		attributes.put(key, value);
	}

	public Object get(String key) {
		return attributes.get(key);
	}

	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}

	@Override
	public String toString() {
		return ToString.simple(this, "$[$: $]", statusCode, path);
	}

}
