package com.eriklievaart.javalightning.bundle.control.param;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eriklievaart.javalightning.bundle.api.MultiPartParameter;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;

public class SingleParameters extends AbstractParameters<List<String>> {

	public SingleParameters(Map<String, List<String>> map) {
		super(map);
	}

	@Override
	public boolean contains(String key) {
		return delegate.containsKey(key);
	}

	@Override
	public List<String> getStrings(String key) {
		CheckCollection.isPresent(delegate, key);
		return delegate.get(key).stream().map(str -> str.trim()).collect(Collectors.toList());
	}

	@Override
	public String getString(String key) {
		return getStrings(key).iterator().next();
	}

	@Override
	public MultiPartParameter getMultiPart(String key) throws IOException {
		throw new IOException("This was not a multipart upload!");
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
