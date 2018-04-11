package com.yidatec.weixin.entity;

/**
 * 我的反馈用
 * @author Lance
 *
 */
public class UserFeedbackEntity extends BaseEntity {
	
	private String weixin_code = null;
	
	private String advisory_account = null;
	
	private String begin_time = null;
	
	private String end_time = null;
	
	// 咨询师姓名
	private String name = null;
	
	// 咨询师电话
	private String phone = null;

	public String getWeixin_code() {
		return weixin_code;
	}

	public void setWeixin_code(String weixin_code) {
		this.weixin_code = weixin_code;
	}

	public String getAdvisory_account() {
		return advisory_account;
	}

	public void setAdvisory_account(String advisory_account) {
		this.advisory_account = advisory_account;
	}

	public String getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(String begin_time) {
		this.begin_time = begin_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

}
