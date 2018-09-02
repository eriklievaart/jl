package com.eriklievaart.javalightning.api;

import com.eriklievaart.toolkit.lang.api.check.Check;

public class UrlMapping {

	private String name;
	private String url;

	public UrlMapping(String name, String url) {
		Check.notBlank(name);
		Check.notBlank(url);

		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}
}
