package com.eriklievaart.javalightning.integration;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import com.eriklievaart.javalightning.api.Parameters;
import com.eriklievaart.javalightning.api.ResponseBuilder;
import com.eriklievaart.javalightning.api.render.StringRenderer;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class ReflectionPageControllerI {

	@Test
	public void test() throws IOException, ServletException {

		String invokeHtml = new BeanIntegrationTest(getClass()).invoke("invoke");
		Check.isEqual(invokeHtml, "invoke");

		String html = new BeanIntegrationTest(getClass()).putParam("myparam", "myvalue").invoke("invokeWithParameters");
		Check.isEqual("withpatams", html);
	}

	public void invoke(ResponseBuilder response) {
		response.setRenderer(new StringRenderer("invoke"));
	}

	public void invokeWithParameters(ResponseBuilder response, Parameters parameters) {
		Check.isEqual(parameters.getString("myparam"), "myvalue");
		response.setRenderer(new StringRenderer("withpatams"));
	}
}
