package com.eriklievaart.jl.bundle.route;

import java.util.HashMap;
import java.util.Map;

import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class ContentTypes {

	public static String getDefaultContentType(String path) {
		Map<String, String> extToMime = createMimeMap();
		String ext = Str.defaultIfEmpty(UrlTool.getExtension(path), "html").toLowerCase();
		return extToMime.getOrDefault(ext, "text/html");
	}

	private static Map<String, String> createMimeMap() {
		Map<String, String> extToMime = new HashMap<>();

		extToMime.put("css", "text/css");
		extToMime.put("js", "text/javascript");
		extToMime.put("png", "image/png");
		extToMime.put("jpg", "image/jpg");

		return extToMime;
	}
}