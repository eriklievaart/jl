package com.eriklievaart.jl.dev.websocket;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.eriklievaart.jl.core.api.websocket.WebSocketService;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class DevWebsocketService implements WebSocketService {

	private List<DevWebsocket> sockets = NewCollection.list();

	@Override
	public String getPath() {
		return "/dev/refresh";
	}

	@Override
	public Supplier<?> webSocketSupplier() {
		return () -> {
			DevWebsocket s = new DevWebsocket();
			sockets.add(s);
			return s;
		};
	}

	public void reload() {
		List<DevWebsocket> clone = new ArrayList<>(sockets);
		for (DevWebsocket socket : clone) {
			if (socket.isClosed()) {
				sockets.remove(socket);
			} else {
				socket.send("reload");
			}
		}
	}
}
