package com.eriklievaart.jl.core.control;

import java.io.IOException;

import javax.servlet.ServletException;

import com.eriklievaart.jl.core.api.RequestContext;

public interface RequestController {

	public void invoke(RequestContext context) throws IOException, ServletException;
}