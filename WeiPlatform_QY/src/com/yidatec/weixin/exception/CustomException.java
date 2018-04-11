package com.yidatec.weixin.exception;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.helper.MessageHelper;

public class CustomException extends Exception {

	private static final Logger log = LogManager.getLogger(CustomException.class);
	
	private String err_code = null;

	public String getErr_code() {
		return err_code;
	}

	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}

	public CustomException() {
		super();
	}

	/**
	 * @param error_id 错误代码
	 * errorMsg.properties文件中保存的错误消息
	 */
	public CustomException(String error_id) {		
		super(MessageHelper.getInstance().getMessage(error_id));
		this.err_code = error_id;
		log.error("Error code:[" + error_id + "], Message:[" + MessageHelper.getInstance().getMessage(error_id) + "]");
	}
	
	/**
	 * @param error_id 错误代码
	 * errorMsg.properties文件中保存的错误消息
	 */
	public CustomException(String error_id, String user_account) {		
		super(MessageHelper.getInstance().getMessage(error_id));
		log.error("User account:[" + user_account + "], Error code:[" + error_id + "], Message:[" + MessageHelper.getInstance().getMessage(error_id) + "]");
	}

	/**
	 * @param error_id 错误代码
	 * errorMsg.properties文件中保存的错误消息
	 */
	public CustomException(String error_id, Throwable cause) {
		super(MessageHelper.getInstance().getMessage(error_id), cause);
		log.error("Error code:[" + error_id + "], Message:[" + MessageHelper.getInstance().getMessage(error_id) + "]");
	}

	public CustomException(Throwable cause) {
		super(cause);
		
	}

}
