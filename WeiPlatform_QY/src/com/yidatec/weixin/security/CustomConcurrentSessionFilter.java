package com.yidatec.weixin.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.session.ConcurrentSessionFilter;

public class CustomConcurrentSessionFilter extends ConcurrentSessionFilter {

	SessionRegistryImpl sessionRegistry = null;
	
	DefaultRedirectStrategy redirectStrategy = null;
	
	public void setRedirectStrategy(DefaultRedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	public void setSessionRegistry(SessionRegistryImpl sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;  
	    HttpServletResponse response = (HttpServletResponse) arg1;	  
	    HttpSession session = request.getSession(false);  
	    if (session != null) {  
	        //这个SessionInformation是在执行SessionManagementFilter时通过sessionRegistry构造的并且放置在map集合中的  
	        SessionInformation info = sessionRegistry.getSessionInformation(session.getId());  
	        //如果当前session已经注册了  
	        if (info == null) {  
	        	String targetUrl = null;
	            String url = request.getRequestURI();
	            if(url.indexOf("admin") != -1){
	                //未登录而访问后台受控资源时，跳转到后台登录页面
	                targetUrl = request.getContextPath() + "/admin/login.jsp";
	                redirectStrategy.sendRedirect(request, response, targetUrl);
	            }
	        }  
	    }
	    chain.doFilter(request, response); 
	}
}
