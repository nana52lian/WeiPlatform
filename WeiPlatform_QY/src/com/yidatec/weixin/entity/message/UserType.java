package com.yidatec.weixin.entity.message;

public class UserType {
	
	/** 管理员  */
	public final static int ADMIN = 1;
	
	/** 标准客服，需要自动分配任务  */
	public final static int SERVICE_STANDARD = 2;
	
	/** 业余客服，自主拉取任务  */
	public final static int SERVICE_AMATEUR = 3;

}
