package com.elephant.notify.router;


import com.elephant.notify.controller.Index;
import com.elephant.notify.controller.Load;
import com.elephant.notify.controller.Success;
import com.elephant.notify.controller.Verify;
import com.elephant.notify.util.Constant;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;

public class Router extends Routes{

	@Override
	public void config() {

		add(Constant.BASE_URL,Index.class);
		add(Constant.BASE_URL + "/load", Load.class);
		add(Constant.BASE_URL + "/verify", Verify.class);
		add(Constant.BASE_URL + "/success", Success.class);
	}

}
