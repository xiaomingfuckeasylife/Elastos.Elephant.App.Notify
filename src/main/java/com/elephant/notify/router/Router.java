package com.elephant.notify.router;


import com.elephant.notify.controller.Index;
import com.jfinal.config.Routes;

public class Router extends Routes{

	@Override
	public void config() {
		add("/", Index.class);
	}

}
