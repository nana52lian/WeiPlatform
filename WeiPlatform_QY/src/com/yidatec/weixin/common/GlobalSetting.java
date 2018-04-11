package com.yidatec.weixin.common;

/**
 * 网站设定
 * @author Lance
 *
 */
public class GlobalSetting {
	
	/**
	 * 全职员工一级队列长
	 */
	private static String full_time_first_long = "";

	public static String getFull_time_first_long() {
		return full_time_first_long;
	}

	public static void setFull_time_first_long(String full_time_first_long) {
		GlobalSetting.full_time_first_long = full_time_first_long;
	}
	
}
