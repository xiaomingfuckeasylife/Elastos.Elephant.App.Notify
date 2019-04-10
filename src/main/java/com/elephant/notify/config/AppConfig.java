package com.elephant.notify.config;

import com.elephant.notify.interceptor.LoginInterceptor;
import com.elephant.notify.router.Router;
import com.elephant.notify.websocket.Client;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.i18n.I18nInterceptor;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.ModelRecordElResolver;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfplugin.mail.MailPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppConfig extends JFinalConfig{
	private Logger logger = LoggerFactory.getLogger(AppConfig.class);
	public void afterJFinalStart(){
		logger.info("initial websocket");
		new Thread(new Runnable() {
			@Override
			public void run() {
				try{
					Client.connect();
				}catch (Exception ex){
					ex.printStackTrace();
					System.exit(-1);
				}
			}
		}).start();
	}
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {
		PropKit.use("a_little_config.txt");
		me.setDevMode(PropKit.getBoolean("devMode", false));
		me.setViewType(ViewType.JSP);
		ModelRecordElResolver.setResolveBeanAsModel(true);
		me.setJsonFactory(new FastJsonFactory());
		me.setI18nDefaultBaseName("i18n");
	}
	
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		
		me.add(new Router());

	}
	
	public static C3p0Plugin createC3p0Plugin() {
		return new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}
	
	public static DruidPlugin createDruidPlugin() {
		return new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
	}

	public static Cron4jPlugin createCron4jPlugin() {
		Cron4jPlugin cp = new Cron4jPlugin();
		return cp;
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		
		DruidPlugin duildPlugin = createDruidPlugin();
		me.add(duildPlugin);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(duildPlugin);
		arp.setShowSql(true);
		me.add(arp);

		// cron4j plugin
		Cron4jPlugin cp = createCron4jPlugin();
		me.add(cp);

		// mail
		me.add(new MailPlugin(PropKit.use("mail.properties").getProperties()));
		com.elephant.notify.entity._MappingKit.mapping(arp);
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		me.add(new I18nInterceptor());
		me.add(new LoginInterceptor());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		
	}
	
	@Override
	public void configEngine(Engine arg0) {
		
	}
	public static void main(String[] args) {
		/**
		 * 特别注意：Eclipse 之下建议的启动方式
		 */
//		JFinal.start("src/main/webapp", 8080, "/", 5);
		
		/**
		 * 特别注意：IDEA 之下建议的启动方式，仅比 eclipse 之下少了最后一个参数
		 */
		 JFinal.start("src/main/webapp", 8081, "/");
	}
}
