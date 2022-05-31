package com.eriklievaart.jl.bundle.api.osgi;

import com.eriklievaart.toolkit.lang.api.check.Check;

public class JavalightningId {

	public static void validateSyntax(String id) {
		Check.matches(id, "[a-z.]++");
	}
}