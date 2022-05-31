package com.eriklievaart.jl.bundle.control;

import java.io.IOException;

import javax.servlet.ServletException;

import com.eriklievaart.jl.bundle.api.RequestContext;

public interface RequestController {

	public void invoke(RequestContext context) throws IOException, ServletException;
}