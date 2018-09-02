package com.eriklievaart.javalightning.control;

import java.io.Closeable;

public interface CloseableSilently extends Closeable {

	@Override
	public void close();
}
