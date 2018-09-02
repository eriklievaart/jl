package com.eriklievaart.javalightning.filter;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.eriklievaart.toolkit.io.api.ini.IniNode;
import com.eriklievaart.toolkit.io.api.ini.IniNodeIO;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.io.api.CheckFile;
import com.eriklievaart.toolkit.lang.api.collection.NewCollection;
import com.eriklievaart.toolkit.logging.api.LogTemplate;

public class MvcGeneralConfig {
	private static final String FREEMARKER_ID = "freemarker";
	private static final String FREEMARKER_REFRESH_PROPERTY = "refresh";
	private static final String FREEMARKER_TEMPLATE_DIR_PROPERTY = "template.dir";

	private LogTemplate log = new LogTemplate(getClass());
	private File root;
	private FreemarkerSettings freemarker = new FreemarkerSettings();

	public MvcGeneralConfig(File root) throws IOException {
		this.root = root;
		init();
	}

	private void init() throws IOException {
		Map<String, IniNode> idToNode = NewCollection.mapNotNull();
		File file = new File(root, "config.ini");
		log.info("reading config from $", file);
		if (file.isFile()) {
			for (IniNode node : IniNodeIO.read(file)) {
				idToNode.put(node.getIdentifier().toLowerCase(), node);
			}
		} else {
			log.info("using defaults; config file does not exist $", file);
		}
		if (idToNode.containsKey(FREEMARKER_ID)) {
			updateFreemarkerSettings(idToNode.get(FREEMARKER_ID));
		}
	}

	void updateFreemarkerSettings(IniNode node) throws IOException {
		if (node == null) {
			return; // use defaults
		}
		if (node.hasProperty(FREEMARKER_REFRESH_PROPERTY)) {
			String refresh = node.getProperty(FREEMARKER_REFRESH_PROPERTY);
			log.info("freemarker refresh timeout: %", refresh);
			Check.matches(refresh, "\\d++", "freemarker property % is not a number", FREEMARKER_REFRESH_PROPERTY);
			freemarker.setTemplateUpdateDelayMillis(Integer.parseInt(refresh));
		}
		if (node.hasProperty(FREEMARKER_TEMPLATE_DIR_PROPERTY)) {
			String templateDir = node.getProperty(FREEMARKER_TEMPLATE_DIR_PROPERTY);
			CheckFile.isDirectory(new File(templateDir), "Non existing Template dir specified: %", templateDir);
			log.info("freemarker template dir: %", templateDir);
			freemarker.setTemplateDir(templateDir);
		}
	}

	public FreemarkerSettings getFreemarkerSettings() {
		return freemarker;
	}

}
