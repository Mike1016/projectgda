/**
 * yanhp
 */

package com.daka.interceptor;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.daka.constants.SystemConstant;
import com.daka.entry.SysUserVO;
import com.daka.util.StringUtil;
import com.daka.util.Tools;

/**
 * ClassName:LoginHandlerInterceptor <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016��4��11�� ����11:06:10 <br/>
 * 
 * @author yanhp
 * @version
 * @since JDK 1.6
 * @see
 */
public class LoginHandlerInterceptor extends HandlerInterceptorAdapter
{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
	{
		String url = request.getRequestURI();
		if (url.endsWith("daka/toList.do") || url.endsWith("login.do") || url.contains("app"))
		{
			return true;
		}
		if (request.getSession() == null || null == request.getSession().getAttribute(SystemConstant.SYS_USER))
		{
			sendUrl(request, response, "/index.jsp");
			return false;
		} 		
		return true;
	}

	private void sendUrl(HttpServletRequest request, HttpServletResponse response, String url)
	{
		try
		{
			response.sendRedirect(request.getServletContext().getContextPath() + url);
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
