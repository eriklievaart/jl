package com.eriklievaart.javalightning.bundle.control;

import java.io.IOException;

import javax.servlet.ServletException;

import com.eriklievaart.javalightning.bundle.api.RequestContext;

public interface RequestController {

	public void invoke(RequestContext context) throws IOException, ServletException;
}
