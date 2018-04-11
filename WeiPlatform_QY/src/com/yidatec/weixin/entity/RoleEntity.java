package com.yidatec.weixin.entity;

import java.util.List;

/**
 * 角色实体
 * @author CNDEV01
 *
 */
public class RoleEntity extends BaseEntity {
	
	private String role_name = null;
	
	private String role_desc = null;
	
	private int enabled = 0;
	
	private int issys = 0;
	
	private List<AuthorityEntity> authorities = null;

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_desc() {
		return role_desc;
	}

	public void setRole_desc(String role_desc) {
		this.role_desc = role_desc;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	public List<AuthorityEntity> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<AuthorityEntity> authorities) {
		this.authorities = authorities;
	}

	public int getIssys() {
		return issys;
	}

	public void setIssys(int issys) {
		this.issys = issys;
	}

}
