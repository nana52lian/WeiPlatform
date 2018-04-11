package com.yidatec.weixin.action;

import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.LocaleProvider;

/**
 * 语言控制
 */
public class I18nAction extends BaseAction implements LocaleProvider  {

	private static final long serialVersionUID = 6007225029714176249L;
	
	private Logger m_logger = null;
	
	private String local = null;
	
	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	/**
	 * 修改语言
	 */
	public String changeLang(){
		Locale locale=null;
//		JSONObject jsonObj = JSONObject.fromObject(req_params);
//		String local = jsonObj.optString("loginlang");
		try {
	        if(local.equals("en_US")){
	        	locale=new Locale("en", "US"); 
			} else if(local.equals("zh_CN")){
				locale=new Locale("zh", "cn");
			}
	        ActionContext ac=ActionContext.getContext();
			ac.setLocale(locale);
			
			Map<String,Object> session= ac.getSession();
			session.put("lang", locale);
		} catch (Exception e) {
			m_logger.error(e.getMessage(), e);
		}
		return "changelang";
	}
	
}
