package com.yidatec.weixin.entity.message;

import com.yidatec.weixin.entity.BaseEntity;

public class TaskQueueEntity extends BaseEntity{

	private static final long serialVersionUID = -7424304692668209825L;
    //ID
	private String id;
	//客户ID
	private String open_id;
	//客服ID
	private String service_id;
	//任务状态
	private Integer task_status;
	//客服账户
	private String service_account;
	//姓名
	private String name;
	//rid
	private String rid;
	//请求类型
	private String type;
	//客户被评分
	private String custom_score;
	//从某号来的咨询区分
	private String weichat_type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpen_id() {
		return open_id;
	}

	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}

	public String getService_id() {
		return service_id;
	}

	public void setService_id(String service_id) {
		this.service_id = service_id;
	}

	public Integer getTask_status() {
		return task_status;
	}

	public void setTask_status(Integer task_status) {
		this.task_status = task_status;
	}

	public String getService_account() {
		return service_account;
	}

	public void setService_account(String service_account) {
		this.service_account = service_account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCustom_score() {
		return custom_score;
	}

	public void setCustom_score(String custom_score) {
		this.custom_score = custom_score;
	}

	public String getWeichat_type() {
		return weichat_type;
	}

	public void setWeichat_type(String weichat_type) {
		this.weichat_type = weichat_type;
	}

}
