package com.yidatec.weixin.message;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * 用户24小时内没有反馈，发送请求已被关闭的消息
 * @author tony
 *
 */
public class SendCloseMessageQuartz {
	
	private static final Logger log = LogManager.getLogger(SendCloseMessageQuartz.class);
	
	SendCloseMessage sendCloseMessage;
	
	public void setSendCloseMessage(SendCloseMessage sendCloseMessage) {
		this.sendCloseMessage = sendCloseMessage;
	}

	public void work() {
		try {
			sendCloseMessage.SendClose();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
