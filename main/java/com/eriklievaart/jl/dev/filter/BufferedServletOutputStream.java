package com.eriklievaart.jl.dev.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class BufferedServletOutputStream extends ServletOutputStream {
	private ByteArrayOutputStream capture;

	public BufferedServletOutputStream(ByteArrayOutputStream capture) {
		this.capture = capture;
	}

	@Override
	public void write(int b) throws IOException {
		capture.write(b);
	}

	@Override
	public void flush() throws IOException {
		capture.flush();
	}

	@Override
	public void close() throws IOException {
		capture.close();
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {
	}
}
