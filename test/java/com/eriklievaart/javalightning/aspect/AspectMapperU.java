package com.eriklievaart.javalightning.aspect;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.eriklievaart.javalightning.api.aspect.AspectController;
import com.eriklievaart.javalightning.route.Route;
import com.eriklievaart.javalightning.route.RouteType;
import com.eriklievaart.javalightning.route.UrlConfig;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;

public class AspectMapperU {

	@Test
	public void listInterceptorsGetWithPost() throws IOException {
		String aspect = "GET				*				com.eriklievaart.javalightning.aspect.TraceAspect";

		AspectMapper mapper = new AspectMapper(UrlConfig.parse(Arrays.asList(aspect)));
		List<AspectController> interceptors = mapper.listInterceptors(new Route(RouteType.POST, "/home"));

		CheckCollection.isEmpty(interceptors);
	}

	@Test
	public void listInterceptorsGetWithGet() throws IOException {
		String aspect = "GET				*				com.eriklievaart.javalightning.aspect.TraceAspect";

		AspectMapper mapper = new AspectMapper(UrlConfig.parse(Arrays.asList(aspect)));
		List<AspectController> interceptors = mapper.listInterceptors(new Route(RouteType.GET, "/home"));

		CheckCollection.isSize(interceptors, 1);
	}

	@Test
	public void listInterceptorsStarWithGet() throws IOException {
		String aspect = "*				*				com.eriklievaart.javalightning.aspect.TraceAspect";

		AspectMapper mapper = new AspectMapper(UrlConfig.parse(Arrays.asList(aspect)));
		List<AspectController> interceptors = mapper.listInterceptors(new Route(RouteType.GET, "/home"));

		CheckCollection.isSize(interceptors, 1);
	}

	@Test
	public void listInterceptorsGetBoth() throws IOException {
		String trace = "GET				*				com.eriklievaart.javalightning.aspect.TraceAspect";
		String exc = "GET				*				com.eriklievaart.javalightning.aspect.ExceptionAspect";

		AspectMapper mapper = new AspectMapper(UrlConfig.parse(Arrays.asList(trace, exc)));
		List<AspectController> interceptors = mapper.listInterceptors(new Route(RouteType.GET, "/home"));

		CheckCollection.isSize(interceptors, 2);
	}

	@Test
	public void listInterceptorsGetUrlMatches() throws IOException {
		String trace = "GET				/home/*				com.eriklievaart.javalightning.aspect.TraceAspect";

		AspectMapper mapper = new AspectMapper(UrlConfig.parse(Arrays.asList(trace)));
		List<AspectController> interceptors = mapper.listInterceptors(new Route(RouteType.GET, "/home/hello.html"));

		CheckCollection.isSize(interceptors, 1);
	}

	@Test
	public void listInterceptorsGetUrlMisses() throws IOException {
		String trace = "GET				/home/*				com.eriklievaart.javalightning.aspect.TraceAspect";

		AspectMapper mapper = new AspectMapper(UrlConfig.parse(Arrays.asList(trace)));
		List<AspectController> interceptors = mapper.listInterceptors(new Route(RouteType.GET, "/elsewhere"));

		CheckCollection.isEmpty(interceptors);
	}

}
