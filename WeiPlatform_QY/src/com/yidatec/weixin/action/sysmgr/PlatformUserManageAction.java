package com.yidatec.weixin.action.sysmgr;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.action.BaseAction;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ResponseEntity;
import com.yidatec.weixin.service.sysmgr.PlatformUserService;

public class PlatformUserManageAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger(PlatformUserManageAction.class);
	
	private PlatformUserEntity platformUserEntity = null;
	
	public PlatformUserEntity getPlatformUserEntity() {
		return platformUserEntity;
	}

	public void setPlatformUserEntity(PlatformUserEntity platformUserEntity) {
		this.platformUserEntity = platformUserEntity;
	}

	// 平台账户service
	private PlatformUserService platformUserService = null;
	
	public void setPlatformUserService(PlatformUserService platformUserService) {
		this.platformUserService = platformUserService;
	}
	
	private String newPassword = null;

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	/**
	 * 加载系统中所有的平台账户
	 */
	public void loadPlatformUsers() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();			
			jsonMap.put("total", platformUserService.loadPlatformUserCount());
			jsonMap.put("rows", platformUserService.loadPlatformUsers(getOffset(), getRows()));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 加载系统中所有的座席
	 */
	public void loadSeatUsers() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();			
			jsonMap.put("total", platformUserService.loadSeatUserCount());
			jsonMap.put("rows", platformUserService.loadSeatUsers(getOffset(), getRows()));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	

	/**
	 * 获取Leader
	 */
	public void loadLeader(){
		try {
			List<Map<String,String>> typeList = platformUserService.loadLeader();
			sendJson(JSONArray.fromObject(typeList).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 查询管理账户信息
	 */
	public void queryPlatformUsers() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();			
			jsonMap.put("total", platformUserService.queryPlatformUserCount(req_params));
			jsonMap.put("rows", platformUserService.queryPlatformUser(req_params,  getOffset(), getRows()));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 处理管理账户的增、删、改
	 */
	public void platformUserAction() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setResDesc(platformUserService.platformUserAction(req_params));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}	
	
	/**
	 * 修改密码时，验证输入的原始密码是否正确
	 */
	public void validateOldPass() {
		int flag ;
		try {
			PlatformUserEntity platformUserEntity = platformUserService
					.validateOldPass(getCurrentUser().getId());

			if (!req_params.trim().equals(platformUserEntity.getPassword())) {
				flag = 0;
			} else {
				flag = 1;
			}
			sendJson(String.valueOf(flag));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * 修改账户密码
	 * 
	 * @return
	 */
	public String changePassword() {
		try {
			platformUserService.changePassword(newPassword);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * 退出，重新分配任务
	 */
	public void reAllot(){
		try {
			platformUserService.reAllot(getCurrentUser().getId());
			sendSuccessToFront();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
}
