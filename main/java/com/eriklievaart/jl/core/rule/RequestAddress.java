package com.eriklievaart.jl.core.rule;

import javax.servlet.http.HttpServletRequest;

import com.eriklievaart.jl.core.api.page.RouteType;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class RequestAddress {

	private final RouteType method;
	private final String domain;
	private final boolean secure;
	private String path;

	public RequestAddress(HttpServletRequest req) {
		this.method = RouteType.parse(req.getMethod());
		this.domain = req.getServerName();
		this.path = req.getRequestURI();
		this.secure = req.isSecure();
	}

	public RequestAddress(RouteType method, String path) {
		this(method, "localhost", path);
	}

	public RequestAddress(RouteType method, String domain, String path) {
		this.method = method;
		this.domain = domain;
		this.path = path;
		this.secure = false;
	}

	public String getDomain() {
		return domain;
	}

	public RouteType getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String value) {
		path = value;
	}

	public boolean isSecure() {
		return secure;
	}

	public boolean isFavicon() {
		return path.endsWith("/favicon.ico") && method == RouteType.GET;
	}

	@Override
	public String toString() {
		return Str.sub("$:$", method, path);
	}
}