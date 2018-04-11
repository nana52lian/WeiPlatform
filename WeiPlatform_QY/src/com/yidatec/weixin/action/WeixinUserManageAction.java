package com.yidatec.weixin.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.entity.ResponseEntity;
import com.yidatec.weixin.service.WeixinUserManageService;

public class WeixinUserManageAction extends BaseAction {

	private static final long serialVersionUID = 317587766286576626L;
	
	private static final Logger log = LogManager.getLogger(WeixinUserManageAction.class);
	
	private WeixinUserManageService weixinUserManageService = null;
	
	public void setWeixinUserManageService(
			WeixinUserManageService weixinUserManageService) {
		this.weixinUserManageService = weixinUserManageService;
	}

	/**
	 * 查询微信用户列表
	 */
	public void queryWeixinUsers(){
		
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();			
			jsonMap.put("total", weixinUserManageService.queryWeixinUserCount(req_params));
			jsonMap.put("rows", weixinUserManageService.queryWeixinUser(req_params,  getOffset(), getRows()));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
		
	}
	
	/**
	 * 绑定K账号
	 */
	public void bindKeyAccount() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setResDesc(weixinUserManageService.bindKeyAccount(req_params));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
		
		
	}

}
