package com.yidatec.weixin.entity.message;

public class TaskDisplayEntity {

	//任务ID
	private String id;
	//客户ID
	private String open_id;
	//客服ID
    private String service_id;	
	//任务创建时间
	private String create_date;
	//用户昵称
	private String nickname;
	//任务状态
	private String task_status;
	//任务最后消息
	private String content;
	//消息类型
	private String msg_type;
	//评价
	private String service_score;
	//Rid
	private String rid;
	//K账号
	private String key_account;
	
	private String create_user;
	
	private String weichat_type;
	
	//请求类型
	private String type;
	//历时
	private String last;
	//用户知识评级分数
	private String custom_score;
	//客服姓名
	private String service_name;
	//提醒flag
	private String remindFlag = "0";
	
	
	private String id1;
	

	
	public String getId1() {
		return id1;
	}

	public void setId1(String id1) {
		this.id1 = id1;
	}

	public String getCreate_user() {
		return create_user;
	}

	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}

	public String getRemindFlag() {
		return remindFlag;
	}

	public void setRemindFlag(String remindFlag) {
		this.remindFlag = remindFlag;
	}

	//挂起超过最大数量弹出flag
	private String maxAlterFlag;
	//消息说话时间
	private String message_create_date;
	
	public String getMessage_create_date() {
		return message_create_date;
	}

	public void setMessage_create_date(String message_create_date) {
		this.message_create_date = message_create_date;
	}

	public String getMaxAlterFlag() {
		return maxAlterFlag;
	}

	public void setMaxAlterFlag(String maxAlterFlag) {
		this.maxAlterFlag = maxAlterFlag;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

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

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getTask_status() {
		return task_status;
	}

	public void setTask_status(String task_status) {
		this.task_status = task_status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsg_type() {
		return msg_type;
	}

	public void setMsg_type(String msg_type) {
		this.msg_type = msg_type;
	}

	public String getService_score() {
		return service_score;
	}

	public void setService_score(String service_score) {
		this.service_score = service_score;
	}

	public String getRid() {
		return rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public String getKey_account() {
		return key_account;
	}

	public void setKey_account(String key_account) {
		this.key_account = key_account;
	}

	public String getService_name() {
		return service_name;
	}

	public void setService_name(String service_name) {
		this.service_name = service_name;
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
