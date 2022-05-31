package com.eriklievaart.jl.bundle.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import com.eriklievaart.jl.bundle.control.CloseableSilently;
import com.eriklievaart.jl.bundle.control.param.FileMultipartSupplier;
import com.eriklievaart.jl.bundle.control.param.MemoryMultiPartSupplier;
import com.eriklievaart.toolkit.io.api.StreamTool;

public interface MultiPartParameter extends CloseableSilently {

	public long getSize();

	public InputStream getInputStream() throws IOException;

	public String getName();

	public default void copyToDestination(File destination) throws IOException {
		StreamTool.copyStream(getInputStream(), new FileOutputStream(destination));
	}

	public default String getAsString() throws IOException {
		InputStream is = getInputStream();
		StringBuilder builder = new StringBuilder("MPP bytes: ");
		while (is.available() > 0) {
			builder.append((byte) is.read());
		}
		System.out.println(builder);
		return getSize() == 0 ? "" : StreamTool.toString(getInputStream(), "UTF-8");
	}

	public static MultiPartParameter instance(String name, InputStream is) throws IOException {
		try {
			byte[] bytes = new byte[1024];

			int read = is.read(bytes);
			if (read < 1024) {
				return new MemoryMultiPartSupplier(name, read < 1 ? new byte[0] : Arrays.copyOf(bytes, read));
			}
			return new FileMultipartSupplier(name, bytes, is);

		} finally {
			is.close();
		}
	}
}