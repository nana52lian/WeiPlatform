package com.yidatec.weixin.entity;

public class RegionEntity {

	private int region_id;
	
	private int parent_id;
	
	private String region_name;
	
	private int region_type;
	
	private int agency_id;

	public int getRegion_id() {
		return region_id;
	}

	public void setRegion_id(int regionId) {
		region_id = regionId;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parentId) {
		parent_id = parentId;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String regionName) {
		region_name = regionName;
	}

	public int getRegion_type() {
		return region_type;
	}

	public void setRegion_type(int regionType) {
		region_type = regionType;
	}

	public int getAgency_id() {
		return agency_id;
	}

	public void setAgency_id(int agencyId) {
		agency_id = agencyId;
	}
}
