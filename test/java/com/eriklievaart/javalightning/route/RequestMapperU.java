package com.eriklievaart.javalightning.route;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Test;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.javalightning.aspect.ExceptionAspect;
import com.eriklievaart.javalightning.control.RequestController;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.ListTool;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class RequestMapperU {

	@Test
	public void adviseNull() {
		Check.isNull(RequestMapper.advise(null, NewCollection.list()));
	}

	@Test
	public void adviseNoInterceptors() throws IOException, ServletException {
		DummyController controller = new DummyController();
		RequestController actual = RequestMapper.advise(controller, NewCollection.list());
		Check.isEqual(actual, controller);

		actual.invoke(null);
		Check.isTrue(controller.invoked);
	}

	@Test
	public void adviseOnlyAspect() throws IOException, ServletException {
		ExceptionAspect aspect = new ExceptionAspect();
		RequestController actual = RequestMapper.advise(null, ListTool.of(aspect));
		Check.isEqual(actual, aspect);

		actual.invoke(null); // Aspects should be able to handle no controller being present
	}

	@Test
	public void advise() throws IOException, ServletException {
		DummyController controller = new DummyController();
		ExceptionAspect aspect = new ExceptionAspect();
		RequestController actual = RequestMapper.advise(controller, ListTool.of(aspect));
		Check.isEqual(actual, aspect);

		actual.invoke(null);
		Check.isTrue(controller.invoked);
	}

	private class DummyController implements RequestController {
		private boolean invoked = false;

		@Override
		public void invoke(FilterContext context) {
			invoked = true;
		}
	}
}
