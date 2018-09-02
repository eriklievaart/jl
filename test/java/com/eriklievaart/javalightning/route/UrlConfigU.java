package com.eriklievaart.javalightning.route;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.eriklievaart.toolkit.io.api.RuntimeIOException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;
import com.eriklievaart.toolkit.lang.api.check.CheckStr;
import com.eriklievaart.toolkit.test.api.Bomb;
import com.eriklievaart.toolkit.test.api.BombSquad;

public class UrlConfigU {

	@Test
	public void createUrlMapForConstants() throws IOException {
		Map<String, String> map = UrlConfig.createUrlMapForConstants(UrlMappingDummy.class);

		Assertions.assertThat(map).containsEntry("e.t.", "/et");
		Assertions.assertThat(map).containsEntry("phone", "/phone");
		Assertions.assertThat(map).containsEntry("home", "/home");
		Assertions.assertThat(map).hasSize(3);
	}

	@Test
	public void parseComment() {
		CheckCollection.isEmpty(callParse("# ignore comment"));
	}

	@Test
	public void parseMissingWhitespace() {
		BombSquad.diffuse(RuntimeIOException.class, "Invalid line", new Bomb() {
			@Override
			public void explode() throws Exception {
				callParse("GET:/path com.controller.Whitespace");
			}
		});
	}

	@Test
	public void parseSuccess() {
		List<RouteMapping> mappings = callParse("GET /path com.pkg.Literal");
		CheckCollection.isSize(mappings, 1);

		RouteMapping mapping = mappings.get(0);
		CheckStr.isEqual(mapping.getAction(), "com.pkg.Literal");
		Check.isEqual(mapping.getRoute().getType(), RouteType.GET);
		Check.isEqual(mapping.getRoute().getPath(), "/path");
	}

	@Test
	public void parseAll() {
		List<RouteMapping> mappings = callParse("* /path com.pkg.Literal");
		CheckCollection.isSize(mappings, RouteType.values().length);
	}

	private List<RouteMapping> callParse(String... lines) {
		return UrlConfig.parse(Arrays.asList(lines));
	}
}
