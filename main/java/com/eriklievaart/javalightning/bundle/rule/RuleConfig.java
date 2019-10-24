package com.eriklievaart.javalightning.bundle.rule;

import java.util.List;

import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.pattern.WildcardTool;

public class RuleConfig {

	private final List<String> domains = NewCollection.concurrentList();
	private final List<String> paths = NewCollection.concurrentList();
	private final List<String> patterns = NewCollection.concurrentList();

	public RuleConfig addDomain(String value) {
		domains.add(value.toLowerCase());
		return this;
	}

	public RuleConfig addPath(String value) {
		paths.add(UrlTool.removeLeadingSlashes(value));
		return this;
	}

	public RuleConfig addPattern(String value) {
		String noSlash = UrlTool.removeLeadingSlashes(value);
		if (noSlash.endsWith("~")) {
			patterns.add(noSlash.replaceFirst("~$", ""));
			patterns.add(noSlash.replaceFirst("~$", "/*"));
		} else {
			patterns.add(noSlash);
		}
		return this;
	}

	public boolean isApplicable(RequestAddress address) {
		if (!isApplicableToDomain(address)) {
			return false;
		}
		if (paths.isEmpty() && patterns.isEmpty()) {
			return true;
		}
		String requestPath = UrlTool.removeLeadingSlashes(address.getPath());
		for (String path : paths) {
			if (requestPath.equals(path)) {
				return true;
			}
		}
		for (String pattern : patterns) {
			if (WildcardTool.match(pattern, requestPath)) {
				return true;
			}
		}
		return false;
	}

	private boolean isApplicableToDomain(RequestAddress address) {
		if (domains.isEmpty()) {
			return true;
		}
		for (String domain : domains) {
			String check = address.getDomain().toLowerCase();
			if (check.equals(domain)) {
				return true;
			}
			if (!isIp(domain) && check.endsWith("." + domain)) {
				return true;
			}
		}
		return false;
	}

	private boolean isIp(String domain) {
		return domain.matches("^\\d++[.]\\d++[.]\\d++[.]\\d++(:\\d++)?");
	}
}
