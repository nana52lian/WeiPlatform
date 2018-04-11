package com.yidatec.weixin.entity;

public class GroupEntity extends BaseEntity {

	private static final long serialVersionUID = 5607937674214254303L;
	//ID
	private String id;
	//姓名
	private String name;
	//描述
	private String groupdesc;
	//leader
	private String leader;
	// 组关联平台账户id集合
	private String platfrom_user_ids;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupdesc() {
		return groupdesc;
	}
	public void setGroupdesc(String groupdesc) {
		this.groupdesc = groupdesc;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public String getPlatfrom_user_ids() {
		return platfrom_user_ids;
	}
	public void setPlatfrom_user_ids(String platfrom_user_ids) {
		this.platfrom_user_ids = platfrom_user_ids;
	}
	
}
