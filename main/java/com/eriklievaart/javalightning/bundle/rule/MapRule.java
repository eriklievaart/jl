package com.eriklievaart.javalightning.bundle.rule;

public class MapRule {

	private RuleConfig config;
	private final String destination;

	public MapRule(RuleConfig config, String destination) {
		this.config = config;
		this.destination = destination;
	}

	public void apply(RequestAddress address) {
		if (config.isApplicable(address)) {
			address.setPath(destination);
		}
	}
}
