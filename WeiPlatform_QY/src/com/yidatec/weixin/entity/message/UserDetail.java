package com.yidatec.weixin.entity.message;

import com.yidatec.weixin.entity.BaseEntity;

public class UserDetail extends BaseEntity {

	private static final long serialVersionUID = 685164687122545411L;
	//ID
	private String id;
	//任务ID
	private String taskid;
	//微信用户ID
	private String openid;
	//K账号
	private String key_account;
	//名称
	private String name;
	//性别
	private Integer sex;
	//年龄
	private Integer age;
	//等级
	private String grade;
	//部门
	private String department;
	//区域
	private String area;
	//cellphone
	private String cellphone;
	//邮箱
	private String email;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getKey_account() {
		return key_account;
	}
	public void setKey_account(String key_account) {
		this.key_account = key_account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCellphone() {
		return cellphone;
	}
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
