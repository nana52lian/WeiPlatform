package com.yidatec.weixin.entity;


public class RoleAuthEntity extends BaseEntity {

	private String authority_id = null;
	
	private String resource_id = null;

	public String getAuthority_id() {
		return authority_id;
	}

	public void setAuthority_id(String authority_id) {
		this.authority_id = authority_id;
	}

	public String getResource_id() {
		return resource_id;
	}

	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}
}
