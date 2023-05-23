package com.eriklievaart.jl.dev.websocket;

import com.eriklievaart.jl.core.api.ResponseBuilder;
import com.eriklievaart.jl.core.api.page.PageController;
import com.eriklievaart.jl.core.api.render.StringRenderer;

public class NotifyController implements PageController {

	private DevWebsocketService sockets;

	public NotifyController(DevWebsocketService sockets) {
		this.sockets = sockets;
	}

	@Override
	public void invoke(ResponseBuilder response) throws Exception {
		response.setRenderer(new StringRenderer("OK"));
		sockets.reload();
	}
}
