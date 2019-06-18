package com.eriklievaart.javalightning.bundle.rule;

import java.util.Arrays;

import org.junit.Test;

import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.toolkit.io.api.ini.IniNode;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class RuleEngineParserI {

	@Test
	public void parseBlock() {
		IniNode node = new IniNode("block");

		RequestAddress address = new RequestAddress(RouteType.GET, "/illegal");
		RuleResultType result = RuleEngineParser.parse(Arrays.asList(node)).apply(address);
		Check.isEqual(result, RuleResultType.BLOCK);
	}

	@Test
	public void parseBlockPattern() {
		IniNode node = new IniNode("block");
		node.setProperty("pattern", "*.php, *.json");

		RuleEngine engine = RuleEngineParser.parse(Arrays.asList(node));
		Check.isEqual(engine.apply(new RequestAddress(RouteType.GET, "/illegal.php")), RuleResultType.BLOCK);
		Check.isEqual(engine.apply(new RequestAddress(RouteType.GET, "/illegal.json")), RuleResultType.BLOCK);
		Check.isEqual(engine.apply(new RequestAddress(RouteType.GET, "/legal.html")), RuleResultType.ALLOW);
	}

	@Test
	public void parseAllowHttps() {
		IniNode node = new IniNode("allow");
		node.setProperty("https", "redirect");

		RequestAddress address = new RequestAddress(RouteType.GET, "/secure");
		RuleResultType result = RuleEngineParser.parse(Arrays.asList(node)).apply(address);
		Check.isEqual(result, RuleResultType.HTTPS);
	}

	@Test
	public void parseMapping() {
		IniNode node = new IniNode("map");
		node.setProperty("path", "/redirect");
		node.setProperty("to", "/elsewhere");

		RequestAddress address = new RequestAddress(RouteType.GET, "/redirect");
		RuleEngineParser.parse(Arrays.asList(node)).apply(address);
		Check.isEqual(address.getPath(), "/elsewhere");
	}
}
