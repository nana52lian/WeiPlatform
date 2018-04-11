package com.yidatec.weixin.quartz;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.mail.MailQueue;
import com.yidatec.weixin.mail.SimpleMessageSender;

/**
 * 发送邮件计划任务
 * @author Lance
 *
 */
public class MailQuartz {

	private static final Logger log = LogManager.getLogger(MailQuartz.class);
	
	private SimpleMessageSender messageSender = null;
	
	public void setMessageSender(SimpleMessageSender messageSender) {
		this.messageSender = messageSender;
	}
	
	public void work() {		
		try {
			while(MailQueue.Count() > 0) {
				messageSender.sendMail(MailQueue.getMailFromQueue());
				Thread.sleep(100);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
}
