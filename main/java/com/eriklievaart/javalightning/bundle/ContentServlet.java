package com.eriklievaart.javalightning.bundle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.javalightning.bundle.rule.RequestAddress;
import com.eriklievaart.javalightning.bundle.rule.RuleEngine;
import com.eriklievaart.javalightning.bundle.rule.RuleResultType;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class ContentServlet extends HttpServlet {
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

	private void invoke(HttpServletRequest req, HttpServletResponse res) throws IOException {
		long start = System.currentTimeMillis();
		String url = req.getRequestURL().toString();

		try {
			log.debug("received request for url %", url);
			req.setCharacterEncoding("UTF-8");
			res.setCharacterEncoding("UTF-8");

			RequestAddress address = new RequestAddress(req);
			RuleResultType result = rules.apply(address);
			log.trace("rules applied: $ -> $", url, address);
			new ContentServletCall(beans, req, res).render(address, result);

		} finally {
			trace(start, url);
		}
	}

	private void trace(long start, String url) {
		long spent = System.currentTimeMillis() - start;
		if (spent > 100) {
			log.debug("spent $ms on url %", spent, url);
		}
	}
}