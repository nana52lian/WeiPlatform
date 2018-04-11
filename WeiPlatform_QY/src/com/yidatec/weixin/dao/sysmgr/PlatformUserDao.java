package com.yidatec.weixin.dao.sysmgr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.yidatec.weixin.dao.DataBase;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.message.UserType;
import com.yidatec.weixin.util.Encrypt;

public class PlatformUserDao extends DataBase {

	/**
	 * 查询平台账户数量
	 * @param qry_params
	 * @return
	 */
	public int queryPlatformUserCount(String qry_params) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(qry_params);
		// 判断参数有效性，如果没传值就赋值1
		String qry_account = jsonObject.optString("qry_account");
		if (StringUtils.isEmpty(qry_account)) qry_account = "1";
		String qry_name = jsonObject.optString("qry_name");
		if (StringUtils.isEmpty(qry_name)) qry_name = "1";
		String qry_mobile_phone = jsonObject.optString("qry_mobile_phone");
		if (StringUtils.isEmpty(qry_mobile_phone)) qry_mobile_phone = "1";
		String qry_enabled = jsonObject.optString("qry_enabled");
		if (StringUtils.isEmpty(qry_enabled)) qry_enabled = "1";
		
		String sql = " select count(id) 'count' from " + getTableName("platform_users")
			       + " where "
			       + " ('" + qry_account + "' = '1' or account like ?) "
			       + " and ('" + qry_name + "' = '1' or name like ?) "
			       + " and ('" + qry_mobile_phone + "' = '1' or mobile_phone like ?) "
			       + " and ('" + qry_enabled + "' = '1' or enabled = ?) "
			       + " and type = " + UserType.ADMIN;
		
		@SuppressWarnings("unchecked")
		List<Integer> countList = jdbcTemplate.query(sql, 
				new Object[]{ 
					"%" + qry_account + "%", 
					"%" + qry_name + "%", 
					"%" + qry_mobile_phone + "%", 
					"%" + qry_enabled + "%" }, 
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {				
				return rs.getInt("count");
			}
		});	
		if (countList.size() > 0)
			return countList.get(0);
		
		return 0;
	}
	
	/**
	 * 查询平台账户
	 * @param qry_params
	 * @param offset
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public List<PlatformUserEntity> queryPlatformUser(String qry_params, int offset, int rows) throws Exception {		
		JSONObject jsonObject = JSONObject.fromObject(qry_params);
		// 判断参数有效性，如果没传值就赋值1
		String qry_account = jsonObject.optString("qry_account");
		if (StringUtils.isEmpty(qry_account)) qry_account = " ";
		String qry_name = jsonObject.optString("qry_name");
		if (StringUtils.isEmpty(qry_name)) qry_name = " ";
		String qry_mobile_phone = jsonObject.optString("qry_mobile_phone");
		if (StringUtils.isEmpty(qry_mobile_phone)) qry_mobile_phone = " ";
		String qry_enabled = jsonObject.optString("qry_enabled");
		if (StringUtils.isEmpty(qry_enabled)) qry_enabled = " ";
		String id = jsonObject.optString("id");
		if (StringUtils.isEmpty(id)) id = " ";
		
		String sql = " select * from " + getTableName("platform_users")
	       		   + " where "
	       		   + " ('" + id + "' = ' ' or id = ?) and "
	       		   + " ('" + qry_account + "' = ' ' or account like ?) "
	       		   + " and ('" + qry_name + "' = ' ' or name like ?) "
	       		   + " and ('" + qry_mobile_phone + "' = ' ' or mobile_phone like ?) "
	       		   + " and ('" + qry_enabled + "' = ' ' or enabled = ?) "
			       + " and type = " + UserType.ADMIN + " limit ?, ? ";
		
		@SuppressWarnings("unchecked")
		List<PlatformUserEntity> platformUserEntities = jdbcTemplate.query(sql, 
				new Object[]{ 
				    id,
					"%" + qry_account + "%", 
					"%" + qry_name + "%", 
					"%" + qry_mobile_phone + "%", 
					qry_enabled, 
					offset, rows}, 
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				PlatformUserEntity platformUser = new PlatformUserEntity();
				platformUser.setId(rs.getString("id"));
				platformUser.setAccount(rs.getString("account"));
				platformUser.setName(rs.getString("name"));
				platformUser.setMobile_phone(rs.getString("mobile_phone"));
				platformUser.setMail(rs.getString("mail"));
				platformUser.setEnabled(rs.getInt("enabled"));
				platformUser.setCreate_date(rs.getString("create_date"));
				return platformUser;
			}
		});	
		
		return platformUserEntities;
	}
	
	/**
	 * 加载平台账户数量
	 * @param qry_params
	 * @return
	 */
	public int loadPlatformUserCount() throws Exception {		
		String sql = " select count(id) 'count' from " + getTableName("platform_users");
		@SuppressWarnings("unchecked")
		List<Integer> countList = jdbcTemplate.query(sql, 				
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {				
				return rs.getInt("count");
			}
		});
		if (countList.size() > 0)
			return countList.get(0);
		
		return 0;
	}
	
	/**
	 * 加载所有平台账户
	 * @param offset
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	public List<PlatformUserEntity> loadPlatformUsers(int offset, int rows) throws Exception {
		String sql = " select * from " + getTableName("platform_users")
			       + " limit ?, ? ";		
		@SuppressWarnings("unchecked")
		List<PlatformUserEntity> platformUserEntities = jdbcTemplate.query(sql, 
				new Object[]{ offset, rows}, 
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				PlatformUserEntity platformUser = new PlatformUserEntity();
				platformUser.setId(rs.getString("id"));
				platformUser.setAccount(rs.getString("account"));
				platformUser.setName(rs.getString("name"));
				platformUser.setMobile_phone(rs.getString("mobile_phone"));
				platformUser.setMail(rs.getString("mail"));
				platformUser.setEnabled(rs.getInt("enabled"));
				platformUser.setCreate_date(rs.getString("create_date"));
				return platformUser;
			}
		});			
		return platformUserEntities;
	}
	
	/**
	 * 加载坐席数量
	 * @param qry_params
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int loadSeatUserCount() throws Exception {		
		String sql = " select count(id) 'count' from " + getTableName("platform_users")
				+ " where type <> ? ";
		List<Integer> countList = jdbcTemplate.query(sql, 
				new Object[]{ UserType.ADMIN},
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {				
				return rs.getInt("count");
			}
		});
		if (countList.size() > 0)
			return countList.get(0);
		
		return 0;
	}
	
	/**
	 * 加载坐席
	 * @param offset
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<PlatformUserEntity> loadSeatUsers(int offset, int rows) throws Exception {
		String sql = " select * from " + getTableName("platform_users")
				   + " where type <> ? "
			       + " limit ?, ? ";		
		List<PlatformUserEntity> platformUserEntities = jdbcTemplate.query(sql, 
				new Object[]{ UserType.ADMIN,offset, rows}, 
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				PlatformUserEntity platformUser = new PlatformUserEntity();
				platformUser.setId(rs.getString("id"));
				platformUser.setAccount(rs.getString("account"));
				platformUser.setName(rs.getString("name"));
				platformUser.setMobile_phone(rs.getString("mobile_phone"));
				platformUser.setMail(rs.getString("mail"));
				platformUser.setEnabled(rs.getInt("enabled"));
				platformUser.setCreate_date(rs.getString("create_date"));
				return platformUser;
			}
		});			
		return platformUserEntities;
	}
	
	/**
	 * 加载坐席
	 * @param offset
	 * @param rows
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Map<String,String>> loadLeader() throws Exception {
		String sql = " select * from " + getTableName("platform_users")
				   + " where type <> ? ";		
		List<Map<String,String>> platformUserEntities = jdbcTemplate.query(sql, 
				new Object[]{ UserType.ADMIN}, 
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, String> map = new HashMap<String, String>();
				map.put("value", rs.getString("id"));
				map.put("text", rs.getString("name"));
				return map;
			}
		});			
		return platformUserEntities;
	}
	
	/**
	 * 新增平台账号
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int addPlatformUser(JSONObject jsonObject) throws Exception {		
		String sql = "insert into "	+ getTableName("platform_users")
			+ " (id, account, password, name, mail, mobile_phone, issys, " 
			+ " enabled, locked, expired, credentialsexpired, type, create_user, create_date) "
			+ " values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?) ";
		// 用账户做salt加密密码
		String password = Encrypt.encryptPwd(jsonObject.optString("account") + jsonObject.optString("password"));
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						getGUID(), 
						jsonObject.optString("account"),
						password, 
						jsonObject.optString("name"),
						jsonObject.optString("mail"), 
						jsonObject.optString("mobile_phone"),
						"0", 
						"1",
						"0",
						"0",
						"0",
						UserType.ADMIN, // 平台账户类型
						getCurrentUser().getUserName(),
						getCurrentDate() });
	}
	
	/**
	 * 修改平台账号
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updatePlatformUser(JSONObject jsonObject) throws Exception {		
		String sql = "";
		String password = "";
		int rult = 0;
		if(jsonObject.optString("password") == ""){
			sql = "update "	+ getTableName("platform_users") + " set account = ?, name = ?,mail=?,mobile_phone=?,issys=?, "
					+ " enabled=?,locked=?,expired=?,credentialsexpired=?,modify_user=?,modify_date=? "
					+ " where id = ?";
			rult = jdbcTemplate.update(
					sql,
					new Object[] { 
							jsonObject.optString("account"),
							jsonObject.optString("name"),
							jsonObject.optString("mail"), 
							jsonObject.optString("mobile_phone"),
							"0", 
							"1",
							"0",
							"0",
							"0",
							getCurrentUser().getUserName(),
							getCurrentDate(),
							jsonObject.optString("id")
					});
		} else {
			sql = "update "	+ getTableName("platform_users") + " set account = ?, password = ?,name = ?,mail=?,mobile_phone=?,issys=?,"
					+ " enabled=?,locked=?,expired=?,credentialsexpired=?,modify_user=?,modify_date=? "
					+ " where id = ?";
			
			// 用账户做salt加密密码
			password = Encrypt.encryptPwd(jsonObject.optString("account") + jsonObject.optString("password"));
			rult = jdbcTemplate.update(
					sql,
					new Object[] { 
							jsonObject.optString("account"),
							password, 
							jsonObject.optString("name"),
							jsonObject.optString("mail"), 
							jsonObject.optString("mobile_phone"),
							"0", 
							"1",
							"0",
							"0",
							"0",
							getCurrentUser().getUserName(),
							getCurrentDate(),
							jsonObject.optString("id")
					});
		}
		return rult;
	}
	
	/**
	 * 删除平台账号
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int deletePlatformUser(JSONObject jsonObject) throws Exception {	
		String[] strArray = null; 
		strArray = jsonObject.optString("id").split(",");
		String sql = "delete from "	+ getTableName("platform_users") 
				+ " where id = ?";
		String id = "";
		int rult = 0;
		for(int i=0;i<strArray.length;i++){
			id = strArray[i];
			rult = jdbcTemplate.update(sql,new Object[] {id});
		}
		return rult;
	}
	
	/**
	 * 启用平台账号
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updatePlatformUserForStart(JSONObject jsonObject) throws Exception {	
		String[] strArray = null; 
		strArray = jsonObject.optString("id").split(",");
		String sql = "update "	+ getTableName("platform_users") + " set enabled = ?,modify_user=?,modify_date=? "
				+ " where id = ?";
		String id = "";
		int rult = 0;
		for(int i=0;i<strArray.length;i++){
			id = strArray[i];
			rult = jdbcTemplate.update(
				sql,
				new Object[] { 
						"1",
						getCurrentUser().getUserName(),
						getCurrentDate(),
						id
				});
		}
		return rult;
	}
	
	/**
	 * 停止平台账号
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	public int updatePlatformUserForStop(JSONObject jsonObject) throws Exception {	
		String[] strArray = null; 
		strArray = jsonObject.optString("id").split(",");
		String sql = "update "	+ getTableName("platform_users") + " set enabled = ?,modify_user=?,modify_date=? "
				+ " where id = ?";
		String id = "";
		int rult = 0;
		for(int i=0;i<strArray.length;i++){
			id = strArray[i];
			rult = jdbcTemplate.update(
				sql,
				new Object[] { 
						"2",
						getCurrentUser().getUserName(),
						getCurrentDate(),
						id
				});
		}
		return rult;
	}

	/**
	 * 修改密码时，验证输入的原始密码是否正确
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public PlatformUserEntity validateOldPass(String id) throws Exception {
		String sql = " select * from " + getTableName("platform_users")
					+ " where id = ? ";
		
		@SuppressWarnings("unchecked")
		List<PlatformUserEntity> platformUserEntities = jdbcTemplate.query(sql, 
				new Object[]{ id }, 
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				PlatformUserEntity platformUserEntity = new PlatformUserEntity();
				String pass = Encrypt.decryptPwdWithSalt(rs.getString("password"), rs.getString("account"));
				platformUserEntity.setPassword(pass);
				
				return platformUserEntity;
			}
		});	
		
		if (platformUserEntities.size()>0) {
			return platformUserEntities.get(0);
		}
		
		return null;
	}

	/**
	 * 更改账户密码
	 * @param newPassword
	 * @return
	 * @throws Exception
	 */
	public int changePassword(String newPassword) throws Exception {
		String sql = "update "	+ getTableName("platform_users") + " set password = ? "
				+ " where id = ?";

		// 用账户做salt加密密码
		String password = Encrypt.encryptPwd(getCurrentUser().getAccount() + newPassword);
		int rult = 0;
		rult = jdbcTemplate.update(
				sql,
				new Object[] { 
						password,
						getCurrentUser().getId()
				});
		
		return rult;
	}

	/**
	 * 重新分配
	 * 
	 * @param id
	 */
	public int reAllot(String id) {
		String sql = "update "
				+ getTableName("task_queue")
				+ " set task_status=? where service_id=? and task_status in (1,2,5)";
		return jdbcTemplate.update(sql, new Object[]{0,id});
	}
	
}
