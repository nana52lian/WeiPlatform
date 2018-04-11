package com.yidatec.weixin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class SyncHrDao extends DataBase {
	
	/**
	 * 根据CODE查找KDATA表
	 * @param user_code
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> getKData(String user_code) {
		String sql = "select id from " + getTableName("kdata")
				+ " where user_code = ? ";
		return jdbcTemplate.query(sql,
				new Object[] { user_code }, 
				new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs, int i)
							throws SQLException {
						return rs.getString("id");
					}
				});
	}
	
	/**
	 * 插入KDATA表
	 * @param mapList
	 * @return
	 * @throws Exception
	 */
	public int addKData(Map<String, Object> map)
			throws Exception {
		if (map.size() == 0)
			return  0 ;
		String sql = " insert into " + getTableName("kdata")
				+ " (id, user_code, user_name, gender, user_type, telephone_number, email_address, active_status, "
				+ " create_user, create_date) "
				+ " values(?,?,?,?,?, ?,?,?,?,?) ";
		return jdbcTemplate.update(sql, new Object[] { 
				getGUID(), 
				map.get("PRID"),
				map.get("CHINESE_NAME"),
				map.get("GENDER"),
				map.get("ESM_USER_TYPE"),
				map.get("MOBILE_SMS"),
				map.get("EMAIL"),
				map.get("UPDATE_FLAG"),
				"system",
				getCurrentDate()
				});
	}
	
	/**
	 * 修改KDATA表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public int updateKData(Map<String, Object> map) throws Exception {
		String sql = " update " + getTableName("kdata")
				+ " set user_name = ?, gender = ?, user_type = ? , telephone_number = ? , email_address = ? , active_status = ? , modify_user = ? , modify_date = ?  "
				+ " where user_code = ? ";
		return jdbcTemplate.update(
				sql,
				new Object[] { 
						map.get("CHINESE_NAME"),
						map.get("GENDER"),
						map.get("ESM_USER_TYPE"),
						map.get("MOBILE_SMS"),
						map.get("EMAIL"),
						map.get("UPDATE_FLAG"),
						"system",
						getCurrentDate(),
						map.get("PRID")
						});
	}

}
