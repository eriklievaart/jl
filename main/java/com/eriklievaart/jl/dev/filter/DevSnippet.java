package com.eriklievaart.jl.dev.filter;

import java.io.File;
import java.util.function.Function;

import com.eriklievaart.toolkit.io.api.FileTool;

public class DevSnippet implements Function<String, String> {

	private File snippet;

	public DevSnippet(File file) {
		snippet = file;
	}

	@Override
	public String apply(String input) {
		return input.replaceFirst("(?=</body>)", FileTool.toString(snippet));
	}
}
