package com.eriklievaart.javalightning.freemarker.whiteboard;

import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.template.TemplateSource;
import com.eriklievaart.osgi.toolkit.api.listener.SimpleServiceListener;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class TemplateSourceIndex implements SimpleServiceListener<TemplateSource> {
	private LogTemplate log = new LogTemplate(getClass());

	private Map<String, TemplateSource> index = NewCollection.concurrentHashMap();

	@Override
	public void register(TemplateSource service) {
		log.info("registering TemplateSource %", service.getPrefix());
		String prefix = service.getPrefix();
		CheckId.isValid(prefix);
		index.put(prefix, service);
	}

	@Override
	public void unregistering(TemplateSource service) {
		index.remove(service.getPrefix());
	}

	public TemplateSource lookup(String id) {
		CheckId.isValid(id);
		Check.isTrue(index.containsKey(id), "Missing TemplateSource %!", id);
		return index.get(id);
	}
}