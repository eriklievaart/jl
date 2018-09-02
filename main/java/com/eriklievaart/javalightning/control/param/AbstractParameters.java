package com.eriklievaart.javalightning.control.param;

import java.util.Map;

import com.eriklievaart.javalightning.api.Parameters;
import com.eriklievaart.toolkit.convert.api.ConversionException;
import com.eriklievaart.toolkit.convert.api.construct.IntegerConstructor;
import com.eriklievaart.toolkit.convert.api.construct.LongConstructor;

public abstract class AbstractParameters<V> implements Parameters {

	protected final Map<String, V> delegate;

	public AbstractParameters(Map<String, V> map) {
		this.delegate = map;
	}

	@Override
	public boolean contains(String key) {
		return delegate.containsKey(key);
	}

	@Override
	public long getInteger(String key) throws ConversionException {
		return new IntegerConstructor().createConverter().convertToObject(getString(key));
	}

	@Override
	public long getLong(String key) throws ConversionException {
		return new LongConstructor().createConverter().convertToObject(getString(key));
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
