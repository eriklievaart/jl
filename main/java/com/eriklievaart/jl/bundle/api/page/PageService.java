package com.eriklievaart.jl.bundle.api.page;

public interface PageService {

	public String getPrefix();

	public Route[] getRoutes();

	public PageSecurity getSecurity();
}