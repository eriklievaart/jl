package com.eriklievaart.jl.bundle.page;

import com.eriklievaart.jl.bundle.api.page.PageSecurity;
import com.eriklievaart.jl.bundle.api.page.PageServiceBuilder;

public class AccessiblePageServiceBuilder {

	public static PageServiceBuilder instance() {
		return new PageServiceBuilder().setSecurity(new PageSecurity((a, b) -> true));
	}
}