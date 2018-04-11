package com.yidatec.weixin.dao.seatmgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.yidatec.weixin.dao.DataBase;
import com.yidatec.weixin.entity.AdviserEntity;
import com.yidatec.weixin.entity.GroupEntity;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ScheduleEntity;
import com.yidatec.weixin.entity.UserScheduleEntity;
import com.yidatec.weixin.entity.message.TaskStatus;
import com.yidatec.weixin.entity.message.UserType;
import com.yidatec.weixin.util.Encrypt;

public class AdviserManageDao extends DataBase {

	/**
	 * 查询咨询师数量
	 * 
	 * @param qry_params
	 * @return
	 */
	public int queryAdviserCount(String qry_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(qry_params);
		// 判断参数有效性，如果没传值就赋值7
		String name = jsonObject.optString("name");
		if (StringUtils.isEmpty(name))
			name = "7";
		String sex = jsonObject.optString("sex");
		if (sex.equals("2"))
			sex = "7";
		String account = jsonObject.optString("account");
		if (StringUtils.isEmpty(account))
			account = "7";

		String sql = " select count(id) 'count' from "
				+ getTableName("platform_users") + " where ('" + name
				+ "' = '7' or name like ?) " + " and ('" + sex
				+ "' = '7' or sex = ?) " + " and ('" + account
				+ "' = '7' or account = ?) " + " and (type = "
				+ UserType.SERVICE_AMATEUR + " or type= "
				+ UserType.SERVICE_STANDARD + ")";

		@SuppressWarnings("unchecked")
		List<Integer> countList = jdbcTemplate.query(sql, new Object[] {
				"%" + name + "%","%" + account + "%", sex }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("count");
			}
		});
		if (countList.size() > 0)
			return countList.get(0);

		return 0;
	}

	/**
	 * 查询咨询师
	 * 
	 * @param qry_params
	 * @param offset
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<AdviserEntity> queryAdviser(String qry_params, int offset,
			int rows) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(qry_params);
		// 判断参数有效性，如果没传值就赋值7
		String name = jsonObject.optString("name");
		if (StringUtils.isEmpty(name))
			name = " ";
		String sex = jsonObject.optString("sex");
		if (sex.equals("2"))
			sex = " ";
		String account = jsonObject.optString("account");
		if (StringUtils.isEmpty(account))
			account = " ";
		String id = jsonObject.optString("id");
		if (StringUtils.isEmpty(id))
			id = " ";

		String sql = " select * from " + getTableName("platform_users")
				+ " where ('" + id + "' = ' ' or id = ? ) and " + " ('" + name
				+ "' = ' ' or name like ?) "  +  " and ('" + account
				+ "' = ' ' or account like ?) "  +  " and ('" + sex
				+ "' = ' ' or sex = ?) " + " and (type = "
				+ UserType.SERVICE_AMATEUR + " or type= "
				+ UserType.SERVICE_STANDARD + ")" + " limit ?, ? ";

		@SuppressWarnings("unchecked")
		List<AdviserEntity> adviserEntities = jdbcTemplate.query(sql,
				new Object[] { id, "%" + name + "%", "%" + account + "%", sex, offset, rows },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						AdviserEntity adviserEntity = new AdviserEntity();

						adviserEntity.setId(rs.getString("id"));
						adviserEntity.setAccount(rs.getString("account"));
						adviserEntity.setName(rs.getString("name"));
						adviserEntity.setSex(rs.getInt("sex"));
						adviserEntity.setPhone(rs.getString("mobile_phone"));
						return adviserEntity;
					}
				});

		return adviserEntities;
	}
	
	
	/**
	 * 组数量
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getGroupCount() throws Exception {
		String sql = " select count(id) 'count' from " + getTableName("seat_group") ;
		List<Integer> countList = jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("count");
			}
		});
		if (countList.size() > 0)
			return countList.get(0);

		return 0;
	}
	
	/**
	 * 查找坐席组
	 * @param offset
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getGroup(int offset,int rows) throws Exception {
		String sql = " select a.*,b.name from " + getTableName("seat_group") 
				+ " a left join " + getTableName("platform_users") 
				+ " b on a.leader = b.id "
				+ " limit ?, ? ";
		List<Map<String, Object>> groupList = jdbcTemplate.query(sql,
				new Object[] {offset, rows },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> value = new HashMap<String, Object>();
						value.put("id", rs.getString("a.id"));
						value.put("name", rs.getString("a.name"));
						value.put("groupdesc", rs.getString("a.groupdesc"));
						value.put("leaderID", rs.getString("a.leader"));
						value.put("leader", rs.getString("b.name"));
						value.put("create_date", rs.getString("a.create_date").replace(".0", ""));
						return value;
					}
				});

		return groupList;
	}
	
	/**
	 * 新增组信息
	 * @param groupEntity
	 * @return
	 * @throws Exception
	 */
	public int addGroup(GroupEntity groupEntity) throws Exception {
		//添加组
		String groupID = getGUID();
		String sql1 = "insert into "	+ getTableName("seat_group")
				   + " (id, name, groupdesc, leader, create_user, create_date) "
				   + " values(?,?,?,?,?, ?) ";
		int res1 = jdbcTemplate.update(sql1, 
			new Object[] { 
				groupID,
				groupEntity.getName(), 
				groupEntity.getGroupdesc(),
				groupEntity.getLeader(), 
				getCurrentUser().getUserName(),
				getCurrentDate()
			});
		//添加关系
		int res2 = 0;
		if(res1 > 0){
			String sql2 = "insert into "	+ getTableName("seat_group_relation")
					   + " (id, user_id, group_id, create_user, create_date) "
					   + " values(?,?,?,?,?) ";
			res2 = jdbcTemplate.update(sql2, 
				new Object[] { 
					getGUID(),
					groupEntity.getLeader(),
					groupID,
					getCurrentUser().getUserName(),
					getCurrentDate()
				});
		}
		return res2;
	}
	
	/**
	 * 修改组信息
	 * @param groupEntity
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int updateGroup(GroupEntity groupEntity) throws Exception {
		//查找旧Leader
		String sql1 = " select leader from " + getTableName("seat_group") 
				+ " where id = ? ";
		List<Map<String, Object>> groupList = jdbcTemplate.query(sql1,
				new Object[] {groupEntity.getId() },
				new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> value = new HashMap<String, Object>();
						value.put("leaderID", rs.getString("leader"));
						return value;
					}
				});
		//修改关系表
		String sql2 = " update " + getTableName("seat_group_relation")
				   + " set user_id = ?, modify_user = ?, modify_date = ? where group_id = ? and user_id = ? ";
		jdbcTemplate.update(sql2,
					new Object[] { 
				        groupEntity.getLeader(), 
						getCurrentUser().getUserName(),
						getCurrentDate(),
						groupEntity.getId(),
						groupList.get(0).get("leaderID")
					});
		//修改
		String sql3 = " update " + getTableName("seat_group")
				   + " set name = ?, groupdesc = ?, leader=? ,modify_user = ?, modify_date = ? where id = ? ";
		return jdbcTemplate.update(sql3,
					new Object[] { 
				        groupEntity.getName(), 
				        groupEntity.getGroupdesc(),
				        groupEntity.getLeader(), 
						getCurrentUser().getUserName(),
						getCurrentDate(),
						groupEntity.getId() 
					});
	}
	
	/**
	 * 删除组信息
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int[] deleteGroup(final String[] ids) throws Exception {
		//删除关系表
		String sql1 = "delete from " + getTableName("seat_group_relation")
				   + " where group_id = ? ";
		int[] res1 = jdbcTemplate.batchUpdate(sql1,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						ps.setString(1, ids[index]);
					}

					public int getBatchSize() {
						return ids.length;
					}
				});
		//删除组
		String sql = "delete from " + getTableName("seat_group")
				   + " where id = ? ";
		int[] res = jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {

					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						ps.setString(1, ids[index]);
					}

					public int getBatchSize() {
						return ids.length;
					}
				});

		return res;
	}
	
	/**
	 * 加载某个组下面关联的用户
	 * @param role_id
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public List<PlatformUserEntity> loadGroupUsers(String group_id) throws Exception {
		String sql = " select b.* from " + getTableName("seat_group_relation")
		  		   + " a left join  " + getTableName("platform_users") + " b " 
		  		   + " on a.user_id = b.id where a.group_id = ? ";		
		@SuppressWarnings("unchecked")
		List<PlatformUserEntity> entitys = jdbcTemplate.query(sql, new Object[]{ group_id }, new RowMapper() {
			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				PlatformUserEntity entity = new PlatformUserEntity();
				entity.setId(rs.getString("id"));
				entity.setAccount(rs.getString("account"));
				entity.setName(rs.getString("name"));
				entity.setCreate_date(rs.getString("create_date"));
				entity.setCreate_user(rs.getString("create_user"));
				entity.setModify_date(rs.getString("modify_user"));
				entity.setModify_user(rs.getString("modify_date"));
				return entity;
			}
		});		
		return entitys;
	}
	
	/**
	 * 删除某个组关联的用户
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteGroupPlatformUser(String id) throws Exception {
		String sql = "delete from " + getTableName("seat_group_relation")
				   + " where group_id = ? ";
		return jdbcTemplate.update(sql, new Object[] { id });
	}
	
	/**
	 * 用户关联到组
	 * @param authorityEntity
	 * @return
	 * @throws Exception
	 */
	public int[] addPlatformUserToGroup(final GroupEntity groupEntity) throws Exception {
		String sql = "insert into " + getTableName("seat_group_relation")
				   + " (id, user_id, group_id, create_user, create_date) "
				   + " values(?, ?, ?, ?, ?)";
		final String[] platfrom_user_ids = groupEntity.getPlatfrom_user_ids().split(",");
		int[] res = jdbcTemplate.batchUpdate(sql,
			new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int index)
						throws SQLException {
					ps.setString(1, getGUID());
					ps.setString(2, platfrom_user_ids[index]);
					ps.setString(3, groupEntity.getId());
					ps.setString(4, getCurrentUser().getUserName());
					ps.setString(5, getCurrentDate());
				}
	
				public int getBatchSize() {
					return platfrom_user_ids.length;
				}
			});
		return res;
	}

	/**
	 * 获取咨询师信息
	 * 
	 * @param qry_params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public AdviserEntity getAdviserInfo(String qry_params) throws Exception {
		String sql = " select id,account,name,sex,mobile_phone,type,mail,weichat_type from " + getTableName("platform_users")
				+ " where id = ? ";
		List<AdviserEntity> adviserEntities = jdbcTemplate.query(sql,
				new Object[] { qry_params }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						AdviserEntity adviserEntity = new AdviserEntity();
						adviserEntity.setId(rs.getString("id"));
						adviserEntity.setAccount(rs.getString("account"));
						adviserEntity.setName(rs.getString("name"));
						adviserEntity.setSex(rs.getInt("sex"));
						adviserEntity.setPhone(rs.getString("mobile_phone"));
						adviserEntity.setType(rs.getString("type"));
						adviserEntity.setMail(rs.getString("mail"));
						adviserEntity.setWeichat_type(rs.getString("weichat_type"));
						return adviserEntity;
					}
				});

		if (adviserEntities.size() > 0) {
			return adviserEntities.get(0);
		}

		return null;

	}

	/**
	 * 新增咨询师
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int addAdviser(JSONObject jsonObject) throws Exception {
		String sql = "insert into "
				+ getTableName("platform_users")
				+ " (id, account, password, name, sex, mail, mobile_phone, type, issys, enabled, locked, expired, credentialsexpired, weichat_type, create_user, create_date) "
				+ " values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?) ";
		// 用账户做salt加密密码
		String password = Encrypt.encryptPwd(jsonObject.optString("add_account") + jsonObject.optString("add_account"));
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						getGUID(), 
						jsonObject.optString("add_account"),
						password,
						jsonObject.optString("add_name"),
						jsonObject.optString("add_sex"),
						jsonObject.optString("add_mail"),
						jsonObject.optString("add_phone"),
						jsonObject.optString("add_type"),
						"0",
						"1", 
						"0", 
						"0", 
						"0",
						jsonObject.optString("weichat_type"),
						getCurrentUser().getId(), 
						getCurrentDate() 
						});
	}

	/**
	 * 修改咨询师
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updateAdviser(JSONObject jsonObject) throws Exception {
		String sql = "update "
				+ getTableName("platform_users")
				+ " set name = ?, sex = ?, mobile_phone=?, mail=?, type=?, weichat_type=?, modify_user=?, modify_date=?"
				+ " where id = ?";
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						jsonObject.optString("edit_name"),
						jsonObject.optString("edit_sex"),
						jsonObject.optString("edit_phone"),
						jsonObject.optString("edit_mail"),
						jsonObject.optString("edit_type"),
						jsonObject.optString("edit_weichat_type"),
						getCurrentUser().getId(),
						getCurrentDate(),
						jsonObject.optString("edit_id") 
						});
	}

	/**
	 * 删除咨询师
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int deleteAdviser(JSONObject jsonObject) throws Exception {
		String[] strArray = null;
		strArray = jsonObject.optString("id").split(",");
		String sql = " delete from " + getTableName("platform_users")
				+ " where id = ?";
		String id = "";
		int rult = 0;
		for (int i = 0; i < strArray.length; i++) {
			id = strArray[i];
			rult = jdbcTemplate.update(sql, new Object[] { id });
		}
		return rult;
	}
	
	/**
	 * 删除账号关联的权限角色
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int deleteRoleUser(JSONObject jsonObject) throws Exception {	
		String[] strArray = null; 
		strArray = jsonObject.optString("id").split(",");
		String sql = "delete from "	+ getTableName("platform_users_auths") 
				+ " where user_id = ?";
		String account = "";
		int rult = 0;
		for(int i=0;i<strArray.length;i++){
			account = strArray[i];
			rult = jdbcTemplate.update(sql,new Object[] {account});
		}
		return rult;
	}

	/**
	 * 新增平台账号
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int addPlatformUser(JSONObject jsonObject) throws Exception {
		String sql = "insert into "
				+ getTableName("platform_users")
				+ " (id, account, password, name, issys, "
				+ " enabled, locked, expired, credentialsexpired, type, create_user, create_date) "
				+ " values(?,?,?,?,?, ?,?,?,?,?, ?,?) ";
		// 用账户做salt加密密码
		String password = Encrypt
				.encryptPwd(jsonObject.optString("add_account")
						+ jsonObject.optString("add_account"));
		return jdbcTemplate.update(
				sql,
				new Object[] { getGUID(), jsonObject.optString("add_account"),
						password, jsonObject.optString("add_nick_name"), "0",
						"1", "0", "0", "0",
						jsonObject.optString("add_full_time"),
						getCurrentUser().getUserName(), getCurrentDate() });
	}

	/**
	 * 修改平台账号
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updatePlatformUser(JSONObject jsonObject) throws Exception {
		String sql = "update "
				+ getTableName("platform_users")
				+ " set name = ?,issys=?, "
				+ " enabled=?,locked=?,expired=?,credentialsexpired=?,type=?,modify_user=?,modify_date=? "
				+ " where account = ?";
		return jdbcTemplate.update(
				sql,
				new Object[] { jsonObject.optString("edit_nick_name"), "0",
						"1", "0", "0", "0",
						jsonObject.optString("edit_full_time"),
						getCurrentUser().getUserName(), getCurrentDate(),
						jsonObject.optString("edit_account") });
	}

	/**
	 * 修改平台账户密码
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updatePasswordOfPlatformUser(JSONObject jsonObject)
			throws Exception {
		String sql = "update " + getTableName("platform_users")
				+ " set password = ? ,modify_user=?,modify_date=? "
				+ " where account = ?";
		String password = Encrypt.encryptPwd(jsonObject
				.optString("edit_account")
				+ jsonObject.optString("update_password"));
		return jdbcTemplate
				.update(sql, new Object[] { password,
						getCurrentUser().getUserName(), getCurrentDate(),
						jsonObject.optString("edit_account") });
	}

	/**
	 * 删除平台账号
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int deletePlatformUser(JSONObject jsonObject) throws Exception {
		String[] strArray = null;
		strArray = jsonObject.optString("account").split(",");
		String sql = "delete from " + getTableName("platform_users")
				+ " where account = ?";
		String account = "";
		int rult = 0;
		for (int i = 0; i < strArray.length; i++) {
			account = strArray[i];
			rult = jdbcTemplate.update(sql, new Object[] { account });
		}
		return rult;
	}

	/**
	 * 更新登陆时间
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public int updateLoginTime() throws Exception {
		String sql = "update " + getTableName("platform_users")
				+ " set login_time = ? ,modify_user=?,modify_date=? "
				+ " where account = ?";
		return jdbcTemplate
				.update(sql, new Object[] { 
						getCurrentDate(),
						getCurrentUser().getUserName(), 
						getCurrentDate(),
						getCurrentUser().getAccount() 
						});
	}
	
	/**
	 * 删除原来的咨询时间计划
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int deleteSchedule() throws Exception {
		String sql = "delete from " + getTableName("schedule")
				+ " where account = ?";
		String account = getCurrentUser().getAccount();
		int rult = jdbcTemplate.update(sql, new Object[] { account });
		return rult;
	}

	/**
	 * 插入新的咨询时间表
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int saveSchedule(JSONObject jsonObject) throws Exception {
		Iterator it = jsonObject.keys();
		String sql = "insert into "
				+ getTableName("schedule")
				+ " (id, account, schedule_time, status, create_user, create_date) "
				+ " values(?, ?, ?, ?, ?, ?)";
		int result = 0;
		while (it.hasNext()) {
			String key = it.next().toString();
			String value = jsonObject.getString(key);
			int schedule_time = Integer.valueOf(key.substring(key
					.lastIndexOf("_") + 1));
			System.out.println(key + "*******" + value);
			result = jdbcTemplate.update(sql, new Object[] { getGUID(),
					getCurrentUser().getAccount(), schedule_time, value,
					getCurrentUser().getUserName(), getCurrentDate() });

		}
		return result;
	}

	/**
	 * 添加我的咨询备注
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int saveRemark(String remark) throws Exception {
		String sql = "update " + getTableName("platform_users")
				+ " set remark = ? where account = ?";
		String account = getCurrentUser().getAccount();
		int rult = jdbcTemplate.update(sql, new Object[] { remark, account });
		return rult;
	}

	public int addSchedule(JSONObject jsonObject) throws Exception {
		String account = jsonObject.optString("add_account");
		int result = 0;
		String sql = "insert into "
				+ getTableName("schedule")
				+ " (id, account, schedule_time, status, create_user, create_date) "
				+ " values(?, ?, ?, ?, ?, ?)";
		for (int i = 5; i < 22; i++) {
			result = jdbcTemplate.update(sql, new Object[] { getGUID(),
					account, i, 1, getCurrentUser().getUserName(),
					getCurrentDate() });

		}
		return result;
	}

	/**
	 * 查询时间表信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ScheduleEntity> getSchedule() throws Exception {
		String sql = " select * from " + getTableName("schedule")
				+ " where account = ? ORDER BY schedule_time";
		String account = getCurrentUser().getAccount();
		@SuppressWarnings("unchecked")
		List<ScheduleEntity> scheduleEntities = jdbcTemplate.query(sql,
				new Object[] { account }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						ScheduleEntity scheduleEntity = new ScheduleEntity();

						scheduleEntity.setId(rs.getString("id"));
						scheduleEntity.setAccount(rs.getString("account"));
						scheduleEntity.setSchedule_time(rs
								.getString("schedule_time"));
						scheduleEntity.setStatus(rs.getInt("status"));

						return scheduleEntity;
					}
				});

		return scheduleEntities;

	}

	/**
	 * 查询预约咨询数量
	 * 
	 * @param qry_params
	 * @return
	 */
	public int queryScheduleReserveCount(String qry_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(qry_params);
		String account = getCurrentUser().getAccount();
		String sql = " select count(id) 'count' from "
				+ getTableName("user_schedule")
				+ " where advisory_account = ? ";

		@SuppressWarnings("unchecked")
		List<Integer> countList = jdbcTemplate.query(sql,
				new Object[] { account }, new RowMapper() {
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
	 * 查询预约咨询
	 * 
	 * @param qry_params
	 * @param offset
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public List<UserScheduleEntity> queryScheduleReserve(String qry_params,
			int offset, int rows) throws Exception {
		String account = getCurrentUser().getAccount();
		String sql = " select * from "
				+ getTableName("user_schedule")
				+ " where advisory_account = ? ORDER BY `status` DESC, begin_time desc "
				+ " limit ?, ? ";

		@SuppressWarnings("unchecked")
		List<UserScheduleEntity> userScheduleEntities = jdbcTemplate.query(sql,
				new Object[] { account, offset, rows }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						UserScheduleEntity userScheduleEntity = new UserScheduleEntity();

						userScheduleEntity.setId(rs.getString("id"));
						userScheduleEntity.setAdvisoryAccount(rs
								.getString("advisory_account"));
						if (rs.getString("begin_time") != null) {
							String date = rs.getString("begin_time").substring(
									0, 19);
							/*
							 * StringBuffer sb = new StringBuffer();
							 * sb.append(date).append("点");
							 */
							userScheduleEntity.setBeginTime(date);
						} else {
							userScheduleEntity.setBeginTime("");
						}
						userScheduleEntity.setEndTime(rs.getString("end_time"));
						userScheduleEntity.setStatus(rs.getInt("status"));
						userScheduleEntity.setQq(rs.getString("qq"));
						userScheduleEntity.setMobile_phone(rs
								.getString("mobile_phone"));
						userScheduleEntity.setCode(rs.getString("code"));
						if (rs.getString("create_date") != null) {
							String createDate = rs.getString("create_date")
									.substring(0, 19);
							userScheduleEntity.setCreate_date(createDate);
						} else {
							userScheduleEntity.setCreate_date("");
						}
						return userScheduleEntity;
					}
				});

		return userScheduleEntities;
	}

	/**
	 * 获取预约咨询信息
	 * 
	 * @param qry_params
	 * @return
	 * @throws Exception
	 */
	public UserScheduleEntity getScheduleReserveInfo(String guid)
			throws Exception {
		String sql = " select * from " + getTableName("user_schedule")
				+ " where id = ? ";

		@SuppressWarnings("unchecked")
		List<UserScheduleEntity> userScheduleEntities = jdbcTemplate.query(sql,
				new Object[] { guid }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						UserScheduleEntity userScheduleEntity = new UserScheduleEntity();

						userScheduleEntity.setFeedback(rs.getString("feedback"));

						return userScheduleEntity;
					}
				});

		if (userScheduleEntities != null) {
			return userScheduleEntities.get(0);
		}

		return null;

	}

	/**
	 * 预约咨询的各种操作
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int scheduleReserveAction(String req_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(req_params);
		String id = jsonObject.optString("id");
		String status = jsonObject.optString("status");
		String sql = "update " + getTableName("user_schedule")
				+ " set status = ? " + " where id = ?";
		int rult = 0;
		rult = jdbcTemplate.update(sql, new Object[] { status, id });
		return rult;
	}

	/**
	 * 预约完成
	 * 
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int completeReserve(String id) throws Exception {
		int rult = 0;
		String sql = "update " + getTableName("user_schedule")
				+ " set status = 3 " + " where id = ?";
		rult = jdbcTemplate.update(sql, new Object[] { id });
		return rult;
	}

	/**
	 * 加一次时间
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int addDuration(String account) throws Exception {
		int rult = 0;
		String sql = "update " + getTableName("adviser")
				+ " set duration = duration+1 " + " where account = ?";
		rult = jdbcTemplate.update(sql, new Object[] { account });
		return rult;
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
	 * 修改预约时间
	 * 
	 * @param id
	 * @param fromDt
	 * @param toDt
	 * @return
	 */
	public int updateReserveTime(String id, Date fromDt, Date toDt) {
		String sql = "update " + getTableName("user_schedule")
				+ " set begin_time = ?, " + " end_time = ? where id = ? ";
		return jdbcTemplate.update(sql, new Object[] { fromDt, toDt, id });
	}

	/**
	 * 取得咨询师评价统计情况
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getAppraisalCountInfo() {
		String sql = " select a.id, a.name,a.create_date,a.type,a.login_time,d.completed_count,  b.assess_count, c.total_score / b.assess_count 'avg_score' from "
				+ getTableName("platform_users")
				+ " a left join ( select service_id, count(id) 'assess_count' from " + getTableName("task_queue")
				+ " where service_score > 0 group by service_id) b " 
				+ " on a.id = b.service_id "
				+ " left join ( select service_id, sum(service_score) 'total_score' from " + getTableName("task_queue")
				+ " where service_score > 0 group by service_id) c " 
				+ " on a.id = c.service_id "
				+ " left join ( select service_id, count(id) 'completed_count' from " + getTableName("task_queue")
				+ " where task_status = ? or task_status = ? group by service_id) d "
				+ " on a.id = d.service_id "
				+ " where a.type <> ? " ;
		return jdbcTemplate.query(sql, 
				new Object[] { TaskStatus.COMPLETED, TaskStatus.STOP, UserType.ADMIN }, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> value = new HashMap<String, Object>();
				value.put("id", rs.getString("id"));
				value.put("name", rs.getString("name"));
				value.put("create_date", rs.getString("create_date").substring(0, 19));
				value.put("type", rs.getString("type"));
				if(rs.getString("login_time") != null && rs.getString("login_time") != ""){
					value.put("login_time", rs.getString("login_time").substring(0, 19));
				} else {
					value.put("login_time", rs.getString("login_time"));
				}
				if(rs.getString("completed_count") != null){
					value.put("completed_count", rs.getString("completed_count"));
				} else {
					value.put("completed_count", 0);
				}
				value.put("assess_count", rs.getString("assess_count"));
				value.put("avg_score", rs.getInt("avg_score"));
				return value;
			}
		});
	}

	/**
	 * 根据客服ID取得所有评价信息
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, Object>> getAllAppraisalOfAdviser(String id) {
		String sql = " select a.id, b.nickname,a.modify_date,a.task_status,a.service_score, a.custom_asses_desc from "
				+ getTableName("task_queue")
				+ " a left join "
				+ getTableName("users")
				+ " b on a.open_id = b.openid "
				+ " where task_status = ? and service_id = ? "
				+ " order by a.modify_date desc ";
		return jdbcTemplate.query(sql,
				new Object[] { TaskStatus.COMPLETED, id }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Object> value = new HashMap<String, Object>();
						value.put("id", rs.getString("id"));
						if (rs.getString("nickname") != null) {
							value.put("nickname", rs.getString("nickname"));
						} else {
							value.put("nickname", "");
						}
						value.put("time", rs.getString("modify_date")
								.substring(0, 19));
						value.put("task_status", rs.getInt("task_status"));
						value.put("score", rs.getInt("service_score"));
						value.put("custom_asses_desc",
								rs.getString("custom_asses_desc"));
						return value;
					}
				});
	}

}
