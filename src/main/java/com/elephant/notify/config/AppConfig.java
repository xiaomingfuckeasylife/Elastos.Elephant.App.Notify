package com.elephant.notify.config;

import com.elephant.notify.interceptor.LoginInterceptor;
import com.elephant.notify.router.Router;
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
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

public class AppConfig extends JFinalConfig{
	
	public void afterJFinalStart(){
		
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
	
	public static RedisPlugin createRedisPlugin() {
		return new RedisPlugin("redpacket", "localhost");
	}
	
	public static Cron4jPlugin createCron4jPlugin() {
		Cron4jPlugin cp = new Cron4jPlugin();
		return cp;
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		
//		C3p0Plugin C3p0Plugin = createC3p0Plugin();
		DruidPlugin duildPlugin = createDruidPlugin();
		me.add(duildPlugin);
		
		ActiveRecordPlugin arp = new ActiveRecordPlugin(duildPlugin);
//		arp.setTransactionLevel(transactionLevel)
		arp.setShowSql(true);
		me.add(arp);
		
		// redis plugin
		RedisPlugin redisPlugin = createRedisPlugin();
		me.add(redisPlugin);
		
		// cron4j plugin
		Cron4jPlugin cp = createCron4jPlugin();
		me.add(cp);

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
