package com.eriklievaart.javalightning.filter;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eriklievaart.javalightning.api.FilterContext;
import com.eriklievaart.javalightning.route.RequestMapper;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class MvcFilter implements Filter {
	private LogTemplate log = new LogTemplate(getClass());

	private RequestMapper mapper;
	private ApplicationContext context;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain c) throws IOException, ServletException {
		req.setCharacterEncoding("UTF-8");
		res.setCharacterEncoding("UTF-8");

		mapper.invoke(new FilterContext(context, (HttpServletRequest) req, (HttpServletResponse) res, c));
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		context = MvcBeans.createApplicationContext();
		overrideConfigDirFromInitParam(config);

		try {
			MvcGeneralConfig iniConfig = new MvcGeneralConfig(context.getConfigDir());
			FreemarkerConfigurationFactory.setFreemarkerSettings(iniConfig.getFreemarkerSettings());
		} catch (IOException e) {
			throw new ServletException("invalid config file: " + context.getConfigDir(), e);
		}
		mapper = MvcBeans.createRequestMapper();
		MvcBeans.createInitializer().runJobs(context);
	}

	private void overrideConfigDirFromInitParam(FilterConfig config) {
		String configDir = config.getInitParameter("mvc.config.dir");
		if (!Str.isBlank(configDir)) {
			log.debug("overriding config dir from init param `mvc.config.dir`");
			context.setConfigDir(new File(configDir));
		}
		log.info("Config dir is set to %", context.getConfigDir());
	}
}
