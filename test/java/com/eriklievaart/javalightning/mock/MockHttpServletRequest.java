package com.eriklievaart.javalightning.mock;

import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import com.eriklievaart.toolkit.io.api.UrlTool;

public class MockHttpServletRequest extends MockServletRequest implements HttpServletRequest {

	private String url = "https://www.example.com/path";
	private MockHttpSession session = new MockHttpSession();

	public void setUrl(String value) {
		url = value;
	}

	@Override
	public boolean authenticate(HttpServletResponse arg0) throws IOException, ServletException {
		return false;
	}

	@Override
	public String changeSessionId() {
		return null;
	}

	@Override
	public String getAuthType() {
		return null;
	}

	@Override
	public String getContextPath() {
		return null;
	}

	@Override
	public Cookie[] getCookies() {
		return null;
	}

	@Override
	public long getDateHeader(String arg0) {
		return 0;
	}

	@Override
	public String getHeader(String arg0) {
		return null;
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return null;
	}

	@Override
	public Enumeration<String> getHeaders(String arg0) {
		return null;
	}

	@Override
	public int getIntHeader(String arg0) {
		return 0;
	}

	@Override
	public String getMethod() {
		return null;
	}

	@Override
	public Part getPart(String arg0) throws IOException, ServletException {
		return null;
	}

	@Override
	public Collection<Part> getParts() throws IOException, ServletException {
		return null;
	}

	@Override
	public String getPathInfo() {
		return null;
	}

	@Override
	public String getPathTranslated() {
		return null;
	}

	@Override
	public String getQueryString() {
		return null;
	}

	@Override
	public String getRemoteUser() {
		return null;
	}

	@Override
	public String getRequestURI() {
		return UrlTool.getPath(url);
	}

	@Override
	public StringBuffer getRequestURL() {
		return new StringBuffer(url);
	}

	@Override
	public String getRequestedSessionId() {
		return null;
	}

	@Override
	public String getServletPath() {
		return null;
	}

	@Override
	public MockHttpSession getSession() {
		return session;
	}

	@Override
	public MockHttpSession getSession(boolean arg0) {
		return session;
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	@Override
	public boolean isRequestedSessionIdFromCookie() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromURL() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	@Override
	public boolean isRequestedSessionIdValid() {
		return false;
	}

	@Override
	public boolean isUserInRole(String arg0) {
		return false;
	}

	@Override
	public void login(String arg0, String arg1) throws ServletException {
	}

	@Override
	public void logout() throws ServletException {
	}

	@Override
	public <T extends HttpUpgradeHandler> T upgrade(Class<T> arg0) throws IOException, ServletException {
		return null;
	}
}
