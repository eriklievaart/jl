package com.eriklievaart.jl.mock;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletResponse;

public class MockServletResponse implements ServletResponse {

	private MockServletOutputStream output = new MockServletOutputStream();

	@Override
	public void flushBuffer() throws IOException {
	}

	@Override
	public int getBufferSize() {
		return 0;
	}

	@Override
	public String getCharacterEncoding() {
		return null;
	}

	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public Locale getLocale() {
		return null;
	}

	@Override
	public MockServletOutputStream getOutputStream() throws IOException {
		return output;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		return null;
	}

	@Override
	public boolean isCommitted() {
		return false;
	}

	@Override
	public void reset() {
	}

	@Override
	public void resetBuffer() {
	}

	@Override
	public void setBufferSize(int arg0) {
	}

	@Override
	public void setCharacterEncoding(String arg0) {
	}

	@Override
	public void setContentLength(int arg0) {
	}

	@Override
	public void setContentLengthLong(long arg0) {
	}

	@Override
	public void setContentType(String arg0) {
	}

	@Override
	public void setLocale(Locale arg0) {
	}
}