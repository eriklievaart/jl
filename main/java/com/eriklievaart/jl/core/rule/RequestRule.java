package com.eriklievaart.jl.core.rule;

public interface RequestRule {

	public RuleResultType apply(RequestAddress address);

	public boolean isLastRule();
}