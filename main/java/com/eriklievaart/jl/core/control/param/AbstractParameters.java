package com.eriklievaart.jl.core.control.param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.eriklievaart.jl.core.api.Parameters;
import com.eriklievaart.toolkit.convert.api.ConversionException;
import com.eriklievaart.toolkit.convert.api.construct.IntegerConstructor;
import com.eriklievaart.toolkit.convert.api.construct.LongConstructor;
import com.eriklievaart.toolkit.lang.api.collection.ListTool;
import com.eriklievaart.toolkit.lang.api.collection.MapTool;

public abstract class AbstractParameters<V> implements Parameters {

	protected final Map<String, V> delegate;

	public AbstractParameters(Map<String, V> map) {
		this.delegate = map;
	}

	@Override
	public boolean contains(String key, String... tail) {
		List<String> keys = ListTool.of(key);
		ListTool.addAll(keys, tail);
		return containsAll(keys);
	}

	private boolean containsAll(List<String> keys) {
		for (String key : keys) {
			if (!delegate.containsKey(key)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public Optional<String> getOptional(String key) {
		return contains(key) ? Optional.of(getString(key)) : Optional.empty();
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
	public int getInteger(String key, int fallback) throws ConversionException {
		return contains(key) ? getInteger(key) : fallback;
	}

	@Override
	public void getInteger(String key, Consumer<Integer> consumer) throws ConversionException {
		if (contains(key)) {
			consumer.accept(getInteger(key));
		}
	}

	@Override
	public long getLong(String key) throws ConversionException {
		return new LongConstructor().createConverter().convertToObject(getString(key));
	}

	@Override
	public long getLong(String key, long fallback) throws ConversionException {
		return contains(key) ? getLong(key) : fallback;
	}

	@Override
	public void getLong(String key, Consumer<Long> consumer) throws ConversionException {
		if (contains(key)) {
			consumer.accept(getLong(key));
		}
	}

	@Override
	public Collection<String> getKeys() {
		List<String> keys = new ArrayList<>(delegate.keySet());
		Collections.sort(keys);
		return keys;
	}

	@Override
	public Map<String, String> getMap() {
		Map<String, String> map = new HashMap<>();
		delegate.keySet().forEach(key -> map.put(key, getString(key)));
		return map;
	}

	@Override
	public Map<String, String> getMap(String... keys) {
		return MapTool.toMap(Arrays.asList(keys), this::getString);
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}