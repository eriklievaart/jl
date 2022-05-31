package com.eriklievaart.jl.bundle.control.param;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import com.eriklievaart.jl.bundle.control.param.MemoryMultiPartSupplier;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class MemoryMultiPartSupplierU {

	@Test
	public void getInputStream() throws IOException {
		byte[] bytes = new byte[2];
		bytes[0] = -61;
		bytes[1] = -77;

		try (MemoryMultiPartSupplier mmps = new MemoryMultiPartSupplier("bla", bytes);) {
			InputStream is = mmps.getInputStream();
			Check.isEqual(is.read(), 256 - 61);
			Check.isEqual(is.read(), 256 - 77);
		}
	}

	@Test
	public void getString() throws IOException {
		byte[] bytes = new byte[2];
		bytes[0] = -61;
		bytes[1] = -77;

		try (MemoryMultiPartSupplier mmps = new MemoryMultiPartSupplier("bla", bytes);) {
			Check.isEqual("ó", mmps.getAsString());
		}
	}
}