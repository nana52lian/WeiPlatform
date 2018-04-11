package com.yidatec.weixin.util;

import java.util.Properties;


public class PropertiesUtil {

	/**
	 * 配置参数
	 */
	public static Properties ppsConfig;	
	
	public static Properties getPpsConfig() {
		return ppsConfig;
	}

	public static void setPpsConfig(Properties ppsConfig) {
		PropertiesUtil.ppsConfig = ppsConfig;
	}
	
}
