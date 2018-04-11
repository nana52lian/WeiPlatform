package com.yidatec.weixin.action.sysmgr;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.action.BaseAction;
import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.entity.AuthorityEntity;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ResponseEntity;
import com.yidatec.weixin.entity.RoleAuthEntity;
import com.yidatec.weixin.entity.SysMessageEntity;
import com.yidatec.weixin.service.sysmgr.RoleAuthsManageService;
import com.yidatec.weixin.util.CommonMethod;


public class RoleAuthsManageAction extends BaseAction {

	private static final long serialVersionUID = 1L;

	private static final Logger log = LogManager.getLogger(RoleAuthsManageAction.class);
	
	private RoleAuthsManageService roleAuthsManageService = null;
	
	// 从DATAGRID 选中多行的ID
	private String ids = null;
	
	// 角色权限实体
	private AuthorityEntity authorityEntity = null;
	
    private String role_id = null;
	
	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}
	
	public void setIds(String ids) {
		this.ids = ids;
	}

	public AuthorityEntity getAuthorityEntity() {
		return authorityEntity;
	}

	public void setAuthorityEntity(AuthorityEntity authorityEntity) {
		this.authorityEntity = authorityEntity;
	}

	public void setRoleAuthsManageService(
			RoleAuthsManageService roleAuthsManageService) {
		this.roleAuthsManageService = roleAuthsManageService;
	}

	/**
	 * 加载添加的角色
	 */
	public void loadRoles() {		
		try {
			List<AuthorityEntity> authorities = roleAuthsManageService.loadRoles();
			sendJson(JSONArray.fromObject(authorities).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 加载权限树
	 */
	public void loadAuthTree() {		
		try {
			String jsonStr = JSONArray.fromObject(roleAuthsManageService.loadAuthTree()).toString();
			sendJson(jsonStr);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 加载角色权限树
	 * 如果存在要check
	 */
	public void loadRoleAuthTree() {
		try {
			String jsonStr = JSONArray.fromObject(roleAuthsManageService.loadRoleAuthTree(role_id)).toString();
			sendJson(jsonStr);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 保存角色的资源权限
	 */
	public void saveRoleAuth() {
		try {
			String[] resourceId = ids.split(",");
			List<RoleAuthEntity> roleAuthEntities = new ArrayList<RoleAuthEntity>();
			RoleAuthEntity tmpAuth = null;
			for (int i=0; i<resourceId.length; i++) {
				tmpAuth = new RoleAuthEntity();
				tmpAuth.setAuthority_id(role_id);
				tmpAuth.setResource_id(resourceId[i]);
				tmpAuth.setCreate_user(getCurrentUser().getId());
				tmpAuth.setCreate_date(CommonMethod.getStringDateLong());
				roleAuthEntities.add(tmpAuth);
			}
			roleAuthsManageService.syncSaveRoleAuth(roleAuthEntities);
			SysMessageEntity sysMessage = new SysMessageEntity();
			sysMessage.setMessage(EnumRes.SUCCESS.getDescription());
			sendJson(JSONObject.fromObject(sysMessage).toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * 添加角色
	 */
	public void roleAction() {
		try {
			ResponseEntity res = new ResponseEntity();
			res.setResDesc(roleAuthsManageService.roleAction(authorityEntity));
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
			res.setResDesc(roleAuthsManageService.assignUser(authorityEntity));
			sendJson(JSONObject.fromObject(res).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
	/**
	 * 加载某个角色下面关联的用户
	 */
	public void loadRoleUsers() {
		try {
			List<PlatformUserEntity> users = roleAuthsManageService.loadRoleUsers(authorityEntity.getId());
			sendJson(JSONArray.fromObject(users).toString());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			sendErrorToFront();
		}
	}
	
}
