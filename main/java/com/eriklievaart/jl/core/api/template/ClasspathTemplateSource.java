package com.eriklievaart.jl.core.api.template;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import com.eriklievaart.jl.core.api.osgi.JavalightningId;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class ClasspathTemplateSource implements TemplateSource {

	private long modified = System.currentTimeMillis();
	private String prefix;
	private Class<?> loader;
	private List<TemplateGlobal> globals = NewCollection.concurrentList();

	public ClasspathTemplateSource(Class<?> loader, String prefix) {
		this(loader, prefix, new ArrayList<>());
	}

	public ClasspathTemplateSource(Class<?> loader, String prefix, TemplateGlobal... globals) {
		this(loader, prefix, Arrays.asList(globals));
	}

	public ClasspathTemplateSource(Class<?> loader, String prefix, List<TemplateGlobal> globals) {
		JavalightningId.validateSyntax(prefix);
		this.globals.addAll(globals);
		this.loader = loader;
		this.prefix = prefix;
	}

	public void addGlobal(String name, Object value) {
		addGlobal(name, () -> value);
	}

	public void addGlobal(String name, Supplier<Object> value) {
		globals.add(TemplateGlobal.of(name, value));
	}

	public void addGlobals(List<TemplateGlobal> values) {
		globals.addAll(values);
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public InputStream getTemplate(String path) {
		String expected = Str.sub("/$/", prefix);
		Check.isTrue(path.startsWith(expected), "% does not start with %", path, expected);
		InputStream is = loader.getResourceAsStream(path);
		Check.notNull(is, "Template % not found in TemplateSource %", path, prefix);
		return is;
	}

	@Override
	public long getLastModified() {
		return modified;
	}

	@Override
	public List<TemplateGlobal> getGlobals() {
		return new ArrayList<>(globals);
	}
}