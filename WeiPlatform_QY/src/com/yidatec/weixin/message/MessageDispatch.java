package com.yidatec.weixin.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.yidatec.weixin.common.ParamsConfig;
import com.yidatec.weixin.dao.WeixinDao;
import com.yidatec.weixin.entity.message.OnlineCustomerServiceEntity;
import com.yidatec.weixin.entity.message.ServiceMessage;
import com.yidatec.weixin.entity.message.TaskQueueEntity;
import com.yidatec.weixin.entity.message.TaskStatus;
import com.yidatec.weixin.helper.MessageHelper;
import com.yidatec.weixin.service.WeixinService;
import com.yidatec.weixin.util.PropertiesUtil;

/**
 * 处理消息分发逻辑
 * @author Lance
 *
 */
public class MessageDispatch {
	private static final Logger log = LogManager.getLogger(MessageDispatch.class);

	MessageDao messageDao = null;
	
	private WeixinDao weixinDao = null;
	
	List<OnlineCustomerServiceEntity> onlineCustomerService = null;
	
	private int task_count;

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}
	
	public void setWeixinDao(WeixinDao weixinDao) {
		this.weixinDao = weixinDao;
	}
	
	/**
	 * 分配方法
	 */
	public void dispatchTaskQueue(List<Object> allPrincipals){
		if (allPrincipals.size() == 0) return;
		List<TaskQueueEntity> taskQueueList = new ArrayList<TaskQueueEntity>();
		try {
			// 获取当前在线的客服
			onlineCustomerService = messageDao.getCustomerService(allPrincipals);
			if (onlineCustomerService.size() == 0) return;
			// 获取等待处理的任务列表 
			taskQueueList = messageDao.getTaskQueueByStatus(TaskStatus.UN_PROCESS);
			if (taskQueueList.size() == 0) return;			
			List<TaskQueueEntity> list_update_task = new ArrayList<TaskQueueEntity>();
			OnlineCustomerServiceEntity service = null;
			TaskQueueEntity task = null;
			for(int i=0;i<taskQueueList.size();i++){
				//从英文号发出的数据要先交给英文客服处理
//				if(taskQueueList.get(i).getWeichat_type().equals("az_en")){
//					//获取当前任务最少的那个客服
//					service = this.getMinTaskServiceForEN();
//					//如果没有英文工程师在线
//					if(service.getTask_count() == 1000){
//						onlineCustomerService.get(0).setTask_count(task_count);
//						continue;
//					} else {
//						//如果最小的客服已经满负荷
//						if(service.getTask_count() >= Integer.valueOf(ParamsConfig.getParams().get("queue_max_count").getParam_value())){
//							continue;
//						} else {
//							task = new TaskQueueEntity();
//							task.setId(taskQueueList.get(i).getId());
//							task.setOpen_id(taskQueueList.get(i).getOpen_id());//为了给微信客服发消息用
//							task.setService_id(service.getId());
//							task.setService_account(service.getAccount());//为了给微信客服发消息用
//							task.setName(service.getName());//为了给微信客服发消息用
//							list_update_task.add(task);
//							service.setTask_count(service.getTask_count() + 1);
//						}
//					}
//				} else {
					//获取当前任务最少的那个客服
					service = this.getMinTaskService();
					//如果最小的客服已经满负荷
					if(service.getTask_count() >= Integer.valueOf(ParamsConfig.getParams().get("queue_max_count").getParam_value())){
						break;
					} else {
						task = new TaskQueueEntity();
						task.setId(taskQueueList.get(i).getId());
						task.setOpen_id(taskQueueList.get(i).getOpen_id());//为了给微信客服发消息用
						task.setService_id(service.getId());
						task.setService_account(service.getAccount());//为了给微信客服发消息用
						task.setName(service.getName());//为了给微信客服发消息用
						list_update_task.add(task);
						service.setTask_count(service.getTask_count() + 1);
					}
				}
//			}
			// 没有需要分配的任务就退出
			if (list_update_task.size() == 0) return;
			//更新任务队列
			messageDao.ModifyCustomerServiceTaskCount(list_update_task);
			//更新消息列表
			messageDao.ModifyTaskMessage(list_update_task);
			//给微信客户发送分配的客服ID的消息
			sendServiceMessage(list_update_task);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 获取当前任务最少的那个客服
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private OnlineCustomerServiceEntity getMinTaskService() {
		Collections.sort(onlineCustomerService, new Comparator() {
			 public int compare(Object arg0, Object arg1) {
				 int task_count0 = ((OnlineCustomerServiceEntity)arg0).getTask_count();
				 int task_count1 = ((OnlineCustomerServiceEntity)arg1).getTask_count();
				 int res = 0;
				 if (task_count0 > task_count1) {
					 res = 1;
				 } else if (task_count0 < task_count1) {
					 res = -1;
				 }						 
				 return res;
			 }
		});
		return getFinalService(onlineCustomerService);
	}
	
	/**
	 * 获取当前任务最少的那个客服
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private OnlineCustomerServiceEntity getMinTaskServiceForEN() {
		//先从在线客服中筛选出英文客服
		List<OnlineCustomerServiceEntity> onlineCustomerServiceForEN = new ArrayList<OnlineCustomerServiceEntity>();
//		for(int x=0;x<onlineCustomerService.size();x++){
			//英文号
//			if("az_en".equals(onlineCustomerService.get(x).getWeichat_type())){
//				onlineCustomerServiceForEN.add(onlineCustomerService.get(x));
//			}
//		}
		//
		OnlineCustomerServiceEntity returnService = new OnlineCustomerServiceEntity();
		//如果没有英文客服在线
		if(onlineCustomerServiceForEN.size()>0){
			Collections.sort(onlineCustomerServiceForEN, new Comparator() {
				 public int compare(Object arg0, Object arg1) {
					 int task_count0 = ((OnlineCustomerServiceEntity)arg0).getTask_count();
					 int task_count1 = ((OnlineCustomerServiceEntity)arg1).getTask_count();
					 int res = 0;
					 if (task_count0 > task_count1) {
						 res = 1;
					 } else if (task_count0 < task_count1) {
						 res = -1;
					 }						 
					 return res;
				 }
			});
			//有英文工程师在线进行平均分配
			returnService = getFinalService(onlineCustomerServiceForEN);
		} else {
			task_count = onlineCustomerService.get(0).getTask_count();
			//为了返回无英文工程师在线把这个设置成最大值
			onlineCustomerService.get(0).setTask_count(1000);
			returnService = onlineCustomerService.get(0);
		}
		return returnService;
	}

	/**
	 * 查找要分配的客服
	 * @param onlineCustomerService
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public OnlineCustomerServiceEntity getFinalService(List<OnlineCustomerServiceEntity> onlineCustomerService) {
		//如果最小的几个的数量相同，先用比较取得跟最小客服相同处理数量的那几个客服
		List<OnlineCustomerServiceEntity> onlineCustomerServiceForSameCount = new ArrayList<OnlineCustomerServiceEntity>();
		for(int y=0;y<onlineCustomerService.size();y++){
			if(onlineCustomerService.get(0).getTask_count() == onlineCustomerService.get(y).getTask_count()){
				onlineCustomerServiceForSameCount.add(onlineCustomerService.get(y));
			}
		}
		/*
		此处处理要考虑6种情况。例如三个状态为0或1或2相同数量请求单的在线客服。
		第一种情况   状态为0或1或2的数量为  ：0 0 0  状态为3或4或5的数量为  ：0 0 0
		第二种情况   状态为0或1或2的数量为  ：0 0 0  状态为3或4或5的数量为  ：1 1 1
		第三种情况   状态为0或1或2的数量为  ：0 0 0  状态为3或4或5的数量为  ：1 2 3
		第四种情况   状态为0或1或2的数量为  ：1 1 1  状态为3或4或5的数量为  ：0 0 0
		第五种情况   状态为0或1或2的数量为  ：1 1 1  状态为3或4或5的数量为  ：1 1 1
		第六种情况   状态为0或1或2的数量为  ：1 1 1  状态为3或4或5的数量为  ：1 2 3
		以及其他情况
		*/
		//得到的客服列表（相同处理数量），如果这些客服当天没有分配过请求单，就无序排列，
		//如果有找到该客服状态不是3，4，5的最后的请求单，根据创建时间找到最先创建的那个客服
		List<Map<String, Object>> finalservice = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> serviceForTask = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> serviceForModifyDate = new ArrayList<Map<String, Object>>();
		/*List<Map<String, Object>> countList = new ArrayList<Map<String, Object>>();*/
		OnlineCustomerServiceEntity onlineCustomerServiceEntity = new OnlineCustomerServiceEntity();
		boolean flag = false;
		//循环最小客服相同处理数量的那几个客服（状态为0,1,2,数量相同）
		for(int x=0;x<onlineCustomerServiceForSameCount.size();x++){
			try {
				//查找客服当天是否被分配过
				serviceForTask = messageDao.getCustomerServiceTask(onlineCustomerServiceForSameCount.get(x).getId());
				//如果被分配过
				if(serviceForTask.size()>0){//（除第一种情况外的所有情况）
					flag = true;
					//查找该客服处理结束更新时间“最晚”的请求单
					serviceForModifyDate = messageDao.getFinalCustomerService(onlineCustomerServiceForSameCount.get(x).getId());
					//如果有
					if(serviceForModifyDate.size()>0){//第四种以上情况
						finalservice.add(serviceForModifyDate.get(0));
					} else {
						//如果没有处理完毕的请求单,不处理。
						
						
						//如果没有处理完毕的请求单，直接分给这个人
						/*flag = false;
						onlineCustomerServiceEntity = onlineCustomerServiceForSameCount.get(x);
						break;*/
					}
					/*flag = true;
					//根据ID查找当天被分配的数量
					countList = messageDao.getTaskQueueByServiceID(onlineCustomerServiceForSameCount.get(x).getId());
					finalservice.add(countList.get(0));*/
				} else {
					//如果没被分配过直接分给他（第一种情况）
					flag = false;
					onlineCustomerServiceEntity = onlineCustomerServiceForSameCount.get(x);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//（除第一种情况外的所有情况）,时间比较
		if(flag){
			if(finalservice.size()>0){
				//更新时间最早的
				Collections.sort(finalservice, new Comparator() {
					 public int compare(Object arg0, Object arg1) {
						 String modify_date0 = ((Map<String, Object>)arg0).get("modify_date").toString();
						 String modify_date1 = ((Map<String, Object>)arg1).get("modify_date").toString();
						 return modify_date0.compareTo(modify_date1);				 
					 }
				});
				/*Collections.sort(finalservice, new Comparator() {
				 public int compare(Object arg0, Object arg1) {
					 int task_count0 = Integer.valueOf(((Map<String, Object>)arg0).get("task_count").toString()) ;
					 int task_count1 = Integer.valueOf(((Map<String, Object>)arg1).get("task_count").toString());
					 int res = 0;
					 if (task_count0 > task_count1) {
						 res = 1;
					 } else if (task_count0 < task_count1) {
						 res = -1;
					 }						 
					 return res;
				 }
			});*/
				//为了找出相关数据
				for(int n=0;n<onlineCustomerServiceForSameCount.size();n++){
					//最终客服进行匹配数据
					if(onlineCustomerServiceForSameCount.get(n).getId().equals(finalservice.get(0).get("service_id"))){
						onlineCustomerServiceEntity = onlineCustomerServiceForSameCount.get(n);
						break;
					}
				}
			} else {
				//如果所有人都没有处理完的情况下，要随机分配。
				onlineCustomerServiceEntity = onlineCustomerServiceForSameCount.get(0);
			}
		}
		return onlineCustomerServiceEntity;
	}
	
	/**
	 * 给微信客户端发送消息
	 * @param reqEvent
	 * @return
	 * @throws Exception
	 */
	public void sendServiceMessage(List<TaskQueueEntity> list_update_task) throws Exception {
		WeixinHelper helper = new WeixinHelper();
		ServiceMessage message = new ServiceMessage();
		for(int i=0;i<list_update_task.size();i++){
			//装载给微信客户端发的消息
			message.setTouser(list_update_task.get(i).getOpen_id());
			String ipAndPort =PropertiesUtil.ppsConfig.getProperty("SERVER_IP") + ":" + PropertiesUtil.ppsConfig.getProperty("SERVER_PORT");
//			String url = "<a href=\""+ ipAndPort +"/WeiPlatform_QY/weixin!getServiceDetail?account="+list_update_task.get(i).getService_account()+"\">"+list_update_task.get(i).getName()+"</a>";
			//根据OPENID查找是哪个微信公众平台
			List<Map<String,Object>> appTypeList = weixinDao.getTaskQueueByOpenID(list_update_task.get(i).getOpen_id());
			String url = list_update_task.get(i).getName();
			String str = MessageHelper.getInstance().getMessage("SERVICE_MESSAGE",new String[] { url });
			message.setText(str);
			//在消息表中插入一条数据
			messageDao.addSystemMessage(list_update_task.get(i),str);
//			SendMessage2Takeda.testPost(PropertiesUtil.ppsConfig.getProperty("TAKEDA_URL"), message);
		}
	}

	public int getTask_count() {
		return task_count;
	}

	public void setTask_count(int task_count) {
		this.task_count = task_count;
	}
	
	/**
	 * 根据ID查找客服繁忙状态
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int getPlatfromBusyStatus(String id) throws Exception {
		return messageDao.getPlatfromBusyStatus(id);
	}
}
