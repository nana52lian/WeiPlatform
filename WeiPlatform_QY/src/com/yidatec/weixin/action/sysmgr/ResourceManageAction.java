package com.yidatec.weixin.action.sysmgr;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.yidatec.weixin.action.BaseAction;
import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.entity.ParamEntity;
import com.yidatec.weixin.entity.ResourcesEntity;
import com.yidatec.weixin.service.sysmgr.ResourceService;
import com.yidatec.weixin.util.CommonMethod;

public class ResourceManageAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	private static final Logger log = LogManager.getLogger(ResourceManageAction.class);
	
	private String pid;

	private String resource_name;
	
	private String resource_type;
	
	private String resource_string;
	
	private int resource_priority;
		
	private String resource_desc;
	
	private String ids = null;	

	ResourceService resourceService = null;
	
	/**
	 * 更改参数
	 */
	private List<ParamEntity> params = null;
	
	private String paramDatas;

	private String id;
	
	private String paramValue;

	private String message = null;
	
	public String resMgr() {
		return "resource_mgr";
	}
	
	private String requestTypeId;
	private String requestTypeDescription;
	
	/**
	 * 添加资源
	 */
	public String addResource() {
		ResourcesEntity resourcesEntity = new ResourcesEntity();
		resourcesEntity.setPid(pid);
		resourcesEntity.setResource_name(resource_name);
		resourcesEntity.setResource_type(resource_type);
		resourcesEntity.setResource_string(resource_string);
		resourcesEntity.setResource_priority(resource_priority);
		resourcesEntity.setResource_desc(resource_desc);		
		resourcesEntity.setCreate_user(getCurrentUser().getId());
		resourcesEntity.setCreate_date(CommonMethod.getStringDateLong());		
		try {
			resourceService.addResource(resourcesEntity);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "resource_mgr";
	}
	
	public void getParent() {
		try {
			List<ResourcesEntity> resources = resourceService.loadResources(true);
			ResourcesEntity tmpResources = new ResourcesEntity();
			tmpResources.setId("0");
			HttpServletRequest request = ServletActionContext.getRequest();  
	        Locale locale = (Locale)request.getSession().getAttribute("lang");  
	        if(locale != null){  
	            ActionContext.getContext().setLocale(locale);  
	        } 
	        if(locale.toString().equals("en_US")){
	        	tmpResources.setResource_name("--- please select ---");
			} else {
				tmpResources.setResource_name("--- 请选择   ---");
			}
			tmpResources.setSelected(true);
			resources.add(0, tmpResources);
			String jsonStr = JSONArray.fromObject(resources).toString();
			//System.out.println("getParent : ============== " + jsonStr);
			sendJson(jsonStr);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 获取系统中的资源
	 */
	public void getResources() {
		try {
			List<ResourcesEntity> resources = resourceService.loadResources(false, getOffset(), getRows());	
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			jsonMap.put("total", resourceService.getResourcesCount());
			jsonMap.put("rows", resources);
					
			sendJson(JSONObject.fromObject(jsonMap).toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 删除资源
	 */
	@SuppressWarnings("unused")
	public String delResource() {
		int[] res = {};
		try {
			res = resourceService.delResources(ids.split(","));
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return "resource_mgr";
	}
	
	/**
	 * 加载参数信息列表
	 */
	public void loadParams() {
		try {
			sendJson(JSONArray.fromObject(resourceService.loadParams()).toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 修改参数信息
	 */
	public void updateParams() {
		try {
			resourceService.updateParams(id, paramDatas, message);
			sendJson(EnumRes.UPDATESUCCESS.getDescription());
		} catch (Exception e) {
			sendJson(EnumRes.UPDATEFAILED.getDescription());
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 加载请求类型列表
	 */
	public void loadRequestTypes(){
		try{
			Map<String, Object> jsonMap = new HashMap<String, Object>();
			List<HashMap<String, String>> requestType = resourceService.loadRequestType(
					getOffset(), getRows());
			jsonMap.put("total", resourceService.getRequestTypeCount());
			jsonMap.put("rows", requestType);
			sendJson(JSONObject.fromObject(jsonMap).toString());
		}
		catch(Exception e){
			log.error(e.getMessage(),e);
		}
		
	}
	
	/**
	 * 添加或者更新请求类型
	 */
	public void addOrUpdateRequestType(){
		try{
			resourceService.addOrUpdateRequestType(requestTypeId,requestTypeDescription);
			sendJson(EnumRes.SUCCESS.getDescription());
		}
		catch(Exception e){
			sendJson(EnumRes.FAILED.getDescription());
			log.error(e.getMessage(),e);
		}
	}
	
	public void delRequestType(){
		try{
			resourceService.delRequestType(requestTypeId);
			sendJson(EnumRes.SUCCESS.getDescription());
		}
		catch(Exception e){
			sendJson(EnumRes.FAILED.getDescription());
			log.error(e.getMessage(),e);
		}
	}
	
	public void saveSubRequestType(){
		try{
			resourceService.saveSubRequestType(requestTypeId,requestTypeDescription);
			sendJson(EnumRes.SUCCESS.getDescription());
		}
		catch(Exception e){
			sendJson(EnumRes.FAILED.getDescription());
			log.error(e.getMessage(),e);
		}
	}
	
	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public void setIds(String ids) {
		this.ids = ids;
	}
	
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public void setResource_string(String resource_string) {
		this.resource_string = resource_string;
	}

	public void setResource_desc(String resource_desc) {
		this.resource_desc = resource_desc;
	}
	
	public void setResource_priority(int resource_priority) {
		this.resource_priority = resource_priority;
	}

	public List<ParamEntity> getParams() {
		return params;
	}

	public void setParams(List<ParamEntity> params) {
		this.params = params;
	}

	public String getParamDatas() {
		return paramDatas;
	}

	public void setParamDatas(String paramDatas) {
		this.paramDatas = paramDatas;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getRequestTypeId() {
		return requestTypeId;
	}

	public void setRequestTypeId(String requestTypeId) {
		this.requestTypeId = requestTypeId;
	}

	public String getRequestTypeDescription() {
		return requestTypeDescription;
	}

	public void setRequestTypeDescription(String requestTypeDescription) {
		this.requestTypeDescription = requestTypeDescription;
	}

}
