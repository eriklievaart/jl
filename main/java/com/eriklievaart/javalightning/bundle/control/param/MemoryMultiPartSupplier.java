package com.eriklievaart.javalightning.bundle.control.param;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.eriklievaart.javalightning.bundle.api.MultiPartParameter;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class MemoryMultiPartSupplier implements MultiPartParameter {
	private LogTemplate log = new LogTemplate(getClass());

	private byte[] bytes;
	private String name;

	public MemoryMultiPartSupplier(String name, byte[] bytes) {
		Check.notNull(name, bytes);

		this.name = name;
		this.bytes = bytes;

		dump();
		log.trace("upload % keeping $ bytes in memory", name, bytes.length);
	}

	private void dump() {
		if (bytes.length > 0) {
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				builder.append(bytes[i]).append(" ");
			}
			log.trace("bytes: " + builder);
		}
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public long getSize() {
		return bytes.length;
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(bytes);
	}

	@Override
	public void close() {
	}
}
