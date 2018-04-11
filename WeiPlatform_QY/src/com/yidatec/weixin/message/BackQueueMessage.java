package com.yidatec.weixin.message;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.dao.WeixinDao;

public class BackQueueMessage {
	private static final Logger log = LogManager.getLogger(BackQueueMessage.class);
	
	WeixinDao weixinDao;

	public void setWeixinDao(WeixinDao weixinDao) {
		this.weixinDao = weixinDao;
	}
//	public void BackQueueMessageall(){
//		try {
//			int delQcount = 0;
//			int delMcount = 0;
//			int  queue = weixinDao.synBackOldTaskQueueData();
//			int  message =weixinDao.synBackOldTaskQueueMessageData();
//			if (queue > 0 && message > 0){
//				 delMcount = weixinDao.synDelTaskQueueMessageData();
//				 delQcount = weixinDao.synDelTaskQueueData();
//				
//			}
//			System.out.println("INTQ"+ queue);
//			System.out.println("INTM"+ message);
//			System.out.println("delQ"+ delQcount);
//			System.out.println("delM"+ delMcount);
//			log.info("INTQ"+ queue );
//			log.info("INTM"+ message );
//			log.info("delQ"+ delQcount );
//			log.info("delM"+ delMcount );
//			log.info("备份成功");
//		} catch (Exception e) {
//			log.error(e.getMessage(),e);
//			log.info("备份失败");
//		}
//	}
}
