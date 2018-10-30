/**
 * File Name:LogService.java
 * Package Name:com.qianye.log
 * Date:2016年3月30日上午10:35:50
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.interceptor;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.daka.annotation.Distinguish;
import com.daka.annotation.Log;
import com.daka.constants.SystemConstant;
import com.daka.entry.SysUserVO;
import com.daka.util.DateUtil;

/**
 * ClassName:LogService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年3月30日 上午10:35:50 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
@Aspect
@Component
public class LogInterceptor
{
	private static final String COMMA = ",".intern();

	private static final String VO_BEFORE_SIGN = "get".intern();

	private static final String EMPTY_STRING = "".intern();

	private static final String[] CONTROLLER_CONTROL =
	{ "导出" };

	private final Logger logger = Logger.getLogger(LogInterceptor.class);

	// private LogService logService;

	// public LogService getLogService()
	// {
	// return logService;
	// }

	// 自动注入日志记录service
	@Autowired
//	private ISysLogDao logDao;

	// public void setLogService(LogService logService)
	// {
	// this.logService = logService;
	// }
	@Pointcut("execution(* com.daka.service..*(..))")
	private void anyMethod()
	{
	}

	@Before("anyMethod()")
	public void beforeOpration(JoinPoint joinPoint)

	{

	}

	private void buildInParam(Class[] args, String[] sArgs)
	{
		for (int i = 0; i < args.length; i++)
		{
			if (sArgs[i].endsWith("String[]"))
			{
				try
				{
					args[i] = Array.newInstance(Class.forName("java.lang.String"), 1).getClass();
				} catch (Exception e)
				{

					logger.error("业务日志--------------" + e.getCause());

				}
			} else if (sArgs[i].endsWith("Long[]"))
			{
				try
				{
					args[i] = Array.newInstance(Class.forName("java.lang.Long"), 1).getClass();
				} catch (Exception e)
				{

					logger.error("业务日志--------------" + e.getCause());

				}
			} else if (sArgs[i].indexOf(".") == -1)
			{
				if (sArgs[i].equals("int"))
				{
					args[i] = int.class;
				} else if (sArgs[i].equals("char"))
				{
					args[i] = char.class;
				} else if (sArgs[i].equals("float"))
				{
					args[i] = float.class;
				} else if (sArgs[i].equals("long"))
				{
					args[i] = long.class;
				} else if (sArgs[i].equals("boolean"))
				{
					args[i] = boolean.class;
				}
			} else
			{
				try
				{
					args[i] = Class.forName(sArgs[i]);
				} catch (ClassNotFoundException e)
				{

					logger.error("业务日志--------------" + e.getCause());

				}
			}
		}
	}

	@After("anyMethod()")
	public void after(JoinPoint joinPoint)
	{

	}

	@AfterThrowing("anyMethod()")
	public void doAfterThrow(JoinPoint joinPoint)
	{
	}

	@Around("anyMethod()")
	public Object doBasicProfiling(ProceedingJoinPoint joinPoint) throws Throwable

	{
		Object result = null;
		// class跟形参
		String longTemp = joinPoint.getStaticPart().toLongString();

		// method方法
		String methodName = joinPoint.getSignature().getName();

		// 形参类型，用于反射获取到方法
		Class[] args = new Class[joinPoint.getArgs().length];
		String[] sArgs = (longTemp.substring(longTemp.lastIndexOf("(") + 1, longTemp.length() - 2)).split(COMMA);

		buildInParam(args, sArgs);

		// 方法的参数
		Object[] param = joinPoint.getArgs();
		Class clazz = joinPoint.getTarget().getClass();
		Method method = null;
		try
		{
			method = clazz.getDeclaredMethod(methodName, args);
		} catch (NoSuchMethodException e1)
		{

			logger.error("业务日志：------------无此方法----" + e1.getCause());

		} catch (SecurityException e1)
		{

			logger.error("业务日志：" + e1.getCause());
		}
		String option = EMPTY_STRING;
		String modelName = EMPTY_STRING;
		String valueSignType = EMPTY_STRING;
		String valueSign = EMPTY_STRING;
		int index = 0;
		boolean flag = false;
		String levle = EMPTY_STRING;
		String valueSignCNDesc = EMPTY_STRING;
		StringBuffer sbInfo = new StringBuffer(" ");
		StringBuffer exInfo = new StringBuffer(" ");
		// 如果注解，则进行相应的业务日志存储
		if (method.isAnnotationPresent(Log.class))
		{
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
					.getRequest();
			// UserInfo userInfoInSession = Tools.getUserInfoInSession(request);
			// String userNameAndID = userInfoInSession.getUserName() + "(用户ID："
			// + userInfoInSession.getUserId() + ")";
			// sbInfo = new StringBuffer("用户【" + userNameAndID + "】对");
			exInfo = new StringBuffer(" ");

			flag = true;
			Log annotation = method.getAnnotation(Log.class);

			// 操作类型
			option = annotation.option();

			// 模块名称
			modelName = annotation.modelName();

			// 取值标志类型
			valueSignType = annotation.getValueSignType();

			// 取值标志
			valueSign = annotation.getValueSign();

			index = annotation.index();

			valueSignCNDesc = annotation.valueSignCNDesc();

			boolean needControllerControl = annotation.needControllerControl();

			// 如果是VO，则根据反射获取到需要获取的值
			sbInfo.append("【" + modelName + "】模块 -- ").append(valueSignCNDesc).append(":");

			// 如果需要依赖Controller来区分service
			if (needControllerControl)
			{
				StackTraceElement stack[] = Thread.currentThread().getStackTrace();
				for (StackTraceElement stackTraceElement : stack)
				{
					Method preMethod = null;
					if (stackTraceElement.getClassName().endsWith("Controller"))
					{
						Method[] methods = stackTraceElement.getClass().getMethods();
						for (Method mtd : methods)
						{
							if (method.getName().equals(stackTraceElement.getMethodName()))
							{
								preMethod = mtd;
								break;
							}
						}
					}
					if (!preMethod.isAnnotationPresent(Distinguish.class))
					{
						try
						{
							return joinPoint.proceed();
						} catch (Throwable e)
						{
							logger.error("业务日志：" + e.getCause());
						}
					}
				}
			}
			// 如果取值标志为空，则表示不需要取值
			if (valueSign.equals(EMPTY_STRING) && valueSignType.equals(EMPTY_STRING))
			{
				sbInfo.substring(0, sbInfo.length() - 1);
			} else
			{
				if (valueSignType.equals(Log.VALUESIGNTYPE_VO))
				{
					Class clazzParam = param[0].getClass();
					Method declaredMethod = null;
					try
					{
						declaredMethod = clazzParam.getDeclaredMethod(valueSign, null);
					} catch (Exception e)
					{
						logger.error("业务日志--------------" + e.getCause());
						try
						{
							declaredMethod = clazzParam.getSuperclass().getDeclaredMethod(valueSign, null);
						} catch (NoSuchMethodException e1)
						{
							logger.error("业务日志--------------" + e1.getCause());
						} catch (SecurityException e1)
						{

							logger.error("业务日志--------------" + e1.getCause());

						}
					}

					String value = EMPTY_STRING;
					try
					{
						value = String.valueOf(declaredMethod.invoke(param[index]));
					} catch (Exception e)
					{

						logger.error("业务日志--------------" + e.getCause());

					}
					sbInfo.append("对").append("【").append(value).append("】");
				}

				// 如果是Map，则直接获取到相应的值
				else if (valueSignType.equals(Log.VALUESIGNTYPE_MAP))
				{
					Class clazzParam = Map.class;
					Method declaredMethod = null;
					try
					{
						declaredMethod = clazzParam.getDeclaredMethod(VO_BEFORE_SIGN, Object.class);
					} catch (NoSuchMethodException e)
					{
						logger.error("业务日志--------------" + e.getCause());
					}
					String value = EMPTY_STRING;
					try
					{
						value = String.valueOf(declaredMethod.invoke(param[index], valueSign));
					} catch (Exception e)
					{

						logger.error("业务日志--------------" + e.getCause());

					}
					sbInfo.append("【").append(value).append("】");
				}

				// 若只是传值，则获取到是第几个值
				else if (valueSignType.equals(Log.VALUESIGNTYPE_INDEX))
				{
					sbInfo.append("【").append(param[index]).append("】");
				}

				// 如果是List，则得区分是ListVO还是ListMap
				else if (valueSignType.equals(Log.VALUESIGNTYPE_LIST))
				{
					List<?> list = (List) param[index];
					StringBuffer sb = new StringBuffer();
					for (Object object : list)
					{

						// 表示是VO
						if (valueSign.contains(VO_BEFORE_SIGN))
						{
							Class clazzParam = object.getClass();
							Method declaredMethod = null;
							try
							{
								declaredMethod = clazzParam.getDeclaredMethod(valueSign, null);
							} catch (Exception e)
							{
								logger.error("业务日志--------------" + e.getCause());
								// TODO Auto-generated catch block
								try
								{
									declaredMethod = clazzParam.getSuperclass().getDeclaredMethod(valueSign, null);
								} catch (Exception e1)
								{

									logger.error("业务日志--------------" + e1.getCause());

								}

							}
							String value = EMPTY_STRING;
							try
							{
								value = String.valueOf(declaredMethod.invoke(object));
							} catch (Exception e)
							{
								logger.error("业务日志--------------" + e.getCause());

							}
							if (!sb.toString().contains(value + COMMA))
							{
								sb.append(value).append(COMMA);
							}
						}
						if (valueSign.equals(EMPTY_STRING))
						{
							sb.append(String.valueOf(object)).append(COMMA);
						}
						// 否则就是Map
						else
						{
							Class clazzParam = Map.class;
							Method declaredMethod = null;
							try
							{
								declaredMethod = clazzParam.getDeclaredMethod(VO_BEFORE_SIGN, Object.class);
							} catch (Exception e)
							{
								logger.error("业务日志--------------" + e.getCause());

							}
							String value = EMPTY_STRING;
							try
							{
								value = String.valueOf(declaredMethod.invoke(object, valueSign));
							} catch (Exception e)
							{

								logger.error("业务日志--------------" + e.getCause());
							}

							if (!sb.toString().contains(value + COMMA))
							{
								sb.append(value).append(COMMA);
							}
						}
					}
					sbInfo.append("【").append(sb.substring(0, sb.length() - 1)).append("】");
				}
			}
			try
			{
				result = joinPoint.proceed();
				sbInfo.append("进行了【").append(option).append("】操作，操作成功");
				levle = "Info";
			} catch (Throwable e)
			{
				exInfo.append(e.getMessage());
				sbInfo.append("进行了【").append(option).append("】操作，操作失败");
				levle = "exception";
			}// 执行该方法
		} else
		{
			result = joinPoint.proceed();
		}
		if (flag)
		{
			try
			{
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
						.getRequest();
				SysUserVO userInfoInSession = (SysUserVO) request.getSession().getAttribute(SystemConstant.SYS_USER);
				Map<String, Object> logParam = new HashMap<String, Object>();
				logParam.put("createTime", DateUtil.getTime());
				logParam.put("level", levle);
				logParam.put("logInfo", sbInfo.toString());
				logParam.put("exInfo", exInfo.toString().equals(" ") ? "无" : exInfo.toString());
				logParam.put("source", longTemp);
				logParam.put("userID", userInfoInSession.getId());
//				if (logDao != null)
//				{
//					logDao.save(logParam);
//				}
				// if (logService != null)
				// {
				// logService.saveLog(logParam);
				// }
			} catch (Exception e)
			{
				logger.error("业务日志--------------" + e.getCause());
				try
				{
					return joinPoint.proceed();
				} catch (Throwable e1)
				{
					logger.error("业务日志--------------" + e1.getCause());
				}
			}
		}
		return result;
	}
}
