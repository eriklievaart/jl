package com.eriklievaart.javalightning.api.render;

import java.util.Map;

import com.eriklievaart.javalightning.api.UIDefinition;

public interface ServletReponseRendererFactory {

	ServletReponseRenderer createRenderer(UIDefinition view, Map<String, Object> model);

}
