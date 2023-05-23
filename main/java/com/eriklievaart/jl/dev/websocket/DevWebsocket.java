package com.eriklievaart.jl.dev.websocket;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class DevWebsocket implements WebSocketListener {
	private static long init = System.currentTimeMillis();

	private LogTemplate log = new LogTemplate(getClass());

	private Session session;
	private boolean closed = false;

	public boolean isClosed() {
		return closed;
	}

	public void send(String message) {
		try {
			session.getRemote().sendString(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onWebSocketClose(int status, String reason) {
		log.trace("dev socket disconnected");
		closed = true;
	}

	@Override
	public void onWebSocketConnect(Session s) {
		this.session = s;
		log.trace("dev socket connected");
		try {
			s.getRemote().sendString("init=" + init);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onWebSocketText(String message) {
		try {
			if (message.equals("ping")) {
				session.getRemote().sendPong(null);
				return;
			}
			log.debug("Message from " + session.hashCode() + ": " + message);
			session.getRemote().sendString(message);

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onWebSocketBinary(byte[] payload, int offset, int len) {
		log.info("@@@ BINARY @@@");
	}

	@Override
	public void onWebSocketError(Throwable t) {
		t.printStackTrace();
	}
}