package com.yidatec.weixin.quartz;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.yidatec.weixin.message.BackQueueMessage;

public class BackOldTaskQueueMessageDataQuartz {
	private static final Logger log = LogManager.getLogger(BackOldTaskQueueMessageDataQuartz.class);
	
	private BackQueueMessage backQueueMessage = null;


	public void setBackQueueMessage(BackQueueMessage backQueueMessage) {
		this.backQueueMessage = backQueueMessage;
	}

//	public void work() {		
//		try {
//			backQueueMessage.BackQueueMessageall();
//		} catch (Exception e) {
//			log.error(e.getMessage());
//		}
//	}
}
