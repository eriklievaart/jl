package com.eriklievaart.jl.dev.filter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.toolkit.io.api.StreamTool;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.ThrowableTool;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class RefreshFilter implements Filter {
	private LogTemplate log = new LogTemplate(getClass());

	private DevSnippet snippet;

	@Override
	public void init(FilterConfig config) throws ServletException {
		String tail = "/Development/git/jl/main/resources/dev/dev-snippet.txt";
		File file = new File(UrlTool.append(System.getProperty("user.home"), tail));
		log.info("loading dev snippet from: " + file);
		snippet = new DevSnippet(file);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		BufferedResponse buffer = new BufferedResponse((HttpServletResponse) res);

		try {
			fc.doFilter(req, buffer);
			respond(res, buffer.getCaptureAsString());

		} catch (Exception e) {
			log.warn("an exception occurred; showing error page", e);
			respond(res, "<html><body><pre>" + toString(e) + "</pre></body></html>");
		}
	}

	private String toString(Exception e) {
		List<String> lines = Str.listLines(ThrowableTool.toString(e));

		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i).trim();
			if (!line.startsWith("at ")) {
				continue;
			}
			if (isHidden(line.substring(3))) {
				lines.remove(i--);
			}
		}
		return Str.joinLines(lines).replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	private void respond(ServletResponse res, String html) throws IOException {
		try (ServletOutputStream os = res.getOutputStream()) {
			StreamTool.writeString(snippet.apply(html), os);
		}
	}

	private boolean isHidden(String pkg) {
		for (String hidden : getHiddenPackages()) {
			if (pkg.startsWith(hidden)) {
				return true;
			}
		}
		return false;
	}

	private static List<String> getHiddenPackages() {
		List<String> list = NewCollection.list();
		list.add("org.eclipse");
		list.add("org.apache.felix");
		list.add("freemarker.");
		list.add("javax.servlet");
		list.add("java.lang.Thread");
		return list;
	}

	@Override
	public void destroy() {
	}
}
