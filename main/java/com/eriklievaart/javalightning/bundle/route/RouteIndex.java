package com.eriklievaart.javalightning.bundle.route;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import com.eriklievaart.javalightning.bundle.api.page.PageController;
import com.eriklievaart.javalightning.bundle.api.page.PageService;
import com.eriklievaart.javalightning.bundle.api.page.RouteType;
import com.eriklievaart.osgi.toolkit.api.listener.SimpleServiceListener;
import com.eriklievaart.toolkit.io.api.UrlTool;
import com.eriklievaart.toolkit.lang.api.AssertionException;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class RouteIndex implements SimpleServiceListener<PageService> {
	private LogTemplate log = new LogTemplate(getClass());

	private Map<String, ServiceIndex> services = NewCollection.concurrentHashMap();

	@Override
	public void register(PageService service) {
		try {
			log.info("registering PageService[$]: $", service.getPrefix(), service.getClass());
			String prefix = service.getPrefix();
			Check.matches(prefix, "[a-z]++");

			if (!services.containsKey(prefix)) {
				services.putIfAbsent(prefix, new ServiceIndex(service));
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

	public Optional<PageController> resolve(RouteType method, String path) {
		String skipMvc = UrlTool.getTail(path);
		String service = UrlTool.removeTrailingSlash(UrlTool.getHead(skipMvc));
		Check.notNull(service, "requested URL does not start with a service %", path);
		if (!services.containsKey(service)) {
			throw new AssertionException("Cannot resolve rout! Missing service % $", service, listServices());
		}
		return services.get(service).resolve(method, UrlTool.getTail(skipMvc));
	}

	public Collection<String> listServices() {
		return services.keySet();
	}
}
