package com.yidatec.weixin.helper;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.AbstractMessageSource;

/**
 * 获取messages.properties文件中的消息
 * @author Lance
 *
 */
public class MessageHelper {

	// spring 的消息管理
	private AbstractMessageSource messageSource;
	
	private static MessageHelper instance = new MessageHelper();
	
	private MessageHelper() {
        messageSource = null;
    }
	
	public MessageHelper(AbstractMessageSource messageSource) {        
        this.messageSource = null;
        instance.setMessageSource(messageSource);
    }

	public void setMessageSource(AbstractMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	public static MessageHelper getInstance() {
        return instance;
    }
	
	public String getMessage(String msgId) {		
		return this.getMessage(msgId, null);       
    }
	
	public String getMessage(String msgId, String args[]) {		
		return messageSource.getMessage(msgId, args, LocaleContextHolder.getLocale());       
    }
}
