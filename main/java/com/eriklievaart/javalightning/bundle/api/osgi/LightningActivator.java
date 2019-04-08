package com.eriklievaart.javalightning.bundle.api.osgi;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.PageServiceBuilder;
import com.eriklievaart.javalightning.bundle.api.template.ClasspathTemplateSource;
import com.eriklievaart.javalightning.bundle.api.template.TemplateGlobal;
import com.eriklievaart.javalightning.bundle.api.template.TemplateSource;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;
import com.eriklievaart.toolkit.lang.api.check.Check;

public abstract class LightningActivator extends ActivatorWrapper {

	private String name;
	private AtomicBoolean hasPageService = new AtomicBoolean(false);

	public LightningActivator(String name) {
		JavalightningId.validateSyntax(name);
		this.name = name;
	}

	public void addTemplateSource() {
		addServiceWithCleanup(TemplateSource.class, new ClasspathTemplateSource(getClass(), name));
	}

	public void addTemplateSource(TemplateGlobal... globals) {
		addServiceWithCleanup(TemplateSource.class, new ClasspathTemplateSource(getClass(), name, globals));
	}

	public void addTemplateSource(TemplateSource source) {
		addServiceWithCleanup(TemplateSource.class, source);
	}

	public void addPageService(Consumer<PageServiceBuilder> consumer) {
		boolean previous = hasPageService.getAndSet(true);
		Check.isFalse(previous, "duplicate PageService registration!");
		PageServiceBuilder builder = new PageServiceBuilder();
		consumer.accept(builder);
		addServiceWithCleanup(PageService.class, builder.createPageService(name));
	}
}
