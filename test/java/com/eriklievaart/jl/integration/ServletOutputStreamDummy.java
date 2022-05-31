package com.eriklievaart.jl.integration;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

import com.eriklievaart.toolkit.io.api.StringBuilderOutputStream;

public class ServletOutputStreamDummy extends ServletOutputStream {

	private StringBuilderOutputStream buffer = new StringBuilderOutputStream();

	@Override
	public boolean isReady() {
		return true;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {
	}

	@Override
	public void write(int b) throws IOException {
		buffer.write(b);
	}

	public String getResult() {
		return buffer.getResult();
	}
}