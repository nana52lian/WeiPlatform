package com.yidatec.weixin.mail;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 发邮件用
 * @author Lance
 *
 */
public class MailEntity {

	private String from = null;
	
	private String[] to = null;
	
	private String[] cc = null;

	private String[] bcc = null;
	
	private String subject = null;
	
	private String content = null;	
	
	private boolean isHtml = true;
	
	private String mailRegex = "\\w+@(\\w+.)+[a-z]{2,3}";
	
	private Pattern mailRegexP = Pattern.compile(this.mailRegex); 
	
	public boolean hasFrom() {
		return from != null && from != "";
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String[] getTo() {
		return to;
	}

	public void setTo(String[] to) {
		this.to = to;
	}
	
	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean hasCc() {
		return cc != null && cc.length > 0;
	}

	public boolean hasBcc() {
		return bcc != null && bcc.length > 0;
	}

	public boolean isHtml() {
		return isHtml;
	}

	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}
	
	public boolean isValidFrom() {
		return isValidMail(from);
	}
	
    public boolean isValidTo() {
    	boolean re = true;
    	if (to == null || to.length == 0) {
    		return false;
    	}
    	for (String mail : to) {
    		if (!isValidMail(mail)) {
    			re = false;
    			break;
    		}
    	}
    	return re;
	}
    
    public boolean isValidCc() {
    	boolean re = true;
    	if (cc == null || cc.length == 0) {
    		return false;
    	}
    	for (String mail : cc) {
    		if (!isValidMail(mail)) {
    			re = false;
    			break;
    		}
    	}
    	return re;
	}
    
    public boolean isValidBcc() {
    	boolean re = true;
    	if (bcc == null || bcc.length == 0) {
    		return false;
    	}
    	for (String mail : bcc) {
    		if (!isValidMail(mail)) {
    			re = false;
    			break;
    		}
    	}
    	return re;
	}
	
	private boolean isValidMail(String mail) {
		if (mail != null && mail.length() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
}
