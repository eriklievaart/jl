package com.eriklievaart.jl.email;

import org.osgi.framework.BundleContext;

import com.eriklievaart.jl.email.api.EmailService;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;

public class Activator extends ActivatorWrapper {
	public static final String HOST = "com.eriklievaart.jl.email.host";

	@Override
	protected void init(BundleContext context) throws Exception {
		EmailServiceImpl service = new EmailServiceImpl();
		getContextWrapper().getPropertyStringOptional(HOST, host -> {
			service.setHost(host);
		});
		addServiceWithCleanup(EmailService.class, service);
	}
}