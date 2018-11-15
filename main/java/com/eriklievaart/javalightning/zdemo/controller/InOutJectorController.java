package com.eriklievaart.javalightning.zdemo.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eriklievaart.javalightning.bundle.api.Bean;
import com.eriklievaart.javalightning.bundle.api.Parameters;
import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.render.StringRenderer;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class InOutJectorController implements PageController {
	private static final String BR = "<br/>";

	@Bean
	private HttpServletRequest request;
	@Bean
	private HttpServletResponse response;
	@Bean
	private RequestContext context;
	@Bean
	private HttpSession session;
	@Bean
	private Parameters parameters;

	@Override
	public void invoke(ResponseBuilder rb) {
		StringBuilder builder = new StringBuilder("<html>").append(new Date()).append(BR).append(BR);

		builder.append("parameters: ").append(BR);
		for (String key : parameters.getKeys()) {
			builder.append(Str.sub("* $ = $ <br/>", key, parameters.getString(key)));
		}
		builder.append(BR);

		builder.append("request: ").append(request.getRequestURL()).append(BR);
		builder.append("session (previous request): ").append(session.getAttribute("previousRequest")).append(BR);
		builder.append("response: ").append(response.toString()).append(BR);
		builder.append("context: ").append(context.toString()).append(BR);
		builder.append("</html>");

		session.setAttribute("previousRequest", new Date());
		rb.setRenderer(new StringRenderer(builder.toString()));
	}
}
