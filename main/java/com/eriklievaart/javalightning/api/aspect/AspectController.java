package com.eriklievaart.javalightning.api.aspect;

import com.eriklievaart.javalightning.control.RequestController;

public interface AspectController extends RequestController {

	public void setDelegate(RequestController delegate);

}
