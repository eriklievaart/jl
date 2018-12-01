package com.eriklievaart.javalightning.bundle.route;

import com.eriklievaart.toolkit.io.api.UrlTool;

public class ContentTypes {

	public static String getDefaultContentType(String path) {
		String ext = UrlTool.getExtension(path);
		if (ext != null && ext.equals("css")) {
			return "text/css";
		}
		return "text/html";
	}
}
