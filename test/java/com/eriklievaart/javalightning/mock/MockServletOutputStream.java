package com.eriklievaart.javalightning.mock;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import com.eriklievaart.toolkit.lang.api.check.Check;

public class MockServletOutputStream extends ServletOutputStream {

	private StringBuilder builder = new StringBuilder();
	private boolean closed = false;

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {
	}

	@Override
	public void write(int value) throws IOException {
		Check.isFalse(closed);
		builder.append(value);
	}

	@Override
	public void close() throws IOException {
		closed = true;
	}

	public boolean isClosed() {
		return closed;
	}

	public String getWrittenData() {
		return builder.toString();
	}

	public void checkIsClosed() {
		Check.isTrue(isClosed());
	}

	public void checkNoDataWritten() {
		Check.isTrue(builder.length() == 0);
	}
}
