package com.yidatec.weixin.entity;

import java.util.ArrayList;
import java.util.List;

public class AuthTreeEntity extends BaseEntity {

	private String text = null;
	
	private String iconCls = null;
	
	// 是否展开节点
	private String state = null;
	
	private List<AuthTreeEntity> children = new ArrayList<AuthTreeEntity>();
	
	private String pid = null;
	
	private String checked = null;
	
	private String attributes = null;

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	public String getChecked() {
		return checked;
	}

	/**
	 * true | false
	 * @param checked
	 */
	public void setChecked(String checked) {
		this.checked = checked;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIconCls() {
		return iconCls;
	}

	/**
	 * icon-add ...
	 * @param iconCls
	 */
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public String getState() {
		if (children.size() == 0) 
			return "";
		return state;
	}

	/**
	 * open | closed
	 * @param state
	 */
	public void setState(String state) {
		this.state = state;
	}

	public List<AuthTreeEntity> getChildren() {
		return children;
	}

	public void setChildren(List<AuthTreeEntity> children) {
		this.children = children;
	}
	
	public boolean hasChildren() {
		if (null == children) {
			return false;
		}
		return this.children.size() > 0;
	}
		
}
