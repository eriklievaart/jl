package com.eriklievaart.javalightning.bundle.page;

import com.eriklievaart.javalightning.bundle.api.page.PageSecurity;
import com.eriklievaart.javalightning.bundle.api.page.PageServiceBuilder;

public class AccessiblePageServiceBuilder {

	public static PageServiceBuilder instance() {
		return new PageServiceBuilder().setSecurity(new PageSecurity((a, b) -> true));
	}
}
