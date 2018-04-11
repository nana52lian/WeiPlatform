package com.yidatec.weixin.message;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.yidatec.weixin.dao.WeixinDao;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.message.ServiceMessage;
import com.yidatec.weixin.entity.message.TaskQueueEntity;
import com.yidatec.weixin.entity.message.TaskStatus;
import com.yidatec.weixin.helper.MessageHelper;
import com.yidatec.weixin.util.PropertiesUtil;

public class SendCloseMessage {
	
	private static final Logger log = LogManager.getLogger(SendCloseMessage.class);
	
	WeixinDao weixinDao;

	public void setWeixinDao(WeixinDao weixinDao) {
		this.weixinDao = weixinDao;
	}

	public void SendClose(){
		try {
			//获取所有超过规定时间而没有反馈的请求单
			List<TaskQueueEntity> list_task = weixinDao.getCloseResquest();
			if(list_task != null){
				for(int i= 0;i<list_task.size();i++){
					if(weixinDao.setStatusAuto(TaskStatus.COMPLETED,list_task.get(i).getId())>1){
						WeixinHelper helper = new WeixinHelper();
						ServiceMessage message = new ServiceMessage();
						message.setTouser(list_task.get(i).getOpen_id());
						//根据OPENID查找是哪个微信公众平台
						List<Map<String,Object>> appTypeList = weixinDao.getTaskQueueByOpenID(list_task.get(i).getOpen_id());
//						if("az_en".equals(appTypeList.get(0).get("weichat_type").toString())){
//							message.setText(MessageHelper.getInstance().getMessage("CLOSE_MESSAGE_EN"));
//						} else {
							message.setText(MessageHelper.getInstance().getMessage("CLOSE_MESSAGE"));
//						}
						//给客户端发消息
						//helper.sendMessageByLand(message,appTypeList.get(0).get("weichat_type").toString());
//						helper.sendMessageForAZ(message.getTouser(), message.getText().get("content").toString());
						SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), message);
					}
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
	}

}
