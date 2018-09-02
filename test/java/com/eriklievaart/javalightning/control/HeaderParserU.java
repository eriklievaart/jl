package com.eriklievaart.javalightning.control;

import java.util.Map;

import org.junit.Test;

import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;

public class HeaderParserU {

	@Test
	public void parse() {
		String raw = "form-data; name=\"fileToUpload\"; filename=\"[HorribleSubs] Luck & Logic - 01 [480p].mkv\"";

		Map<String, String> parsed = HeaderParser.parse(raw);

		CheckCollection.isPresent(parsed, "form-data");
		CheckCollection.isPresent(parsed, "name");
		CheckCollection.isPresent(parsed, "filename");

		Check.isEqual(parsed.get("name"), "fileToUpload");
		Check.isEqual(parsed.get("filename"), "[HorribleSubs] Luck & Logic - 01 [480p].mkv");
	}

	@Test
	public void parseEmpty() {
		String raw = "form-data; name=\"fileToUpload\"; filename=\"\"";

		Map<String, String> parsed = HeaderParser.parse(raw);

		CheckCollection.isPresent(parsed, "form-data");
		CheckCollection.isPresent(parsed, "name");
		CheckCollection.isPresent(parsed, "filename");

		Check.isEqual(parsed.get("filename"), "");
	}
}
