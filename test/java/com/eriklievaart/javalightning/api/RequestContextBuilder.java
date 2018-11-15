package com.eriklievaart.javalightning.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mockito;

import com.eriklievaart.javalightning.bundle.api.RequestContext;

public class RequestContextBuilder {

	private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);

	public RequestContext get() {
		return new RequestContext(null, request, response);
	}
}
