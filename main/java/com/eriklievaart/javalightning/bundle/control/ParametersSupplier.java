package com.eriklievaart.javalightning.bundle.control;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import com.eriklievaart.javalightning.bundle.api.MultiPartParameter;
import com.eriklievaart.javalightning.bundle.api.Parameters;
import com.eriklievaart.javalightning.bundle.control.param.MultiParameters;
import com.eriklievaart.javalightning.bundle.control.param.SingleParameters;
import com.eriklievaart.toolkit.io.api.RuntimeIOException;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class ParametersSupplier implements Supplier<Parameters>, AutoCloseable {
	private static final String FILENAME_HEADER_ATTRIBUTE = "filename";
	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String CONTENT_DISPOSITION_HEADER = "content-disposition";

	private LogTemplate log = new LogTemplate(getClass());

	private final HttpServletRequest request;
	private final List<CloseableSilently> closeables = NewCollection.concurrentList();
	private final AtomicReference<Parameters> reference = new AtomicReference<>();

	public ParametersSupplier(HttpServletRequest request) {
		this.request = request;
	}

	@Override
	public Parameters get() {
		Parameters cached = reference.get();
		if (cached != null) {
			return cached;
		}
		reference.set(extract());
		return get();
	}

	private Parameters extract() {
		String contentType = request.getHeader(CONTENT_TYPE_HEADER);
		log.debug("contentType: $", contentType);
		if (contentType == null || !contentType.toLowerCase().trim().startsWith("multipart/")) {
			return createSimpleParameterMap();
		}
		try {
			return getMultiParts();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeIOException("unable to process upload; " + e.getMessage(), e);
		}
	}

	private Parameters getMultiParts() throws IOException, ServletException {
		Collection<Part> parts = request.getParts();
		Map<String, MultiPartParameter> multiparts = NewCollection.map();

		log.trace("parts.size() = $", parts.size());
		for (Part part : parts) {
			for (String header : part.getHeaderNames()) {
				log.debug("header found $: $", header, part.getHeader(header));
			}
			String header = part.getHeader(CONTENT_DISPOSITION_HEADER);
			MultiPartParameter mpp = MultiPartParameter.instance(getFileName(header), part.getInputStream());
			closeables.add(mpp);
			multiparts.put(part.getName(), mpp);
		}
		return new MultiParameters(multiparts);
	}

	private String getFileName(String header) {
		Map<String, String> parseHttpHeader = HeaderParser.parse(header);

		String filename = parseHttpHeader.get(FILENAME_HEADER_ATTRIBUTE);

		if (Str.isBlank(filename)) {
			return "unknown";
		}
		log.trace("filename found: %", filename);
		return filename;
	}

	private Parameters createSimpleParameterMap() {
		log.trace("singlepart form");
		Map<String, List<String>> parameters = NewCollection.map();
		request.getParameterMap().forEach((key, array) -> parameters.put(key, Arrays.asList(array)));
		return new SingleParameters(parameters);
	}

	@Override
	public void close() throws Exception {
		for (CloseableSilently close : closeables) {
			try {
				System.currentTimeMillis(); // dummy statement for check style
			} finally {
				close.close();
			}
		}
	}
}
