package com.eriklievaart.jl.freemarker.whiteboard;

import java.util.Map;

import com.eriklievaart.jl.core.api.template.TemplateSource;
import com.eriklievaart.osgi.toolkit.api.listener.SimpleServiceListener;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class TemplateSourceIndex implements SimpleServiceListener<TemplateSource> {
	private LogTemplate log = new LogTemplate(getClass());

	private Map<String, TemplateSource> index = NewCollection.concurrentHashMap();
	private GlobalsIndex globals;

	public TemplateSourceIndex(GlobalsIndex globals) {
		this.globals = globals;
	}

	@Override
	public void register(TemplateSource service) {
		log.debug("registering TemplateSource %", service.getPrefix());
		String prefix = service.getPrefix();
		CheckId.isValid(prefix);
		index.put(prefix, service);
		service.getGlobals().forEach(g -> {
			log.debug("new global % for service %", g.getName(), service.getPrefix());
			globals.register(g);
		});
	}

	@Override
	public void unregistering(TemplateSource service) {
		log.debug("cleaning TemplateSource %", service.getPrefix());
		index.remove(service.getPrefix());
		service.getGlobals().forEach(globals::unregistering);
	}

	public TemplateSource lookup(String id) {
		CheckId.isValid(id);
		String msg = "Missing TemplateSource %! Did you call addTemplateSource() in the Activator?";
		Check.isTrue(index.containsKey(id), msg, id);
		return index.get(id);
	}
}
