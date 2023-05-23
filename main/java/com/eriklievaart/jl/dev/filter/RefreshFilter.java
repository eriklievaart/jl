package com.eriklievaart.jl.dev.filter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

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
import com.eriklievaart.toolkit.lang.api.collection.SetTool;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class RefreshFilter implements Filter {
	private static Set<String> hide = getHiddenPackages();

	private LogTemplate log = new LogTemplate(getClass());
	private DevSnippet snippet;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain fc) throws IOException, ServletException {
		BufferedResponse buffer = new BufferedResponse((HttpServletResponse) res);

		try {
			fc.doFilter(req, buffer);
			respond(res, buffer.getCaptureAsString());

		} catch (Exception e) {
			log.warn(e);
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
			if (hide.contains(firstThree(line.substring(3)))) {
				lines.remove(i--);
			}
		}
		return Str.joinLines(lines).replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}

	private String firstThree(String line) {
		return line.replaceFirst("([^.]*[.][^.]*[.][^.:]*).*", "$1");
	}

	private void respond(ServletResponse res, String html) throws IOException {
		try (ServletOutputStream os = res.getOutputStream()) {
			StreamTool.writeString(snippet.apply(html), os);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String tail = "/Development/git/jl/main/resources/dev/dev-snippet.txt";
		File file = new File(UrlTool.append(System.getProperty("user.home"), tail));
		log.info("loading dev snippet from: " + file);
		snippet = new DevSnippet(file);
	}

	private static Set<String> getHiddenPackages() {
		return SetTool.of("org.eclipse.jetty", "org.apache.felix", "javax.servlet.http", "java.lang.Thread");
	}
}
