package com.eriklievaart.jl.core.route;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import com.eriklievaart.jl.core.api.ResponseBuilder;
import com.eriklievaart.jl.core.api.page.PageController;
import com.eriklievaart.jl.core.api.render.StringRenderer;

public class DummyPageController implements PageController {

	private AtomicBoolean invoked = new AtomicBoolean();

	@Override
	public void invoke(ResponseBuilder builder) {
		invoked.set(true);
		builder.setRenderer(new StringRenderer("test: " + new Date()));
	}

	public boolean isInvoked() {
		return invoked.get();
	}
}