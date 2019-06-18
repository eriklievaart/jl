package com.eriklievaart.javalightning.mock;

import com.eriklievaart.javalightning.bundle.api.RequestContext;

public class MockRequestContext extends RequestContext {

	public MockRequestContext() {
		this(new MockHttpServletRequest(), new MockHttpServletResponse());
	}

	public MockRequestContext(MockHttpServletRequest request, MockHttpServletResponse response) {
		super(null, request, response);
	}

	@Override
	public MockHttpServletResponse getResponse() {
		return (MockHttpServletResponse) super.getResponse();
	}

	@Override
	public MockHttpServletRequest getRequest() {
		return (MockHttpServletRequest) super.getRequest();
	}
}
