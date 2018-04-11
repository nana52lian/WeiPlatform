package com.yidatec.weixin.security;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.opensymphony.xwork2.ActionContext;

public class CustomUsernamePasswordAuthenticationFilter extends
		UsernamePasswordAuthenticationFilter {

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException {		
		String imageCode = request.getSession().getAttribute("ver_rand") == null ? "" : (String)request.getSession().getAttribute("ver_rand");
		String verCode = request.getParameter("verCode");
		 Locale locale = (Locale)request.getSession().getAttribute("lang");  
		 String messageStr = "验证码错误！";
		 if(locale == null){
			 locale = Locale.getDefault();
			 if(locale.toString().equals("zh_CN")){
				 messageStr = "验证码错误！";
			 } else if(locale.toString().equals("en_US")) {
				 messageStr = "verify code error";
			 }
		 } else {
			 messageStr = "verify code error";
		 }
		 
		if(!imageCode.equals(verCode))
			throw new AuthenticationServiceException(messageStr);
		return super.attemptAuthentication(request, response);
	}
}
