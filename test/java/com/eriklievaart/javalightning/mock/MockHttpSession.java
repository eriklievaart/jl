package com.eriklievaart.javalightning.mock;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.eriklievaart.toolkit.lang.api.collection.FromCollection;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

@SuppressWarnings("deprecation")
public class MockHttpSession implements HttpSession {

	private Map<String, Object> attributes = NewCollection.map();

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		return FromCollection.toEnumeration(attributes.keySet());
	}

	@Override
	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	@Override
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	@Override
	public long getCreationTime() {
		return 0;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public long getLastAccessedTime() {
		return 0;
	}

	@Override
	public int getMaxInactiveInterval() {
		return 0;
	}

	@Override
	public ServletContext getServletContext() {
		return null;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return null;
	}

	@Override
	public Object getValue(String arg0) {
		return null;
	}

	@Override
	public String[] getValueNames() {
		return null;
	}

	@Override
	public void invalidate() {
	}

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public void putValue(String arg0, Object arg1) {
	}

	@Override
	public void removeValue(String arg0) {
	}

	@Override
	public void setMaxInactiveInterval(int arg0) {
	}
}
