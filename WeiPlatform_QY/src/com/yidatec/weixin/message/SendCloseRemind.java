package com.yidatec.weixin.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.common.ParamsConfig;
import com.yidatec.weixin.dao.WeixinDao;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.message.ServiceMessage;
import com.yidatec.weixin.entity.message.TaskDisplayEntity;
import com.yidatec.weixin.helper.MessageHelper;
import com.yidatec.weixin.util.CommonMethod;
import com.yidatec.weixin.util.PropertiesUtil;

public class SendCloseRemind {
	
	private static final Logger log = LogManager.getLogger(SendCloseRemind.class);
	
	WeixinDao weixinDao;

	public void setWeixinDao(WeixinDao weixinDao) {
		this.weixinDao = weixinDao;
	}

	public void SendClose(){
		try {
			//挂起的列表
			List<TaskDisplayEntity> taskDisplayList = weixinDao.getAllWaitAdvisory();
			for(int i=0;i<taskDisplayList.size();i++){
				//屏蔽关闭提醒消息
				  if(StringUtils.isNotEmpty(taskDisplayList.get(i).getService_id())){
					//和系统时间进行比较
					int count = CommonMethod.dateSubtractForMinute(taskDisplayList.get(i).getMessage_create_date());
					//最大挂起时间转成INT类型
					int max_wait = Integer.valueOf(ParamsConfig.getParams().get("max_timeout").getParam_value());
					if(count < max_wait * 60){
						//最大挂起时间之前的关闭提醒小时数（设置时要小于最大挂起时间）
						int max_before = Integer.valueOf(ParamsConfig.getParams().get("max_timeout_before").getParam_value());
						//未到达最大挂起时间,但是等于提醒时间,发送消息给微信端
						/*if((max_wait * 60 - count) == max_before * 60){*/
						int now = (max_wait * 60 - count);
						if(max_before * 60 == now){
							WeixinHelper helper = new WeixinHelper();
							ServiceMessage message = new ServiceMessage();
							message.setTouser(taskDisplayList.get(i).getOpen_id());
							String str = MessageHelper.getInstance().getMessage("CLOSE_REMIND_MESSAGE");
							//根据OPENID查找是哪个微信公众平台
							List<Map<String,Object>> appTypeList = weixinDao.getTaskQueueByOpenID(taskDisplayList.get(i).getOpen_id());
//							if("az_en".equals(appTypeList.get(0).get("weichat_type").toString())){
//								str = MessageHelper.getInstance().getMessage("CLOSE_REMIND_MESSAGE_EN");
//							}
							message.setText(str);
							Map<String, Object> taskQueue = new HashMap<String, Object>();
							taskQueue.put("openID", taskDisplayList.get(i).getOpen_id());
							taskQueue.put("task_queue_id", taskDisplayList.get(i).getId());
							taskQueue.put("service_id", "");
							List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
							mapList.add(taskQueue);
							//在消息表中插入一条数据
							weixinDao.addSystemMessage(mapList.get(0),str);
							//给客户端发消息
							//迁移了helper.sendMessageByLand(message,appTypeList.get(0).get("weichat_type").toString());
//							helper.sendMessageForAZ(message.getTouser(), str);
							SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), message);
						}
					}
				  }
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		
	}

}
