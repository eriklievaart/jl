package com.eriklievaart.javalightning.mock.api;

import com.eriklievaart.javalightning.bundle.api.RequestContext;

public class MockRequestContext extends RequestContext {

	public MockRequestContext(MockHttpServletRequest request, MockHttpServletResponse response) {
		super(null, request, response);
	}

	public static MockRequestContext instance() {
		return new MockRequestContext(new MockHttpServletRequest(), new MockHttpServletResponse());
	}
}
