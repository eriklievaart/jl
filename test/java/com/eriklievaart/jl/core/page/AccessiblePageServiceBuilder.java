package com.eriklievaart.jl.core.page;

import com.eriklievaart.jl.core.api.page.PageSecurity;
import com.eriklievaart.jl.core.api.page.PageServiceBuilder;

public class AccessiblePageServiceBuilder {

	public static PageServiceBuilder instance() {
		return new PageServiceBuilder().setSecurity(new PageSecurity((a, b) -> true));
	}
}