package com.yidatec.weixin.message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.yidatec.weixin.dao.DataBase;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.message.InMessage;
import com.yidatec.weixin.entity.message.OnlineCustomerServiceEntity;
import com.yidatec.weixin.entity.message.TaskQueueEntity;
import com.yidatec.weixin.entity.message.TaskStatus;
import com.yidatec.weixin.entity.message.UserType;

/**
 * 客服消息处理
 * @author Lance
 *
 */
public class MessageDao extends DataBase {
	
	/**
	 * 根据任务状态判断OPEN_ID是否未完成
	 * @param openID
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TaskQueueEntity> getTaskQueue(String openID) {
		String sql = " select id, open_id,service_id,task_status from " + getTableName("task_queue") 
				   + " where open_id = ? and task_status <> ? and task_status <> ?";
		return jdbcTemplate.query(sql, 
				new Object[]{ openID, TaskStatus.COMPLETED, TaskStatus.STOP },
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setId(rs.getString("id"));
				tq.setOpen_id(rs.getString("open_id"));
				tq.setService_id(rs.getString("service_id"));
				tq.setTask_status(rs.getInt("task_status"));
				return tq;
			}
		});		
	}
	
	
	/**
	 * 增加任务队列关系
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public String addTaskQueue(InMessage inMsg,SerialNoType syncSerialNoType) throws Exception {	
		String str = getSerialNo(syncSerialNoType);
		int result = 0;		
		
		String sql = "insert into "	+ getTableName("task_queue")
				   + " (id, open_id, service_id, task_status, weichat_type ,create_user, create_date)" 	
				   + " select ?,?,?,?,?,?,? "
				   + " from dual where not exists ( "
				   + " select id from wei_task_queue "
				   + " where open_id = ? and task_status <> ? ) ";
		result = jdbcTemplate.update(
						sql,
						new Object[] { 
								str,
								inMsg.getFromUserName(),
								"",
								TaskStatus.UN_PROCESS,
								inMsg.getCid(),
								inMsg.getFromUserName(),
								getCurrentDate(),
								inMsg.getFromUserName(),
								TaskStatus.COMPLETED
						}
				);
		if(result > 0){
			return str;
		} else {
			return "0";
		}
	}
	
	/**
	 * 增加消息
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int addMessage(InMessage inMsg,String task_queue_id,String service_id) throws Exception {		
		String sql = "insert into "	+ getTableName("task_message")
				   + " (id, task_queue_id, open_id, service_id, content, picurl, " 
				   + " mediaid, format, msg_type, recognition, create_user, create_date) "				  
				   + " select ?,?,?,?,?, ?,?,?,?,?, ?,?  "
				   + " from dual ";
//				   + " where not exists ( "
//				   + " select id from wei_task_message "
//				   + " where task_queue_id = ? and open_id = ? and service_id = ? and content = ? ) ";
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						getGUID(),
						task_queue_id,
						inMsg.getFromUserName(),
						service_id, // 这个消息是由客户端发送，可能有值可能没值
						inMsg.getContent(),
						inMsg.getPicUrl(),
						inMsg.getMediaId(),
						inMsg.getFormat(),
						inMsg.getMsgType(),
						inMsg.getRecognition(),
						inMsg.getFromUserName(),
						getCurrentDate()
//						, 
//						task_queue_id,
//						inMsg.getFromUserName(),
//						service_id,
//						inMsg.getContent()
				});
	}
	
	/**
	 * 插入系统消息
	 * @param taskQueue
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public int addSystemMessage(TaskQueueEntity taskQueue,String message) throws Exception {		
		String sql = "insert into "	+ getTableName("task_message")
				   + " (id, task_queue_id, open_id, service_id, content, " 
				   + " msg_type, create_user, create_date) "				  
				   + " values(?,?,?,?,?, ?,?,? ) ";
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						getGUID(),
						taskQueue.getId(),
						taskQueue.getOpen_id(),
						taskQueue.getService_id(), 
						message,
						"text",
						"system",
						getCurrentDate() });
	}
	
	/**
	 * 取得在线客服相关数据
	 * @return
	 * @throws Exception
	 */
	public List<OnlineCustomerServiceEntity> getCustomerService(List<Object> allPrincipals) throws Exception {
		// 拼查询条件
		String ids = "(";
		for(int i=0;i<allPrincipals.size();i++){
			Object obj = allPrincipals.get(i);			
			if (obj instanceof PlatformUserEntity){
				ids = ids + "'" + ((PlatformUserEntity)obj).getId() + "'";
				if (i < allPrincipals.size() - 1)
					ids = ids + ",";
			}			
		}
		ids = ids + ")";
		String sql = "select service_id, count(id) task_count from "
				   + getTableName("task_queue") 
				   + " where task_status <> ? and task_status <> ? and task_status <> ? "
				   + " and service_id in " + ids
				   + " group by service_id ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<OnlineCustomerServiceEntity> result = jdbcTemplate.query(sql, 
				new Object[]{ TaskStatus.COMPLETED,TaskStatus.STOP,TaskStatus.WAIT},
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				OnlineCustomerServiceEntity entity = new OnlineCustomerServiceEntity();
				entity.setId(rs.getString("service_id"));
				entity.setTask_count(rs.getInt("task_count"));
				return entity;
			}
		});
		// 构建返回数据 
		List<OnlineCustomerServiceEntity> list_online = new ArrayList<OnlineCustomerServiceEntity>();
		OnlineCustomerServiceEntity online = null;
		// 循环在线用户，获取任务数量
		for (int i=0; i<allPrincipals.size(); i++) {
			Object obj = allPrincipals.get(i);			
			if (obj instanceof PlatformUserEntity){
				PlatformUserEntity user = (PlatformUserEntity)obj;
				// 如果不是标准客服不分配任务
				if (user.getType() != UserType.SERVICE_STANDARD) continue;
				online = new OnlineCustomerServiceEntity();
				online.setId(user.getId());
				online.setAccount(user.getAccount());
				online.setName(user.getName());
				online.setWeichat_type(user.getWeichat_type());
				for (int j=0; j<result.size(); j++) {
					if (online.getId().equals(result.get(j).getId())) {
						online.setTask_count(result.get(j).getTask_count());
						break;
					}
				}
				list_online.add(online);
			}
		}
		return list_online;
	}
	
	/**
	 * 
	 * @param serviceID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getCustomerServiceTask(String serviceID) throws Exception {
		String sql = "select id from " + getTableName("task_queue_all") 
				   + " where service_id = ? " 
				   + " and year(create_date) = year(now()) and month(create_date) = month(now()) and day(create_date)=day(now())  ";
		return jdbcTemplate.query(sql, 
				new Object[]{ serviceID},
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rs.getString("id"));
				return map;
			}
		});
	}
	
	
	/**
	 * 
	 * @param serviceID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getFinalCustomerService(String serviceID) throws Exception {
		String sql = "select service_id,modify_date from " + getTableName("task_queue_all") 
				   + " where (task_status = ? or task_status = ?) "
				   + " and service_id = ? and year(create_date) = year(now()) and month(create_date) = month(now()) and day(create_date)=day(now()) " 
				   + " order by modify_date desc "
				   + " limit 0,1 ";
		return jdbcTemplate.query(sql, 
				new Object[]{ TaskStatus.COMPLETED,TaskStatus.STOP,serviceID},
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("service_id", rs.getString("service_id"));
				map.put("modify_date", rs.getString("modify_date"));
				return map;
			}
		});
	}
	
	
	/**
	 * 
	 * @param serviceID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getTaskQueueByServiceID(String serviceID) throws Exception {
		String sql = "select service_id,count(service_id) task_count from " + getTableName("task_queue") 
				   + " where service_id = ? and year(create_date) = year(now()) and month(create_date) = month(now()) and day(create_date)=day(now()) " ;
		return jdbcTemplate.query(sql, 
				new Object[]{ serviceID},
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("service_id", rs.getString("service_id"));
				map.put("task_count", rs.getString("task_count"));
				return map;
			}
		});
	}
	
    /**
     * 更新任务队列分配客服
     * @param list_update_task
     * @return
     * @throws Exception
     */
	public int[] ModifyCustomerServiceTaskCount(final List<TaskQueueEntity> list_update_task) throws Exception {
		String sql = "update "	+ getTableName("task_queue") + " set task_status = ?, service_id = ?"
				+ " where id = ?";
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			        @Override
		            public int getBatchSize() {
		            	return list_update_task.size();
		            }
					@Override
					public void setValues(PreparedStatement ps,int i) throws SQLException {
						TaskQueueEntity taskQueueEntity = (TaskQueueEntity) list_update_task.get(i);
						ps.setInt(1, TaskStatus.DISPATCH);
						ps.setString(2, taskQueueEntity.getService_id());
						ps.setString(3, taskQueueEntity.getId());
					}
        });
	}
	
	/**
	 * 根据任务队列ID更新任务消息表
	 * @param list_update_task
	 * @return
	 * @throws Exception
	 */
	public int[] ModifyTaskMessage(final List<TaskQueueEntity> list_update_task) throws Exception {
		String sql = "update "	+ getTableName("task_message") + " set service_id = ?"
				+ " where task_queue_id = ? and service_id = ''";
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			        @Override
		            public int getBatchSize() {
		            	return list_update_task.size();
		            }
					@Override
					public void setValues(PreparedStatement ps,int i) throws SQLException {
						TaskQueueEntity taskQueueEntity = (TaskQueueEntity) list_update_task.get(i);
						ps.setString(1, taskQueueEntity.getService_id());
						ps.setString(2, taskQueueEntity.getId());
					}
        });
	}
	
	/**
	 * 根据状态取得任务队列数据
	 * @param task_status
	 * @return
	 * @throws Exception
	 */
	public List<TaskQueueEntity> getTaskQueueByStatus(int status) throws Exception {	
		String sql = "select id, open_id, service_id, task_status,weichat_type from "
				   + getTableName("task_queue") 
				   + " where task_status = ?";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskQueueEntity> taskQueue = jdbcTemplate.query(sql, 
				new Object[]{ status },
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setId(rs.getString("id"));
				tq.setOpen_id(rs.getString("open_id"));
				tq.setService_id(rs.getString("service_id"));
				tq.setTask_status(rs.getInt("task_status"));
				tq.setWeichat_type(rs.getString("weichat_type"));
				return tq;
			}
		});		
		return taskQueue;
	}
	
	/**
	 * 取得客服发送的信息
	 * @return
	 * @throws Exception
	 *//*
	public List<TaskMessageEntity> getTaskMessageForService(String taskID) throws Exception {
		String sql = "select id, open_id, service_id, task_status from "
				   + getTableName("task_message") 
				   + " where task_queue_id = ? and data_read = 0 ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskMessageEntity> taskMessage = jdbcTemplate.query(sql, 
				new Object[]{ taskID },
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskMessageEntity tq = new TaskMessageEntity();
				tq.setId(rs.getString("id"));
				tq.setOpen_id(rs.getString("open_id"));
				tq.setService_id(rs.getString("service_id"));
				tq.setContent(rs.getString("content"));
				return tq;
			}
		});		
		return taskMessage;
	}*/
	
	/**
	 * 更新状态
	 * @param taskID
	 * @return
	 * @throws Exception
	 */
	public int modifyTaskStatus(int status,String taskID) throws Exception {
		String sql = " update " + getTableName("task_queue")
				   + " set task_status = ? where id = ? ";
		return jdbcTemplate.update(sql, new Object[]{ status,taskID });
	}
	
	/**
	 * 微信用户月登陆的次数
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public List<TaskQueueEntity> getTaskQueueOfMonthByOpenID(String openID) throws Exception {	
		String sql = "select id from "
				   + getTableName("task_queue") 
				   + " where open_id = ? and substring(create_date,6,2) = substring(sysdate(),6,2) ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskQueueEntity> taskQueue = jdbcTemplate.query(sql, 
				new Object[]{ openID },
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setId(rs.getString("id"));
				return tq;
			}
		});		
		return taskQueue;
	}
	
	/**
	 * 咨询师正在咨询的数量
	 * @param serviceID
	 * @return
	 * @throws Exception
	 */
	public List<TaskQueueEntity> getTaskQueueCountByServiceID(String serviceID) throws Exception {	
		String sql = "select id from "
				   + getTableName("task_queue") 
				   + " where service_id = ? and task_status <> ? and task_status <> ? and task_status <> ?";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskQueueEntity> taskQueue = jdbcTemplate.query(sql, 
				new Object[]{ serviceID,TaskStatus.COMPLETED,TaskStatus.STOP,TaskStatus.WAIT },
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setId(rs.getString("id"));
				return tq;
			}
		});		
		return taskQueue;
	}


	/**
	 * 获取未分配的客人数目
	 * @return
	 */
	public int getUnAllotPerson() {
		String sql = "select count(0) from " + getTableName("task_queue") + " where task_status=?";
		return jdbcTemplate.queryForInt(sql,new Object[]{TaskStatus.UN_PROCESS});
	}
	
	/**
	 * 根据ID查找客服繁忙状态
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getPlatfromBusyStatus(String id){
		String sql = " select busy from " + getTableName("platform_users")
				+ " where id = ? " ;
		List<Map<String, Object>> platformList = jdbcTemplate.query(sql, new Object[]{id}, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String,Object> value = new HashMap<String,Object>();
				value.put("busy", rs.getString("busy"));
				return value;
			}
		});	
		return Integer.valueOf(platformList.get(0).get("busy").toString());
	}
}
