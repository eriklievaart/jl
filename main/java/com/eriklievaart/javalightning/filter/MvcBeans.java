package com.eriklievaart.javalightning.filter;

import com.eriklievaart.javalightning.api.render.ServletReponseRendererFactory;
import com.eriklievaart.javalightning.aspect.AspectMapper;
import com.eriklievaart.javalightning.init.MvcInit;
import com.eriklievaart.javalightning.init.MvcInitConfig;
import com.eriklievaart.javalightning.route.RequestMapper;
import com.eriklievaart.javalightning.route.Router;
import com.eriklievaart.javalightning.route.UrlConfig;
import com.eriklievaart.toolkit.reflect.api.LiteralTool;

public class MvcBeans {

	static RequestMapper createRequestMapper() {
		return new RequestMapper(createRouter(), createAspectMapper());
	}

	static Router createRouter() {
		return new Router(UrlConfig.parseRoutes());
	}

	static AspectMapper createAspectMapper() {
		return new AspectMapper(UrlConfig.parseAspects());
	}

	static ApplicationContext createApplicationContext() {
		return new ApplicationContext(createResponseRendererFactory());
	}

	static ServletReponseRendererFactory createResponseRendererFactory() {
		return LiteralTool.newInstance("com.eriklievaart.javalightning.filter.FreemarkerRendererFactory");
	}

	static MvcInit createInitializer() {
		return new MvcInit(MvcInitConfig.parseJobs());
	}

}
