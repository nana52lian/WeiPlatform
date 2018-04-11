package com.yidatec.weixin.service.seatmgr;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.xwork.StringUtils;

import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.dao.seatmgr.AdviserManageDao;
import com.yidatec.weixin.entity.AdviserEntity;
import com.yidatec.weixin.entity.GroupEntity;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ScheduleEntity;
import com.yidatec.weixin.entity.UserScheduleEntity;

public class AdviserManageService {

	private AdviserManageDao adviserManageDao = null;
	public AdviserManageDao getAdviserManageDao() {
		return adviserManageDao;
	}

	public void setAdviserManageDao(AdviserManageDao adviserManageDao) {
		this.adviserManageDao = adviserManageDao;
	}

	/**
	 * 查询咨询师数量
	 * @param qry_params
	 * @return
	 */
	public int queryAdviserCount(String qry_params) throws Exception {
		return adviserManageDao.queryAdviserCount(qry_params);
	}
	
	/**
	 * 查询咨询师
	 * @param qry_params
	 * @return
	 */
	public List<AdviserEntity> queryAdviser(String qry_params, int offset, int rows) throws Exception {
		return adviserManageDao.queryAdviser(qry_params, offset, rows);
	}
	
	/**
	 * 查询组的数量
	 * @param qry_params
	 * @return
	 */
	public int getGroupCount() throws Exception {
		return adviserManageDao.getGroupCount();
	}
	
	/**
	 * 查询所有组
	 * @param qry_params
	 * @return
	 */
	public List<Map<String, Object>> getGroup(int offset, int rows) throws Exception {
		return adviserManageDao.getGroup(offset, rows);
	}
	
	/**
	 * 添加、修改、删除组
	 * @param authorityEntity
	 * @return
	 * @throws Exception
	 */
	public String groupAction(GroupEntity groupEntity) throws Exception {
		int res = 0;
		switch (groupEntity.getAction_no()) {
			case 1: // 新增
				res = adviserManageDao.addGroup(groupEntity);
				break;
			case 2: // 修改
				res = adviserManageDao.updateGroup(groupEntity);
				break;
			case 3: // 删除
				adviserManageDao.deleteGroup(groupEntity.getId().split(","));
				res = 1;
				break;
		}
		if (res > 0) {
			return EnumRes.SUCCESS.getDescription();
		} else {
			return EnumRes.FAILED.getDescription();
		}
	}
	
	/**
	 * 将用户关联到角色
	 * @param authorityEntity
	 * @return
	 * @throws Exception
	 */
	public String assignUser(GroupEntity groupEntity) throws Exception {
		// 1. 先删除该组关联的坐席
		adviserManageDao.deleteGroupPlatformUser(groupEntity.getId());
		if (StringUtils.isNotEmpty(groupEntity.getPlatfrom_user_ids())) {
			adviserManageDao.addPlatformUserToGroup(groupEntity);
		}
		return EnumRes.SUCCESS.getDescription();
	}
	
	/**
	 * 加载组关联的用户
	 * @param role_id
	 * @return
	 * @throws Exception
	 */
	public List<PlatformUserEntity> loadGroupUsers(String group_id) throws Exception {
		return adviserManageDao.loadGroupUsers(group_id);
	}
	
	/**
	 * 查询咨询师信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public AdviserEntity getAdviserInfo(String req_params) throws Exception {
		return adviserManageDao.getAdviserInfo(req_params);
	}
	
	/**
	 * 咨询师的新增、修改、删除
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public String syncAdviserAction(String req_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		int action_no = jsonObject.optInt("action_no");
		int res = 0;
		switch (action_no) {
			case 1: //新增
				res = adviserManageDao.addAdviser(jsonObject);
				/*
				if(res>0){
					res = adviserManageDao.addSchedule(jsonObject);
					if(res>0){
						res = adviserManageDao.addPlatformUser(jsonObject);
					}
				}
				*/
				break;
			case 2: //修改
				res = adviserManageDao.updateAdviser(jsonObject);
				/*
				if(res>0){
					res = adviserManageDao.updatePlatformUser(jsonObject);
				}
				*/
				break;
			case 3: //删除
				res = adviserManageDao.deleteAdviser(jsonObject);
				if(res>0){
					adviserManageDao.deleteRoleUser(jsonObject);
				}
				/*
                if(res>0){
                	res = adviserManageDao.deletePlatformUser(jsonObject);
				}
				*/
//				if(res>0){
//					res = adviserManageDao.deleteRoleUser(jsonObject);
//				}
				break;
			case 4: //修改密码
				res = adviserManageDao.updatePasswordOfPlatformUser(jsonObject);
				break;
		}
		if (res > 0) {
			return EnumRes.SUCCESS.getDescription();
		} else {
			return EnumRes.FAILED.getDescription();
		}
	}
	
	/**
	 * 保存当前咨询师的时间计划
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public int syncSaveSchedule(String req_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		int res = 0;
		//1. 先删除当前账号咨询时间计划
		int resDel = adviserManageDao.deleteSchedule();
		//2. 保存新的时间计划
		if(resDel > -1) {
			res = adviserManageDao.saveSchedule(jsonObject);
		}
		return res;
	}
	
	/**
	 * 添加我的咨询备注
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public int saveRemark(String req_params) throws Exception {
		int res = adviserManageDao.saveRemark(req_params);
		return res;
	}
	
	/**
	 * 获取时间计划信息
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleEntity> getSchedule() throws Exception {
		return adviserManageDao.getSchedule();
	}
	
	/**
	 * 查询预约咨询数量
	 * @param qry_params
	 * @return
	 */
	public int queryScheduleReserveCount(String qry_params) throws Exception {
		return adviserManageDao.queryScheduleReserveCount(qry_params);
	}
	
	/**
	 * 查询预约咨询
	 * @param qry_params
	 * @return
	 */
	public List<UserScheduleEntity> queryScheduleReserve(String qry_params, int offset, int rows) throws Exception {
		return adviserManageDao.queryScheduleReserve(qry_params, offset, rows);
	}
	
	/**
	 * 获取预约咨询详细信息
	 * @param qry_params
	 * @return
	 */
	public UserScheduleEntity getScheduleReserveInfo(String guid) throws Exception {
		return adviserManageDao.getScheduleReserveInfo(guid);
	}
	
	/**
	 * 预约咨询操作
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public String scheduleReserveAction(String req_params) throws Exception {
		int res = 0;
		res = adviserManageDao.scheduleReserveAction(req_params);
		if (res > 0) {
			return EnumRes.SUCCESS.getDescription();
		} else {
			return EnumRes.FAILED.getDescription();
		}
	}
	
	/**
	 * 预约咨询完成操作
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public String syncCompleteReserve(String req_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		String id = jsonObject.optString("id");
		String account = jsonObject.optString("account");
		int resDuration = 0;
		//1:改变预约咨询的状态
		int res = adviserManageDao.completeReserve(id);
		if(res > 0){
			//2：每完成一次，给咨询师加一次时间
			resDuration = adviserManageDao.addDuration(account);
			
		}
		if (resDuration > 0) {
			return EnumRes.SUCCESS.getDescription();
		} else {
			return EnumRes.FAILED.getDescription();
		}
	}
	
	/**
	 * 获取预约咨询时间
	 * @param adviserEntity
	 * @return 
	 * @throws Exception
	 */
	public AdviserEntity getScheduleReserveTimes(AdviserEntity adviserEntity) throws Exception{
		List<String> freeTimes1 = adviserManageDao.getFreeTimes(adviserEntity.getAccount());
		List<String> freeTimes2 = new ArrayList<String>();
		freeTimes2.addAll(freeTimes1);
		List<String> todayFreeTimes = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("H");
		int hour = Integer.parseInt(sdf.format(new Date()));
		for(String time:freeTimes1){
			if(Integer.parseInt(time)>hour){
				todayFreeTimes.add(time);
			}
		}
		
		List<String> usedTimes = adviserManageDao.getAllUsedTimes(adviserEntity.getAccount());
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(new Date());
		int today = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(calendar.DAY_OF_MONTH, 1);
		int tomorrow = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(calendar.DAY_OF_MONTH, 1);
		int acquired = calendar.get(Calendar.DAY_OF_MONTH);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String time:usedTimes){
			Calendar calendar2 = new GregorianCalendar();
			calendar2.setTime(format.parse(time));
			if(calendar2.get(Calendar.DAY_OF_MONTH)==today){
				todayFreeTimes.remove(String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY)));
			}
			else if(calendar2.get(Calendar.DAY_OF_MONTH)==tomorrow){
				freeTimes1.remove(String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY)));
			}
			else if(calendar2.get(Calendar.DAY_OF_MONTH)==acquired){
				freeTimes2.remove(String.valueOf(calendar2.get(Calendar.HOUR_OF_DAY)));
			}
		}
		
		adviserEntity.setTodayFreeTimes(todayFreeTimes);
		adviserEntity.setFreeTimes1(freeTimes1);
		adviserEntity.setFreeTimes2(freeTimes2);
		
		return adviserEntity;
	}
	
	/**
	 * 修改预约时间
	 * @param id
	 * @param radioTime
	 * @throws Exception
	 */
	public int updateReserveTime(String req_params) throws Exception{
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		String id = jsonObject.optString("id");
		String freeTimeRadio = jsonObject.optString("freeTimeRadio");
		int dataInt = Integer.parseInt(freeTimeRadio.substring(1));
		String flag = freeTimeRadio.substring(0,1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		Date date = new Date();
		String currentDate = sdf.format(date);
		if(flag.equals("b")){
			 Calendar calendar = new GregorianCalendar();
			 calendar.setTime(date);
			 calendar.add(calendar.DATE,1);
			 date=calendar.getTime(); 
			 currentDate = sdf.format(date);
		}
		else if(flag.equals("c")){
			 Calendar calendar = new GregorianCalendar();
			 calendar.setTime(date);
			 calendar.add(calendar.DATE,2);
			 date=calendar.getTime(); 
			 currentDate = sdf.format(date);
		}
		
		String fromDt = currentDate + String.format("%02d", dataInt);
		String toDt = currentDate + String.format("%02d", dataInt+1);
		sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		
		return adviserManageDao.updateReserveTime(id, sdf.parse(fromDt), sdf.parse(toDt));
	}
	
	/**
	 * 取得咨询师评价统计情况
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAppraisalCountInfo() throws Exception{
		return adviserManageDao.getAppraisalCountInfo();
	}
	
	/**
	 * 根据客服ID取得所有评价信息
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> getAllAppraisalOfAdviser(String id) throws Exception{
		return adviserManageDao.getAllAppraisalOfAdviser(id);
	}
}
