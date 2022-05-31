package com.eriklievaart.jl.bundle.route;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import com.eriklievaart.jl.bundle.api.ResponseBuilder;
import com.eriklievaart.jl.bundle.api.page.PageController;
import com.eriklievaart.jl.bundle.api.render.StringRenderer;

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