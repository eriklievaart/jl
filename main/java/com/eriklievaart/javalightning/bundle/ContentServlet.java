package com.eriklievaart.javalightning.bundle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class ContentServlet extends HttpServlet {
	private LogTemplate log = new LogTemplate(getClass());

	private MvcBeans beans;
	private final String home;

	public ContentServlet(MvcBeans beans, String home) {
		this.beans = beans;
		this.home = home;
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
		log.debug("received request for url %", req.getRequestURL());
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");

		RouteType method = RouteType.parse(req.getMethod());
		String path = req.getRequestURI();

		if (method == RouteType.GET && path.matches("^/*+$")) {
			new ContentServletCall(beans, req, res).render(method, home);
		} else {
			new ContentServletCall(beans, req, res).render(method, path);
		}
	}
}