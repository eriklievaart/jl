package com.eriklievaart.javalightning.filter;

import java.util.Map;

import com.eriklievaart.javalightning.api.UIDefinition;
import com.eriklievaart.javalightning.api.render.FreemarkerRenderer;
import com.eriklievaart.javalightning.api.render.ServletReponseRenderer;
import com.eriklievaart.javalightning.api.render.ServletReponseRendererFactory;
import com.eriklievaart.toolkit.lang.api.check.Check;
import com.eriklievaart.toolkit.lang.api.str.Str;

public class FreemarkerRendererFactory implements ServletReponseRendererFactory {

	@Override
	public ServletReponseRenderer createRenderer(UIDefinition view, Map<String, Object> model) {
		Check.notNull(view.getPath(), "No view specified!");
		FreemarkerRenderer renderer = new FreemarkerRenderer(Str.sub("$.tpl", view.getPath()));
		renderer.putAll(model);
		return renderer;
	}
}
