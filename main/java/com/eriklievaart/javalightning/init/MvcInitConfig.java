package com.eriklievaart.javalightning.init;

import java.io.File;
import java.util.List;
import java.util.Optional;

import com.eriklievaart.toolkit.io.api.FileTool;
import com.eriklievaart.toolkit.io.api.ResourceTool;
import com.eriklievaart.toolkit.io.api.RuntimeIOException;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;
import com.eriklievaart.toolkit.reflect.api.string.ReflectionRepresentation;
import com.eriklievaart.toolkit.reflect.api.string.ReflectionRepresentationTool;

public class MvcInitConfig {
	private static LogTemplate log = new LogTemplate(MvcInitConfig.class);

	public static List<ReflectionRepresentation> parseJobs() {
		String path = "/WEB-INF/mvc/init.txt";
		Optional<File> optional = ResourceTool.getOptionalFile(path);

		if (optional.isPresent()) {
			try {
				return parse(FileTool.readLines(optional.get()));
			} catch (RuntimeIOException e) {
				log.warn("Unable to read File %", e, optional.get());
			}

		} else {
			log.warn("% not found, not running any init jobs", path);
		}
		return NewCollection.list();
	}

	private static List<ReflectionRepresentation> parse(List<String> lines) {
		List<ReflectionRepresentation> commands = NewCollection.list();

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (Str.isBlank(line) || line.trim().startsWith("#")) {
				continue;
			}
			commands.add(ReflectionRepresentationTool.fromString(line));
		}
		return commands;
	}
}
