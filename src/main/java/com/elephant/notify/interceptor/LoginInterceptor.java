package com.elephant.notify.interceptor;

import javax.servlet.http.HttpServletRequest;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author clark
 * 
 * May 5, 2018 3:02:49 PM
 */
public class LoginInterceptor implements Interceptor {

	private Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	public void intercept(Invocation inv) {
		Controller c = inv.getController();
		try {
			HttpServletRequest req = c.getRequest();
			String uri = req.getRequestURI();
			String full_url = req.getRequestURL().toString();
			logger.info("URI = {} , URL = {}",uri ,full_url);
			inv.invoke();
		}catch(Throwable ex) {
			ex.printStackTrace();
			c.setAttr("error",ex.getMessage());
			c.render("error.jsp");
			return;
		}
	}

}



