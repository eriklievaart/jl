package com.eriklievaart.jl.dev;

import java.util.Map;

import javax.servlet.Filter;

import org.osgi.framework.BundleContext;
import org.osgi.service.http.whiteboard.HttpWhiteboardConstants;

import com.eriklievaart.jl.core.api.osgi.LightningActivator;
import com.eriklievaart.jl.core.api.websocket.WebSocketService;
import com.eriklievaart.jl.dev.filter.RefreshFilter;
import com.eriklievaart.jl.dev.websocket.DevWebsocketService;
import com.eriklievaart.jl.dev.websocket.NotifyController;
import com.eriklievaart.osgi.toolkit.api.ContextWrapper;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class Activator extends LightningActivator {
	private static final String DEV_PROPERTY = "com.eriklievaart.jl.dev";
	private static final String DEV_FILTER_REGEX = "com.eriklievaart.jl.dev.filter";

	@Override
	protected void init(BundleContext context) throws Exception {
		ContextWrapper wrapper = getContextWrapper();
		if (!wrapper.getPropertyBoolean(DEV_PROPERTY, false)) {
			return;
		}
		addServiceWithCleanup(Filter.class, new RefreshFilter(), createFilterProperties());
		DevWebsocketService sockets = new DevWebsocketService();
		addServiceWithCleanup(WebSocketService.class, sockets);
		addPageService(builder -> {
			builder.newIdentityRoutePost("notify", () -> new NotifyController(sockets));
		});
	}

	private Map<String, Object> createFilterProperties() {
		Map<String, Object> props = NewCollection.hashtable();
		String regex = getContextWrapper().getPropertyString(DEV_FILTER_REGEX, "^.*.html$");
		props.put(HttpWhiteboardConstants.HTTP_WHITEBOARD_FILTER_REGEX, regex);
		return props;
	}
}
