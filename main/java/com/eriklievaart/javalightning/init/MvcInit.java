package com.eriklievaart.javalightning.init;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.eriklievaart.javalightning.filter.ApplicationContext;
import com.eriklievaart.toolkit.logging.api.LogTemplate;
import com.eriklievaart.toolkit.reflect.api.LiteralTool;
import com.eriklievaart.toolkit.reflect.api.MethodTool;
import com.eriklievaart.toolkit.reflect.api.string.ReflectionRepresentation;

public class MvcInit {
	private LogTemplate log = new LogTemplate(getClass());

	private List<ReflectionRepresentation> jobs;

	public MvcInit(List<ReflectionRepresentation> jobs) {
		this.jobs = jobs;
	}

	public void runJobs(ApplicationContext context) {
		ExecutorService executor = Executors.newFixedThreadPool(4);
		for (ReflectionRepresentation job : jobs) {
			executor.execute(() -> {
				try {
					log.info("running job $", job);
					Object object = LiteralTool.newInstance(job.getLiteralName());
					Method method = MethodTool.getMethod(job.getLiteral(), job.getMemberName());
					method.invoke(object);
				} catch (Exception e) {
					log.error("Error in job $ => $", e, job, e.getMessage());
				}
			});
		}
	}

}
