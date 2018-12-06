package com.eriklievaart.javalightning.freemarker;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.concurrent.atomic.AtomicReference;

import com.eriklievaart.javalightning.freemarker.whiteboard.TemplateSourceIndex;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

import freemarker.cache.TemplateLoader;

public class OsgiTemplateLoader implements TemplateLoader {
	private LogTemplate log = new LogTemplate(getClass());

	private TemplateSourceIndex whiteboard;
	private AtomicReference<String> templatePath = new AtomicReference<>();

	public OsgiTemplateLoader(TemplateSourceIndex whiteboard) {
		this.whiteboard = whiteboard;
	}

	public void setTemplatePath(String value) {
		templatePath.set(value);
	}

	@Override
	public Object findTemplateSource(String name) throws IOException {
		return "/" + name;
	}

	@Override
	public long getLastModified(Object templateSource) {
		File file = getTemplateFile(templateSource);
		if (file != null) {
			return file.lastModified();
		}
		String head = UrlTool.getHead(templateSource.toString());
		return whiteboard.lookup(head).getLastModified();
	}

	@Override
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		File file = getTemplateFile(templateSource);
		if (file != null) {
			if (file.exists()) {
				log.debug("loading template from filesystem: $", file.getPath());
				return new FileReader(file);
			}
			log.trace("file does not exist, switching to classpath for $", file.getPath());
		}
		String path = templateSource.toString();
		String service = UrlTool.getHead(path);
		log.debug("resolving service % template: $", service, templateSource);
		return new InputStreamReader(whiteboard.lookup(service).getTemplate(path));
	}

	public File getTemplateFile(Object templateSource) {
		String path = templateSource.toString();
		String root = templatePath.get();
		if (root != null) {
			return new File(UrlTool.append(root, path));
		}
		return null;
	}

	@Override
	public void closeTemplateSource(Object templateSource) throws IOException {
	}
}
