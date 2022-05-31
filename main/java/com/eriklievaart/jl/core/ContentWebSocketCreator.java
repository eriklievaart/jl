package com.eriklievaart.jl.core;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class ContentWebSocketCreator implements WebSocketCreator {

	private MvcBeans beans;

	public ContentWebSocketCreator(MvcBeans beans) {
		this.beans = beans;
	}

	@Override
	public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
		return beans.getWebSocketIndex().getSupplier(request.getRequestPath()).get();
	}
}