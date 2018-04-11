package com.yidatec.weixin.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 权限拦截器，用于控制商户管理页面
 * @author Administrator
 *
 */
public class AuthorityInterceptor extends AbstractInterceptor {
	
	private static final Logger log = LogManager.getLogger(AuthorityInterceptor.class);

	private static final long serialVersionUID = 1L;
	
	@Override
	public String intercept(ActionInvocation invocation) throws Exception {		
		interceptAjax();
        return invocation.invoke();
	}
	
	/**
	 * 处理Ajax请求
	 */
	private void interceptAjax() {
		try {
			HttpServletRequest request = ServletActionContext.getRequest();
			HttpServletResponse response = ServletActionContext.getResponse();
			String ajaxRequest = request.getHeader("X-Requested-With");
			if (ajaxRequest != null && ajaxRequest.equalsIgnoreCase("XMLHttpRequest")) {
				Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
				// 如果session失效了
				if (null == currentUser || currentUser.getPrincipal().equals("anonymousUser")) {
					response.setCharacterEncoding("text/html; charset=UTF-8");
					response.setContentType("text/html; charset=UTF-8");
					response.setHeader("sessionstatus", "timeout");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
//	@Override
//	public String intercept(ActionInvocation invocation) throws Exception {
//
//	ActionContext ctx = invocation.getInvocationContext();
//	Map session = ctx.getSession();
//	User user = (User) session.get(Constants.SESSION_USER_KEY);
//	if (user == null) {
//	HttpServletRequest request = ServletActionContext.getRequest();
//	HttpServletResponse response = ServletActionContext.getResponse();
//	PrintWriter pw = response.getWriter();
//	String flag = "";
//	//对ajax请求的拦截
//	if (request.getHeader("X-Requested-With") != null
//	&& request.getHeader("X-Requested-With").equalsIgnoreCase(     
//	"XMLHttpRequest")) {  
//	log.info("用户没登录或登录过期，不能访问");
//	response.setCharacterEncoding("text/html;charset=utf-8");
//	response.setContentType("text/html;charset=utf-8");
//	flag = "9999";
//	pw.write(flag);
//	return null;
//	//不是异步请求的拦截
//	} else {
//	response.setCharacterEncoding("text/html;charset=utf-8");
//	response.sendRedirect("/businessTest/login.jsp");
//	log.info("用户没登录或登录过期，不能访问");
//	return "login";
//	}
//	}
//	return invocation.invoke();
//	}
}
