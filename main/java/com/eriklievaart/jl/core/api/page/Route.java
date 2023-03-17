package com.eriklievaart.jl.core.api.page;

import java.util.EnumSet;
import java.util.function.Supplier;

import com.eriklievaart.toolkit.lang.api.ToString;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class Route {
	private String id;
	private RouteAddress address;
	private final Supplier<PageController> supplier;

	public Route(String id, RouteAddress address, Supplier<PageController> supplier) {
		Check.noneNull(id, address, supplier);
		Check.matches(id, "[a-z0-9.]*+");
		this.id = id;
		this.address = address;
		this.supplier = supplier;
	}

	public String getId() {
		return id;
	}

	public EnumSet<RouteType> getTypes() {
		return address.getTypes();
	}

	public String getPath() {
		return address.getPath();
	}

	public PageController createController() {
		return supplier.get();
	}

	@Override
	public String toString() {
		return ToString.simple(this, "$[$]", id);
	}
}