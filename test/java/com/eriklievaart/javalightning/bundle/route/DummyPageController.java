package com.eriklievaart.javalightning.bundle.route;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import com.eriklievaart.javalightning.bundle.api.ResponseBuilder;
import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.render.StringRenderer;

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
