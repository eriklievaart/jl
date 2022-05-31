package com.eriklievaart.jl.freemarker;

import org.osgi.framework.BundleContext;

import com.eriklievaart.jl.bundle.api.template.TemplateService;
import com.eriklievaart.jl.bundle.api.template.TemplateSource;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;

public class Activator extends ActivatorWrapper {
	public static final String TEMPLATE_TIMEOUT_PROPERTY = "com.eriklievaart.jl.freemarker.timeout";
	public static final String TEMPLATE_PATH_PROPERTY = "com.eriklievaart.jl.freemarker.path";

	@Override
	public void init(BundleContext context) throws Exception {
		FreemarkerBeans beans = new FreemarkerBeans();
		beans.getFreemarkerOsgiConfig().config(getContextWrapper());

		addWhiteboardWithCleanup(TemplateSource.class, beans.getTemplateSourceListener());
		addServiceWithCleanup(TemplateService.class, new FreemarkerTemplateService(beans));
	}
}