package com.yidatec.weixin.entity;

public class ResourceTreeEntity {

	private String parentId;

	private String parentName;
	
	private int parentPriority;
	
	private String childId;
	
	private String childName;
	
	private int childPriority;
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public int getParentPriority() {
		return parentPriority;
	}

	public void setParentPriority(int parentPriority) {
		this.parentPriority = parentPriority;
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	public int getChildPriority() {
		return childPriority;
	}

	public void setChildPriority(int childPriority) {
		this.childPriority = childPriority;
	}
}
