package com.eriklievaart.javalightning.freemarker.whiteboard;

import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class GlobalsIndex {
	private LogTemplate log = new LogTemplate(getClass());

	private Map<String, TemplateGlobal> index = NewCollection.concurrentHashMap();

	public boolean contains(String key) {
		return index.containsKey(key);
	}

	public Object get(String id) {
		CheckCollection.isPresent(index, id, "missing global %", id);
		return index.get(id).getObject();
	}

	public void register(TemplateGlobal global) {
		String name = global.getName();
		CheckId.isValid(name);
		CheckCollection.notPresent(index, name, "Duplicate global %", name);
		log.debug("registering global %", global.getName());
		index.put(name, global);
	}

	public void unregistering(TemplateGlobal global) {
		log.debug("deleting global %", global.getName());
		index.remove(global.getName());
	}
}
