package com.yidatec.weixin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import com.yidatec.weixin.entity.WeixinUsersEntity;

public class WeixinUserManageDao extends DataBase {

	public int queryWeixinUserCount(String qry_params) {
		JSONObject jsonObject = JSONObject.fromObject(qry_params);
		String sql = "";
		// 判断参数有效性
		String k_account = jsonObject.optString("k_account");
		if (StringUtils.isEmpty(k_account)) {
			k_account = " ";
		}
		String name = jsonObject.optString("name");
		if (StringUtils.isEmpty(name)) {
			name = " ";
		}
		String openid = jsonObject.optString("openid");
		if (StringUtils.isEmpty(openid)) {
			openid = " ";
		}
/*		String grade = jsonObject.optString("grade");
		if (StringUtils.isEmpty(grade)) {
			grade = "7";
		}
		String department = jsonObject.optString("department");
		if (StringUtils.isEmpty(department)) {
			department = "7";
		}
		String area = jsonObject.optString("area");
		if (StringUtils.isEmpty(area)) {
			area = "7";
		}*/
		String cellphone = jsonObject.optString("cellphone");
		if (StringUtils.isEmpty(cellphone)) {
			cellphone = " ";
		}
		String email = jsonObject.optString("email");
		if (StringUtils.isEmpty(email)) {
			email = " ";
		}
		String sex = jsonObject.optString("sex");
		if (sex.equals("3")) sex = " ";
		String taskId = jsonObject.optString("taskId").trim();
		if(!StringUtils.isEmpty(taskId)){
			/*sql = " select c.id,c.count,a.*,b.*, count(c.id) 'allCount' from " + getTableName("users")
				+ " a left join " + getTableName("user_detail")
				+ " b on a.openid = b.openid "
				+ " left join (" 
				+ " ( select m.id,n.open_id,n.count from " + getTableName("task_queue")
				+ " m left join ("
				+ " select open_id 'open_id', count(open_id) 'count' from " + getTableName("task_queue")
				+ " group by open_id ) n on m.open_id = n.open_id )"
				+ " c ) on a.openid = c.open_id "
				+ " left join (" 
				+ " ( select open_id 'open_id', create_date 'createDate' from " + getTableName("task_message")
				+ " group by open_id order by create_date desc ) d ) on a.openid = d.open_id "
				+ " where "
				+ " ('" + taskId + "' = '7' or c.id like ?) "
				+ " and ('" + name + "' = '7' or name like ?) "
				+ " and ('" + k_account + "' = '7' or b.key_account like ?) "
				+ " and ('" + openid + "' = '7' or a.openid like ?) "
				+ " and ('" + grade + "' = '7' or grade like ?) "
				+ " and ('" + department + "' = '7' or department like ?) "
				+ " and ('" + area + "' = '7' or area like ?) "
				+ " and ('" + cellphone + "' = '7' or cellphone like ?) "
				+ " and ('" + email + "' = '7' or email like ?) "
				+ " and ('" + sex + "' = '7' or a.sex = ?) ";*/
			sql = " select count(a.id) 'allCount',a.*,b.user_code,b.user_name,b.telephone_number,b.email_address,b.gender,c.*,d.* from " + getTableName("users")
					+ " a left join "
					+ " ( select * from " + getTableName("kdata") + " m left join ( select openid,key_account from " + getTableName("user_detail")
					+ " group by openid ) n  on m.user_code = n.key_account ) "
					+ " b on a.openid = b.openid " 
					+ " left join (" 
					+ " ( select m.id, n.type, n.open_id, n.count from " + getTableName("task_queue")
					+ " m left join ("
					+ " select type 'type',open_id 'open_id', count(open_id) 'count' from " + getTableName("task_queue")
					+ " group by open_id ) n on m.open_id = n.open_id )"
					+ " c ) on a.openid = c.open_id "
					+ " left join (" 
					+ " ( select open_id 'open_id', create_date 'createDate' from " + getTableName("task_message")
					+ " group by open_id order by create_date desc ) d ) on a.openid = d.open_id "
					+ " where "
	                + " ('" + taskId + "' = ' ' or c.id like ?) "
					+ " and ('" + name + "' = ' ' or b.user_name like ?) "
					+ " and ('" + k_account + "' = ' ' or b.user_code like ?) "
					+ " and ('" + openid + "' = ' ' or a.openid like ?) "
					+ " and ('" + cellphone + "' = ' ' or b.telephone_number like ?) "
					+ " and ('" + email + "' = ' ' or b.email_address like ?) "
					+ " and ('" + sex + "' = ' ' or b.gender = ?) ";
		} else {
			/*sql = " select count(a.id) 'allCount' from " + getTableName("users")
		       + " a left join " + getTableName("user_detail")
		       + " b on a.openid = b.openid " 
		       + " left join ("  
		       + " ( select open_id 'open_id', count(open_id) 'count' from " + getTableName("task_queue")
		       + " group by open_id ) c ) on a.openid = c.open_id "
		       + " left join (" 
				+ " ( select open_id 'open_id', create_date 'createDate' from " + getTableName("task_message")
				+ " group by open_id order by create_date desc ) d ) on a.openid = d.open_id "
				+ " where "
		       + " ('" + taskId + "' like ?) "
		       + " and ('" + name + "' = '7' or name like ?) "
		       + " and ('" + k_account + "' = '7' or b.key_account like ?) "
		       + " and ('" + openid + "' = '7' or a.openid like ?) "
		       + " and ('" + grade + "' = '7' or grade like ?) "
		       + " and ('" + department + "' = '7' or department like ?) "
		       + " and ('" + area + "' = '7' or area like ?) "
		       + " and ('" + cellphone + "' = '7' or cellphone like ?) "
		       + " and ('" + email + "' = '7' or email like ?) "
			   + " and ('" + sex + "' = '7' or a.sex = ?) ";*/
			sql = " select count(a.id) 'allCount',a.*,b.user_code,b.user_name,b.telephone_number,b.email_address,b.gender,c.*,d.* from " + getTableName("users")
					+ " a left join "
					+ " ( select * from " + getTableName("kdata") + " m left join ( select openid,key_account from " + getTableName("user_detail")
					+ " group by openid ) n  on m.user_code = n.key_account ) "
					+ " b on a.openid = b.openid " 
					+ " left join ("  
					+ " ( select type 'type', open_id 'open_id', count(open_id) 'count' from " + getTableName("task_queue")
					+ " group by open_id ) c ) on a.openid = c.open_id " 
					+ " left join (" 
					+ " ( select open_id 'open_id', create_date 'createDate' from " + getTableName("task_message")
					+ " group by open_id order by create_date desc ) d ) on a.openid = d.open_id "
					+ " where "
					+ " ('" + taskId + "' like ?) "
					+ " and ('" + name + "' = ' ' or b.user_name like ?) "
					+ " and ('" + k_account + "' = ' ' or b.user_code like ?) "
					+ " and ('" + openid + "' = ' ' or a.openid like ?) "
					+ " and ('" + cellphone + "' = ' ' or b.telephone_number like ?) "
					+ " and ('" + email + "' = ' ' or b.email_address like ?) "
					+ " and ('" + sex + "' = ' ' or b.gender = ?) ";
		}
		
		@SuppressWarnings("unchecked")
		List<Integer> countList = jdbcTemplate.query(sql, 
				new Object[]{ 
					"%" + taskId + "%",
					"%" + name + "%",
					"%" + k_account + "%",
					"%" + openid + "%",
					/*"%" + grade + "%",
					"%" + department + "%",
					"%" + area + "%",*/
					"%" + cellphone + "%",
					"%" + email + "%",
					sex,
					}, 
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {				
				return rs.getInt("allCount");
			}
		});	
		if (countList.size() > 0)
			return countList.get(0);
		
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<WeixinUsersEntity> queryWeixinUser(String qry_params,
			int offset, int rows) {
		String sql = "";
		JSONObject jsonObject = JSONObject.fromObject(qry_params);
		// 判断参数有效性，如果没传值就赋值1
		String k_account = jsonObject.optString("k_account");
		if (StringUtils.isEmpty(k_account)) {
			k_account = " ";
		}
		String name = jsonObject.optString("name");
		if (StringUtils.isEmpty(name)) {
			name = " ";
		}
		String openid = jsonObject.optString("openid");
		if (StringUtils.isEmpty(openid)) {
			openid = " ";
		}
		/*String grade = jsonObject.optString("grade");
		if (StringUtils.isEmpty(grade)) {
			grade = " ";
		}
		String department = jsonObject.optString("department");
		if (StringUtils.isEmpty(department)) {
			department = " ";
		}
		String area = jsonObject.optString("area");
		if (StringUtils.isEmpty(area)) {
			area = " ";
		}*/
		String cellphone = jsonObject.optString("cellphone");
		if (StringUtils.isEmpty(cellphone)) {
			cellphone = " ";
		}
		String email = jsonObject.optString("email");
		if (StringUtils.isEmpty(email)) {
			email = " ";
		}
		String sex = jsonObject.optString("sex");
		if (sex.equals("3")) sex = " ";
		String taskId = jsonObject.optString("taskId").trim();
		//请求单号不为空时，以请求单号为查询条件
		if(!StringUtils.isEmpty(taskId)){
			/*sql = " select c.*,a.*,b.name,b.key_account,b.grade,b.department,b.area,b.cellphone,b.email,d.* from " + getTableName("users")
				+ " a left join " + getTableName("user_detail")
				+ " b on a.openid = b.openid "
				+ " left join (" 
				+ " ( select m.id,n.open_id,n.count from " + getTableName("task_queue")
				+ " m left join ("
				+ " select open_id 'open_id', count(open_id) 'count' from " + getTableName("task_queue")
				+ " group by open_id ) n on m.open_id = n.open_id )"
				+ " c ) on a.openid = c.open_id "
				+ " left join (" 
				+ " ( select open_id 'open_id', create_date 'createDate' from " + getTableName("task_message")
				+ " group by open_id order by create_date desc ) d ) on a.openid = d.open_id "
				+ " where "
				+ " ('" + taskId + "' = ' ' or c.id like ?) "
				+ " and ('" + name + "' = ' ' or name like ?) "
				+ " and ('" + k_account + "' = ' ' or b.key_account like ?) "
				+ " and ('" + openid + "' = ' ' or a.openid like ?) "
				+ " and ('" + grade + "' = ' ' or grade like ?) "
				+ " and ('" + department + "' = ' ' or department like ?) "
				+ " and ('" + area + "' = ' ' or area like ?) "
				+ " and ('" + cellphone + "' = ' ' or cellphone like ?) "
				+ " and ('" + email + "' = ' ' or email like ?) "
				+ " and ('" + sex + "' = ' ' or a.sex = ?) "
		        + " limit ?, ? ";*/
			sql = " select a.*,b.user_code,b.user_name,b.telephone_number,b.email_address,b.gender,c.*,d.* from " + getTableName("users")
				+ " a left join "
				+ " ( select * from " + getTableName("kdata") + " m left join ( select openid,key_account from " + getTableName("user_detail")
				+ " group by openid ) n  on m.user_code = n.key_account ) "
				+ " b on a.openid = b.openid " 
				+ " left join (" 
				+ " ( select m.id,n.type,n.open_id,n.count from " + getTableName("task_queue")
				+ " m left join ("
				+ " select type 'type',open_id 'open_id', count(open_id) 'count' from " + getTableName("task_queue")
				+ " group by open_id ) n on m.open_id = n.open_id )"
				+ " c ) on a.openid = c.open_id "
				+ " left join (" 
				+ " ( select open_id 'open_id', create_date 'createDate' from " + getTableName("task_message")
				+ " group by open_id order by create_date desc ) d ) on a.openid = d.open_id "
				+ " where "
                + " ('" + taskId + "' = ' ' or c.id like ?) "
				+ " and ('" + name + "' = ' ' or b.user_name like ?) "
				+ " and ('" + k_account + "' = ' ' or b.user_code like ?) "
				+ " and ('" + openid + "' = ' ' or a.openid like ?) "
				+ " and ('" + cellphone + "' = ' ' or b.telephone_number like ?) "
				+ " and ('" + email + "' = ' ' or b.email_address like ?) "
				+ " and ('" + sex + "' = ' ' or b.gender = ?) "
		        + " limit ?, ? ";
		} else {
			/*sql = " select a.*,b.name,b.key_account,b.grade,b.department,b.area,b.cellphone,b.email,c.*,d.* from " + getTableName("users")
				+ " a left join " + getTableName("user_detail")
				+ " b on a.openid = b.openid " 
				+ " left join ("  
				+ " ( select type 'type', open_id 'open_id', count(open_id) 'count' from " + getTableName("task_queue")
				+ " group by open_id ) c ) on a.openid = c.open_id " 
				+ " left join (" 
				+ " ( select open_id 'open_id', create_date 'createDate' from " + getTableName("task_message")
				+ " group by open_id order by create_date desc ) d ) on a.openid = d.open_id "
				+ " where "
				+ " ('" + taskId + "' = ' ' or a.id like ?) "
				+ " and ('" + name + "' = ' ' or name like ?) "
				+ " and ('" + k_account + "' = ' ' or b.key_account like ?) "
				+ " and ('" + openid + "' = ' ' or a.openid like ?) "
				+ " and ('" + grade + "' = ' ' or grade like ?) "
				+ " and ('" + department + "' = ' ' or department like ?) "
				+ " and ('" + area + "' = ' ' or area like ?) "
				+ " and ('" + cellphone + "' = ' ' or cellphone like ?) "
				+ " and ('" + email + "' = ' ' or email like ?) "
				+ " and ('" + sex + "' = ' ' or a.sex = ?) "
		        + " limit ?, ? ";*/
			sql = " select a.*,b.user_code,b.user_name,b.telephone_number,b.email_address,b.gender,c.*,d.* from " + getTableName("users")
					+ " a left join "
					+ " ( select * from " + getTableName("kdata") + " m left join ( select openid,key_account from " + getTableName("user_detail")
					+ " group by openid ) n  on m.user_code = n.key_account ) "
					+ " b on a.openid = b.openid " 
					+ " left join ("  
					+ " ( select type 'type', open_id 'open_id', count(open_id) 'count' from " + getTableName("task_queue")
					+ " group by open_id ) c ) on a.openid = c.open_id " 
					+ " left join (" 
					+ " ( select open_id 'open_id', create_date 'createDate' from " + getTableName("task_message")
					+ " group by open_id order by create_date desc ) d ) on a.openid = d.open_id "
					+ " where "
					+ " ('" + taskId + "' like ?) "
					+ " and ('" + name + "' = ' ' or b.user_name like ?) "
					+ " and ('" + k_account + "' = ' ' or b.user_code like ?) "
					+ " and ('" + openid + "' = ' ' or a.openid like ?) "
					+ " and ('" + cellphone + "' = ' ' or b.telephone_number like ?) "
					+ " and ('" + email + "' = ' ' or b.email_address like ?) "
					+ " and ('" + sex + "' = ' ' or b.gender = ?) "
			        + " limit ?, ? ";
		}

		List<WeixinUsersEntity> WeixinUsersEntities = jdbcTemplate.query(sql, 
				new Object[]{ 
					"%" + taskId + "%",
					"%" + name + "%", 
					"%" + k_account + "%",
					"%" + openid + "%",
					/*"%" + grade + "%",
					"%" + department + "%",
					"%" + area + "%",*/
					"%" + cellphone + "%",
					"%" + email + "%",
					sex,
					offset, rows}, 
				new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				WeixinUsersEntity weixinUser = new WeixinUsersEntity();
				weixinUser.setId(rs.getString("id"));
				weixinUser.setOpenid(rs.getString("a.openid"));
				weixinUser.setKey_account(rs.getString("b.user_code"));
				weixinUser.setName(rs.getString("b.user_name"));
				/*weixinUser.setGrade(rs.getString("b.grade"));
				weixinUser.setDepartment(rs.getString("b.department"));
				weixinUser.setArea(rs.getString("b.area"));*/
				weixinUser.setCellphone(rs.getString("b.telephone_number"));
				weixinUser.setEmail(rs.getString("b.email_address"));
				weixinUser.setRequest_count(rs.getInt("c.count"));
				weixinUser.setUser_level(rs.getString("c.type"));
				String createDate = "";
				if(rs.getString("createDate")!=null){
					createDate = rs.getString("createDate").substring(0, 19);
				}
				weixinUser.setActive_time(createDate);
				/*String nickname = "";
				try {
					nickname = new String(Base64.decodeLines(rs.getString("nickname")), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				weixinUser.setNickname(nickname);*/
				weixinUser.setNickname(rs.getString("nickname"));
				
				if(rs.getString("b.gender")!=null && !rs.getString("b.gender").equals("")){
					if(rs.getString("b.gender").equals("F")){
						weixinUser.setSex(2);
					} else if(rs.getString("b.gender").equals("M")){
						weixinUser.setSex(1);
					} else {
						weixinUser.setSex(0);
					}
				} else {
					weixinUser.setSex(rs.getInt("a.sex"));
				}
				
				weixinUser.setSubscribe(rs.getInt("subscribe"));
				/*weixinUser.setCountry(rs.getString("country"));
				weixinUser.setProvince(rs.getString("province"));
				weixinUser.setCity(rs.getString("city"));*/
				/*String subscribe_time = rs.getString("subscribe_time");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date = sdf.format(new Date(Long.parseLong(subscribe_time)));*/
				String subscribe_time = "";
				if(rs.getString("a.create_date")!=null){
					subscribe_time = rs.getString("a.create_date").substring(0, 19);
				}
				weixinUser.setFormatSubscribeTime(subscribe_time);
				return weixinUser;
			}
		});	
		return WeixinUsersEntities;
	}

	public int bindKeyAccount(JSONObject jsonObject) throws Exception {
		// TODO 绑定K账号
		return 0;
	}

}
