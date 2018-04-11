package com.yidatec.weixin.action.seatmgr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.session.SessionRegistry;

import com.yidatec.weixin.action.BaseAction;
import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.entity.AdviserEntity;
import com.yidatec.weixin.entity.GroupEntity;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ResponseEntity;
import com.yidatec.weixin.entity.ScheduleEntity;
import com.yidatec.weixin.entity.UserScheduleEntity;
import com.yidatec.weixin.entity.message.UserType;
import com.yidatec.weixin.service.seatmgr.AdviserManageService;

public class AdviserManageAction extends BaseAction{
	private String params = "";
		
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(AdviserManageAction.class);
	
	private AdviserEntity adviserEntity = null;
	
	private GroupEntity groupEntity = null;
	
	private AdviserManageService adviserManageService = null;
	
	private String serviceID;

	SessionRegistry sessionRegistry;  
	
	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public AdviserEntity getAdviserEntity() {
		return adviserEntity;
	}

	public void setAdviserEntity(AdviserEntity adviserEntity) {
		this.adviserEntity = adviserEntity;
	}

	public GroupEntity getGroupEntity() {
		return groupEntity;
	}

	public void setGroupEntity(GroupEntity groupEntity) {
		this.groupEntity = groupEntity;
	}

	public AdviserManageService getAdviserManageService() {
		return adviserManageService;
	}

	public void setAdviserManageService(AdviserManageService adviserManageService) {
		this.adviserManageService = adviserManageService;
	}

	/**
	 * 查询咨询师列表
	 */
	public void queryAdviser() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();			
			jsonMap.put("total", adviserManageService.queryAdviserCount(req_params));
			jsonMap.put("rows", adviserManageService.queryAdviser(req_params,  getOffset(), getRows()));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 查询咨询师信息
	 */
	public void getAdviserInfo() {
		try {
			AdviserEntity adviserEntity = adviserManageService.getAdviserInfo(params);
			sendJson(JSONObject.fromObject(adviserEntity).toString());			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 咨询师的增、删、改
	 */
	public void adviserAction() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setResDesc(adviserManageService.syncAdviserAction(req_params));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	/**
	 * 时间计划管理
	 */
	public void saveSchedule() {
		try{
			ResponseEntity res = new ResponseEntity();
			int result = adviserManageService.syncSaveSchedule(req_params);
			
			if (result > 0) {
				
				res.setResDesc(EnumRes.SUCCESS.getDescription());
			} else {
				res.setResDesc(EnumRes.FAILED.getDescription());
			}	
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e){
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 添加备注
	 */
	public void saveRemark() {
		try{
			ResponseEntity res = new ResponseEntity();
			int result = adviserManageService.saveRemark(req_params);
			
			if (result > 0) {
				res.setResDesc(EnumRes.SUCCESS.getDescription());
			} else {
				res.setResDesc(EnumRes.FAILED.getDescription());
			}	
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e){
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 获取时间计划信息
	 */
	public void getSchedule(){
		try{
			List<ScheduleEntity> scheduleEntityList = adviserManageService.getSchedule();
			sendJson(JSONArray.fromObject(scheduleEntityList).toString());
		} catch (Exception e){
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 查询预约咨询
	 */
	public void queryScheduleReserve(){
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();			
			jsonMap.put("total", adviserManageService.queryScheduleReserveCount(req_params));
			jsonMap.put("rows", adviserManageService.queryScheduleReserve(req_params,  getOffset(), getRows()));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e){
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 预约咨询操作
	 */
	public void scheduleReserveAction(){
		try {
			ResponseEntity res = new ResponseEntity();
			res.setResDesc(adviserManageService.scheduleReserveAction(req_params));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 预约咨询操作(每次完成咨询后，给咨询师加一次时间)
	 */
	public void completeReserve(){
		try {
			ResponseEntity res = new ResponseEntity();
			res.setResDesc(adviserManageService.syncCompleteReserve(req_params));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 获取预约咨询信息
	 */
	public void getScheduleReserveInfo() {
		try {
			UserScheduleEntity userScheduleEntity = adviserManageService.getScheduleReserveInfo(params);
			sendJson(JSONObject.fromObject(userScheduleEntity).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 获取预约咨询时间
	 */
	public void getScheduleReserveTimes() {
		try {
			adviserEntity = adviserManageService.getAdviserInfo(params);
			AdviserEntity adviserEntityInfo = adviserManageService.getScheduleReserveTimes(adviserEntity);
			sendJson(JSONObject.fromObject(adviserEntityInfo).toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 修改预约时间
	 * @return
	 */
	public void updateReserveTime(){
		try {
			ResponseEntity res = new ResponseEntity();
			int result = adviserManageService.updateReserveTime(req_params);
			if (result > 0) {
				res.setResDesc(EnumRes.SUCCESS.getDescription());
			} else {
				res.setResDesc(EnumRes.FAILED.getDescription());
			}	
			sendJson(JSONObject.fromObject(res).toString());
			
		} catch (NumberFormatException e) {
			log.error(e.getMessage(),e);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 取得咨询师评价统计情况
	 */
	public void getAppraisalCountInfo(){
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<Map<String, Object>> mapList = adviserManageService.getAppraisalCountInfo();
			jsonMap.put("total", mapList.size());
			jsonMap.put("rows", mapList);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 根据客服ID取得所有评价信息
	 */
	public void getAllAppraisalOfAdviser(){
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<Map<String, Object>> mapList = adviserManageService.getAllAppraisalOfAdviser(serviceID);
			jsonMap.put("total", mapList.size());
			jsonMap.put("rows", mapList);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	public void getAllOnlineAdviser(){
		try{
			//取得所有在线人员
			List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
			List<Object> standard = new ArrayList<Object>();
			for(int i=0;i<allPrincipals.size();i++){
				Object obj = allPrincipals.get(i);			
				if (obj instanceof PlatformUserEntity){
					//查询客服
					if(((PlatformUserEntity)obj).getType() != UserType.ADMIN){
						standard.add(obj);
					}
				}
			}
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", standard.size());
			jsonMap.put("rows", standard);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 取得所有组
	 */
	public void getGroup() {
		try {
			Map<String, Object> jsonMap = new HashMap<String, Object>();			
			jsonMap.put("total", adviserManageService.getGroupCount());
			jsonMap.put("rows", adviserManageService.getGroup(getOffset(), getRows()));
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 组管理（添加，修改，删除）
	 */
	public void groupAction() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setResDesc(adviserManageService.groupAction(groupEntity));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 将用户添加到角色
	 */
	public void assignUser() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setResDesc(adviserManageService.assignUser(groupEntity));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 加载某个角色下面关联的用户
	 */
	public void loadGroupUsers() {
		try {
			List<PlatformUserEntity> users = adviserManageService.loadGroupUsers(groupEntity.getId());
			sendJson(JSONArray.fromObject(users).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}

	private String fromUserName;
	
	private String freeTimeRadio;
	
	private String radioId;
	
	public String getRadioId() {
		return radioId;
	}

	public void setRadioId(String radioId) {
		this.radioId = radioId;
	}

	public String getFreeTimeRadio() {
		return freeTimeRadio;
	}

	public void setFreeTimeRadio(String freeTimeRadio) {
		this.freeTimeRadio = freeTimeRadio;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getServiceID() {
		return serviceID;
	}

	public void setServiceID(String serviceID) {
		this.serviceID = serviceID;
	}
	
}
