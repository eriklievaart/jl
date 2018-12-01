package com.eriklievaart.javalightning.zdemo;

import java.util.List;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.zdemo.controller.DownloadController;
import com.eriklievaart.javalightning.zdemo.controller.ExternalRedirectController;
import com.eriklievaart.javalightning.zdemo.controller.FreemarkerController;
import com.eriklievaart.javalightning.zdemo.controller.InOutJectorController;
import com.eriklievaart.javalightning.zdemo.controller.InternalRedirectController;
import com.eriklievaart.javalightning.zdemo.controller.StringRendererController;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class ExamplePageService implements PageService {

	private List<Route> routes = NewCollection.list();

	{
		routes.add(new Route("exact", () -> new StringRendererController()));
		routes.add(new Route("wildcard/*", () -> new StringRendererController()));
		routes.add(new Route("download", () -> new DownloadController()));
		routes.add(new Route("internal", () -> new InternalRedirectController()));
		routes.add(new Route("external", () -> new ExternalRedirectController()));
		routes.add(new Route("freemarker", () -> new FreemarkerController()));
		routes.add(new Route("ioj", () -> new InOutJectorController()));
	}

	@Override
	public String getPrefix() {
		return "zdemo";
	}

	@Override
	public Route[] getRoutes() {
		return routes.toArray(new Route[] {});
	}
}