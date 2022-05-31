package com.eriklievaart.jl.bundle.rule;

import java.util.List;
import java.util.function.Consumer;

import com.eriklievaart.toolkit.io.api.ini.IniNode;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class RuleEngineParser {

	public static RuleEngine parse(List<IniNode> nodes) {
		return new RuleEngine(parseRules(nodes), parseMappings(nodes));
	}

	private static List<RequestRule> parseRules(List<IniNode> nodes) {
		List<RequestRule> rules = NewCollection.list();

		for (IniNode node : nodes) {

			if (node.getName().equals("block")) {
				rules.add(new BlockRule(parseConfig(node)));

			} else if (node.getName().equals("allow")) {
				AllowRule allow = new AllowRule(parseConfig(node));
				if (node.hasProperty("https") && node.getProperty("https").equals("redirect")) {
					allow.setHttps();
				}
				rules.add(allow);
			}
		}
		return rules;
	}

	private static List<MapRule> parseMappings(List<IniNode> nodes) {
		List<MapRule> mappings = NewCollection.list();
		for (IniNode node : nodes) {
			if (node.getName().equals("map")) {
				RuleConfig config = parseConfig(node);
				String to = node.getProperty("to");
				if (node.hasProperty("regex")) {
					mappings.add(new MapRule(config, address -> {
						address.setPath(address.getPath().replaceAll(node.getProperty("regex"), to));
					}));
				} else {
					mappings.add(new MapRule(config, address -> address.setPath(to)));
				}
			}
		}
		return mappings;
	}

	private static RuleConfig parseConfig(IniNode node) {
		RuleConfig config = new RuleConfig();

		for (String property : node.getPropertyNames()) {
			switch (property) {

			case "domain":
				each(node.getProperty(property), value -> config.addDomain(value));
				break;

			case "path":
				each(node.getProperty(property), value -> config.addPath(value));
				break;

			case "pattern":
				each(node.getProperty(property), value -> config.addPattern(value));
				break;
			}
		}
		return config;
	}

	private static void each(String property, Consumer<String> consumer) {
		for (String value : property.split(",")) {
			if (Str.notBlank(value)) {
				consumer.accept(value.trim());
			}
		}
	}
}