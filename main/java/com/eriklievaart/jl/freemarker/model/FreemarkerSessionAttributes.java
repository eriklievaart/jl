package com.eriklievaart.jl.freemarker.model;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import com.eriklievaart.toolkit.lang.api.str.Str;

public class FreemarkerSessionAttributes {

	private HttpSession session;

	public FreemarkerSessionAttributes(HttpSession session) {
		this.session = session;
	}

	public String get(String key) {
		return session.getAttribute(key).toString();
	}

	public String get(String key, String fallback) {
		Object attribute = session.getAttribute(key);
		return attribute != null ? attribute.toString() : fallback;
	}

	public boolean is(String key, String expected) {
		if (session.getAttribute(key) == null) {
			return false;
		}
		return Str.isEqual(get(key), expected);
	}

	public boolean is(String key, Integer expected) {
		if (session.getAttribute(key) == null) {
			return false;
		}
		Integer value = Integer.valueOf(get(key));
		return Objects.equals(value, expected);
	}

	public boolean is(String key, Long expected) {
		if (session.getAttribute(key) == null) {
			return false;
		}
		Long value = Long.valueOf(get(key));
		return Objects.equals(value, expected);
	}
}
