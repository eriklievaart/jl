package com.eriklievaart.javalightning.zdemo;

import java.util.Date;

import org.osgi.framework.BundleContext;

import com.eriklievaart.javalightning.bundle.api.osgi.LightningActivator;
import com.eriklievaart.javalightning.bundle.api.page.PageSecurity;
import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.javalightning.zdemo.controller.DownloadController;
import com.eriklievaart.javalightning.zdemo.controller.ExternalRedirectController;
import com.eriklievaart.javalightning.zdemo.controller.FreemarkerController;
import com.eriklievaart.javalightning.zdemo.controller.InOutJectorController;
import com.eriklievaart.javalightning.zdemo.controller.InternalRedirectController;
import com.eriklievaart.javalightning.zdemo.controller.StringRendererController;

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