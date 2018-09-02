package com.eriklievaart.javalightning.control;

import java.io.IOException;

import javax.servlet.ServletException;

import com.eriklievaart.javalightning.api.FilterContext;

public interface RequestController {

	public void invoke(FilterContext context) throws IOException, ServletException;
}
