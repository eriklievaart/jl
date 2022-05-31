package com.eriklievaart.jl.bundle.rule;

import org.junit.Test;

import com.eriklievaart.jl.bundle.api.page.RouteType;
import com.eriklievaart.toolkit.lang.api.check.Check;

public class RuleConfigU {

	@Test
	public void isApplicableDomainPass() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/");
		Check.isTrue(new RuleConfig().addDomain("bla.com").isApplicable(request));
	}

	@Test
	public void isApplicableDomainPassCaseInsensitive() {
		RequestAddress request = new RequestAddress(RouteType.GET, "BLA.COM", "/");
		Check.isTrue(new RuleConfig().addDomain("bla.com").isApplicable(request));
	}

	@Test
	public void isApplicableDomainPassPrefix() {
		RequestAddress request = new RequestAddress(RouteType.GET, "prefix.bla.com", "/");
		Check.isTrue(new RuleConfig().addDomain("bla.com").isApplicable(request));
	}

	@Test
	public void isApplicableDomainFailPrefixIp() {
		RequestAddress request = new RequestAddress(RouteType.GET, "prefix.192.168.1.1", "/");
		Check.isFalse(new RuleConfig().addDomain("192.168.1.1").isApplicable(request));
	}

	@Test
	public void isApplicableDomainFail() {
		RequestAddress request = new RequestAddress(RouteType.GET, "foo.com", "/");
		Check.isFalse(new RuleConfig().addDomain("bar.com").isApplicable(request));
	}

	@Test
	public void isApplicablePathPass() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/foo");
		Check.isTrue(new RuleConfig().addPath("foo").isApplicable(request));
	}

	@Test
	public void isApplicablePathFail() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/bar");
		Check.isFalse(new RuleConfig().addPath("foo").isApplicable(request));
	}

	@Test
	public void isApplicableMultiplePathPass() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/foo");
		Check.isTrue(new RuleConfig().addPath("bar").addPath("foo").isApplicable(request));
	}

	@Test
	public void isApplicableMultiplePathFail() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/bla");
		Check.isFalse(new RuleConfig().addPath("bar").addPath("foo").isApplicable(request));
	}

	@Test
	public void isApplicablePatternPass() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/foo");
		Check.isTrue(new RuleConfig().addPattern("f*").isApplicable(request));
	}

	@Test
	public void isApplicablePatternFail() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/foo");
		Check.isFalse(new RuleConfig().addPattern("b*").isApplicable(request));
	}

	@Test
	public void isApplicableMultiplePatternPass() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/foo");
		Check.isTrue(new RuleConfig().addPattern("e*").addPattern("f*").isApplicable(request));
	}

	@Test
	public void isApplicableMultiplePatternFail() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/foo");
		Check.isFalse(new RuleConfig().addPattern("b*").addPath("c*").isApplicable(request));
	}

	@Test
	public void isApplicableToAll() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/bar");
		// no domain set and no path or pattern set => applicable to all requests
		Check.isTrue(new RuleConfig().isApplicable(request));
	}

	@Test
	public void isApplicablePathAndDomainPass() {
		RequestAddress request = new RequestAddress(RouteType.GET, "bla.com", "/foo");
		Check.isTrue(new RuleConfig().addDomain("bla.com").addPath("foo").isApplicable(request));
	}

	@Test
	public void isApplicablePathPassDomainFail() {
		RequestAddress request = new RequestAddress(RouteType.GET, "foo.com", "/foo");
		Check.isFalse(new RuleConfig().addDomain("bar.com").addPath("foo").isApplicable(request));
	}

	@Test
	public void isApplicablePathFailDomainPass() {
		RequestAddress request = new RequestAddress(RouteType.GET, "foo.com", "/foo");
		Check.isFalse(new RuleConfig().addDomain("foo.com").addPath("bar").isApplicable(request));
	}
}