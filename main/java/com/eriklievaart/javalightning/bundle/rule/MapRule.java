package com.eriklievaart.javalightning.bundle.rule;

import java.util.function.Consumer;

import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class MapRule {
	private LogTemplate log = new LogTemplate(getClass());

	private RuleConfig config;
	private final Consumer<RequestAddress> action;

	public MapRule(RuleConfig config, Consumer<RequestAddress> action) {
		this.config = config;
		this.action = action;
	}

	public void apply(RequestAddress address) {
		if (config.isApplicable(address)) {
			log.trace("rule applicable $ - $", address, config);
			action.accept(address);
		} else {
			log.trace("rule NOT applicable $ - $", address, config);
		}
	}
}
