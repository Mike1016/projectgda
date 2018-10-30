package com.daka.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.daka.controller.user.UserController;

@Component
public class SpringUtils
		implements ApplicationContextInitializer<ConfigurableApplicationContext>, ApplicationContextAware
{
	//
	private static ApplicationContext applicationContext;

	@Override
	public void initialize(ConfigurableApplicationContext context)
	{
		UserController u = (UserController) context.getBean("userController");
		System.out.println(u.getClass());
	}

	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException
	{
		this.applicationContext = context;
	}

	// 获取applicationContext
	public static ApplicationContext getApplicationContext()
	{
		return applicationContext;
	}

	// 通过name获取 Bean.
	public static Object getBean(String name)
	{
		return getApplicationContext().getBean(name);
	}

	// 通过class获取Bean.
	public static <T> T getBean(Class<T> clazz)
	{
		return getApplicationContext().getBean(clazz);
	}

	// 通过name,以及Clazz返回指定的Bean
	public static <T> T getBean(String name, Class<T> clazz)
	{
		return getApplicationContext().getBean(name, clazz);
	}

	public static boolean containsBean(String name)
	{
		return getApplicationContext().containsBean(name);
	}
}
