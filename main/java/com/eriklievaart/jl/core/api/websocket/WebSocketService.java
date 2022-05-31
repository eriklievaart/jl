package com.eriklievaart.jl.core.api.websocket;

import java.util.function.Supplier;

public interface WebSocketService {

	/**
	 * Gets the path on which this WebSocketService accepts web sockets.
	 *
	 * @return a relative path.
	 */
	public String getPath();

	/**
	 * Returns a supplier for creating the backing beans for websockets.
	 *
	 * @return the returned Object must either implement WebSocketListener or be annotated with WebSocket
	 */
	public Supplier<?> webSocketSupplier();
}