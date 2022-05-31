package com.eriklievaart.jl.bundle.control;

import java.util.Map;

import com.eriklievaart.toolkit.lang.api.collection.NewCollection;

public class HeaderParser {

	public static Map<String, String> parse(String raw) {
		Map<String, String> result = NewCollection.mapNullable();

		for (String entry : raw.trim().split("\\s*+;\\s*+")) {
			if (entry.contains("=")) {
				String[] nameToValue = entry.split("\\s*+=\\s*+");
				String value = nameToValue[1].replaceAll("^\"", "").replaceAll("\"$", "");
				result.put(nameToValue[0], value);
			} else {
				result.put(entry, null);
			}
		}
		return result;
	}
}