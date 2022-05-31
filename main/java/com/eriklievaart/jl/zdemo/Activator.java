package com.eriklievaart.jl.zdemo;

import java.util.Date;

import org.osgi.framework.BundleContext;

import com.eriklievaart.jl.core.api.osgi.LightningActivator;
import com.eriklievaart.jl.core.api.page.PageSecurity;
import com.eriklievaart.jl.core.api.template.TemplateGlobal;
import com.eriklievaart.jl.zdemo.controller.DownloadController;
import com.eriklievaart.jl.zdemo.controller.ExternalRedirectController;
import com.eriklievaart.jl.zdemo.controller.FreemarkerController;
import com.eriklievaart.jl.zdemo.controller.InOutJectorController;
import com.eriklievaart.jl.zdemo.controller.InternalRedirectController;
import com.eriklievaart.jl.zdemo.controller.StringRendererController;

public class Activator extends LightningActivator {

	public Activator() {
		super("zdemo");
	}

	@Override
	public void init(BundleContext context) throws Exception {
		addTemplateSource(TemplateGlobal.of("date", () -> new Date()));

		addPageService(builder -> {

			builder.newRoute("exact").mapGet("exact", () -> new StringRendererController());
			builder.newRoute("wildcard").mapGet("wildcard/*", () -> new StringRendererController());
			builder.newRoute("download").mapGet("download", () -> new DownloadController());
			builder.newRoute("internal").mapGet("internal", () -> new InternalRedirectController());
			builder.newRoute("external").mapGet("external", () -> new ExternalRedirectController());
			builder.newRoute("freemarker").mapGet("freemarker", () -> new FreemarkerController());
			builder.newRoute("ioj").mapGet("ioj", () -> new InOutJectorController());

			builder.setSecurity(new PageSecurity((a, b) -> true));
		});
	}
}