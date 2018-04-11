package com.yidatec.weixin.entity.message;

/**
 * 当前在线的客服实体
 * @author cht
 *
 */
public class OnlineCustomerServiceEntity {
    //ID
	private String id;
	//账号
	private String account;
	//姓名
	private String name;
	//任务数量
	private int task_count = 0;
	//中英文号区分
	private String weichat_type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public int getTask_count() {
		return task_count;
	}

	public void setTask_count(int task_count) {
		this.task_count = task_count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWeichat_type() {
		return weichat_type;
	}

	public void setWeichat_type(String weichat_type) {
		this.weichat_type = weichat_type;
	}
}
