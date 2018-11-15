package com.eriklievaart.javalightning.bundle.api.template;

import java.io.InputStream;

import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class ClasspathTemplateSource implements TemplateSource {

	private long modified = System.currentTimeMillis();
	private String prefix;
	private Class<?> loader;

	public ClasspathTemplateSource(Class<?> loader, String prefix) {
		Check.matches(prefix, "[a-z]++");
		this.loader = loader;
		this.prefix = prefix;
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

}
