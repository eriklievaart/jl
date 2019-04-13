package com.eriklievaart.javalightning.bundle.route;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.exception.RouteUnavailableException;
import com.eriklievaart.javalightning.bundle.api.osgi.JavalightningId;
import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.osgi.toolkit.api.listener.SimpleServiceListener;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.AssertionException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class PageServiceIndex implements SimpleServiceListener<PageService> {
	private LogTemplate log = new LogTemplate(getClass());

	private Map<String, RouteIndex> services = NewCollection.concurrentHashMap();
	private AtomicReference<String> prefixReference = new AtomicReference<>("");

	@Override
	public void register(PageService service) {
		try {
			log.info("registering PageService[$]: $", service.getPrefix(), service.getClass());
			String prefix = service.getPrefix();
			JavalightningId.validateSyntax(prefix);

			if (!services.containsKey(prefix)) {
				services.putIfAbsent(prefix, new RouteIndex(service));
			}
		} catch (Exception e) {
			log.warn("Unable to register service %", e, service.getPrefix());
		}
	}

	@Override
	public void unregistering(PageService service) {
		log.info("cleaning PageService[$]: $", service.getPrefix(), service.getClass());
		services.remove(service.getPrefix());
	}

	public Optional<SecureRoute> resolve(RouteType method, String path) {
		String skipPrefix = skipSlash(path).substring(skipSlash(prefixReference.get()).length());
		String service = UrlTool.removeTrailingSlash(UrlTool.getHead(skipPrefix));
		Check.notNull(service, "requested URL does not start with a service %", path);
		if (!services.containsKey(service)) {
			throw new AssertionException("Cannot resolve rout! Missing service % $", service, listServices());
		}
		return services.get(service).resolve(method, UrlTool.getTail(skipPrefix));
	}

	private String skipSlash(String path) {
		return path.replaceFirst("^[/]++", "");
	}

	public Collection<String> listServices() {
		return services.keySet();
	}

	public Route getRoute(String service, String route) throws RouteUnavailableException {
		RouteIndex index = services.get(service);
		if (index == null) {
			throw new RouteUnavailableException("Missing service %", service);
		}
		return index.getRoute(route);
	}

	public String getRemotePath(String service, String route) throws RouteUnavailableException {
		String path = UrlTool.append(prefixReference.get(), service, getRoute(service, route).getPath());
		return path.startsWith("/") ? path : "/" + path;
	}

	public void setServletPrefix(String prefix) {
		prefixReference.set(prefix);
	}

	public boolean isAccessible(String service, String route, RequestContext context) throws RouteUnavailableException {
		CheckCollection.isPresent(services, service, "missing service % for route %", service, route);
		return services.get(service).isAccessible(route, context);
	}
}
