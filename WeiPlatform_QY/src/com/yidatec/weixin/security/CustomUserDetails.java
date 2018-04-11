package com.yidatec.weixin.security;

import org.springframework.security.core.userdetails.UserDetails;

/**
 *实现了UserDetails，扩展几项信息，比如getSubSystem()方法筄1�7
 */
public interface CustomUserDetails extends UserDetails {

	//用户id
	public String getUserId();

	//用户账户
	public String getUserAccount();
	
	//用户密码
	public String getUserPassword();

	//用户各1�7
	public String getUserName();

	//用户邮箱
	public String getUserMail();

	//用户是否能用
	public int getEnabled();

	//是否超级用户
	public int getIssys();
}
