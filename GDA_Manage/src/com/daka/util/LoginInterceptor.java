package com.daka.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter
{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception
    {
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null)
        {
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }
        Map map = request.getParameterMap();
        Set set = map.entrySet();
        Iterator it = set.iterator();
        String regx = "exec|insert|select|delete|update|master|truncate|declare| join | into | from | char | where";
        Pattern p = Pattern.compile(regx);
        while (it.hasNext())
        {
            Entry entry = (Entry)it.next();
            String[] tmp = (String[])entry.getValue();
            for (int i = 0; i < tmp.length; i++)
            {
                tmp[i] = tmp[i].replace("'", "");
                if (tmp[i].matches(regx))
                {
                    response.setContentType("text/html");
                    response.setCharacterEncoding("utf-8");
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                    return false;
                }
            }
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView)
        throws Exception
    {
        super.postHandle(request, response, handler, modelAndView);
    }
}
