package com.eriklievaart.javalightning.integration;

import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.Mockito;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.javalightning.control.ReflectionPageController;
import com.eriklievaart.javalightning.filter.ApplicationContext;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class BeanIntegrationTest {

	private Class<?> type;
	private ApplicationContext applicationContext = Mockito.mock(ApplicationContext.class);
	private HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
	private HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
	private FilterChain chain = Mockito.mock(FilterChain.class);
	private Map<String, String[]> params = NewCollection.map();
	private ServletOutputStreamDummy output = new ServletOutputStreamDummy();

	public BeanIntegrationTest(Class<?> type) {
		this.type = type;
	}

	public String invoke(String method) {
		try {
			Mockito.when(request.getParameterMap()).thenReturn(params);
			Mockito.when(response.getOutputStream()).thenReturn(output);

			FilterContext context = new FilterContext(applicationContext, request, response, chain);
			new ReflectionPageController(type.getName(), method).invoke(context);
			return output.getResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public BeanIntegrationTest putParam(String name, String value) {
		params.put(name, new String[] { value });
		return this;
	}

}
