package com.eriklievaart.javalightning.bundle.route;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.eriklievaart.javalightning.bundle.api.RequestContext;
import com.eriklievaart.javalightning.bundle.api.exception.NotFound404Exception;
import com.eriklievaart.javalightning.bundle.api.osgi.JavalightningId;
import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.Route;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.osgi.toolkit.api.listener.SimpleServiceListener;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.check.CheckCollection;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.lang.api.str.Str;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class PageServiceIndex implements SimpleServiceListener<PageService> {
	private LogTemplate log = new LogTemplate(getClass());

	private Map<String, RouteIndex> services = NewCollection.concurrentHashMap();
	private AtomicReference<String> prefixReference = new AtomicReference<>("");
	private AtomicReference<String> hostReference = new AtomicReference<>("");
	private AtomicBoolean httpsReference = new AtomicBoolean();
	private AtomicReference<String> exceptionRedirect = new AtomicReference<>();

	public String getServletPrefix() {
		return prefixReference.get();
	}

	public void setServletPrefix(String value) {
		prefixReference.set(value);
		log.info("servlet prefix: %", value);
	}

	public void setHost(String value) {
		hostReference.set(value);
		log.info("host: %", value);
	}

	public void setHttps(boolean value) {
		httpsReference.set(value);
		log.info("https: %", value);
	}

	@Override
	public void register(PageService service) {
		try {
			log.info("registering PageService[$]", service.getPrefix());
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
			log.debug("unknown service $:$", method, path);
			return Optional.empty();
		}
		return services.get(service).resolve(method, UrlTool.getTail(skipPrefix));
	}

	private String skipSlash(String path) {
		return path.replaceFirst("^[/]++", "");
	}

	public Collection<String> listServices() {
		return services.keySet();
	}

	public Route getRoute(String service, String route) throws NotFound404Exception {
		RouteIndex index = services.get(service);
		if (index == null) {
			throw new NotFound404Exception("missing service %", service);
		}
		return index.getRoute(route);
	}

	public String getRemotePath(String service, String route) throws NotFound404Exception {
		String path = UrlTool.append(prefixReference.get(), service, getRoute(service, route).getPath());
		return path.startsWith("/") ? path : "/" + path;
	}

	public boolean isAccessible(String service, String route, RequestContext context) throws NotFound404Exception {
		CheckCollection.isPresent(services, service, "missing service % for route %", service, route);
		return services.get(service).isAccessible(route, context);
	}

	public String getHost() {
		return hostReference.get();
	}

	public boolean isHttps() {
		return httpsReference.get();
	}

	public String getExceptionRedirect() {
		String path = exceptionRedirect.get();
		if (Str.isBlank(path)) {
			return "";
		}
		Optional<SecureRoute> optional = resolve(RouteType.GET, path);
		if (optional.isPresent()) {
			return path;

		} else {
			log.warn("no path registered for exception redirect %", path);
			return "";
		}
	}

	public void setExceptionRedirect(String path) {
		exceptionRedirect.set(path);
	}
}
