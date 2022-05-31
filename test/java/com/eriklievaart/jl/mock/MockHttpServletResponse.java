package com.eriklievaart.jl.mock;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.toolkit.lang.api.check.Check;

public class MockHttpServletResponse extends MockServletResponse implements HttpServletResponse {

	private String redirected;
	private int status = 0;

	@Override
	public void sendRedirect(String url) throws IOException {
		this.redirected = url;
	}

	public void checkIsRedirectedTo(String expected) {
		Check.isEqual(redirected, expected);
	}

	@Override
	public void addCookie(Cookie arg0) {
	}

	@Override
	public void addDateHeader(String arg0, long arg1) {
	}

	@Override
	public void addHeader(String arg0, String arg1) {
	}

	@Override
	public void addIntHeader(String arg0, int arg1) {
	}

	@Override
	public boolean containsHeader(String arg0) {
		return false;
	}

	@Override
	public String encodeRedirectURL(String arg0) {
		return null;
	}

	@Override
	public String encodeRedirectUrl(String arg0) {
		return null;
	}

	@Override
	public String encodeURL(String arg0) {
		return null;
	}

	@Override
	public String encodeUrl(String arg0) {
		return null;
	}

	@Override
	public String getHeader(String arg0) {
		return null;
	}

	@Override
	public Collection<String> getHeaderNames() {
		return null;
	}

	@Override
	public Collection<String> getHeaders(String arg0) {
		return null;
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public void sendError(int code) throws IOException {
		this.status = code;
	}

	@Override
	public void sendError(int code, String message) throws IOException {
		this.status = code;
	}

	@Override
	public void setDateHeader(String arg0, long arg1) {
	}

	@Override
	public void setHeader(String arg0, String arg1) {
	}

	@Override
	public void setIntHeader(String arg0, int arg1) {
	}

	@Override
	public void setStatus(int value) {
		status = value;
	}

	@Override
	public void setStatus(int value, String message) {
		status = value;
	}

	public void checkSendError(int code) {
		Check.isEqual(status, code);
	}
}