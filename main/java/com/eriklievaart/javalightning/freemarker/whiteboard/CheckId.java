package com.eriklievaart.javalightning.freemarker.whiteboard;

import com.eriklievaart.toolkit.lang.api.check.Check;

public class CheckId {

	public static void isValid(String id) {
		Check.matches(id, "[a-z]++");
	}
}
