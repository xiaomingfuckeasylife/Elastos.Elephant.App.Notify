package com.elephant.notify.router;


import com.elephant.notify.controller.Index;
import com.elephant.notify.controller.Load;
import com.elephant.notify.controller.Verify;
import com.jfinal.config.Routes;

public class Router extends Routes{

	@Override
	public void config() {
		add("/", Index.class);
		add("/load", Load.class);
		add("/verify", Verify.class);
	}

}
