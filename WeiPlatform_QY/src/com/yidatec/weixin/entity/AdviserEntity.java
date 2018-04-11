package com.yidatec.weixin.entity;

import java.util.List;

public class AdviserEntity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//关联的账号
	private String account = null;

	//姓名
	private String name = null;
	
	//性别
	private int sex = 1;
	
	//身份证号
	private String card_number = null;
	
	//省
	private String province = null;
	
	//市
	private String city = null;
	
	//区县
	private String region = null;
	
	//邮编
	private String postcode = null;
	
	//联系电话
	private String phone = null;
	
	//邮箱
	private String mail = null;
	
	//咨询师级别
	private String level = null;
	
	//咨询师证号
	private String certificate_number = null;
	
	//其他资格证书及号码
	private String other_number = null;
	
	//咨询师昵称
	private String nick_name = null;
	
	//全职/兼职
	private String full_time = null;
	
	//年龄段
	private String age_group = null;
	
	//咨询范围
	private String advisory_scope = null;
	
	//培训情况
	private String training_situation = null;
	
	//擅长的方法
	private String good_way = null;
	
	//添加的其他咨询范围
	private String other_scope = null;
	
	//对自己的其他描述
	private String description = null;
	
	//省份名
	private String provinceName;
	
	//城市名
	private String cityName;
	
	//地区名
	private String regionName;
	
	//明天空闲的时间段
	private List<String> freeTimes1;
	
	//后天空闲的时间段
	private List<String> freeTimes2;
	
	//今天的空闲时间段
	private List<String> todayFreeTimes;
	
	//定价方式
	private String price_plan;
	
	//专业程度
	private Integer professional;
	
	//咨询时长
	private String duration = "0";
	
	//评价1
	private String assess1;
	
	//评价2
	private String assess2;
	
	//评价3
	private String assess3;
	
	//评价4
	private String assess4;
	
	//评价5
	private String assess5;
	
	//评价6
	private String assess6;
	
	//评价7
	private String assess7;
	
	//评价柱状图url
	private String assessImgUrl;
	
	//收费账号
	private String charge_account;	
	
	//本月心情能量图
	private String moodImg;
	
	//提示语
	private String presentation;
	
	//上个月心情URL
	private String lastMonthMoodUrl;
	
	//微信code
	private String fromUserName;
	
	//当前图表中显示的月份
	private Integer currentMonth;
	
	//手机访问慰微信，咨询师年龄
	private Integer age;
	
	//备注
	private String remark;
	
	//类型
	private String type;
	
	//微信平台应用入口
	private String weichat_type;
	
	/**
	 * 标记删除
	 * @return
	 */
	private int dr = 0;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCertificate_number() {
		return certificate_number;
	}

	public void setCertificate_number(String certificate_number) {
		this.certificate_number = certificate_number;
	}

	public String getOther_number() {
		return other_number;
	}

	public void setOther_number(String other_number) {
		this.other_number = other_number;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getFull_time() {
		return full_time;
	}

	public void setFull_time(String full_time) {
		this.full_time = full_time;
	}

	public String getAge_group() {
		return age_group;
	}

	public void setAge_group(String age_group) {
		this.age_group = age_group;
	}

	public String getAdvisory_scope() {
		return advisory_scope;
	}

	public void setAdvisory_scope(String advisory_scope) {
		this.advisory_scope = advisory_scope;
	}

	public int getDr() {
		return dr;
	}

	public void setDr(int dr) {
		this.dr = dr;
	}

	public String getTraining_situation() {
		return training_situation;
	}

	public void setTraining_situation(String training_situation) {
		this.training_situation = training_situation;
	}

	public String getGood_way() {
		return good_way;
	}

	public void setGood_way(String good_way) {
		this.good_way = good_way;
	}

	public String getOther_scope() {
		return other_scope;
	}

	public void setOther_scope(String other_scope) {
		this.other_scope = other_scope;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	public String getProvinceName() {
		if (provinceName == null)
			return "";
		
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		if (cityName == null)
			return "";
		
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getRegionName() {
		if (regionName == null)
			return "";
			
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	
	
	public List<String> getFreeTimes1() {
		return freeTimes1;
	}

	public void setFreeTimes1(List<String> freeTimes1) {
		this.freeTimes1 = freeTimes1;
	}

	public List<String> getFreeTimes2() {
		return freeTimes2;
	}

	public void setFreeTimes2(List<String> freeTimes2) {
		this.freeTimes2 = freeTimes2;
	}

	public Integer getProfessional() {
		return professional;
	}

	public void setProfessional(Integer professional) {
		this.professional = professional;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String getAssess1() {
		return assess1;
	}

	public void setAssess1(String assess1) {
		this.assess1 = assess1;
	}

	public String getAssess2() {
		return assess2;
	}

	public void setAssess2(String assess2) {
		this.assess2 = assess2;
	}

	public String getAssess3() {
		return assess3;
	}

	public void setAssess3(String assess3) {
		this.assess3 = assess3;
	}

	public String getAssess4() {
		return assess4;
	}

	public void setAssess4(String assess4) {
		this.assess4 = assess4;
	}

	public String getAssess5() {
		return assess5;
	}

	public void setAssess5(String assess5) {
		this.assess5 = assess5;
	}

	public String getAssess6() {
		return assess6;
	}

	public void setAssess6(String assess6) {
		this.assess6 = assess6;
	}

	public String getAssess7() {
		return assess7;
	}

	public void setAssess7(String assess7) {
		this.assess7 = assess7;
	}

	public String getPrice_plan() {
		return price_plan;
	}

	public void setPrice_plan(String price_plan) {
		this.price_plan = price_plan;
	}
	
	public String getAssessImgUrl() {
		return assessImgUrl;
	}

	public void setAssessImgUrl(String assessImgUrl) {
		this.assessImgUrl = assessImgUrl;
	}
	
	public List<String> getTodayFreeTimes() {
		return todayFreeTimes;
	}

	public void setTodayFreeTimes(List<String> todayFreeTimes) {
		this.todayFreeTimes = todayFreeTimes;
	}

	public String getCharge_account() {
		return charge_account;
	}

	public void setCharge_account(String charge_account) {
		this.charge_account = charge_account;
	}
	
	public String getMoodImg() {
		return moodImg;
	}

	public void setMoodImg(String moodImg) {
		this.moodImg = moodImg;
	}

	public String getPresentation() {
		return presentation;
	}

	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}

	public String getLastMonthMoodUrl() {
		return lastMonthMoodUrl;
	}

	public void setLastMonthMoodUrl(String lastMonthMoodUrl) {
		this.lastMonthMoodUrl = lastMonthMoodUrl;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public Integer getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(Integer currentMonth) {
		this.currentMonth = currentMonth;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getWeichat_type() {
		return weichat_type;
	}

	public void setWeichat_type(String weichat_type) {
		this.weichat_type = weichat_type;
	}

}
