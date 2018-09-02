package com.eriklievaart.javalightning.api;

import java.io.IOException;
import java.util.List;

import com.eriklievaart.toolkit.convert.api.ConversionException;

public interface Parameters {

	public boolean contains(String key);

	public abstract List<String> getStrings(String key);

	public abstract String getString(String key);

	public long getInteger(String key) throws ConversionException;

	public long getLong(String key) throws ConversionException;

	public abstract MultiPartParameter getMultiPart(String key) throws IOException;

}
