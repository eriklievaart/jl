package com.eriklievaart.jl.core.control;

import java.io.Closeable;

public interface CloseableSilently extends Closeable {

	@Override
	public void close();
}