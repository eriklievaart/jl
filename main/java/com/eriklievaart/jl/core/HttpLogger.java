package com.eriklievaart.jl.core;

import java.util.Date;
import java.util.logging.Level;

import com.eriklievaart.toolkit.lang.api.date.TimestampTool;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class HttpLogger {

	private LogTemplate log;
	private Level level;
	private int ignore;

	public HttpLogger(String logger, Level level, int millis) {
		this.level = level;
		this.ignore = millis;
		this.log = new LogTemplate(logger);
		log.info("@init@ " + new Date());
	}

	public void log(String url, long spent) {
		if (spent > ignore) {
			log.log(level, "$: $", getMillis(spent), url);
		}
	}

	private String getMillis(long millis) {
		return millis < 10000 ? String.format("%4dms", millis) : TimestampTool.humanReadable(millis);
	}
}
