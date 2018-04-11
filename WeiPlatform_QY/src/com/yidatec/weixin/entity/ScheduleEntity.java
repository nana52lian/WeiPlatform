package com.yidatec.weixin.entity;

public class ScheduleEntity extends BaseEntity{
	
	//关联的账号
	private String account = null;

	//时间
	private String schedule_time = null;
	
	//状态0：空闲 1：繁忙
	private int status = 0;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSchedule_time() {
		return schedule_time;
	}

	public void setSchedule_time(String schedule_time) {
		this.schedule_time = schedule_time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
