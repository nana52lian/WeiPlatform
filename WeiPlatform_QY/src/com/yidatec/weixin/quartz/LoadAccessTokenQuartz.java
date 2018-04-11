package com.yidatec.weixin.quartz;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.message.WeixinHelper;


public class LoadAccessTokenQuartz {
	
	private static final Logger log = LogManager.getLogger(LoadAccessTokenQuartz.class);	

	public void work() {
		try {
			WeixinHelper msgHelper = new WeixinHelper();
			WeixinHelper.ACCESS_TOKEN = msgHelper.getAccessToken();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
