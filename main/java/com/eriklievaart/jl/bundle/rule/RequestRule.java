package com.eriklievaart.jl.bundle.rule;

public interface RequestRule {

	public RuleResultType apply(RequestAddress address);

	public boolean isLastRule();
}