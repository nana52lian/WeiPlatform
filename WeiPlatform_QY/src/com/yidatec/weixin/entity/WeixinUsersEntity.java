package com.yidatec.weixin.entity;

public class WeixinUsersEntity extends BaseEntity{
	
	private static final long serialVersionUID = 5048336929336634314L;

	/**
	 * 
	 */
	
	//是否关注
	private int subscribe;
	
	//open_id
	private String openid = null;
	
	//K账号
	private String key_account = null;
	
	//姓名
	private String name = null;
	
	//等级
	private String grade = null;
	
	//部门
	private String department = null;
	
	//地域
	private String area = null;
	
	//电话
	private String cellphone = null;
	
	//邮箱
	private String email = null;
	
	//请求单号
	//private String  = null;
	
	//用户评级
	private String user_level = null;
	
	//请求数
	private Integer request_count;
	
	//最新活跃时间
	private String active_time = null;
	
	//昵称	
	private String nickname = null;
	
	//性别
	private int sex;
	
	//语言
	private String language = null;
	
	//城市
	private String city = null;
	
	//省份
	private String province = null;
	
	//国家
	private String country = null;
	
	//头像
	private String headimgurl = null;
	
	//关注时间
	private int subscribe_time;
	
	//
	private int bind;
	
	private String formatSubscribeTime;

	public int getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(int subscribe) {
		this.subscribe = subscribe;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public int getSubscribe_time() {
		return subscribe_time;
	}

	public void setSubscribe_time(int subscribe_time) {
		this.subscribe_time = subscribe_time;
	}

	public int getBind() {
		return bind;
	}

	public void setBind(int bind) {
		this.bind = bind;
	}

	public String getFormatSubscribeTime() {
		return formatSubscribeTime;
	}

	public void setFormatSubscribeTime(String formatSubscribeTime) {
		this.formatSubscribeTime = formatSubscribeTime;
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

	public String getUser_level() {
		return user_level;
	}

	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}

	public Integer getRequest_count() {
		return request_count;
	}

	public void setRequest_count(Integer request_count) {
		this.request_count = request_count;
	}

	public String getActive_time() {
		return active_time;
	}

	public void setActive_time(String active_time) {
		this.active_time = active_time;
	}


}
