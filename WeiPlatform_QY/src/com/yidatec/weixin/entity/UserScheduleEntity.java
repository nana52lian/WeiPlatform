package com.yidatec.weixin.entity;

public class UserScheduleEntity extends BaseEntity {

	private static final long serialVersionUID = 7226257139720815204L;

	private String weixinCode;
	private String advisoryAccount;
	private String beginTime;
	private String endTime;
	private Integer status;
	private String code;
	private String advisoryName;
	private String qq;
	private String mobile_phone;
	private String feedback;
	private Integer rank;
	private String chargeAccount;

	public String getWeixinCode() {
		return weixinCode;
	}

	public void setWeixinCode(String weixinCode) {
		this.weixinCode = weixinCode;
	}

	public String getAdvisoryAccount() {
		return advisoryAccount;
	}

	public void setAdvisoryAccount(String advisoryAccount) {
		this.advisoryAccount = advisoryAccount;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getAdvisoryName() {
		return advisoryName;
	}

	public void setAdvisoryName(String advisoryName) {
		this.advisoryName = advisoryName;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getMobile_phone() {
		return mobile_phone;
	}

	public void setMobile_phone(String mobile_phone) {
		this.mobile_phone = mobile_phone;
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	public String getFormat_feedback() {
		return "点击查看反馈";
	}
	
	public String getChargeAccount() {
		return chargeAccount;
	}

	public void setChargeAccount(String chargeAccount) {
		this.chargeAccount = chargeAccount;
	}

}
