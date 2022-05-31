package com.eriklievaart.jl.mock;

import java.util.Map;

import com.eriklievaart.jl.bundle.api.RequestContext;
import com.eriklievaart.osgi.mock.MockServiceCollection;
import com.eriklievaart.osgi.toolkit.api.ServiceCollection;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class MockRequestContext extends RequestContext {

	private Map<Class<?>, ServiceCollection<?>> services = NewCollection.map();

	public MockRequestContext() {
		this(new MockHttpServletRequest(), new MockHttpServletResponse());
	}

	public MockRequestContext(MockHttpServletRequest request) {
		this(request, new MockHttpServletResponse());
	}

	public MockRequestContext(MockHttpServletResponse response) {
		this(new MockHttpServletRequest(), response);
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

	@Override
	@SuppressWarnings("unchecked")
	public <E> ServiceCollection<E> getServiceCollection(Class<E> type) {
		CheckCollection.isPresent(services, type);
		return (ServiceCollection<E>) services.get(type);
	}

	public <E> MockRequestContext mockService(Class<E> type, E service) {
		services.put(type, MockServiceCollection.of(service));
		return this;
	}
}