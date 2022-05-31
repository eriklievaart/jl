package com.eriklievaart.jl.core.rule;

import java.util.Arrays;

import org.junit.Test;

import com.eriklievaart.jl.core.api.page.RouteType;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class RuleEngineU {

	@Test
	public void map() {
		RuleConfig config = new RuleConfig().addPath("remap");
		RuleEngine engine = createEngine(new MapRule(config, address -> address.setPath("home")));

		RequestAddress address = new RequestAddress(RouteType.GET, "remap");
		engine.apply(address);
		Check.isEqual(address.getPath(), "home");
	}

	@Test
	public void mapTrailingSlash() {
		RuleConfig config = new RuleConfig().addPath("remap/");
		RuleEngine engine = createEngine(new MapRule(config, address -> address.setPath("home")));

		RequestAddress address = new RequestAddress(RouteType.GET, "remap");
		engine.apply(address);
		Check.isEqual(address.getPath(), "home");
	}

	@Test
	public void mapResolveSlash() {
		RuleConfig config = new RuleConfig().addPath("remap");
		RuleEngine engine = createEngine(new MapRule(config, address -> address.setPath("home")));

		RequestAddress address = new RequestAddress(RouteType.GET, "remap/");
		engine.apply(address);
		Check.isEqual(address.getPath(), "home");
	}

	@Test
	public void mapIgnore() {
		RuleConfig config = new RuleConfig().addPath("remap");
		RuleEngine engine = createEngine(new MapRule(config, address -> address.setPath("home")));

		RequestAddress address = new RequestAddress(RouteType.GET, "ignore");
		engine.apply(address);
		Check.isEqual(address.getPath(), "ignore");
	}

	@Test
	public void blockAll() {
		RuleConfig config = new RuleConfig(); // all requests
		RuleEngine engine = new RuleEngine(new BlockRule(config));

		RequestAddress address = new RequestAddress(RouteType.GET, "block");
		Check.isEqual(engine.apply(address), RuleResultType.BLOCK);
	}

	@Test
	public void blockDomain() {
		RuleConfig config = new RuleConfig().addDomain("block.com");
		RuleEngine engine = new RuleEngine(new BlockRule(config));

		RequestAddress address = new RequestAddress(RouteType.GET, "block.com", "path");
		Check.isEqual(engine.apply(address), RuleResultType.BLOCK);
	}

	@Test
	public void blockPath() {
		RuleConfig config = new RuleConfig().addPath("block");
		RuleEngine engine = new RuleEngine(new BlockRule(config));

		RequestAddress address = new RequestAddress(RouteType.GET, "block");
		Check.isEqual(engine.apply(address), RuleResultType.BLOCK);
	}

	@Test
	public void blockPathIgnore() {
		RuleConfig config = new RuleConfig().addPath("block");
		RuleEngine engine = new RuleEngine(new BlockRule(config));

		RequestAddress address = new RequestAddress(RouteType.GET, "ignore");
		Check.isEqual(engine.apply(address), RuleResultType.ALLOW);
	}

	@Test
	public void blockPattern() {
		RuleConfig config = new RuleConfig().addPattern("b*");
		RuleEngine engine = new RuleEngine(new BlockRule(config));

		RequestAddress address = new RequestAddress(RouteType.GET, "block");
		Check.isEqual(engine.apply(address), RuleResultType.BLOCK);
	}

	@Test
	public void blockPatternIgnore() {
		RuleConfig config = new RuleConfig().addPattern("b*");
		RuleEngine engine = new RuleEngine(new BlockRule(config));

		RequestAddress address = new RequestAddress(RouteType.GET, "pass");
		Check.isEqual(engine.apply(address), RuleResultType.ALLOW);
	}

	@Test
	public void allowDomainThenBlockPass() {
		RuleConfig config = new RuleConfig().addDomain("allow.com");
		RuleEngine engine = new RuleEngine(new AllowRule(config), getBlockAllRule());

		RequestAddress address = new RequestAddress(RouteType.GET, "allow.com", "pass");
		Check.isEqual(engine.apply(address), RuleResultType.ALLOW);
	}

	@Test
	public void allowDomainThenBlockFail() {
		RuleConfig config = new RuleConfig().addDomain("allow.com");
		RuleEngine engine = new RuleEngine(new AllowRule(config), getBlockAllRule());

		RequestAddress address = new RequestAddress(RouteType.GET, "invaliddomain.com", "fail");
		Check.isEqual(engine.apply(address), RuleResultType.BLOCK);
	}

	@Test
	public void allowRedirectHttps() {
		AllowRule allow = new AllowRule(new RuleConfig().addDomain("secure.com"));
		allow.setHttps();
		RuleEngine engine = new RuleEngine(allow);

		RequestAddress address = new RequestAddress(RouteType.GET, "secure.com", "path");
		Check.isEqual(engine.apply(address), RuleResultType.HTTPS);
	}

	@Test
	public void mapAndAllow() {
		RuleConfig config = new RuleConfig().addPath("remap");
		AllowRule rules = new AllowRule(new RuleConfig());
		MapRule map = new MapRule(config, address -> address.setPath("home"));
		RuleEngine engine = createEngine(rules, map);

		RequestAddress address = new RequestAddress(RouteType.GET, "remap");
		engine.apply(address);
		Check.isEqual(address.getPath(), "home");
	}

	private BlockRule getBlockAllRule() {
		return new BlockRule(new RuleConfig());
	}

	private RuleEngine createEngine(MapRule map) {
		return new RuleEngine(Arrays.asList(), Arrays.asList(map));
	}

	private RuleEngine createEngine(RequestRule rule, MapRule map) {
		return new RuleEngine(Arrays.asList(rule), Arrays.asList(map));
	}
}