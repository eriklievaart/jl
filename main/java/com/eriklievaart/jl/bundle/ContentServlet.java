package com.eriklievaart.jl.bundle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.eriklievaart.jl.bundle.rule.RequestAddress;
import com.eriklievaart.jl.bundle.rule.RuleEngine;
import com.eriklievaart.jl.bundle.rule.RuleResultType;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class ContentServlet extends WebSocketServlet {
	private LogTemplate log = new LogTemplate(getClass());

	private MvcBeans beans;
	private RuleEngine rules;

	public ContentServlet(MvcBeans beans, RuleEngine rules) {
		this.beans = beans;
		this.rules = rules;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		invoke(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		invoke(req, res);
	}

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		invoke(req, res);
	}

	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		invoke(req, res);
	}

	@Override
	protected void doHead(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		invoke(req, res);
	}

	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		invoke(req, res);
	}

	@Override
	protected void doTrace(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		invoke(req, res);
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.setCreator(new ContentWebSocketCreator(beans));
	}

	private void invoke(HttpServletRequest req, HttpServletResponse res) throws IOException {
		long start = System.currentTimeMillis();
		log.debug("received request for url % from $", req.getRequestURL().toString(), req.getRemoteAddr());

		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");

		try {
			RequestAddress address = new RequestAddress(req);
			RuleResultType result = applyRules(req, address);
			new ContentServletCall(beans, req, res).render(address, result);

		} finally {
			trace(start, req.getRequestURI());
		}
	}

	private RuleResultType applyRules(HttpServletRequest req, RequestAddress address) {
		RuleResultType result = rules.apply(address);
		if (result != RuleResultType.BLOCK) {
			log.debug("rules applied: $ -> $", req.getRequestURI(), address);
		}
		req.setAttribute("path", address.getPath());
		return result;
	}

	private void trace(long start, String url) {
		long spent = System.currentTimeMillis() - start;
		if (spent > 100) {
			log.debug("spent $ms on url %", spent, url);
		}
	}
}