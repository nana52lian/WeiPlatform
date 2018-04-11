package com.yidatec.weixin.mail;

import org.springframework.mail.javamail.JavaMailSender;

public abstract class AbstractMessageSender {

	/** To address of email */
	protected String to;

	/** From address of email */
	protected String from;

	/** subject of email */
	protected String subject;

	protected JavaMailSender sender;

	public void setSender(JavaMailSender sender) {
		this.sender = sender;
	}

	/**
     * setter methods for above variables
     */
	public void setTo(String to) {
		this.to = to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}