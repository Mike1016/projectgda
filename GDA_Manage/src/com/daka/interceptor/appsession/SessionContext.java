package com.daka.interceptor.appsession;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.daka.constants.SystemConstant;
import com.daka.entry.CustomerVO;
import com.daka.util.MD5;

public class SessionContext
{
	private static ConcurrentHashMap<String, HttpSession> sessions = new ConcurrentHashMap<String, HttpSession>();

	public static synchronized void AddSession(HttpSession session)
	{
		if (session != null)
		{
			sessions.putIfAbsent(MD5.md5(session.getId()), session);
			// sessions.put(MD5.md5(session.getId()), session);
		}
	}

	public static synchronized void DelSession(HttpSession session)
	{
		if (session != null)
		{
			sessions.remove(MD5.md5(session.getId()));
		}
	}

	public static synchronized HttpSession getSession(String sessionId)
	{
		if (sessionId == null)
		{
			return null;
		}
		return sessions.get(sessionId);
	}

	/**
	 * 新增session 适用于app
	 * 
	 * @param request
	 *            request对象
	 * @param key
	 *            键值，一般为SystemConstant.APP_USER
	 * @param Info
	 *            Customer对象
	 * @return 返回加密的sessionID 用于客户端保存
	 */
	public static synchronized String createSession(HttpServletRequest request, String key, Object Info)
	{
		HttpSession session = request.getSession();
		session.setAttribute(key, Info);
		sessions.put(MD5.md5(session.getId()), session);
		String sessionId = MD5.md5(session.getId());
		return sessionId;
	}

	/**
	 * 根据sessionId获取用户ID
	 * 
	 * @param request
	 * @param sessionId
	 * @return CustomerVO 对象
	 */
	public static CustomerVO getConstomerInfoBySessionId(HttpServletRequest request, String sessionId)
	{
		HttpSession session = getSession(sessionId);
		if (null == session)
		{
			return null;
		}
		return (CustomerVO) session.getAttribute(SystemConstant.APP_USER);
	}

	/**
	 * 获取session 数量
	 * @return
	 */
	public static int getCountSessions() {
		return sessions.size();
	}
}
