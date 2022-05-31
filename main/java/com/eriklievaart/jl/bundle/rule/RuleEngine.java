package com.eriklievaart.jl.bundle.rule;

import java.util.List;

import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class RuleEngine {

	private List<RequestRule> rules = NewCollection.concurrentList();
	private List<MapRule> mappings = NewCollection.concurrentList();

	public RuleEngine(RequestRule... config) {
		for (RequestRule rule : config) {
			rules.add(rule);
		}
	}

	public RuleEngine(List<RequestRule> config, List<MapRule> redirects) {
		rules.addAll(config);
		mappings.addAll(redirects);
	}

	public RuleResultType apply(RequestAddress address) {
		for (RequestRule rule : rules) {
			RuleResultType result = rule.apply(address);

			if (result == RuleResultType.ALLOW) {
				break;
			}
			if (result != RuleResultType.UNKNOWN) {
				return result;
			}
		}
		for (MapRule mapping : mappings) {
			mapping.apply(address);
		}
		return RuleResultType.ALLOW;
	}
}