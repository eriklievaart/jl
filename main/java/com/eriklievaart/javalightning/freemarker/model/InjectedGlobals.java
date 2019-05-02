package com.eriklievaart.javalightning.freemarker.model;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.freemarker.whiteboard.GlobalsIndex;

public class InjectedGlobals {

	private GlobalsIndex index;
	private RequestContext context;

	public InjectedGlobals(GlobalsIndex globals, RequestContext context) {
		this.index = globals;
		this.context = context;
	}

	public boolean isAvailable(String key) {
		return index.contains(key);
	}

	public boolean contains(String key) {
		return index.contains(key);
	}

	public Object get(String id) {
		Object object = index.get(id);
		context.injectBeanAnnotations(object);
		return object;
	}
}
