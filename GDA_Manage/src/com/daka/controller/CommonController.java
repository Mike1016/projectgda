/**
 * Project Name:SpringMVC_Frame
 * File Name:CommonController.java
 * Package Name:com.bus.controller.test
 * Date:2016年10月25日下午12:42:59
 * Copyright (c) 2016, chenzhou1025@126.com All Rights Reserved.
 *
 */

package com.daka.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.comet4j.core.CometConnection;
import org.comet4j.core.CometContext;
import org.comet4j.core.CometEngine;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.daka.constants.PushMessageConstant;
import com.daka.quartz.QuartzManager;
import com.daka.quartz.TaskJob;

import net.sf.json.JSONObject;

/**
 * ClassName:CommonController <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年10月25日 下午12:42:59 <br/>
 * 
 * @author Administrator
 * @version
 * @since JDK 1.6
 * @see
 */
@Controller
public class CommonController
{
	Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 记录用户跟链接ID的对应关系
	 * 
	 * @author yanhp
	 * @param request
	 * @since JDK 1.6
	 */
	@ResponseBody
	@RequestMapping(value = "/recordUserConnID")
	public void recordUserConnID(HttpServletRequest request)
	{
		String connID = request.getParameter("connID");
		PushMessageConstant.userID2ConnID.put("userID", connID);
		List<CometConnection> conns = new ArrayList<CometConnection>();
		CometEngine engine = CometContext.getInstance().getEngine();
		conns.add(engine.getConnection(connID));
		engine.sendTo(PushMessageConstant.APPLY_NOTICE, conns, "这是一个长链接，可向前台推送消息，链接ID为：" + connID);
		try
		{
			ServletInputStream inputStream = request.getInputStream();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/daka/toList")
	public Object list(HttpServletRequest request)
	{
		logger.info("---------/daka/toList--------------");
//		QuartzManager mng = new QuartzManager();
//		mng.addJob("测试1", TaskJob.class, "*/10 * * * * ?", "测试");
		ModelAndView mv = new ModelAndView("login");
		return mv;
	}

	@RequestMapping(value = "/daka/toIndex")
	public Object toIndex(HttpServletRequest request)
	{
		logger.info("-----------/daka/toIndex------------");
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}

	@RequestMapping(value = "/daka/bg")
	public Object bg(HttpServletRequest request)
	{
		logger.info("----------/daka/bg-------------");
		ModelAndView mv = new ModelAndView("bg");
		return mv;
	}

	protected Object getParam(HttpServletRequest request, Class... clazz)
	{
		Map<String, String[]> parameterMap = request.getParameterMap();
		Map<String, Object> paramMap = new HashMap<>();
		for (Entry<String, String[]> entry : parameterMap.entrySet())
		{
			paramMap.put(entry.getKey(), entry.getValue()[0]);
		}
		if (null != clazz && clazz.length > 0)
		{
			return JSONObject.toBean(JSONObject.fromObject(paramMap), clazz[0]);
		}
		return paramMap;
	}
}
