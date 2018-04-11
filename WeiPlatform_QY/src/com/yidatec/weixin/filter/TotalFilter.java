package com.yidatec.weixin.filter;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class TotalFilter extends ActionSupport {

	private static final long serialVersionUID = 1L;  
      
    private String url;  
      
    public String getUrl() {  
        return url;  
    }  
      
    public String  execute() throws Exception{
    	try {
		    HttpServletRequest request = ServletActionContext.getRequest();  
	        Locale locale = (Locale)request.getSession().getAttribute("lang");  
	        if(locale != null){  
	            ActionContext.getContext().setLocale(locale);  
	        }  else {
	        	ActionContext.getContext().getSession().put("lang", locale.getDefault());
	        }
	        String result = (String)request.getAttribute("jsp");          
	        url = result;  
	        if(request.getQueryString() != null){  
	            url = url + "?" + request.getQueryString();  
	        }  
    	} catch (Exception e){
    		e.printStackTrace();
    	}
        return "url";  
    }  
}
