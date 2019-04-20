package com.eriklievaart.javalightning.email;

import org.osgi.framework.BundleContext;

import com.eriklievaart.javalightning.email.api.EmailService;
import com.eriklievaart.osgi.toolkit.api.ActivatorWrapper;

public class Activator extends ActivatorWrapper {

	@Override
	protected void init(BundleContext context) throws Exception {
		addServiceWithCleanup(EmailService.class, new EmailServiceImpl());
	}
}
