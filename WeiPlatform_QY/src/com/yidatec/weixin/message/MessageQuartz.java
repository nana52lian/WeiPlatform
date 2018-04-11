package com.yidatec.weixin.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.session.SessionRegistry;

import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.message.UserType;

/**
 * 客服消息计划任务，负责进行任务分发
 * @author Lance
 *
 */
public class MessageQuartz {
	
    private static final Logger log = LogManager.getLogger(MessageQuartz.class);	
	
	MessageDispatch messageDispatch;
	
	SessionRegistry sessionRegistry;  
	
	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	public void setMessageDispatch(MessageDispatch messageDispatch) {
		this.messageDispatch = messageDispatch;
	}
	
	public void work() {
		try {
			//取得所有在线人员
			List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
			List<Object> standard = new ArrayList<Object>();
			for(int i=0;i<allPrincipals.size();i++){
				Object obj = allPrincipals.get(i);			
				if (obj instanceof PlatformUserEntity){
					//查找当前这个人的繁忙状态
					int busyStatus = messageDispatch.getPlatfromBusyStatus(((PlatformUserEntity)obj).getId());
					//只有标准客服，需要自动分配任务
					if(((PlatformUserEntity)obj).getType() == UserType.SERVICE_STANDARD && busyStatus == 0){
						standard.add(obj);
					}
				}
			}
			//执行分配方法
			messageDispatch.dispatchTaskQueue(standard);
			Thread.sleep(100);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
}
