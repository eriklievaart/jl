package com.eriklievaart.jl.bundle.rule;

public abstract class AbstractRule implements RequestRule {

	private RuleConfig config;

	public AbstractRule(RuleConfig config) {
		this.config = config;
	}

	protected boolean isApplicable(RequestAddress address) {
		return config.isApplicable(address);
	}

	@Override
	public boolean isLastRule() {
		return false;
	}
}