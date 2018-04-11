package com.yidatec.weixin.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class LangFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain arg2) throws IOException, ServletException {
		HttpServletRequest httpRequest=(HttpServletRequest)request;      
		
		String url = httpRequest.getRequestURI();         
           
        try{    
            if ("imageUp.jsp".equals(url.substring(url.length()-11))) {    
            	System.out.println(url); 
            	arg2.doFilter(request, response); 
            } else {    
                RequestDispatcher   ds   =   request.getRequestDispatcher( "totalfilter.action");   
                request.setAttribute("jsp", httpRequest.getServletPath());  
                request.setAttribute("param", httpRequest.getQueryString());  
                ds.forward(request,   response);  
            }    
        }catch(Exception e){    
            e.printStackTrace();    
        }  
		
       
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
