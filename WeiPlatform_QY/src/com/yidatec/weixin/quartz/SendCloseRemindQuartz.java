package com.yidatec.weixin.quartz;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.message.SendCloseRemind;

public class SendCloseRemindQuartz {
	
	private static final Logger log = LogManager.getLogger(SendCloseRemindQuartz.class);
	
	SendCloseRemind sendCloseRemind;

	public void setSendCloseRemind(SendCloseRemind sendCloseRemind) {
		this.sendCloseRemind = sendCloseRemind;
	}

	public void work() {
		try {
			sendCloseRemind.SendClose();
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}

}
