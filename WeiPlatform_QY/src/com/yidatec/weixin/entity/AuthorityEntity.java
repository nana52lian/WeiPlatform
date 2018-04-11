package com.yidatec.weixin.entity;

import java.util.List;

/**
 * 权限实体
 * @author CNDEV01
 *
 */
public class AuthorityEntity extends BaseEntity {
		
	private String auth_name = null;
	
	private String auth_desc = null;
	
	private int enabled = 1;
	
	private int issys = 0;
	
	private String pid = null;
	
	// 关联了多少个平台账户
	private String platform_users_count = null;

	private List<ResourcesEntity> resources = null;
	
	private String id = null;
	
	private String clickable_platform_users_count = null;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlatfrom_user_ids() {
		return platfrom_user_ids;
	}

	public void setPlatfrom_user_ids(String platfrom_user_ids) {
		this.platfrom_user_ids = platfrom_user_ids;
	}

	// 角色关联平台账户id集合
	private String platfrom_user_ids = null;

	public String getAuth_name() {
		return auth_name;
	}

	public void setAuth_name(String auth_name) {
		this.auth_name = auth_name;
	}

	public String getAuth_desc() {
		return auth_desc;
	}

	public void setAuth_desc(String auth_desc) {
		this.auth_desc = auth_desc;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	public String getStatus() {
		return enabled == 1 ? "正常" : "已停用";
	}
	
	public List<ResourcesEntity> getResources() {
		return resources;
	}

	public void setResources(List<ResourcesEntity> resources) {
		this.resources = resources;
	}

	public int getIssys() {
		return issys;
	}

	public void setIssys(int issys) {
		this.issys = issys;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPlatform_users_count() {
		return platform_users_count;
	}

	public void setPlatform_users_count(String platform_users_count) {
		this.platform_users_count = platform_users_count;
	}
	
	public String getClickable_platform_users_count() {
		if (clickable_platform_users_count == null) {
			String roleId = getId();
			String userCount = getPlatform_users_count();
			clickable_platform_users_count = "<a href='javascript:void(0);' onclick='usersCountHref(\""+userCount+"\",\""+roleId+"\")'>"+userCount+"</a>";
		} 
		return clickable_platform_users_count;
	}

	public void setClickable_platform_users_count(
			String clickable_platform_users_count) {
		this.clickable_platform_users_count = clickable_platform_users_count;
	}
	
	
}
