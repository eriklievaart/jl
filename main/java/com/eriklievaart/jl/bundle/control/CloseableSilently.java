package com.eriklievaart.jl.bundle.control;

import java.io.Closeable;

public interface CloseableSilently extends Closeable {

	@Override
	public void close();
}