package com.yidatec.weixin.entity;

/**
 * 资源实体
 * @author CNDEV01
 *
 */
public class ResourcesEntity extends BaseEntity {

	/**
	 * 资源名称
	 */
	private String resource_name = null;
	
	/**
	 * url、method
	 */
	private String resource_type = null;
	
	/**
	 * 资源的优先权，即排序
	 */
	private int resource_priority = 0;
	
	/**
	 * 资源链接
	 */
	private String resource_string = null;
	
	/**
	 * 描述
	 */
	private String resource_desc = null;
	
	/**
	 * 是否启用
	 */
	private int enabled = 1;
	
	private int issys = 0;
	
	private String pid = "0";
	
	private boolean selected = false;

	public int getIssys() {
		return issys;
	}

	public void setIssys(int issys) {
		this.issys = issys;
	}

	public String getResource_name() {
		return resource_name;
	}

	public void setResource_name(String resource_name) {
		this.resource_name = resource_name;
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		this.resource_type = resource_type;
	}

	public int getResource_priority() {
		return resource_priority;
	}

	public void setResource_priority(int resource_priority) {
		this.resource_priority = resource_priority;
	}

	public String getResource_string() {
		return resource_string;
	}

	public void setResource_string(String resource_string) {
		this.resource_string = resource_string;
	}

	public String getResource_desc() {
		return resource_desc;
	}

	public void setResource_desc(String resource_desc) {
		this.resource_desc = resource_desc;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
