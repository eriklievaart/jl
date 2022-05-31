package com.eriklievaart.jl.core.rule;

import java.util.concurrent.atomic.AtomicBoolean;

public class AllowRule extends AbstractRule {

	private AtomicBoolean https = new AtomicBoolean(false);

	public AllowRule(RuleConfig config) {
		super(config);
	}

	public void setHttps() {
		https.set(true);
	}

	public boolean isHttps() {
		return https.get();
	}

	@Override
	public RuleResultType apply(RequestAddress address) {
		if (isApplicable(address)) {
			if (https.get() && !address.isSecure()) {
				return RuleResultType.HTTPS;
			}
			return RuleResultType.ALLOW;
		}
		return RuleResultType.UNKNOWN;
	}
}