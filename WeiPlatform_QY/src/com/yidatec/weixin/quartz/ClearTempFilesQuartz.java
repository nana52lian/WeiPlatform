package com.yidatec.weixin.quartz;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.util.CommonMethod;


public class ClearTempFilesQuartz {
	
	private static final Logger log = LogManager.getLogger(ClearTempFilesQuartz.class);	

	public void work() {
		try {
			CommonMethod.delAllFile(null);
			Thread.sleep(100);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
