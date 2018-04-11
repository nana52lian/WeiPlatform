package com.yidatec.weixin.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.hundsun.t2sdk.common.util.StringUtils;
import com.yidatec.weixin.entity.AdviserEntity;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.UserFeedbackEntity;
import com.yidatec.weixin.entity.UserScheduleEntity;
import com.yidatec.weixin.entity.message.ArticleEntity;
import com.yidatec.weixin.entity.message.DataSetValue;
import com.yidatec.weixin.entity.message.TaskDisplayEntity;
import com.yidatec.weixin.entity.message.TaskQueueEntity;
import com.yidatec.weixin.entity.message.TaskStatus;
import com.yidatec.weixin.entity.message.UserDetail;
import com.yidatec.weixin.entity.message.UserType;
import com.yidatec.weixin.message.SendCloseMessage;
import com.yidatec.weixin.message.WeixinHelper;

public class WeixinDao extends DataBase {
	private static final Logger log = LogManager.getLogger(WeixinDao.class);
	/**
	 * 我的当前可用预约
	 * 
	 * @param userCode
	 * @return
	 */
	public UserScheduleEntity getCurrentUserSchedule(String userCode) {
		String sql = "select name,begin_time,end_time,phone,charge_account from "
				+ getTableName("user_schedule")
				+ " left join "
				+ getTableName("adviser")
				+ " on advisory_account = account "
				+ " where weixin_code=? and status in (1,2) ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<UserScheduleEntity> userScheduleList = jdbcTemplate.query(sql,
				new Object[] { userCode }, new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						UserScheduleEntity userSchedule = new UserScheduleEntity();
						userSchedule.setAdvisoryName(rs.getString("name"));
						userSchedule.setBeginTime(rs.getString("begin_time"));
						userSchedule.setEndTime(rs.getString("end_time"));
						userSchedule.setMobile_phone(rs.getString("phone"));
						userSchedule.setChargeAccount(rs
								.getString("charge_account"));
						return userSchedule;
					}
				});
		if (userScheduleList.size() > 0)
			return userScheduleList.get(0);
		return null;
	}

	/**
	 * 添加心情指数
	 * 
	 * @param fromUserName
	 * @param date
	 * @param score
	 */
	public int saveMoodScore(String fromUserName, Date date, int score) {
		String sql = "insert into "
				+ getTableName("user_mood")
				+ " (id, weixin_code, score, mood_date, create_user, create_date)"
				// + " values(?,?,?,?,?,?)";
				+ " select ?,?,?,?,?,? from Dual " + " where not exists"
				+ " (select * from " + getTableName("user_mood")
				+ " where weixin_code=? and mood_date=? )";
		return jdbcTemplate.update(sql, new Object[] { getGUID(), fromUserName,
				score, date, null, new Date(), fromUserName, date });
	}

	/**
	 * 加载用户某月心情数据，用于做图表
	 * 
	 * @param weixin_code
	 * @param month
	 *            传0表示取当前月的
	 * @return
	 * @throws Exception
	 */
	public List<DataSetValue> loadMoodData(String weixin_code, int month)
			throws Exception {
		String sql = " select score, DAYOFMONTH(mood_date) day from "
				+ getTableName("user_mood") + " where weixin_code = ? ";
		Object[] params = null;
		if (month > 0) {
			sql = sql + " and MONTH(mood_date) = ? order by day ";
			params = new Object[] { weixin_code, month };
		} else {
			sql = sql + " and MONTH(mood_date) = MONTH(now()) order by day ";
			params = new Object[] { weixin_code };
		}
		@SuppressWarnings("unchecked")
		List<DataSetValue> values = jdbcTemplate.query(sql, params,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						DataSetValue value = new DataSetValue();
						value.setValue(rs.getInt("score"));
						value.setRowkey(rs.getString("day"));
						value.setColumnKey(rs.getString("day"));
						return value;
					}
				});
		return values;
	}

	/**
	 * 获取空闲的咨询师
	 * 
	 * @return
	 */
	public List<AdviserEntity> getOptionalAdvisers(int line) {
		String sql = "select distinct a.description, a.professional, a.id,a.name,a.`level`,c.region_name provinceName,d.region_name cityName,e.region_name regionName from "
				+ getTableName("adviser")
				+ " a left join "
				+ getTableName("schedule")
				+ " b on a.account=b.account "
				+ " left join "
				+ getTableName("region")
				+ " c on a.province = c.region_id "
				+ " left join "
				+ getTableName("region")
				+ " d on a.city = d.region_id "
				+ " left join "
				+ getTableName("region")
				+ " e on a.region = e.region_id "
				+ " where b.`status` = 0 and a.dr = 0 limit " + line + ",8 ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<AdviserEntity> advisers = jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				AdviserEntity ae = new AdviserEntity();
				ae.setId(rs.getString("id"));
				ae.setName(rs.getString("name"));
				ae.setLevel(rs.getString("level"));
				ae.setProvinceName(rs.getString("provinceName"));
				ae.setCityName(rs.getString("cityName"));
				ae.setRegionName(rs.getString("regionName"));
				ae.setProfessional(rs.getInt("professional"));
				ae.setDescription(rs.getString("description"));
				return ae;
			}
		});
		if (advisers.size() > 0)
			return advisers;
		return null;
	}

	/**
	 * 获取预约过的咨询师
	 * 
	 * @param fromUserName
	 * @return
	 */
	public List<AdviserEntity> getReservedAdvisers(String fromUserName) {
		String sql = "select distinct a.id,a.name,a.`level`,d.region_name provinceName,e.region_name cityName,f.region_name regionName from "
				+ getTableName("adviser")
				+ " a left join "
				+ getTableName("user_schedule")
				+ " b on a.account=b.advisory_account left join "
				+ getTableName("schedule")
				+ " c on a.account=c.account "
				+ " left join "
				+ getTableName("region")
				+ " d on a.province = d.region_id "
				+ " left join "
				+ getTableName("region")
				+ " e on a.city = e.region_id "
				+ " left join "
				+ getTableName("region")
				+ " f on a.region = f.region_id "
				+ " where b.weixin_code=? and c.`status` = 0 and a.dr = 0 ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<AdviserEntity> advisers = jdbcTemplate.query(sql,
				new Object[] { fromUserName }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						AdviserEntity ae = new AdviserEntity();
						ae.setId(rs.getString("id"));
						ae.setName(rs.getString("name"));
						ae.setLevel(rs.getString("level"));
						ae.setProvinceName(rs.getString("provinceName"));
						ae.setCityName(rs.getString("cityName"));
						ae.setRegionName(rs.getString("regionName"));
						return ae;
					}
				});
		if (advisers.size() > 0)
			return advisers;
		return null;
	}

	/**
	 * 获取某位咨询师的空闲时间
	 * 
	 * @param account
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getFreeTimes(String account) {
		String sql = "select schedule_time from " + getTableName("schedule")
				+ " where account=? and status=0 order by schedule_time";
		@SuppressWarnings("rawtypes")
		List<String> results = jdbcTemplate.query(sql,
				new Object[] { account }, new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						return rs.getString("schedule_time");
					}
				});
		return results;
	}

	/**
	 * 加载当前微信用户需要反馈的记录
	 * 
	 * @param weixin_code
	 * @return
	 * @throws Exception
	 */
	public List<UserFeedbackEntity> loadMyFeedback(String weixin_code)
			throws Exception {
		String sql = " select a.id, a.weixin_code, a.advisory_account, a.begin_time, "
				+ " a.end_time, b.name, b.phone from "
				+ getTableName("user_schedule")
				+ " a "
				+ " left join "
				+ getTableName("adviser")
				+ " b "
				+ " on a.advisory_account = b.account "
				+ " where a.weixin_code = ? "
				+ " and a.status = 3 and (a.feedback IS NULL or a.feedback = '') ";
		@SuppressWarnings("unchecked")
		List<UserFeedbackEntity> userFeedbackEntities = jdbcTemplate.query(sql,
				new Object[] { weixin_code }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						UserFeedbackEntity entity = new UserFeedbackEntity();
						entity.setId(rs.getString("id"));
						entity.setWeixin_code(rs.getString("weixin_code"));
						entity.setAdvisory_account(rs
								.getString("advisory_account"));
						entity.setBegin_time(rs.getString("begin_time"));
						entity.setEnd_time(rs.getString("end_time"));
						entity.setName(rs.getString("name"));
						entity.setPhone(rs.getString("phone"));
						return entity;
					}
				});
		return userFeedbackEntities;
	}

	/**
	 * 创建预约咨询
	 * 
	 * @param fromUserName
	 * @param account
	 * @param fromDt
	 * @param toDt
	 * @param phone
	 * @param qqNo
	 * @return
	 */
	public int createAdvisory(String fromUserName, String account, Date fromDt,
			Date toDt, String phone, String qqNo, int adviseType) {
		String sql = "insert into advisory_user_schedule "
				+ "(id,weixin_code,advisory_account,qq,mobile_phone,begin_time,end_time,status,type,create_user,create_date)"
				+ " values(?,?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql, new Object[] { getGUID(), fromUserName,
				account, qqNo, phone, fromDt, toDt, 1, adviseType, null,
				new Date() });
	}

	/**
	 * 保存反馈信息
	 * 
	 * @param req_params
	 * @return
	 * @throws Exception
	 */
	public int saveFeedback(JSONObject jsonObject) throws Exception {
		String sql = " update " + getTableName("user_schedule")
				+ " set rank = ?, feedback = ? "
				+ " where id = ? and (feedback IS NULL or feedback = '')";
		return jdbcTemplate.update(
				sql,
				new Object[] { jsonObject.optInt("rank"),
						jsonObject.optString("feedback"),
						jsonObject.optString("user_schedule_id") });
	}

	/**
	 * 保存对咨询师的评价 assess1 有收货的 assess2 被理解和尊重 assess3 感觉舒适的 assess4 可信赖的 assess5
	 * 倾向于指导 assess6 倾向于启发 assess7 倾向于倾听
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int saveAdviserAssess(JSONObject jsonObject) throws Exception {
		String sql = " update "
				+ getTableName("adviser")
				+ " set assess1 = assess1 + ?, assess2 = assess2 + ?, assess3 = assess3 + ?, "
				+ " assess4 = assess4 + ?, assess5 = assess5 + ?, assess6 = assess6 + ?, "
				+ " assess7 = assess7 + ? where account = ? ";
		return jdbcTemplate.update(
				sql,
				new Object[] { jsonObject.optInt("assess1"),
						jsonObject.optInt("assess2"),
						jsonObject.optInt("assess3"),
						jsonObject.optInt("assess4"),
						jsonObject.optInt("assess5"),
						jsonObject.optInt("assess6"),
						jsonObject.optInt("assess7"),
						jsonObject.optString("account") });
	}

	/**
	 * 更新咨询记录的状态 1:预约待确认 2:咨询待完成 3:等待反馈 4:完成
	 * 
	 * @param user_schedule_id
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public int updateUserScheduleStatus(String user_schedule_id, int status)
			throws Exception {
		String sql = " update " + getTableName("user_schedule")
				+ " set status = ? where id = ? ";
		return jdbcTemplate.update(sql,
				new Object[] { status, user_schedule_id });
	}

	/**
	 * 获取咨询师空闲时间
	 * 
	 * @param account
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> getAllUsedTimes(String account) {
		String sql = "select begin_time from " + getTableName("user_schedule")
				+ " where advisory_account=? and status in(1,2,3)";
		return jdbcTemplate.query(sql, new Object[] { account },
				new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						return rs.getString("begin_time");
					}

				});
	}

	/**
	 * 查询是否有正在进行中的预约
	 * 
	 * @param fromUserName
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String checkIfExistsOrder(String fromUserName) {
		String sql = "select status from " + getTableName("user_schedule")
				+ " where weixin_code=? and status in (1,2,3)";
		List returnList = jdbcTemplate.query(sql,
				new Object[] { fromUserName }, new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						return rs.getString("status");
					}
				});
		if (returnList.size() > 0)
			return (String) returnList.get(0);
		return "";
	}

	/**
	 * 获取上次查询到哪一行
	 * 
	 * @param fromUserName
	 * @param type
	 *            1- 可预约心理顾问
	 * @return
	 */
	public int getLastLine(String type, String fromUserName) {
		String sql = "select line from " + getTableName("pagination_value")
				+ " where type=? and weixin_code=?";
		List<Integer> list = jdbcTemplate.query(sql, new Object[] { type,
				fromUserName }, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("line");
			}

		});
		if (list.size() > 0)
			return list.get(0);
		return 0;
	}

	/**
	 * 初始化分页表
	 * 
	 * @param type
	 *            1- 可预约心理顾问
	 * @param line
	 */
	public void initPagination(int type, int line, String fromUserName) {
		String sql = "insert into " + getTableName("pagination_value")
				+ "(id,weixin_code,type,line,create_date)"
				+ " values(?,?,?,?,?)";
		jdbcTemplate.update(sql, new Object[] { getGUID(), fromUserName, type,
				line, new Date() });
	}

	/**
	 * 查询后，更新当前行数
	 * 
	 * @param type
	 *            1- 可预约心理顾问
	 * @param line
	 * @param fromUserName
	 */
	public void updatePagination(int type, int line, String fromUserName) {
		String sql = "update " + getTableName("pagination_value")
				+ " set line=? where weixin_code=? and type=?";
		jdbcTemplate.update(sql, new Object[] { line, fromUserName, type });
	}

	/**
	 * 获取咨询师总数
	 * 
	 * @return
	 */
	public int getAdvisersCount() {
		String sql = "select count(0) from " + getTableName("adviser");
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Integer> returnList = jdbcTemplate.query(sql, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt(1);
			}

		});
		return returnList.get(0);
	}

	/**
	 * 根据open_id获取微信用户数
	 * 
	 * @return
	 */
	public int getUsersCountByOpenId(String open_id) {
		String sql = "select count(id) 'count' from " + getTableName("users")
				+ " where openid = ? ";
		@SuppressWarnings("unchecked")
		List<Integer> countList = jdbcTemplate.query(sql,
				new Object[] { open_id }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						return rs.getInt("count");
					}
				});
		if (countList.size() > 0)
			return countList.get(0);
		return 0;
	}

	/**
	 * 微信用户订阅
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int subscribe(JSONObject jsonObject) throws Exception {
		String sql = "insert into "
				+ getTableName("users")
				+ " (id, subscribe, openid, nickname, sex, language, city, "
				+ " province, country, headimgurl, subscribe_time, bind, create_user, create_date) "
				+ " values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?) ";
		return jdbcTemplate.update(
				sql,
				new Object[] { getGUID(), "1", jsonObject.optString("openid"),
						jsonObject.optString("nickname"),
						jsonObject.optInt("sex"),
						jsonObject.optString("language"),
						jsonObject.optString("city"),

						jsonObject.optString("province"),
						jsonObject.optString("country"),
						jsonObject.optString("headimgurl"),
						jsonObject.optInt("subscribe_time"), 0, SYSTEM_USER,
						getCurrentDate() });
	}

	/**
	 * 再次关注时根据open_id更新微信用户信息
	 * 
	 * @param open_id
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updateForSubscribe(String open_id, JSONObject jsonObject)
			throws Exception {
		String sql = "update "
				+ getTableName("users")
				+ " set subscribe = ?, nickname = ?, sex=?, language=?, city=?, province=?,  country=?, headimgurl=?, "
				+ " subscribe_time=?, bind=?, modify_user=?, modify_date=? "
				+ " where openid = ?";
		return jdbcTemplate.update(
				sql,
				new Object[] { "1", jsonObject.optString("nickname"),
						jsonObject.optInt("sex"),
						jsonObject.optString("language"),
						jsonObject.optString("city"),
						jsonObject.optString("province"),
						jsonObject.optString("country"),
						jsonObject.optString("headimgurl"),
						jsonObject.optInt("subscribe_time"), 0, SYSTEM_USER,
						getCurrentDate(),

						open_id });
	}

	/**
	 * 用户取消关注
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int unsubscribe(String open_id) throws Exception {
		String sql = "update " + getTableName("users") + " set subscribe = 0 "
				+ " where openid = ?";
		int rult = jdbcTemplate.update(sql, new Object[] { open_id });
		return rult;
	}

	/**
	 * 加载已经订阅人的openid
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<String> loadSubscribeOpenids() throws Exception {
		String sql = " select openid from " + getTableName("users")
				+ " where subscribe = 1 ";
		return jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getString("openid");
			}
		});
	}

	/**
	 * 批量添加微信用户
	 * 
	 * @param userInfo_create
	 * @return
	 * @throws Exception
	 */
	public int[] addBatchWeixinUser(final List<JSONObject> userInfo_create)
			throws Exception {
		if (userInfo_create.size() == 0)
			return new int[] { 0 };
		String sql = " insert into "
				+ getTableName("users")
				+ " (id, subscribe, openid, nickname, sex, language, city, "
				+ " province, country, headimgurl, subscribe_time, bind, create_user, create_date) "
				+ " values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?) ";
		return jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {

					@Override
					public int getBatchSize() {
						return userInfo_create.size();
					}

					@Override
					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						ps.setString(1, getGUID());
						ps.setInt(2,
								userInfo_create.get(index).optInt("subscribe"));
						ps.setString(3,
								userInfo_create.get(index).optString("openid"));
						/*
						 * try { ps.setString(4, new
						 * String(userInfo_create.get(index
						 * ).optString("nickname").getBytes("GB2312")));
						 * //ps.setString(4,
						 * Base64.encodeString(userInfo_create.
						 * get(index).optString("nickname").getBytes("UTF-8")));
						 * /
						 * /System.out.println(userInfo_create.get(index).optString
						 * ("nickname") + ":" +
						 * Base64.encodeString(userInfo_create
						 * .get(index).optString
						 * ("nickname").getBytes("UTF-8"))); } catch
						 * (UnsupportedEncodingException e) {}
						 */
						ps.setString(4,
								userInfo_create.get(index)
										.optString("nickname"));
						ps.setInt(5, userInfo_create.get(index).optInt("sex"));
						ps.setString(6,
								userInfo_create.get(index)
										.optString("language"));
						ps.setString(7,
								userInfo_create.get(index).optString("city"));
						ps.setString(8,
								userInfo_create.get(index)
										.optString("province"));
						ps.setString(9,
								userInfo_create.get(index).optString("country"));
						ps.setString(
								10,
								userInfo_create.get(index).optString(
										"headimgurl"));
						ps.setInt(
								11,
								userInfo_create.get(index).optInt(
										"subscribe_time"));
						ps.setInt(12, 0);
						ps.setString(13, SYSTEM_USER);
						ps.setString(14, getCurrentDate());
					}
				});
	}

	/**
	 * 批量添加微信用户
	 * 
	 * @param userInfo_create
	 * @return
	 * @throws Exception
	 */
	public int[] updateBatchWeixinUser(final List<JSONObject> userInfo_update)
			throws Exception {
		String sql = " update "
				+ getTableName("users")
				+ " set subscribe = ?, nickname = ?, sex = ?, language = ?, city = ?, province = ?, "
				+ " country = ?, headimgurl = ?, subscribe_time = ?, modify_user = ?, modify_date = ? "
				+ " where openid = ? ";
		return jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {

					@Override
					public int getBatchSize() {
						return userInfo_update.size();
					}

					@Override
					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						ps.setInt(1,
								userInfo_update.get(index).optInt("subscribe"));
						/*
						 * try { //ps.setString(2,
						 * Base64.encodeString(userInfo_update
						 * .get(index).optString
						 * ("nickname").getBytes("UTF-8"))); ps.setString(2, new
						 * String
						 * (userInfo_update.get(index).optString("nickname"
						 * ).getBytes("GB2312"))); } catch
						 * (UnsupportedEncodingException e) {}
						 */
						ps.setString(2,
								userInfo_update.get(index)
										.optString("nickname"));
						ps.setInt(3, userInfo_update.get(index).optInt("sex"));
						ps.setString(4,
								userInfo_update.get(index)
										.optString("language"));
						ps.setString(5,
								userInfo_update.get(index).optString("city"));
						ps.setString(6,
								userInfo_update.get(index)
										.optString("province"));
						ps.setString(7,
								userInfo_update.get(index).optString("country"));
						ps.setString(
								8,
								userInfo_update.get(index).optString(
										"headimgurl"));
						ps.setInt(
								9,
								userInfo_update.get(index).optInt(
										"subscribe_time"));
						ps.setString(10, SYSTEM_USER);
						ps.setString(11, getCurrentDate());
						ps.setString(12,
								userInfo_update.get(index).optString("openid"));
					}

				});
	}

	/**
	 * 我的当前咨询（包括被激活的状态）
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getUnCompleteAdvisory() throws Exception {
		String sql = " SELECT d.id,d.open_id,d.service_id,d.create_date,d.nickname,d.task_status,d.content,d.msg_type FROM "
				+ " (select a.id, a.open_id, a.service_id,a.create_date,b.nickname ,a.task_status,c.content,c.msg_type from "   //,c.id1,c.create_user 
				+ getTableName("task_queue")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				//+ "(select id as id1,create_user,task_queue_id,create_date,content,msg_type from(select * from "
				+ getTableName("task_message")
				//+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ " where a.service_id = ? and a.task_status in ("
				+ TaskStatus.DISPATCH
				+ ","
				+ TaskStatus.PROCESSING
				+ ")"
				+ " order by a.task_status desc,c.create_date desc ) "
				+ " d "
				+ " GROUP BY d.id";


		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { getCurrentUser().getId() }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setService_id(rs.getString("service_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						/*if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}*/
						tq.setNickname(rs.getString("open_id"));
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						
						//tq.setId1(rs.getString("id1"));
						//tq.setCreate_user(rs.getString("create_user"));
						
						return tq;
					}
				});
		return taskQueue;
	}
	
	/**
	 * 我的当前咨询（给兼职用，已分配的，不包含已处理）
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getUnCompleteAdvisoryForJZ() throws Exception {
		String sql = "select a.id, a.open_id, a.service_id,a.create_date,a.weichat_type,b.nickname ,a.task_status,c.content,c.msg_type from "
				+ getTableName("task_queue")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				+ "(select task_queue_id,create_date,content,msg_type from(select * from "
				+ getTableName("task_message")
				+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ " where a.service_id = ? and a.task_status = " + TaskStatus.DISPATCH
				+ " order by create_date desc ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { getCurrentUser().getId() }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setService_id(rs.getString("service_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setWeichat_type(rs.getString("weichat_type"));
						return tq;
					}
				});
		return taskQueue;
	}
	

	/**
	 * 我完成的咨询(包含终止状态)
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getCompleteAdvisoryCount() throws Exception {
		String sql = "select a.id, a.open_id, a.create_date,b.nickname ,a.task_status,c.content,c.msg_type,a.service_score from "
				+ getTableName("task_queue_all")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				+ "(select task_queue_id,content,msg_type from(select * from "
				+ getTableName("task_message_all")
				+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ " where a.service_id = ? and (a.task_status = ? or a.task_status = ?) "
				+ " order by create_date desc ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { getCurrentUser().getId(), TaskStatus.COMPLETED,
						TaskStatus.STOP }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						// System.getProperty("file.encoding");
						if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setService_score(rs.getString("service_score"));
						return tq;
					}
				});
		return taskQueue;
	}

	/**
	 * 我完成的咨询(包含终止状态)
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getCompleteAdvisory(int offset, int rows)
			throws Exception {
		String sql = "select a.id,a.open_id, a.create_date,b.nickname ,a.task_status,c.content,c.msg_type,a.service_score from "
				+ getTableName("task_queue_all")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				+ "(select task_queue_id,content,msg_type from(select * from "
				+ getTableName("task_message_all")
				+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ " where a.service_id = ? and (a.task_status = ? or a.task_status = ?) "
				+ " order by create_date desc " + " limit ?, ? ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { getCurrentUser().getId(), TaskStatus.COMPLETED,
						TaskStatus.STOP, offset, rows }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						// System.getProperty("file.encoding");
						if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setService_score(rs.getString("service_score"));
						return tq;
					}
				});
		return taskQueue;
	}

	/**
	 * 根据openid获得以往请求列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getRequestByOpenId(String req_params)
			throws Exception {
		String sql = "select a.id, a.open_id, a.create_date,b.nickname ,a.task_status,c.content,c.msg_type,a.service_score from "
				+ getTableName("task_queue")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				+ "(select task_queue_id,content,msg_type from(select * from "
				+ getTableName("task_message")
				+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ " where a.open_id = ? "
				+ " order by create_date desc ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { req_params }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						// System.getProperty("file.encoding");
						if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setService_score(rs.getString("service_score"));
						return tq;
					}
				});
		return taskQueue;
	}

	/**
	 * 管理员取得系统所有请求单个数
	 * 
	 * @param req_params
	 * @param request_type
	 * @return
	 * @throws Exception
	 */
	public int getAllRequestCount(String req_params, String request_type)
			throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		String rid = jsonObject.getString("rid");
		if (StringUtils.isEmpty(rid)) {
			rid = " ";
		}
		String openid = jsonObject.getString("openid");
		if (StringUtils.isEmpty(openid)) {
			openid = " ";
		}
		String key_account = jsonObject.getString("key_account");
		if (StringUtils.isEmpty(key_account)) {
			key_account = " ";
		}
		String service_id = jsonObject.getString("service_id");
		if (StringUtils.isEmpty(service_id)) {
			service_id = " ";
		}
		String service_name = jsonObject.getString("service_name");
		if (StringUtils.isEmpty(service_name)) {
			service_name = " ";
		}
		String date = jsonObject.getString("date");
		if (StringUtils.isEmpty(date)) {
			date = " ";
		}

		// 判断查询的是否为完成的请求单
		String strSql = "";
		// 完成的请求单
		String sql = "";
		if (request_type.equals("complete")) {
			strSql = "(a.task_status = 3 or a.task_status = 4)";
			 sql = "select count(a.id) 'count', d.name,TIME_FORMAT( TIMEDIFF(a.modify_date,a.create_date) , '%H小时%i分%s秒' ) 'last', "
					+ " a.*,b.key_account,b.nickname,c.content,c.msg_type from "
					+ getTableName("task_queue_all")
					+ " a left join "
					+ getTableName("users")
					+ " b on a.open_id = b.openid "
					+ " left join "
					+ "(select task_queue_id,content,msg_type from(select * from "
					+ getTableName("task_message_all")
					+ " order by create_date desc) b group by task_queue_id)"
					+ " c on a.id = c.task_queue_id "
					+ " left join "
					+ getTableName("platform_users")
					+ " d on a.service_id = d.id "
					+ " where "
					+ strSql
					+ " and ('"
					+ rid
					+ "' = ' ' or rid like ?) "
					+ " and ('"
					+ openid
					+ "' = ' ' or openid like ?) "
					+ " and ('"
					+ key_account
					+ "' = ' ' or key_account like ?) "
					+ " and ('"
					+ service_id
					+ "' = ' ' or service_id like ?) "
					+ " and ('"
					+ date
					+ "' = ' ' or a.create_date like ?) "
					+ " and ('"
					+ service_name
					+ "' = ' ' or d.name like ?) "
					+ " order by create_date desc ";
		}
		// 未完成的请求单
		if (request_type.equals("uncomplete")) {
			strSql = "(a.task_status = 1 or a.task_status = 2 or a.task_status = 5)";
			 sql = "select count(a.id) 'count', d.name,TIME_FORMAT( TIMEDIFF(a.modify_date,a.create_date) , '%H小时%i分%s秒' ) 'last', "
					+ " a.*,b.key_account,b.nickname,c.content,c.msg_type from "
					+ getTableName("task_queue")
					+ " a left join "
					+ getTableName("users")
					+ " b on a.open_id = b.openid "
					+ " left join "
					+ "(select task_queue_id,content,msg_type from(select * from "
					+ getTableName("task_message")
					+ " order by create_date desc) b group by task_queue_id)"
					+ " c on a.id = c.task_queue_id "
					+ " left join "
					+ getTableName("platform_users")
					+ " d on a.service_id = d.id "
					+ " where "
					+ strSql
					+ " and ('"
					+ rid
					+ "' = ' ' or rid like ?) "
					+ " and ('"
					+ openid
					+ "' = ' ' or openid like ?) "
					+ " and ('"
					+ key_account
					+ "' = ' ' or key_account like ?) "
					+ " and ('"
					+ service_id
					+ "' = ' ' or service_id like ?) "
					+ " and ('"
					+ date
					+ "' = ' ' or a.create_date like ?) "
					+ " and ('"
					+ service_name
					+ "' = ' ' or d.name like ?) "
					+ " order by create_date desc ";
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<Integer> countList = jdbcTemplate.query(sql, new Object[] {
				"%" + rid + "%", "%" + openid + "%", "%" + key_account + "%",
				"%" + service_id + "%","%" + date + "%","%" + service_name + "%" }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("count");
			}
		});
		if (countList.size() > 0)
			return countList.get(0);

		return 0;
	}

	/**
	 * 管理员取得系统所有请求单
	 * 
	 * @param request_type
	 * @param rows
	 * @param offset
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getAllRequest(String req_params,
			String request_type, int offset, int rows) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		String rid = jsonObject.getString("rid");
		if (StringUtils.isEmpty(rid)) {
			rid = " ";
		}
		String openid = jsonObject.getString("openid");
		if (StringUtils.isEmpty(openid)) {
			openid = " ";
		}
		String key_account = jsonObject.getString("key_account");
		if (StringUtils.isEmpty(key_account)) {
			key_account = " ";
		}
		String service_id = jsonObject.getString("service_id");
		if (StringUtils.isEmpty(service_id)) {
			service_id = " ";
		}
		String service_name = jsonObject.getString("service_name");
		if (StringUtils.isEmpty(service_name)) {
			service_name = " ";
		}
		
		String date = jsonObject.getString("date");
		if (StringUtils.isEmpty(date)) {
			date = " ";
		}

		// 判断查询的是否为完成的请求单
		String strSql = "";
		
		String sql = "";
		// 完成的请求单
		if (request_type.equals("complete")) {
			strSql = "(a.task_status = 3 or a.task_status = 4)";
			 sql = "select d.name,TIME_FORMAT( TIMEDIFF(a.modify_date,a.create_date) , '%H小时%i分%s秒' ) 'last', "
					+ " a.*,e.key_account,b.nickname,c.content,c.msg_type from "
					+ getTableName("task_queue_all")
					+ " a left join "
					+ getTableName("users")
					+ " b on a.open_id = b.openid "
					+ " left join "
					+ "(select task_queue_id,content,msg_type from(select * from "
					+ getTableName("task_message_all")
					+ " order by create_date desc) b group by task_queue_id)"
					+ " c on a.id = c.task_queue_id "
					+ " left join "
					+ getTableName("platform_users")
					+ " d on a.service_id = d.id "
					+ " left join "
					+ getTableName("user_detail")
					+ " e on a.id = e.taskid "
					+ " where "
					+ strSql
					+ " and ('"
					+ rid
					+ "' = ' ' or rid like ?) "
					+ " and ('"
					+ openid
					+ "' = ' ' or a.open_id like ?) "
					+ " and ('"
					+ key_account
					+ "' = ' ' or e.key_account like ?) "
					+ " and ('"
					+ service_id
					+ "' = ' ' or a.service_id like ?) "
					+ " and ('"
					+ date
					+ "' = ' ' or a.create_date like ?) "
					+ " and ('"
					+ service_name
					+ "' = ' ' or d.name like ?) "
					+ " order by create_date desc " + " limit ?, ? ";
		}
		// 未完成的请求单
		if (request_type.equals("uncomplete")) {
			strSql = "(a.task_status = 1 or a.task_status = 2 or a.task_status = 5)";
			 sql = "select d.name,TIME_FORMAT( TIMEDIFF(a.modify_date,a.create_date) , '%H小时%i分%s秒' ) 'last', "
						+ " a.*,e.key_account,b.nickname,c.content,c.msg_type from "
						+ getTableName("task_queue")
						+ " a left join "
						+ getTableName("users")
						+ " b on a.open_id = b.openid "
						+ " left join "
						+ "(select task_queue_id,content,msg_type from(select * from "
						+ getTableName("task_message")
						+ " order by create_date desc) b group by task_queue_id)"
						+ " c on a.id = c.task_queue_id "
						+ " left join "
						+ getTableName("platform_users")
						+ " d on a.service_id = d.id "
						+ " left join "
						+ getTableName("user_detail")
						+ " e on a.id = e.taskid "
						+ " where "
						+ strSql
						+ " and ('"
						+ rid
						+ "' = ' ' or rid like ?) "
						+ " and ('"
						+ openid
						+ "' = ' ' or a.open_id like ?) "
						+ " and ('"
						+ key_account
						+ "' = ' ' or e.key_account like ?) "
						+ " and ('"
						+ service_id
						+ "' = ' ' or a.service_id like ?) "
						+ " and ('"
						+ date
						+ "' = ' ' or a.create_date like ?) "
						+ " and ('"
						+ service_name
						+ "' = ' ' or d.name like ?) "
						+ " order by create_date desc " + " limit ?, ? ";
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { "%" + rid + "%", "%" + openid + "%",
						"%" + key_account + "%", "%" + service_id + "%","%" + date + "%","%" + service_name + "%",
						offset, rows }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						// System.getProperty("file.encoding");
						if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setService_score(rs.getString("service_score"));
						tq.setRid(rs.getString("rid"));
						tq.setKey_account(rs.getString("key_account"));
						tq.setService_id(rs.getString("service_id"));
						// 请求类型
						tq.setType(rs.getString("type"));
						// 用户知识评级分数
						tq.setCustom_score(rs.getString("custom_score"));
						tq.setLast(rs.getString("last"));
						tq.setService_name(rs.getString("name"));
						tq.setTask_status(rs.getString("task_status"));
						return tq;
					}
				});
		return taskQueue;
	}

	/**
	 * 根据ID或者K账户查找任务列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getCompleteAdvisoryByTaskIDAndKcodeCount(
			JSONObject jsonObj) throws Exception {
		String sqlStr = " where a.id = ? ";
		String objectID = jsonObj.optString("code");
		if (!jsonObj.optString("kcode").trim().equals("")) {
			if (jsonObj.optString("code").trim().equals("")) {
				objectID = jsonObj.optString("kcode");
				sqlStr = " , " + getTableName("user_detail") + " d "
						+ " where a.id = d.taskid and d.key_account = ? ";
			} else {
				// 不处理
			}
		} else {
			if (jsonObj.optString("code").trim().equals("")) {
				sqlStr = " where a.id <> ? ";
			} else {
				// 不处理
			}
		}
		String sql = "select a.id, a.open_id, a.create_date,b.nickname ,a.task_status,c.content,c.msg_type,a.service_score from "
				+ getTableName("task_queue_all")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				+ "(select task_queue_id,content,msg_type from(select * from "
				+ getTableName("task_message_all")
				+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ sqlStr
				+ " and a.service_id = ? and (a.task_status = ? or a.task_status = ?) "
				+ " order by create_date desc ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { objectID, getCurrentUser().getId(),
						TaskStatus.STOP, TaskStatus.COMPLETED },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setService_score(rs.getString("service_score"));
						return tq;
					}
				});
		return taskQueue;
	}

	/**
	 * 根据ID或者K账户查找任务列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getCompleteAdvisoryByTaskIDAndKcode(
			JSONObject jsonObj, int offset, int rows) throws Exception {
		String sqlStr = " where a.id = ? ";
		String objectID = jsonObj.optString("code");
		if (!jsonObj.optString("kcode").trim().equals("")) {
			if (jsonObj.optString("code").trim().equals("")) {
				objectID = jsonObj.optString("kcode");
				sqlStr = " where a.open_id = ? ";
			} else {
				// 不处理
			}
		} else {
			if (jsonObj.optString("code").trim().equals("")) {
				sqlStr = " where a.id <> ? ";
			} else {
				// 不处理
			}
		}
		String sql = "select a.id, a.open_id, a.create_date,a.task_status,c.content,c.msg_type,a.service_score from "
				+ getTableName("task_queue_all")
				+ " a left join "
				+ "(select task_queue_id,content,msg_type from(select * from "
				+ getTableName("task_message_all")
				+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ sqlStr
				+ " and a.service_id = ? and (a.task_status = ? or a.task_status = ?) "
				+ " order by create_date desc " + " limit ?, ? ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { objectID, getCurrentUser().getId(),
						TaskStatus.STOP, TaskStatus.COMPLETED, offset, rows },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setService_score(rs.getString("service_score"));
						return tq;
					}
				});
		return taskQueue;
	}

	/**
	 * 根据日期查找任务列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getCompleteAdvisoryByTaskDateCount(
			JSONObject jsonObj) throws Exception {
		String tmpSql = " and  1=1 ";
		if (jsonObj.optString("startDate") != ""
				&& jsonObj.optString("endDate") != "") {
			tmpSql = " and substring(a.create_date,1,10) between '"
					+ jsonObj.optString("startDate") + "' and '"
					+ jsonObj.optString("endDate") + "'";
		} else if (jsonObj.optString("startDate") == ""
				&& jsonObj.optString("endDate") != "") {
			tmpSql = " and substring(a.create_date,1,10) < '"
					+ jsonObj.optString("endDate") + "'";
		} else if (jsonObj.optString("startDate") != ""
				&& jsonObj.optString("endDate") == "") {
			tmpSql = " and substring(a.create_date,1,10) > '"
					+ jsonObj.optString("startDate") + "'";
		}
		String tmpType = " and 2=2 ";
		if (getCurrentUser().getType() != UserType.ADMIN) {
			tmpType = " and a.service_id = '" + getCurrentUser().getId() + "'";
		}
		String sql = "select a.id, a.open_id, a.create_date,b.nickname ,a.task_status,c.content,c.msg_type,a.service_score from "
				+ getTableName("task_queue")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				+ "(select task_queue_id,content,msg_type from(select * from "
				+ getTableName("task_message")
				+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ " where  (a.task_status = ? or a.task_status = ?) "
				+ tmpSql
				+ tmpType + " order by create_date desc ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { TaskStatus.COMPLETED, TaskStatus.STOP },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setService_score(rs.getString("service_score"));
						return tq;
					}
				});
		return taskQueue;
	}

	/**
	 * 根据日期查找任务列表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getCompleteAdvisoryByTaskDate(
			JSONObject jsonObj, int offset, int rows) throws Exception {
		String tmpSql = " and  1=1 ";
		if (jsonObj.optString("startDate") != ""
				&& jsonObj.optString("endDate") != "") {
			tmpSql = " and substring(a.create_date,1,10) between '"
					+ jsonObj.optString("startDate") + "' and '"
					+ jsonObj.optString("endDate") + "'";
		} else if (jsonObj.optString("startDate") == ""
				&& jsonObj.optString("endDate") != "") {
			tmpSql = " and substring(a.create_date,1,10) < '"
					+ jsonObj.optString("endDate") + "'";
		} else if (jsonObj.optString("startDate") != ""
				&& jsonObj.optString("endDate") == "") {
			tmpSql = " and substring(a.create_date,1,10) > '"
					+ jsonObj.optString("startDate") + "'";
		}
		String tmpType = " and 2=2 ";
		if (getCurrentUser().getType() != UserType.ADMIN) {
			tmpType = " and a.service_id = '" + getCurrentUser().getId() + "'";
		}
		String sql = "select a.id, a.open_id, a.create_date,b.nickname ,a.task_status,c.content,c.msg_type,a.service_score from "
				+ getTableName("task_queue")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				+ "(select task_queue_id,content,msg_type from(select * from "
				+ getTableName("task_message")
				+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ " where  (a.task_status = ? or a.task_status = ?) "
				+ tmpSql
				+ tmpType + " order by create_date desc " + " limit ?, ? ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { TaskStatus.COMPLETED, TaskStatus.STOP, offset,
						rows }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setService_score(rs.getString("service_score"));
						return tq;
					}
				});
		return taskQueue;
	}

	/**
	 * 我挂起的咨询
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getWaitAdvisory() throws Exception {
		String sql = " select d.id, d.open_id, d.create_date,d.nickname ,d.task_status,d.content,d.msg_type,d.message_create_date from "
				+ " (select a.id, a.open_id, a.create_date,b.nickname ,a.task_status,c.content,c.msg_type,c.create_date 'message_create_date' from "
				+ getTableName("task_queue")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				//+ "(select task_queue_id,content,msg_type,create_date 'message_create_date' from(select * from "
				+ getTableName("task_message")
				//+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ " where a.service_id = ? and a.task_status = ?"
				+ " order by c.create_date desc)"
				+ "d GROUP BY d.id ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { getCurrentUser().getId(), TaskStatus.WAIT },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						// System.getProperty("file.encoding");
						/*if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}*/
						tq.setNickname(rs.getString("open_id"));
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setMessage_create_date(rs.getString(
								"message_create_date").substring(0, 19));
						return tq;
					}
				});
		return taskQueue;
	}
	
	
	/**
	 * 所有挂起的咨询(关闭提醒)
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<TaskDisplayEntity> getAllWaitAdvisory() throws Exception {
		String sql = "select a.id, a.open_id, a.create_date,b.nickname ,a.task_status,c.content,c.msg_type,c.message_create_date,c.create_user,c.service_id from "
				+ getTableName("task_queue")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " left join "
				+ "(select task_queue_id,content,msg_type,create_date 'message_create_date',create_user,service_id from(select * from "
				+ getTableName("task_message")
				+ " order by create_date desc) b group by task_queue_id)"
				+ " c on a.id = c.task_queue_id "
				+ " where a.task_status = ?"
				+ " order by create_date desc ";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskDisplayEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { TaskStatus.WAIT },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskDisplayEntity tq = new TaskDisplayEntity();
						tq.setId(rs.getString("id"));
						tq.setOpen_id(rs.getString("open_id"));
						tq.setCreate_date(rs.getString("create_date")
								.substring(0, 19));
						// System.getProperty("file.encoding");
						if (rs.getString("nickname") != null) {
							tq.setNickname(rs.getString("nickname"));
						} else {
							tq.setNickname("");
						}
						tq.setTask_status(String.valueOf(rs
								.getInt("task_status")));
						// 过滤表情
						tq.setContent(WeixinHelper.parseEmotion(rs
								.getString("content")));
						tq.setMsg_type(rs.getString("msg_type"));
						tq.setMessage_create_date(rs.getString(
								"message_create_date").substring(0, 19));
						tq.setCreate_user(rs.getString("create_user"));
						tq.setService_id(rs.getString("service_id"));
						return tq;
					}
				});
		return taskQueue;
	}

	/**
	 * 根据账号加载消息
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> loadMessage(JSONObject jsonObj)
			throws Exception {
		String taskID = jsonObj.optString("taskID");
		String sql = " select * from "
				+ getTableName("task_message") 
				+ " where task_queue_id = ? " + " order by create_date desc ";
		List<Map<String, Object>> list_message = jdbcTemplate.query(sql,
				new Object[] { taskID }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> value = new HashMap<String, Object>();
						value.put("id", rs.getString("id"));
						value.put("task_queue_id",
								rs.getString("task_queue_id"));
						value.put("open_id", rs.getString("open_id"));
						value.put("service_id", rs.getString("service_id"));
						// 过滤表情
						value.put("content", WeixinHelper.parseEmotion(rs
								.getString("content")));
						value.put("data_read", rs.getInt("data_read"));
						value.put("create_user", rs.getString("create_user"));
						value.put("create_date", rs.getString("create_date")
								.replace(".0", ""));
						//迁移了value.put("nick_name", rs.getString("nickname"));
						value.put("nick_name", rs.getString("open_id"));
						return value;
					}
				});
		return list_message;
	}

	/**
	 * 根据账号加载消息
	 * QQ追加
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> loadMessage_Historyall(JSONObject jsonObj)
			throws Exception {
		String taskID = jsonObj.optString("taskID");
		String sql = " select a.*,b.nickname  from "
				+ getTableName("task_message_all") + " a left join "
				+ getTableName("users") + " b on a.open_id = b.openid "
				+ " where task_queue_id = ? " + " order by create_date desc ";
		List<Map<String, Object>> list_message = jdbcTemplate.query(sql,
				new Object[] { taskID }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> value = new HashMap<String, Object>();
						value.put("id", rs.getString("id"));
						value.put("task_queue_id",
								rs.getString("task_queue_id"));
						value.put("open_id", rs.getString("open_id"));
						value.put("service_id", rs.getString("service_id"));
						// 过滤表情
						value.put("content", WeixinHelper.parseEmotion(rs
								.getString("content")));
						value.put("data_read", rs.getInt("data_read"));
						value.put("create_user", rs.getString("create_user"));
						value.put("create_date", rs.getString("create_date")
								.replace(".0", ""));
						value.put("nick_name", rs.getString("nickname"));
						return value;
					}
				});
		return list_message;
	}
	
	/**
	 * 取得未读消息
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String, Object>> unReadMessage(JSONObject jsonObj)
			throws Exception {
		String taskID = jsonObj.optString("taskID");
		int offset = jsonObj.optInt("offset");
		int rows = jsonObj.optInt("rows") == 0 ? 10 : jsonObj.optInt("rows");
		String sql = " select a.*,b.nickname  from "
				+ getTableName("task_message") + " a left join "
				+ getTableName("users") + " b on a.open_id = b.openid "
				+ " where task_queue_id = ? " + " and data_read = 0 "
				+ " order by create_date desc limit ?, ? ";
		List<Map<String, Object>> list_message = jdbcTemplate.query(sql,
				new Object[] { taskID, offset, rows }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> value = new HashMap<String, Object>();
						value.put("id", rs.getString("id"));
						value.put("task_queue_id",
								rs.getString("task_queue_id"));
						value.put("open_id", rs.getString("open_id"));
						value.put("msg_type", rs.getString("msg_type"));
						value.put("service_id", rs.getString("service_id"));
						// 过滤表情
						value.put("content", WeixinHelper.parseEmotion(rs
								.getString("content")));
						value.put("data_read", rs.getInt("data_read"));
						value.put("create_date", rs.getString("create_date")
								.replace(".0", ""));
						value.put("nick_name", rs.getString("open_id"));
						return value;
					}
				});
		return list_message;
	}

	/**
	 * 取得最后聊天或者创建咨询时间跟系统时间差距小时数
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getInterval(JSONObject jsonObj) throws Exception {
		String sql1 = "select  TIMESTAMPDIFF(hour,create_date,now()) intervalCount  from "
				+ getTableName("task_message")
				+ " where task_queue_id = ? and open_id = ? "
				+ " order by create_date desc limit 0,1";
		String sql2 = "select  TIMESTAMPDIFF(hour,create_date,now())  intervalCount from "
				+ getTableName("task_queue") + " where id = ? ";
		List<Map<String, Object>> list1 = jdbcTemplate.query(
				sql1,
				new Object[] { jsonObj.optString("task_queue_id"),
						jsonObj.optString("open_id") }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("intervalCount", rs.getString("intervalCount"));
						return map;
					}
				});
		int timeout = 0;
		// 如果没有聊天记录
		if (list1.size() == 0) {
			List<Map<String, Object>> list2 = jdbcTemplate.query(sql2,
					new Object[] { jsonObj.optString("task_queue_id") },
					new RowMapper() {
						public Object mapRow(ResultSet rs, int arg1)
								throws SQLException {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("intervalCount",
									rs.getString("intervalCount"));
							return map;
						}
					});
			// 有任务数据
			if (list2.size() != 0) {
				timeout = Integer.valueOf(list2.get(0).get("intervalCount")
						.toString());
			}
		} else {// 有聊天记录
			timeout = Integer.valueOf(list1.get(0).get("intervalCount")
					.toString());
		}
		return timeout;
	}

	/**
	 * 设置非正常关闭原因
	 * @param list_message
	 * @return
	 * @throws Exception
	 */
	public int SetCause(JSONObject jsonObj) throws Exception {
		String sql = " update " + getTableName("task_queue")
				   + " set cause = ? where id = ? ";
		return jdbcTemplate.update(sql, new Object[]{ jsonObj.optString("cause"),jsonObj.optString("task_queue_id") });
	}
	
	/**
	 * 根据taskID更新消息为已读状态
	 * 
	 * @param list_message
	 * @return
	 * @throws Exception
	 */
	public int updateMessageStatus(String taskID) throws Exception {
		String sql = " update "
				+ getTableName("task_message")
				+ " set data_read = 1,modify_user=?,modify_date=? where data_read = 0 and task_queue_id = ? ";
		return jdbcTemplate.update(sql, new Object[] {
				getCurrentUser().getId(), getCurrentDate(), taskID });
	}

	/**
	 * 保存聊天信息
	 * 
	 * @param reqEvent
	 * @return
	 * @throws Exception
	 */
	public int saveMessage(JSONObject jsonObj) throws Exception {
		String sql = " insert into "
				+ getTableName("task_message")
				+ " (id, task_queue_id, open_id, service_id, content, msg_type, data_read,"
				+ " create_user, create_date) "
				//+ " values(?,?,?,?,?, ?,?,?,?) ";
				+ " select ?,?,?,?,?, ?,?,?,?  "
			    + " from dual ";
				//QQ删除
/*			    + "where not exists ( "
			    + " select id from wei_task_message "
			    + " where task_queue_id = ? and open_id = ? and service_id = ? and content = ? ) ";*/
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						getGUID(), 
						jsonObj.optString("task_queue_id"),
						jsonObj.optString("open_id"), 
						getCurrentUser().getId(),
						jsonObj.optString("content"),
						jsonObj.optString("msg_type"), 
						1,
						getCurrentUser().getId(), 
						getCurrentDate()
						//QQ删除
//						,
//						jsonObj.optString("task_queue_id"),
//						jsonObj.optString("open_id"),
//						getCurrentUser().getId(),
//						jsonObj.optString("content")
						}
				);
	}

	/**
	 * 分配变为处理中
	 * 
	 * @param taskID
	 * @return
	 * @throws Exception
	 */
	public int updateTaskStatus(String taskID) throws Exception {
		String sql = " update "
				+ getTableName("task_queue")
				+ " set task_status = ?, modify_user=?, modify_date=? where id = ? ";
		return jdbcTemplate.update(sql, new Object[] { TaskStatus.PROCESSING,
				getCurrentUser().getId(), getCurrentDate(), taskID });
	}

	/**
	 * 根据ID更新消息为已读状态
	 * 
	 * @param list
	 * @return
	 * @throws Exception
	 */
	public int[] ModifyMessageStatus(final List<Map<String, Object>> list)
			throws Exception {
		String sql = " update "
				+ getTableName("task_message")
				+ " set data_read = 1 ,modify_user=?, modify_date=? where id = ? ";
		return jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					@Override
					public int getBatchSize() {
						return list.size();
					}

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setString(1, getCurrentUser().getId());
						ps.setString(2, getCurrentDate());
						ps.setString(3, list.get(i).get("id").toString());
					}
				});
	}

	/**
	 * 根据OPENID取得所有咨询内容
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<UserDetail> getWeixinChatCount(JSONObject jsonObj)
			throws Exception {
		String sql = " select * from " + getTableName("user_detail")
				+ " where openid = ? ";
		return jdbcTemplate.query(sql,
				new Object[] { jsonObj.optString("open_id") }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						UserDetail ud = new UserDetail();
						ud.setTaskid(rs.getString("taskid"));
						ud.setOpenid(rs.getString("openid"));
						ud.setKey_account(rs.getString("key_account"));
						ud.setName(rs.getString("name"));
						ud.setSex(rs.getInt("sex"));
						ud.setAge(rs.getInt("age"));
						ud.setGrade(rs.getString("grade"));
						ud.setDepartment(rs.getString("department"));
						ud.setArea(rs.getString("area"));
						ud.setCellphone(rs.getString("cellphone"));
						ud.setEmail(rs.getString("email"));
						return ud;
					}
				});
	}

	/**
	 * 根据taskID取得咨询内容
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<UserDetail> getWeixinChatInfo(JSONObject jsonObj)
			throws Exception {
		String sql = " select * from " + getTableName("user_detail")
				+ " where taskid = ? ";
		return jdbcTemplate.query(sql,
				new Object[] { jsonObj.optString("task_queue_id") },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						UserDetail ud = new UserDetail();
						ud.setTaskid(rs.getString("taskid"));
						ud.setOpenid(rs.getString("openid"));
						ud.setKey_account(rs.getString("key_account"));
						ud.setName(rs.getString("name"));
						ud.setSex(rs.getInt("sex"));
						ud.setAge(rs.getInt("age"));
						ud.setGrade(rs.getString("grade"));
						ud.setDepartment(rs.getString("department"));
						ud.setArea(rs.getString("area"));
						ud.setCellphone(rs.getString("cellphone"));
						ud.setEmail(rs.getString("email"));
						return ud;
					}
				});
	}

	/**
	 * 根据openID取得咨询内容
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<UserDetail> getWeixinChatInfoByOpenID(JSONObject jsonObj)
			throws Exception {
		String sql = " select * from " + getTableName("user_detail")
				+ " where openid = ? " + " order by create_date desc "
				+ " limit 0,1 ";
		return jdbcTemplate.query(sql,
				new Object[] { jsonObj.optString("open_id") }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						UserDetail ud = new UserDetail();
						ud.setTaskid(rs.getString("taskid"));
						ud.setOpenid(rs.getString("openid"));
						ud.setKey_account(rs.getString("key_account"));
						ud.setName(rs.getString("name"));
						ud.setSex(rs.getInt("sex"));
						ud.setAge(rs.getInt("age"));
						ud.setGrade(rs.getString("grade"));
						ud.setDepartment(rs.getString("department"));
						ud.setArea(rs.getString("area"));
						ud.setCellphone(rs.getString("cellphone"));
						ud.setEmail(rs.getString("email"));
						return ud;
					}
				});
	}

	/**
	 * 更新用户信息
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public int updateUserInfo(JSONObject jsonObj) throws Exception {
		String sql = " update " + getTableName("user_detail")
				+ " set key_account= ?, name = ?, sex = ?,grade = ?, cellphone = ?, email = ?, modify_user = ?, modify_date = ? "
				+ " where openid = ? ";
		return jdbcTemplate.update(
				sql,
				new Object[] {
						jsonObj.optString("keyAccount"),
						jsonObj.optString("name"), 
						jsonObj.optString("sex"),
						jsonObj.optString("usertype"), 
						jsonObj.optString("cellphone"),
						jsonObj.optString("email"),
						getCurrentUser().getId(),
						getCurrentDate(),
						jsonObj.optString("open_id")});
	}
	
	
	/**
	 * 保存用户信息
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public int saveUserInfo(JSONObject jsonObj) throws Exception {
		String sql = " insert into "
				+ getTableName("user_detail")
				+ " (id, taskid, openid, key_account, name, sex, grade, cellphone, email, "
				+ " create_user, create_date) "
				+ " values(?,?,?,?,?, ?,?,?,?,? ,?) ";
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						getGUID(), 
						jsonObj.optString("task_queue_id"),
						jsonObj.optString("open_id"),
						jsonObj.optString("keyAccount"),
						jsonObj.optString("name"), 
						jsonObj.optInt("sex"),
						jsonObj.optString("usertype"),
						jsonObj.optString("cellphone"),
						jsonObj.optString("email"),
						getCurrentUser().getId(),
						getCurrentDate() });
	}
	
	/**
	 * 更新KDATA
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 *//*
	public int updateKData(JSONObject jsonObj) throws Exception {
		String sql = " update "
				+ getTableName("kdata")
				+ " set user_name = ?, gender = ?,user_type = ?, telephone_number = ?, email_address = ?, active_status = ?, modify_user = ?, modify_date = ? "
				+ " where user_code = ? ";
		return jdbcTemplate.update(
				sql,
				new Object[] {
						jsonObj.optString("name"), 
						jsonObj.optString("sex"),
						jsonObj.optString("usertype"), 
						jsonObj.optString("cellphone"),
						jsonObj.optString("email"),
						jsonObj.optString("activestatus"),
						getCurrentUser().getId(),
						getCurrentDate(),
						jsonObj.optString("keyAccount")});
	}

	*//**
	 * 插入KDATA
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 *//*
	public int saveKData(JSONObject jsonObj) throws Exception {
		String sql = " insert into "
				+ getTableName("kdata")
				+ " (id, user_code, user_name, gender, user_type, telephone_number, email_address, active_status, "
				+ " create_user, create_date) "
				+ " values(?,?,?,?,?, ?,?,?,?,?) ";
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						getGUID(), 
						jsonObj.optString("keyAccount"),
						jsonObj.optString("name"), 
						jsonObj.optString("sex"),
						jsonObj.optString("usertype"), 
						jsonObj.optString("cellphone"),
						jsonObj.optString("email"),
						jsonObj.optString("activestatus"),
						getCurrentUser().getId(),
						getCurrentDate() });
	}*/
	
	/**
	 * 更新任务表
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	public int updateTaskByID(JSONObject jsonObj) throws Exception {
		String sql = "update "
				+ getTableName("task_queue")
				+ " set rid = ?, type = ?, custom_score = ?, modify_user=?, modify_date=? "
				+ " where id = ? ";
		return jdbcTemplate.update(
				sql,
				new Object[] { jsonObj.optString("rid"),
						jsonObj.optString("type"),
						jsonObj.optString("customScore"),
						getCurrentUser().getId(), getCurrentDate(),
						jsonObj.optString("task_queue_id"), });
	}

	/**
	 * 自动挂起的任务
	 * 
	 * @param Interval
	 * @return
	 * @throws Exception
	 */
	public int ModifyTaskStatusForWait(String taskID,String Interval) throws Exception {
		String sql = "update "
				+ getTableName("task_queue")
				+ " set task_status = ?, modify_user=?, modify_date=? "
				+ " where id in ("
				+ " select a.task_queue_id from "
				+ " (select task_queue_id,create_date from "
				+ getTableName("task_message")
				+ " where task_queue_id = ? "
				+ " order by create_date desc limit 0,1) a "
				+ " where timestampdiff(minute, a.create_date, sysdate()) > ? )";
		return jdbcTemplate.update(sql, new Object[] { TaskStatus.WAIT,
				getCurrentUser().getId(), getCurrentDate(),
				taskID, Interval });
	}

	/**
	 * 插入系统消息
	 * 
	 * @param taskQueue
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public int addSystemMessage(Map<String, Object> taskQueue,String message) throws Exception {		
		String sql = "insert into "	+ getTableName("task_message")
				   + " (id, task_queue_id, open_id, service_id, content, " 
				   + " msg_type, create_user, create_date) "				  
				   + " values(?,?,?,?,?, ?,?,? ) ";
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						getGUID(),
						taskQueue.get("task_queue_id"),
						taskQueue.get("openID"),
						taskQueue.get("service_id"), 
						message,
						"text",
						"system",
						getCurrentDate() });
	}

	/**
	 * 查找需要挂起的微信用户
	 * 
	 * @param jsonObj
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getOpenIDForWait(String Interval)
			throws Exception {
		String sql = " select a.open_id,a.task_queue_id,a.service_id from "
				+ " (select open_id,task_queue_id,service_id,create_date from " + getTableName("task_message")
				+ " where task_queue_id in (select id  from "
				+ getTableName("task_queue")
				+ " where task_status = ?  or task_status = ?) "
				+ " order by create_date desc limit 0,1) a "
				+ " where timestampdiff(minute, a.create_date, sysdate()) > ? ";
		return jdbcTemplate.query(sql, new Object[] { TaskStatus.DISPATCH,
				TaskStatus.PROCESSING, Interval }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("openID", rs.getString("open_id"));
				value.put("task_queue_id", rs.getString("task_queue_id"));
				value.put("service_id", rs.getString("service_id"));
				return value;
			}
		});
	}

	/**
	 * 手动设置状态(挂起/终止)
	 * 
	 * @param taskID
	 * @return
	 * @throws Exception
	 */
	public int setStatus(int status, String taskID) throws Exception {
		int updcnt = 0;
		String sql = " update "
				+ getTableName("task_queue")
				+ " set task_status = ?, modify_user=?, modify_date=? where id = ? ";
//QQ追加 
		if(status == 3){
			//int updcnt = 0;
			 updcnt = jdbcTemplate.update(sql, new Object[] { status,
					getCurrentUser().getId(), getCurrentDate(), taskID });
			if(updcnt>0){
				int intq = synBackOldTaskQueueData(taskID);
				int intm = synBackOldTaskQueueMessageData(taskID);
				int delQ = synDelTaskQueueData(taskID);
				int delM = synDelTaskQueueMessageData(taskID);
				System.out.println("insertQ:"+intq);
				System.out.println("insertM:"+intm);
				System.out.println("delQ:"+delQ);
				System.out.println("delM:"+delM);
				log.info("INTQ"+ intq );
				log.info("INTM"+ intm );
				log.info("delQ"+ delQ );
				log.info("delM"+ delM );
			}
		}else {
			updcnt =  jdbcTemplate.update(sql, new Object[] { status,
					getCurrentUser().getId(), getCurrentDate(), taskID });
		}
		return updcnt;
	}

	/**
	 * 自动设置状态(挂起/终止)
	 * 
	 * @param status
	 * @param taskID
	 * @return
	 * @throws Exception
	 */
	public int setStatusAuto(int status, String taskID) throws Exception {
		int updcnt = 0;
		String sql = " update "
				+ getTableName("task_queue")
				+ " set task_status = ?, modify_user=?, modify_date=? where id = ? ";

		
		//QQ追加 
				if(status == 3){
					//int updcnt = 0;
					 updcnt = jdbcTemplate.update(sql, new Object[] { status, "admin_name",
								getCurrentDate(), taskID });
					if(updcnt >0){
						int intq = synBackOldTaskQueueData(taskID);
						int intm = synBackOldTaskQueueMessageData(taskID);
						int delQ = synDelTaskQueueData(taskID);
						int delM = synDelTaskQueueMessageData(taskID);
						System.out.println("admininsertQ:"+intq);
						System.out.println("admininsertM:"+intm);
						System.out.println("admindelQ:"+delQ);
						System.out.println("admindelM:"+delM);
						log.info("INTQ"+ intq );
						log.info("INTM"+ intm );
						log.info("delQ"+ delQ );
						log.info("delM"+ delM );
					}
				}else {
					updcnt = jdbcTemplate.update(sql, new Object[] { status, "admin_name",
							getCurrentDate(), taskID });
				}
				return updcnt;
	}

	/**
	 * 取得创建时间最早的一个未分配任务并更新
	 * 
	 * @return
	 * @throws Exception
	 */
	public int modifyOneOfUnProcess() throws Exception {
		String str = " and weichat_type =  '" + getCurrentUser().getWeichat_type() + "'";
		if("az_en".equals(getCurrentUser().getWeichat_type())){
			str = "";
		}
		String sql = "update "
				+ getTableName("task_queue")
				+ " set task_status = ?, service_id = ? , modify_user=?, modify_date=? "
				+ " where task_status = ? " + str + " order by create_date asc limit 1 ";
		return jdbcTemplate.update(sql, new Object[] { 
				TaskStatus.DISPATCH,
				getCurrentUser().getId(), 
				getCurrentUser().getId(),
				getCurrentDate(), 
				TaskStatus.UN_PROCESS
				});
	}

	/**
	 * 取得未分配任务
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getUnProcessTask() throws Exception {
		String sql = " select id from " + getTableName("task_queue")
				+ " where task_status = ? ";
		return jdbcTemplate.query(sql, new Object[] { TaskStatus.UN_PROCESS },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> value = new HashMap<String, Object>();
						value.put("id", rs.getString("id"));
						return value;
					}
				});
	}

	/**
	 * 根据任务队列ID更新任务消息表
	 * 
	 * @param list_update_task
	 * @return
	 * @throws Exception
	 */
	public int[] modifyTaskMessage(
			final List<TaskDisplayEntity> list_update_task) throws Exception {
		String sql = "update " + getTableName("task_message")
				+ " set service_id = ?, modify_user=?, modify_date=? "
				+ " where task_queue_id = ? and service_id = ''";
		return jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					@Override
					public int getBatchSize() {
						return list_update_task.size();
					}

					@Override
					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						TaskDisplayEntity taskQueueEntity = (TaskDisplayEntity) list_update_task
								.get(i);
						ps.setString(1, getCurrentUser().getId());
						ps.setString(2, getCurrentUser().getId());
						ps.setString(3, getCurrentDate());
						ps.setString(4, taskQueueEntity.getId());
					}
				});
	}

	/**
	 * 根据ID修改客服评分
	 * 
	 * @param task_queue_id
	 * @param service_score
	 * @return
	 * @throws Exception
	 */
	public int appraise(String task_queue_id, String openid,
			Integer service_score, String asses) throws Exception {
		String sql = " update "
				+ getTableName("task_queue_all")
				+ " set service_score = ? ,custom_asses_desc = ? , modify_user = ? , modify_date = ? where id = ? ";
		return jdbcTemplate.update(sql, new Object[] { service_score, asses,
				openid, getCurrentDate(), task_queue_id });
	}

	/**
	 * 根据ID查找任务列表
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TaskQueueEntity> getTaskQueueByID(String id) throws Exception {
		String sql = " select open_id,task_status,rid,type,custom_score,weichat_type from " + getTableName("task_queue")
				+ " where id = ? ";
		return jdbcTemplate.query(sql, new Object[] { id }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setOpen_id(rs.getString("open_id"));
				tq.setTask_status(rs.getInt("task_status"));
				tq.setRid(rs.getString("rid"));
				tq.setType(rs.getString("type"));
				tq.setCustom_score(rs.getString("custom_score"));
				tq.setWeichat_type(rs.getString("weichat_type"));
				return tq;
			}
		});
	}

	/**
	 * 根据状态取得当日任务的数量
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TaskQueueEntity> getTodayCountByStatus(int status)
			throws Exception {
		String sql = " select * from "
				+ getTableName("task_queue")
				+ " where task_status = ? and substring(create_date,1,10) = substring(sysdate(),1,10)";
		return jdbcTemplate.query(sql, new Object[] { status },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskQueueEntity tq = new TaskQueueEntity();
						tq.setOpen_id(rs.getString("open_id"));
						tq.setTask_status(rs.getInt("task_status"));
						return tq;
					}
				});
	}

	/**
	 * 取得当日用户请求数量
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TaskQueueEntity> getRequestCount() throws Exception {
//		String sql = " select * from "
//				+ getTableName("task_queue_all")
//				+ " where substring(create_date,1,10) = substring(sysdate(),1,10)";
		
		String sql = " select * from " + getTableName("task_queue")
				+ " where substring(create_date,1,10) = substring(sysdate(),1,10)" 
				+ " union all " + " select * from "
				+ getTableName("task_queue_all") 
				+ " where substring(create_date,1,10) = substring(sysdate(),1,10)";
		return jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setOpen_id(rs.getString("open_id"));
				tq.setTask_status(rs.getInt("task_status"));
				return tq;
			}
		});
	}

	/**
	 * 取得当日活跃用户数量
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TaskQueueEntity> getUserActiveCount() throws Exception {
		String sql = " select distinct open_id from "
				+ getTableName("task_queue_all")
				+ " where substring(create_date,1,10) = substring(sysdate(),1,10)";
		return jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setOpen_id(rs.getString("open_id"));
				return tq;
			}
		});
	}

	/**
	 * 根据时间点取得当日任务数量
	 * 
	 * @param time
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TaskQueueEntity> getTaskCountByTime(int time) throws Exception {
		String sql = " select * from " + getTableName("task_queue_all")
		// + " where substring(create_date,1,10) = substring(sysdate(),1,10) "
		// + " and substring(create_date,11,3) = ? ";
				+ " where day(create_date)=day(now()) and hour(create_date) = ?";
		return jdbcTemplate.query(sql, new Object[] { time }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setOpen_id(rs.getString("open_id"));
				return tq;
			}
		});
	}

	/**
	 * 取得当日客服状态
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getServiceStatus() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List nameList = new ArrayList();
		List idList = new ArrayList();
		List waitList = new ArrayList();
		List processList = new ArrayList();
		String sql = "select distinct service_id  from "
				+ getTableName("task_queue")
				+ " where substring(create_date,1,10) = substring(sysdate(),1,10) "
				+ " and task_status <> ? ";
		List<TaskQueueEntity> taskQueue = jdbcTemplate.query(sql,
				new Object[] { TaskStatus.UN_PROCESS }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskQueueEntity tq = new TaskQueueEntity();
						tq.setService_id(rs.getString("service_id"));
						return tq;
					}
				});
		for (int i = 0; i < taskQueue.size(); i++) {
			idList.add(taskQueue.get(i).getService_id());
			// 取得客服姓名
			nameList.add(getServiceNameByServiceID(
					taskQueue.get(i).getService_id()).get(0).get("name"));
			// 等待的状态
			waitList.add(getWaitingTodayCountByStatusAndServiceID(
					TaskStatus.WAIT, taskQueue.get(i).getService_id()).size());
			// 处理中的状态
			processList.add(getProcessingTodayCountByStatusAndServiceID(
					TaskStatus.DISPATCH, TaskStatus.PROCESSING,
					taskQueue.get(i).getService_id()).size());
		}
		map.put("idList", idList);
		map.put("nameList", nameList);
		map.put("waitList", waitList);
		map.put("processList", processList);
		return map;
	}
	
	/**
	 * 取得在线客服状态
	 * @param allPrincipals
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getServiceStatusOnline(List<Object> allPrincipals) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List nameList = new ArrayList();
		List idList = new ArrayList();
		List waitList = new ArrayList();
		List processList = new ArrayList();
		for (int i = 0; i < allPrincipals.size(); i++) {
			Object obj = allPrincipals.get(i);
			idList.add(((PlatformUserEntity)obj).getId());
			// 取得客服姓名
			nameList.add(((PlatformUserEntity)obj).getName());
			// 等待的状态
			waitList.add(getWaitingTodayCountByStatusAndServiceID(TaskStatus.WAIT, ((PlatformUserEntity)obj).getId()).size());
			// 处理中的状态
			processList.add(getProcessingTodayCountByStatusAndServiceID(TaskStatus.DISPATCH, TaskStatus.PROCESSING,((PlatformUserEntity)obj).getId()).size());
		}
		map.put("idList", idList);
		map.put("nameList", nameList);
		map.put("waitList", waitList);
		map.put("processList", processList);
		return map;
	}

	/**
	 * 根据状态以及客服ID取得当日任务
	 * 
	 * @param status
	 * @param serviceID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TaskQueueEntity> getProcessingTodayCountByStatusAndServiceID(
			int status1, int status2, String serviceID) throws Exception {
		String sql = " select * from "
				+ getTableName("task_queue")
				+ " where ( task_status = ? or task_status = ? ) and service_id = ? and substring(create_date,1,10) = substring(sysdate(),1,10)";
		return jdbcTemplate.query(sql, new Object[] { status1, status2,
				serviceID }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setOpen_id(rs.getString("open_id"));
				return tq;
			}
		});
	}

	/**
	 * 根据状态以及客服ID取得当日任务
	 * 
	 * @param status
	 * @param serviceID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<TaskQueueEntity> getWaitingTodayCountByStatusAndServiceID(
			int status, String serviceID) throws Exception {
		String sql = " select * from "
				+ getTableName("task_queue")
				+ " where task_status = ? and service_id = ? and substring(create_date,1,10) = substring(sysdate(),1,10)";
		return jdbcTemplate.query(sql, new Object[] { status, serviceID },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						TaskQueueEntity tq = new TaskQueueEntity();
						tq.setOpen_id(rs.getString("open_id"));
						return tq;
					}
				});
	}

	/**
	 * 根据客服ID取得姓名
	 * 
	 * @param serviceID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getServiceNameByServiceID(String serviceID)
			throws Exception {
		String sql = " select name from " + getTableName("platform_users")
				+ " where id = ? ";
		return jdbcTemplate.query(sql, new Object[] { serviceID },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("name", rs.getString("name"));
						return map;
					}
				});
	}

	/**
	 * 取得所有文章类型
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getAllArticleCatagories() {
		String sql = "select * from " + getTableName("article_catagories");
		return jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rs.getString("id"));
				map.put("name", rs.getString("name"));
				return map;
			}
		});
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,Object>> getAllArticleCatagoriesBySectionID(String sectionID){
		String sql = "select * from " + getTableName("article_catagories") 
				+ " where section_id = ? order by create_date" ;
		return jdbcTemplate.query(sql, new Object[] { sectionID }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rs.getString("id"));
				map.put("name", rs.getString("name"));
				return map;
			}
		});
	}
	
	/**
	 * 根据ID查找菜单名
	 * @param sectionID
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,Object>> getSectionsNameByID(String sectionID) {
		String sql ="select * from " + getTableName("article_sections")
				+ " where id = ? ";
		return jdbcTemplate.query(sql, new Object[] { sectionID }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", rs.getString("id"));
				map.put("name", rs.getString("name"));
				return map;
			}
		});
	}

	/**
	 * 根据类型ID取相关文章
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ArticleEntity> getArticleList(String id) {
		String sql = "select * from " + getTableName("article")
				+ " where catagory_id = ? and status = 1 order by create_date ";
		return jdbcTemplate.query(sql, new Object[] { id }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				ArticleEntity article = new ArticleEntity();
				article.setId(rs.getString("id"));
				article.setTitle(rs.getString("title"));
				article.setContent(rs.getString("content"));
				return article;
			}
		});

	}

	/**
	 * 根据类型ID取文章
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<ArticleEntity> getArticleByID(String id) {
		String sql = "select * from " + getTableName("article")
				+ " where id = ? and status = ? ";
		return jdbcTemplate.query(sql, new Object[] { id,1 }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				ArticleEntity article = new ArticleEntity();
				article.setId(rs.getString("id"));
				article.setTitle(rs.getString("title"));
				article.setContent(rs.getString("content"));
				return article;
			}
		});
	}

	/**
	 * 根据ID查找平台账户表
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<PlatformUserEntity> getPlatformUsersByID(String id) {
		String sql = "select * from " + getTableName("platform_users")
				+ " where id = ? ";
		return jdbcTemplate.query(sql, new Object[] { id }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				PlatformUserEntity platformUserEntity = new PlatformUserEntity();
				platformUserEntity.setId(rs.getString("id"));
				platformUserEntity.setAccount(rs.getString("account"));
				platformUserEntity.setName(rs.getString("name"));
				platformUserEntity.setWeichat_type("weichat_type");
				return platformUserEntity;
			}
		});
	}

	/**
	 * 个人月数据统计
	 * 
	 * @param service_id
	 * @param date
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getPersonalMonthDataStatistics(
			String service_id, String date) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 个人月数据统计总的请求单数
		int sumCount = 0;
		// 请求单数
		List countList = new ArrayList();
		List dateList = new ArrayList();
		// 1. 判断选择的月份有多少天
		String new_date = date.substring(0, date.indexOf("-") + 3);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(new_date.substring(0, 4)));
		cal.set(Calendar.MONTH, Integer.parseInt(new_date.substring(
				new_date.indexOf("-") + 1, new_date.length())) - 1);
		int day = cal.getActualMaximum(Calendar.DATE);// 当前月最大天数

		int year = Integer.valueOf(date.substring(0, 4));
		int month = Integer.valueOf(date.substring(5, 7));

		// 2. 循环取出每一天的请求单数
		for (int i = 0; i < day; i++) {
			dateList.add(i, i + 1);
			StringBuilder sb = new StringBuilder();
			sb.append(year);
			if (month < 10) {
				sb.append(0);
			}
			sb.append(month);
			if (i < 10) {
				sb.append(0);
				sb.append(i + 1);
			} else {
				sb.append(i + 1);
			}
			String newDate = sb.toString();
			String sql = "select count(*) 'count' from "
					+ getTableName("task_queue_all")
					+ " where DATE_FORMAT(create_date,'%Y%m%d') = ? and service_id = ?";
			@SuppressWarnings("unchecked")
			List<Integer> counts = jdbcTemplate.query(sql, new Object[] {
					newDate, service_id }, new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					return rs.getInt("count");
				}
			});
			if (counts.size() > 0) {
				countList.add(counts.get(0));
				if (counts.get(0) > 0) {
					for (int j = 0; j < counts.get(0); j++) {
						sumCount++;
					}
				}
			} else {
				countList.add(0);
			}
		}
		map.put("sumCount", sumCount);
		map.put("dateList", dateList);
		map.put("countList", countList);
		return map;
	}

	/**
	 * 请求类型分析
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> searchRequestTypes() {
		String sql = "select count(type) taskNum,type "
				+ " from wei_task_queue_all "
				+ " where day(create_date)=day(now()) ";
		sql += " group by type ";
		List<Object> list = jdbcTemplate.query(sql, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				List<Object> tempList = new ArrayList<Object>();
				/*if(rs.getString("type")!= ""){
					tempList.add("Unknown Type");
				} else {*/
					tempList.add(rs.getString("type"));
				/*}*/
				tempList.add(rs.getInt("taskNum"));
				return tempList;
			}

		});
		return list;
	}

	/**
	 * 流水
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getRequestInfoList() {
		String sql = "select tq.id,tq.open_id,tq.service_id,tq.modify_date,tq.create_date,ud.nickname userName,pu.name serverName,tq.task_status from "
				//+ getTableName("task_queue")
				+ " (SELECT id,open_id,service_id,task_status,service_score,custom_asses_desc,custom_score,rid,type,cause,weichat_type,create_user,create_date,modify_user,modify_date "
                + " FROM"
                + " wei_task_queue t "
                + " union all"
                + " SELECT id,open_id,service_id,task_status,service_score,custom_asses_desc,custom_score,rid,type,cause,weichat_type,create_user,create_date,modify_user,modify_date"
                + " FROM"
	            + " wei_task_queue_all t)"
				+ " tq left join "
				+ getTableName("users")
				+ " ud on tq.open_id = ud.openid "
				+ " left join "
				+ getTableName("platform_users")
				+ " pu on tq.service_id = pu.id"
				+ " where task_status in (0,1,2,3)"
				+ " and (day(tq.create_date)=day(now()) or day(tq.modify_date)=day(now())) " 
				+ " and (month(tq.create_date) = month(now()) or month(tq.modify_date) = month(now()))"
				+ " and (year(tq.create_date) = year(now()) or year(tq.modify_date) = year(now())) "
				// +
				// " and (TIMESTAMPDIFF(MINUTE,tq.create_date,now()) < 1 or TIMESTAMPDIFF(MINUTE,tq.modify_date,now()) < 1) "
				// + " group by service_id"
				+ " order by tq.modify_date desc,tq.create_date desc ";

		return jdbcTemplate.query(sql, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("openId", rs.getString("open_id"));
				map.put("serviceId", rs.getString("service_id"));
				map.put("userName", rs.getString("userName"));
				map.put("serverName", rs.getString("serverName"));
				map.put("taskStatus", rs.getString("task_status"));
				if(rs.getString("modify_date")!=null){
					map.put("timeing", rs.getString("modify_date").substring(0, 19));
				} else {
					map.put("timeing", rs.getString("create_date").substring(0, 19));
				}
				return map;
			}

		});
	}

	/**
	 * 获取所有超过规定时间而没有反馈的请求单
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TaskQueueEntity> getCloseResquest() throws Exception {

		String sql = " select a.*,d.* from " + getTableName("task_queue")
				+ " a left join ( ( select b.* from "
				+ getTableName("task_message") + " b left join "
				+ getTableName("task_queue")
				+ " c on b.task_queue_id = c.id ORDER BY b.create_date DESC "
				+ " ) d ) on a.id = d.task_queue_id "
				+ " where HOUR(TIMEDIFF(NOW(),d.create_date)) > 23 "
				+ " and a.task_status <> 3 GROUP BY a.id ";
		return jdbcTemplate.query(sql, new Object[] {}, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setId(rs.getString("id"));
				tq.setOpen_id(rs.getString("a.open_id"));
				return tq;
			}
		});
	}

	/**
	 * 正在咨询的数量
	 * 
	 * @param serviceID
	 * @return
	 * @throws Exception
	 */
	public List<TaskQueueEntity> getTaskQueueCountByServiceID()
			throws Exception {
		String sql = "select id from "
				+ getTableName("task_queue")
				+ " where service_id = ? and task_status <> ? and task_status <> ? and task_status <> ?";
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<TaskQueueEntity> taskQueue = jdbcTemplate.query(sql, new Object[] {
				getCurrentUser().getId(), TaskStatus.COMPLETED,
				TaskStatus.STOP, TaskStatus.WAIT }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				TaskQueueEntity tq = new TaskQueueEntity();
				tq.setId(rs.getString("id"));
				return tq;
			}
		});
		return taskQueue;
	}

	/**
	 * 获取客服最后一次聊天时间
	 * 
	 * @param serviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getLastMessage(String serviceId) {
		String sql = "select create_date from " + getTableName("task_message_all")
				+ " where create_user=? order by create_date desc limit 0,1";
		List<String> list = jdbcTemplate.query(sql, new Object[] { serviceId },
				new RowMapper() {

					@Override
					public String mapRow(ResultSet rs, int arg1)
							throws SQLException {
						return rs.getString("create_date");
					}

				});
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * 获取客服操作queen表的最后一次时间
	 * 
	 * @param serviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getLastOperateTime(String serviceId) {
		String sql = "select modify_date from " + getTableName("task_message_all")
				+ " where modify_user=? order by modify_date desc limit 0,1";
		List<String> list = jdbcTemplate.query(sql, new Object[] { serviceId },
				new RowMapper() {

					@Override
					public String mapRow(ResultSet rs, int arg1)
							throws SQLException {
						return rs.getString("modify_date");
					}

				});
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * 获取客服最后一次保存的时间
	 * 
	 * @param serviceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getLastSaveTime(String serviceId) {
		String sql = "select create_date from " + getTableName("user_detail")
				+ " where create_user=? order by create_date desc limit 0,1";
		List<String> list = jdbcTemplate.query(sql, new Object[] { serviceId },
				new RowMapper() {

					@Override
					public String mapRow(ResultSet rs, int arg1)
							throws SQLException {
						return rs.getString("create_date");
					}

				});
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	/**
	 * 获取请求类型数据tree
	 * 
	 * @return
	 */
	public List<HashMap<String, String>> getRequestType() {
		String sql = "select * from " + getTableName("request_type")
				+ " order by parent_id";
		@SuppressWarnings("unchecked")
		List<HashMap<String, String>> list = jdbcTemplate.query(sql,
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("id", rs.getString("id"));
						map.put("description", rs.getString("description"));
						map.put("parentId", rs.getString("parent_id"));
						return map;
					}
				});
		return list;
	}

	public int queryServiceCount() {
		String sql = "select count(0) from " + getTableName("platform_users")
				+ " where type = " + UserType.SERVICE_AMATEUR + " or type= "
				+ UserType.SERVICE_STANDARD;

		@SuppressWarnings("unchecked")
		List<Integer> countList = jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt(1);
			}
		});
		if (countList.size() > 0)
			return countList.get(0);

		return 0;
	}

	public List<HashMap<String, Object>> queryService(int offset, int rows) {
		String sql = "select * from " + getTableName("platform_users")
				+ " where type = " + UserType.SERVICE_AMATEUR + " or type= "
				+ UserType.SERVICE_STANDARD + " limit ?, ? ";

		@SuppressWarnings("unchecked")
		List<HashMap<String, Object>> list = jdbcTemplate.query(sql,
				new Object[] { offset, rows }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", rs.getString("id"));
						map.put("account", rs.getString("account"));
						map.put("name", rs.getString("name"));
						map.put("sex", rs.getInt("sex"));
						map.put("mobile_phone", rs.getString("mobile_phone"));
						return map;
					}
				});

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> getCurrentLeader(String serviceId) {
		String sql = "select leader ,pu.account,pu.`name`,pu.sex,pu.mobile_phone "
				+ " from wei_seat_group sg "
				+ " left join wei_seat_group_relation sgr "
				+ " on sg.id = sgr.group_id "
				+ " left join wei_platform_users pu  "
				+ " on sg.leader = pu.id " + " where sgr.user_id = ?";
		List<HashMap<String, Object>> list = jdbcTemplate.query(sql,
				new Object[] { serviceId }, new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", rs.getString(1));
						map.put("account", rs.getString(2));
						map.put("name", rs.getString(3));
						map.put("sex", rs.getString(4));
						map.put("mobile_phone", rs.getString(5));
						return map;
					}

				});
		return list;
	}

	public void transferTask(final JSONArray taskID, final JSONArray leaderId,
			final JSONArray transferToId) {
		String sql = "insert into "
				+ getTableName("transfer_task")
				+ " (id,task_id,transfer_to_server_id,transfer_from_server_id,leader_id,create_user,create_date) "
				// + " values(?,?,?,?,?,?,?)";
				+ " select ?,?,?,?,?,?,? from Dual " + " where not exists"
				+ " (select * from " + getTableName("transfer_task")
				+ " where task_id=? and status=?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return taskID.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setString(1, getGUID());
				ps.setString(2, taskID.getString(i));
				ps.setString(3, transferToId.getString(0));
				ps.setString(4, getCurrentUser().getId());
				ps.setString(5, leaderId.getString(0));
				ps.setString(6, getCurrentUser().getName());
				ps.setString(7, getCurrentDate());
				ps.setString(8, taskID.getString(i));
				ps.setString(9, "1");
			}

		});
	}

	@SuppressWarnings("unchecked")
	public Object getTransferingTaskCount() {
		String sql = "select count(0) from " + getTableName("transfer_task")
				+ " where status=? and leader_id=?";
		List<Integer> list = jdbcTemplate.query(sql,new Object[]{1,getCurrentUser().getId()}, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt(1);
			}

		});
		return list.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> getTransferingTaskList(int offset,
			int rows) {
		String sql = "select tt.id,u.nickname,tq.create_date,tq.task_status,pu.`name`,pu2.`name`,tt.transfer_to_server_id,tt.task_id from "
				+ " wei_transfer_task tt "
				+ " left join wei_task_queue tq "
				+ " on tq.id = tt.task_id "
				+ " left join wei_users u "
				+ " on u.openid = tq.open_id "
				+ " left join wei_platform_users pu  "
				+ " on pu.id = tt.transfer_from_server_id "
				+ " left join wei_platform_users pu2 "
				+ " on pu2.id = tt.transfer_to_server_id  " + " where status=? and leader_id=?";
		List<HashMap<String, Object>> list = jdbcTemplate.query(sql,new Object[]{1,getCurrentUser().getId()},
				new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("id", rs.getString(1));
						map.put("nickname", rs.getString(2));
						map.put("createDate", rs.getString(3));
						map.put("status", rs.getString(4));
						map.put("fromUser", rs.getString(5));
						map.put("toUser", rs.getString(6));
						map.put("toUserId", rs.getString(7));
						map.put("taskId", rs.getString(8));
						return map;
					}

				});
		return list;
	}

	public void cancleTransfer(final JSONArray ids) {
		String sql = "update " + getTableName("transfer_task")
				+ " set status=?,modify_user=?,modify_date=? where id=?";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return ids.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setInt(1, 0);
				ps.setString(2, getCurrentUser().getId());
				ps.setString(3, getCurrentDate());
				ps.setString(4, ids.getString(i));
			}

		});
	}

	public void acceptTransferTask(final JSONArray taskIds,
			final JSONArray toUserIds) {
		String sql = "update " + getTableName("task_queue")
				+ " set service_id=?,modify_user=?,modify_date=? where id=?";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public int getBatchSize() {
				return taskIds.size();
			}

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				ps.setString(1, toUserIds.getString(0));
				ps.setString(2, getCurrentUser().getId());
				ps.setString(3, getCurrentDate());
				ps.setString(4, taskIds.getString(i));
			}

		});
	}

	/**
	 * 查找KDATA
	 * @param kid
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> sreachKData(String kid){
		String sql = "select user_name,gender,user_type,telephone_number,email_address,active_status from " + getTableName("kdata")
				+ " where user_code = ? ";
		List<Map<String,Object>> maplist = jdbcTemplate.query(sql,
				new Object[] { kid }, 
				new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						Map<String,Object> value = new HashMap<String,Object>();
						value.put("name", rs.getString("user_name"));
						value.put("sex", rs.getString("gender"));
						value.put("usertype", rs.getString("user_type"));
						value.put("cellphone", rs.getString("telephone_number"));
						value.put("email", rs.getString("email_address"));
						value.put("activestatus", rs.getString("active_status"));
						return value;
					}
				});
		if(maplist.size()>0){
			return maplist.get(0);
		}
		return null;
	}
	
	/**
	 * 更新文章阅读数
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updateReadNumber(String ArticleID) throws Exception {		
		String sql = " update "	+ getTableName("article")
				   + " set number = number + 1 "
				   + " where id = ? ";	
		return jdbcTemplate.update(sql,new Object[] {ArticleID});
	}
	
	/**
	 * 查找文章阅读数
	 * @param ArticleID
	 * @return
	 * @throws Exception
	 */
	public int getReadNumber(String ArticleID) throws Exception {
		String sql = " select number from " + getTableName("article")
				+ " where id = ? ";
		return jdbcTemplate.queryForInt(sql, ArticleID);
	}
	
	/**
	 * 根据OPENID查找公众平台应用类型
	 * @param openID
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String,Object>> getTaskQueueByOpenID(String openID) throws Exception {
		String sql = " select weichat_type from " + getTableName("task_queue")
				+ " where open_id = ? " 
				+ " order by create_date desc "
				+ " limit 0,1";
		return jdbcTemplate.query(sql,new Object[] { openID }, 
				new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						Map<String,Object> value = new HashMap<String,Object>();
						value.put("weichat_type", rs.getString("weichat_type"));
						return value;
					}
				}); 
	}
	
	/**
	 * 更改客服繁忙状态位
	 * @param busyStatus
	 * @return
	 * @throws Exception
	 */
	public int updatePlatfromBusyStatus(String busyStatus) throws Exception {
		String sql = " update " + getTableName("platform_users")
				+ " set busy = ? " 
				+ " where id = ? ";
		return jdbcTemplate.update(sql, new Object[] {busyStatus,getCurrentUser().getId()});
	}
	
	/**
	 * 根据ID查找客服繁忙状态
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getPlatfromBusyStatus(String id) throws Exception{
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
	
	/**
	 * 备份任务列表数据
	 * @return
	 */
	public int synBackOldTaskQueueData(String taskID) {
		String sql = "insert into "
				+ getTableName("task_queue_all")
				+ " (id,open_id,service_id,task_status,service_score,custom_asses_desc,custom_score,rid,type,cause,weichat_type,create_user,create_date,modify_user,modify_date) "
				+ " select id,open_id,service_id,task_status,service_score,custom_asses_desc,custom_score,rid,type,cause,weichat_type,create_user,create_date,modify_user,modify_date from " + getTableName("task_queue")
				+ " where task_status = 3 and id = ? ";
		return jdbcTemplate.update(sql,new Object[] {taskID});
	}
	
	/**
	 * 备份聊天消息
	 * @return
	 */
	public int synBackOldTaskQueueMessageData(String taskID) {
		String sql = "insert into "
				+ getTableName("task_message_all")
				+ " (id,task_queue_id,open_id,service_id,content,picurl,mediaid,format,msg_type,recognition,data_read,create_user,create_date,modify_user,modify_date) "
				+ " select id,task_queue_id,open_id,service_id,content,picurl,mediaid,format,msg_type,recognition,data_read,create_user,create_date,modify_user,modify_date from " + getTableName("task_message")
				+ " WHERE task_queue_id = ? ";
			//	+ " (select f.id from wei_task_queue f WHERE f.task_status = 3)";
		return jdbcTemplate.update(sql,new Object[] {taskID});
	}
	
	/**
	 * 删除任务列表数据
	 * @return
	 */		
	public int synDelTaskQueueData(String taskID) {
		String sql = "DELETE FROM wei_task_queue WHERE task_status = 3 and id = ? ";
		return jdbcTemplate.update(sql,new Object[] {taskID});
	}
	
	/**
	 * 删除任务列表数据
	 * @return
	 */
	public int synDelTaskQueueMessageData(String taskID) {
		String sql = "DELETE FROM wei_task_message WHERE task_queue_id and task_queue_id = ? ";
				//+ "(select f.id from wei_task_queue f WHERE f.task_status = 3 )";
		return jdbcTemplate.update(sql,new Object[] {taskID});
	}
}
