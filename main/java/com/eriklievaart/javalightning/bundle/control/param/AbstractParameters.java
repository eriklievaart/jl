package com.eriklievaart.javalightning.bundle.control.param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.eriklievaart.javalightning.bundle.api.Parameters;
import com.eriklievaart.toolkit.bean.api.BeanInjector;
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
	public String getString(String key, String fallback) {
		return delegate.containsKey(key) ? getString(key) : fallback;
	}

	@Override
	public void getString(String key, Consumer<String> consumer) {
		if (contains(key)) {
			consumer.accept(getString(key));
		}
	}

	@Override
	public int getInteger(String key) throws ConversionException {
		return new IntegerConstructor().createConverter().convertToObject(getString(key));
	}

	@Override
	public long getLong(String key) throws ConversionException {
		return new LongConstructor().createConverter().convertToObject(getString(key));
	}

	@Override
	public Collection<String> getKeys() {
		List<String> keys = new ArrayList<>(delegate.keySet());
		Collections.sort(keys);
		return keys;
	}

	@Override
	public BeanInjector getParamInjector() {
		Map<String, String> values = new HashMap<>();
		delegate.keySet().forEach(key -> values.put(key, getString(key)));
		return new BeanInjector(values);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
