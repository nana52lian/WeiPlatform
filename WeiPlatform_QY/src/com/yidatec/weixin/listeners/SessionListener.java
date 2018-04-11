package com.yidatec.weixin.listeners;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 监听Session的创建与销毁
 * @author Lance
 *
 */
public class SessionListener implements HttpSessionListener, ServletContextListener  {

	private ServletContext servletContext;
	
	private List<String> sidList = null;
	
	
	@Override
	public void sessionCreated(HttpSessionEvent arg0) {
		String sid = arg0.getSession().getId();
		if (null == servletContext.getAttribute("sids")) {
			sidList = new ArrayList<String>();
			sidList.add(sid);
			servletContext.setAttribute("sids", sidList);
		} else {
			sidList = (List<String>)servletContext.getAttribute("sids");
			if (!sidList.contains(sid)) {
				sidList.add(sid);
			}
		}
		//System.out.println("Session Created, ID: [" + arg0.getSession().getId() + "]");		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent arg0) {
		String sid = arg0.getSession().getId();
		if (null != servletContext.getAttribute("sids")) {
			sidList = (List<String>)servletContext.getAttribute("sids");
			sidList.remove(sid);			
		}
		//System.out.println("Session Destroyed, ID: [" + arg0.getSession().getId() + "]");		
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		servletContext = arg0.getServletContext();
	}

}
