package com.yidatec.weixin.message;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.security.core.session.SessionRegistry;

import com.yidatec.weixin.common.ParamsConfig;
import com.yidatec.weixin.dao.DataBase.SerialNoType;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.message.InMessage;
import com.yidatec.weixin.entity.message.OutMessage;
import com.yidatec.weixin.entity.message.ServiceMessage;
import com.yidatec.weixin.entity.message.TaskQueueEntity;
import com.yidatec.weixin.entity.message.TaskStatus;
import com.yidatec.weixin.entity.message.UserType;
import com.yidatec.weixin.helper.MessageHelper;
import com.yidatec.weixin.util.PropertiesUtil;

/**
 * 客服消息处理
 * @author Lance
 *
 */
public class MessageHandle{
	
    private static final Logger log = LogManager.getLogger(MessageHandle.class);
	
	MessageDao messageDao = null;

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}
	
    SessionRegistry sessionRegistry;  
	
	public void setSessionRegistry(SessionRegistry sessionRegistry) {
		this.sessionRegistry = sessionRegistry;
	}

	/**
	 * 客服点击咨询后消息处理
	 * @param inMsg
	 * @param outMsg
	 * @return
	 */
	public OutMessage addMessage(InMessage inMsg,OutMessage outMsg,SerialNoType syncSerialNoType){
		try{
			
				//判断当前发消息用户是否有未处理 、正在处理的任务			
				List<TaskQueueEntity> list_task = messageDao.getTaskQueue(inMsg.getFromUserName());
				//size等于0，代表任务表里没有相关人的数据或者都是完成状态（状态为3）
				if(list_task.size() == 0){
					//查询微信用户月度已使用次数
					int size = messageDao.getTaskQueueOfMonthByOpenID(inMsg.getFromUserName()).size();
					//微信用户小于月度最大使用次数
					if(size < Integer.valueOf(ParamsConfig.getParams().get("month_max_count").getParam_value())){
						//取得所有在线人员
						List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
						if(allPrincipals.size()>0){
							//最大咨询数
							boolean maxCount = true;
							//*******************************************循环在线人员**************************************************
							for (int i=0; i<allPrincipals.size(); i++) {
								Object obj = allPrincipals.get(i);			
								if (obj instanceof PlatformUserEntity){
									PlatformUserEntity user = (PlatformUserEntity)obj;
									//如果该工程师不繁忙
									if(messageDao.getPlatfromBusyStatus(user.getId()) == 0){
										//如果是管理员，弹出“当前没有咨询师”
										if (user.getType() == UserType.ADMIN){	
												outMsg.setContent(MessageHelper.getInstance().getMessage("SERVICE_DATE_MESSAGE"));
										} else {//如果不是管理员
											//取得某咨询师当前咨询数（不包括完成和挂起的咨询）
											List<TaskQueueEntity> count = new ArrayList<TaskQueueEntity>();
											count = messageDao.getTaskQueueCountByServiceID(user.getId());
											//如果该咨询师被咨询数小于最大队列数
											if(count.size() < Integer.valueOf(ParamsConfig.getParams().get("queue_max_count").getParam_value())){
												//判断咨询师最大次数的flag
												maxCount = false;
												//*************************************************进行分配******************************
												
												int num = messageDao.getUnAllotPerson();
												if(num==0)
													num = 1;
												
												//增加任务队列关系 并取得任务队列ID
												String str = messageDao.addTaskQueue(inMsg,syncSerialNoType);
												if("0".equals(str)){
														outMsg.setContent(MessageHelper.getInstance().getMessage("SERVICE_DATE_MESSAGE"));
													break;
												}
												// 您的消息我们已经收到，请等待处理。
//											String ipAndPort =PropertiesUtil.ppsConfig.getProperty("SERVER_IP") + ":" + PropertiesUtil.ppsConfig.getProperty("SERVER_PORT");
//											String page_url = ipAndPort + "/weixin!getContent?ArticleID=4e941b4e-71fa-4365-9480-f9681d20ea33";
													outMsg.setContent(MessageHelper.getInstance().getMessage("UN_PROCESS_MESSAGE", new String[]{ String.valueOf(num)}));
												break;
											} else {//如果该咨询师被咨询数大于等于最大队列数
												//不处理
											}
										}
									} else {//如果工程师繁忙
										//没有咨询师在线
											outMsg.setContent(MessageHelper.getInstance().getMessage("SERVICE_DATE_MESSAGE"));
									}
								}
							}
							if(maxCount){//如果所有咨询师的被咨询数都大于等于最大队列数
									outMsg.setContent(MessageHelper.getInstance().getMessage("SERVICE_DATE_MESSAGE"));
							}
						} else {
							//没有咨询师在线
								outMsg.setContent(MessageHelper.getInstance().getMessage("SERVICE_DATE_MESSAGE"));
						}
					} else {
						//微信用户大于等于月度最大使用次数
							outMsg.setContent(MessageHelper.getInstance().getMessage("MONTH_MAX_COUNT"));
					}
				} 
//				else {
					//outMsg.setContent(MessageHelper.getInstance().getMessage("EXIST_TASK_MESSAGE"));
//					outMsg.setContent("20141112s");
//				}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
		return outMsg;
	}
	
	/**
	 * 客服消息处理(恶意回复或客户文本消息)
	 * @param inMsg
	 * @param outMsg
	 * @return
	 */
	public void modifyTaskStatus(InMessage inMsg,OutMessage outMsg){
		try{
			String task_queue_id = "";
			String service_id = "";
			//判断当前发消息用户是否有未处理 、正在处理的任务			
			List<TaskQueueEntity> list_task = messageDao.getTaskQueue(inMsg.getFromUserName());
			if(list_task.size() == 0) return;//恶意回复不处理（未触发点击）
			for(int i=0;i<list_task.size();i++){
				//如果任务是挂起的状态
				if (list_task.get(i).getTask_status() == TaskStatus.WAIT){
					//更新任务状态
					int result = messageDao.modifyTaskStatus(TaskStatus.PROCESSING,list_task.get(i).getId());
					//发送激活消息
					if(result>0){
						WeixinHelper helper = new WeixinHelper();
						ServiceMessage message = new ServiceMessage();
						message.setTouser(list_task.get(i).getOpen_id());
						String str = MessageHelper.getInstance().getMessage("ACTIVATION_MESSAGE");
						message.setText(str);
						//在消息表中插入一条数据
						messageDao.addSystemMessage(list_task.get(i),str);
						SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), message);					}
				}
				//取得任务队列ID
				task_queue_id = list_task.get(i).getId();
				service_id = list_task.get(i).getService_id();
				//增加消息
				messageDao.addMessage(inMsg, task_queue_id, service_id);
			}
		}catch (Exception e){
			log.error(e.getMessage(), e);
		}
	}
}
