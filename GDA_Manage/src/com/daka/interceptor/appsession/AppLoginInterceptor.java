package com.daka.interceptor.appsession;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.daka.entry.CustomerVO;
import net.sf.json.JSONObject;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.daka.constants.SystemConstant;
import com.daka.entry.CustomerVO;
import com.daka.util.StringUtil;

public class AppLoginInterceptor implements HandlerInterceptor
{

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception
	{
		// TODO Auto-generated method stub

	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception
	{

		if (!request.getRequestURI().contains("app"))
		{
			return true;
		}
		String sessionId = request.getParameter("sessionId");
		if (StringUtil.isEmptyString(sessionId))
		{
			Map<String, Object> hashMap = new HashMap<>();
			hashMap.put(SystemConstant.STATUS, 5000);
			hashMap.put(SystemConstant.MESSAGE, "账号异常，请重新登录");
			String str = JSONObject.fromObject(hashMap).toString();
			StringUtil.sendDataToResponse(response, str);
			return false;
		}

		HttpSession session = SessionContext.getSession(sessionId);
		if (session == null)
		{
			Map<String, Object> hashMap = new HashMap<>();
			hashMap.put(SystemConstant.STATUS, 5000);
			hashMap.put(SystemConstant.MESSAGE, "登录超时，请重新登录");
			String str = JSONObject.fromObject(hashMap).toString();
			StringUtil.sendDataToResponse(response, str);
			return false;
		}

		CustomerVO customer = (CustomerVO) session.getAttribute(SystemConstant.APP_USER);
		if (customer != null && customer.getId() != null)
		{
			return true;
		} else
		{
			Map<String, Object> hashMap = new HashMap<>();
			hashMap.put(SystemConstant.STATUS, 5000);
			hashMap.put(SystemConstant.MESSAGE, "账号异常，请重新登录");
			String str = JSONObject.fromObject(hashMap).toString();
			StringUtil.sendDataToResponse(response, str);
			return false;
		}
	}
}
