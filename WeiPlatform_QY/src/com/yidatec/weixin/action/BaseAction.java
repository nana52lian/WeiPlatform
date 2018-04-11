package com.yidatec.weixin.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.ServletContextAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ResponseEntity;

public class BaseAction extends ActionSupport implements SessionAware,
	ServletRequestAware, ServletResponseAware, ServletContextAware, IActionResult {

	private static final Logger log = LogManager.getLogger(BaseAction.class);
	
	public static final String MIME_TYPE = "text/html; charset=UTF-8";
	
	/** 用于页面JS消息提醒 */
	public String message_js = "";

	protected HttpServletRequest request;
	
	protected HttpServletResponse response;
	
	private Map<String, Object> session;
	
	private ServletContext servletContext;

	private int page = 1;	

	private int rows = 10;	
	
	// 各种请求参数
	protected String req_params = null;
	
	/**
	 * 使用layer提示信息
	 * @param message
	 * @param delay 延时，单位秒
	 * @param icon 1：对勾 3:错误 4:问号 5:禁止 8:哭脸 9:笑脸
	 * @return
	 */
	public String getLayerJSMessage(String message, int delay, int icon) {
		return "<script>layer.msg('" + message + "'," + delay + "," + icon + ");</script>";
	}
	
	/**
	 * 使用layer提示操作成功
	 * @return
	 */
	public String getLayerJSSuccessStr() {
		return "<script>layer.msg('操作成功',1,9);</script>";
	}
	
	/**
	 * 使用layer提示操作成功
	 * @return
	 */
	public String getLayerJSFailedStr() {
		return "<script>layer.msg('操作失败',1,8);</script>";
	}

	/**
	 * 发送成功
	 */
	public void sendSuccessToFront() {		
		ResponseEntity res = new ResponseEntity();
		res.setRes(EnumRes.SUCCESS);
		sendJson(JSONObject.fromObject(res).toString());
	}
	
	/**
	 * 发送错误
	 */
	public void sendErrorToFront() {
		ResponseEntity res = new ResponseEntity();
		res.setRes(EnumRes.FAILED);
		sendJson(JSONObject.fromObject(res).toString());		
	}
	
	/**
	 * 获取当前登录用户的信息
	 * @return
	 */
	public PlatformUserEntity getCurrentUser() {
		SecurityContext ctx = SecurityContextHolder.getContext(); 
		Authentication auth = ctx.getAuthentication(); 
		if (auth.getPrincipal() instanceof PlatformUserEntity) {
			return (PlatformUserEntity) auth.getPrincipal();
		}
		return new PlatformUserEntity();
	}
		
	/**
	 * 发送Json数据
	 * @param sendValue
	 * @return
	 */
	public boolean sendJson(String sendValue) {
		PrintWriter out;
		try {
			response.setContentType(MIME_TYPE);
			out = response.getWriter();
			out.write(sendValue);
			return true;
		} catch (IOException ex) {
			log.error(ex.getMessage());
		}
		return false;
	}
	
	/**
	 * 发送空的Json数据
	 * @return
	 */
	public boolean sendEmptyJson() {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put("total", 0);
		jsonMap.put("rows", new ArrayList<String>());
		return sendJson(JSONObject.fromObject(jsonMap).toString());
	}
	
	/**
	 * 获取服务器绝对路径
	 * @return
	 */
	public String getServerRealPath() {
		return request.getSession().getServletContext().getRealPath("/");
	}
	
	public void setReq_params(String req_params) {
		this.req_params = req_params;
	}

	/**
	 * 获取从哪条记录开始查询
	 * limit offset, rows
	 * @return
	 */
	public int getOffset() {
		return (page - 1) * rows;
	}
	
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public void setServletContext(ServletContext arg0) {
		this.servletContext = arg0;
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		this.response = arg0;
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		this.request = arg0;
	}

	@Override
	public void setSession(Map<String, Object> arg0) {
		this.session = arg0;
	}
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
		
	public String getMessage_js() {
		return message_js;
	}

	/**
	 * 设置操作提示消息
	 * @param message_js
	 */
	public void setOperateMessage(String message_js) {
		this.message_js = message_js;
	}

}
