package com.daka.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.daka.util.session.SessionUtil;

public class CustomerInterceptor extends HandlerInterceptorAdapter {
	
//	@Override
//	public void afterCompletion(HttpServletRequest request,
//			HttpServletResponse response, Object handler, Exception ex)
//			throws Exception {
//		// TODO Auto-generated method stub
//		super.afterCompletion(request, response, handler, ex);
//	}
//	
//	
//	@Override
//	public void postHandle(HttpServletRequest request,
//			HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//		// TODO Auto-generated method stub
//		super.postHandle(request, response, handler, modelAndView);
//	}
//	
//	public boolean prehandle(HttpServletRequest request,HttpServletResponse response,Object handler){
//		HttpSession session=null;
//		String sessionID="";
//		if(StringUtils.isNotBlank(request.getParameter("sessionKey"))){
//			sessionID=request.getParameter("sessionKey");
//		}else if(StringUtils.isNotBlank(request.getHeader("sessionKey"))){
//			sessionID=request.getHeader("sessionKey");
//		}
//		if(sessionID!=null){
//			session=(HttpSession)SessionUtil.getSessionMap().get(sessionID);
//			if(session!=null){
//				String sessionValue=(String)session.getAttribute("customer");
//				if(StringUtils.isNotBlank(sessionValue)){
//					return true;
//				}
//			}
//		}
//		
//		return false;
//	}

}
