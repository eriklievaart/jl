package com.eriklievaart.javalightning.mock.api;

import com.eriklievaart.javalightning.bundle.api.RequestContext;

public class MockRequestContext extends RequestContext {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	public MockRequestContext(MockHttpServletRequest request, MockHttpServletResponse response) {
		super(null, request, response);
		this.request = request;
		this.response = response;
	}

	public static MockRequestContext instance() {
		return new MockRequestContext(new MockHttpServletRequest(), new MockHttpServletResponse());
	}
}
