package com.eriklievaart.jl.bundle.rule;

public class BlockRule extends AbstractRule {

	public BlockRule(RuleConfig config) {
		super(config);
	}

	@Override
	public RuleResultType apply(RequestAddress address) {
		if (isApplicable(address)) {
			return RuleResultType.BLOCK;
		}
		return RuleResultType.UNKNOWN;
	}
}