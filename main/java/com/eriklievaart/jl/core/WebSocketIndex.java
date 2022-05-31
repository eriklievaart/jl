package com.eriklievaart.jl.core;

import java.util.Hashtable;
import java.util.Map;
import java.util.function.Supplier;

import com.eriklievaart.jl.core.api.websocket.WebSocketService;
import com.eriklievaart.osgi.toolkit.api.listener.SimpleServiceListener;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class WebSocketIndex implements SimpleServiceListener<WebSocketService> {
	private LogTemplate log = new LogTemplate(getClass());

	private Map<String, WebSocketService> index = new Hashtable<>();

	@Override
	public void register(WebSocketService service) {
		String path = service.getPath();
		Check.isTrue(path.startsWith("/"), "$#getPath() does not start with slash: %", service.getClass(), path);
		log.info("registering service $ for path %", service.getClass().getSimpleName(), path);

		CheckCollection.notPresent(index, path);
		index.put(path, service);
	}

	@Override
	public void unregistering(WebSocketService service) {
		index.remove(service.getPath());
	}

	public Supplier<?> getSupplier(String path) {
		CheckCollection.isPresent(index, path);
		return index.get(path).webSocketSupplier();
	}
}