package com.daka.interceptor.appsession;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * Application Lifecycle Listener implementation class AppSessionListener
 * 
 */
@WebListener
public class AppSessionListener implements HttpSessionListener
{

	/**
	 * @see HttpSessionListener#sessionCreated(HttpSessionEvent)
	 */
	@Override
	public void sessionCreated(HttpSessionEvent arg0)
	{
		SessionContext.AddSession(arg0.getSession());
	}

	/**
	 * @see HttpSessionListener#sessionDestroyed(HttpSessionEvent)
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent arg0)
	{
		SessionContext.DelSession(arg0.getSession());
	}

}
