package com.eriklievaart.jl.core.api.page;

public interface PageService {

	public String getPrefix();

	public Route[] getRoutes();

	public PageSecurity getSecurity();
}