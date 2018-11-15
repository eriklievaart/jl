package com.eriklievaart.javalightning.freemarker.whiteboard;

import java.util.Map;

import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.osgi.toolkit.api.listener.SimpleServiceListener;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class GlobalsIndex implements SimpleServiceListener<TemplateGlobal> {

	private Map<String, TemplateGlobal> index = NewCollection.concurrentHashMap();

	public boolean isAvailable(String id) {
		return index.containsKey(id);
	}

	public Object get(String id) {
		CheckCollection.isPresent(index, id, "missing global %", id);
		return index.get(id).getObject();
	}

	@Override
	public void register(TemplateGlobal service) {
		String name = service.getName();
		CheckId.isValid(name);
		index.put(name, service);
	}

	@Override
	public void unregistering(TemplateGlobal service) {
		index.remove(service.getName());
	}

}
