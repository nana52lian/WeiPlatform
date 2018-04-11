package com.yidatec.weixin.mail;

import java.util.LinkedList;
import java.util.Queue;


public class MailQueue {

	// 邮件序列
	private static Queue<MailEntity> mailQueue = new LinkedList<MailEntity>();
	
	/**
	 * 将要发送的邮件加入到序列
	 * @param mailMsg
	 */
	public static boolean addMailToQueue(MailEntity mail) {
		synchronized(mailQueue){
			return mailQueue.add(mail);
		}
    }
	
	/**
	 * 从队列中取邮件
	 * @return
	 */
	public static MailEntity getMailFromQueue() {
		synchronized(mailQueue){
			return mailQueue.poll();
		}
    }
	
	/**
	 * 返回邮件数量
	 * @return
	 */
	public synchronized static int Count() {
		synchronized(mailQueue){
			return mailQueue.size();
		}
    }
}
