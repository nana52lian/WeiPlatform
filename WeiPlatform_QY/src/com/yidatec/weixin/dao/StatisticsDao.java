package com.yidatec.weixin.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.utils.StringUtils;
import org.springframework.jdbc.core.RowMapper;

public class StatisticsDao extends DataBase {

	/**
	 * 搜索日每名坐席请求单数
	 * 
	 * @param fromDt
	 * @param toDt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> searchEmployeeTask(Date fromDt, Date toDt,
			String serviceId) {
		String sql = "select count(type) taskNum,tq.type "
				+ " from wei_task_queue_all tq "
				+ " where tq.create_date between ? and ? ";
		if (!StringUtils.isEmpty(serviceId) && !serviceId.equals("-1"))
			sql += " and service_id='" + serviceId+"'";
		sql += " group by tq.type ";
		List<Object> list = jdbcTemplate.query(sql,
				new Object[] { fromDt, toDt }, new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						List<Object> tempList = new ArrayList<Object>();
						tempList.add(rs.getString("type"));
						tempList.add(rs.getInt("taskNum"));
						return tempList;
					}

				});
		return list;
	}

	/**
	 * 搜索日总请求单数
	 * 
	 * @param fromDt
	 * @param toDt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> searchDailyTask(String date,
			String serviceId) {
		String sql = "select hour(create_date) as hh,count(0) as taskNum "
				+ " from wei_task_queue_all " + " where date(create_date) = ?";
		if (!StringUtils.isEmpty(serviceId) && !serviceId.equals("-1"))
			sql += " and service_id='" + serviceId+"'";
		sql += " group by hh " + " order by create_date";
		List<HashMap<String, Object>> list = jdbcTemplate.query(sql,
				new Object[] { date }, new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("categories", rs.getInt("hh"));
						map.put("taskNum", rs.getInt("taskNum"));
						return map;
					}

				});
		return list;
	}

	/**
	 * 日请求单总数
	 * 
	 * @param fromDt
	 * @param toDt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int dailyTotalCount(Date fromDt, Date toDt,String serviceId) {
		String sql = "select count(0) from wei_task_queue_all where create_date between ? and ?";
		if (!StringUtils.isEmpty(serviceId) && !serviceId.equals("-1"))
			sql += " and service_id='" + serviceId+"'";
		List<Integer> list = jdbcTemplate.query(sql, new Object[] { fromDt,
				toDt }, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				return rs.getInt(1);
			}

		});
		return list.get(0);
	}

	/**
	 * 搜索月每名坐席请求单数
	 * 
	 * @param month
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> searchMonthlyEmployeeTask(int month) {
		String sql = "select count(service_id) taskNum,pu.name "
				+ " from wei_task_queue tq "
				+ " left join wei_platform_users pu  "
				+ " on tq.service_id = pu.id "
				+ " where month(tq.create_date) = ? "
				+ " group by tq.service_id ";
		List<Object> list = jdbcTemplate.query(sql, new Object[] { month },
				new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						List<Object> tempList = new ArrayList<Object>();
						tempList.add(rs.getString("name"));
						tempList.add(rs.getInt("taskNum"));
						return tempList;
					}

				});
		return list;
	}

	/**
	 * 月总请求单数
	 * 
	 * @param month
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> searchMonthlyData(int month) {
		String sql = "select year(create_date) year, "
				+ " month(create_date) month, " + " day(create_date) day, "
				+ " count(service_id) taskNum " + " from wei_task_queue "
				+ " where month(create_date) = ? "
				+ " group by year(create_date), " + " month(create_date), "
				+ " day(create_date) " + " order by day ";
		List<HashMap<String, Object>> list = jdbcTemplate.query(sql,
				new Object[] { month }, new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("year", rs.getString("year"));
						map.put("month", rs.getString("month"));
						map.put("categories", rs.getInt("day"));
						map.put("taskNum", rs.getInt("taskNum"));
						return map;
					}

				});
		return list;
	}

	/**
	 * 月请求总单数
	 * 
	 * @param month
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int monthlyTotalCount(int month) {
		String sql = "select count(0) from wei_task_queue where month(create_date) = ?";
		List<Integer> list = jdbcTemplate.query(sql, new Object[] { month },
				new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {

						return rs.getInt(1);
					}

				});
		return list.get(0);
	}

	/**
	 * 搜索年每名坐席请求单数
	 * 
	 * @param fromDt
	 * @param toDt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Object> searchEmployeeYearlyTask(String fromDt, String toDt,String serviceId) {
		String sql = "select count(type) taskNum,tq.type "
				+ " from wei_task_queue_all tq "
				+ " where tq.create_date between ? and ? ";
		if (!StringUtils.isEmpty(serviceId) && !serviceId.equals("-1"))
			sql += " and service_id='" + serviceId+"'";
		sql += " group by tq.type ";
		List<Object> list = jdbcTemplate.query(sql,
				new Object[] { fromDt, toDt }, new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						List<Object> tempList = new ArrayList<Object>();
						tempList.add(rs.getString("type"));
						tempList.add(rs.getInt("taskNum"));
						return tempList;
					}

				});
		return list;
	}

	/**
	 * 年总请求单数
	 * 
	 * @param fromDt
	 * @param toDt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> searchYearlyTask(String fromDt,
			String toDt,String serviceId) {
		String sql = "select DATE_FORMAT(create_date,'%Y%m') as hh,count(0) as taskNum "
				+ " from wei_task_queue_all "
				+ " where create_date between ? and ? ";
		if (!StringUtils.isEmpty(serviceId) && !serviceId.equals("-1"))
			sql += " and service_id='" + serviceId+"'";
		sql += " group by hh " + " order by create_date";
		List<HashMap<String, Object>> list = jdbcTemplate.query(sql,
				new Object[] { fromDt, toDt }, new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("categories", rs.getString("hh"));
						map.put("taskNum", rs.getInt("taskNum"));
						return map;
					}

				});
		return list;
	}

	/**
	 * 年请求单总数
	 * 
	 * @param fromDt
	 * @param toDt
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int yearlyTotalCount(String fromDt, String toDt,String serviceId) {
		String sql = "select count(0) from wei_task_queue_all where create_date between ? and ?";
		if (!StringUtils.isEmpty(serviceId) && !serviceId.equals("-1"))
			sql += " and service_id='" + serviceId+"'";
		List<Integer> list = jdbcTemplate.query(sql, new Object[] { fromDt,
				toDt }, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {

				return rs.getInt(1);
			}

		});
		return list.get(0);
	}

	/**
	 * 获取坐席id和姓名
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getServers() {
		String sql = "select id,name from wei_platform_users where type=2 or type=3";
		return jdbcTemplate.query(sql, new RowMapper() {

			@Override
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("id", rs.getString("id"));
				map.put("name", rs.getString("name"));
				return map;
			}

		});
	}
	
	/**
	 * 查找注册用户表
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Map<String, String>> exportTask(String server,String fromDate,String toDate) throws Exception {
		String sqlWhere = "";
		if(server.equals("-1") || server.equals("")){
			sqlWhere = " 1 = 1 ";
		} else {
			sqlWhere = " service_id = '" + server + "'";
		}
		String sql = " select a.*,b.account,b.name,c.*,d.create_date from " + getTableName("task_queue_all") + " a "
				   + " left join " + getTableName("platform_users") + " b on a.service_id = b.id "
				   + " left join " + getTableName("user_detail") + " c on a.id = c.taskid "
				   + " left join (select x.task_queue_id,x.create_date  from (select task_queue_id, create_date  from " + getTableName("task_message_all") + " where create_user <> open_id and create_user <> 'system' order by create_date) x group by x.task_queue_id)  d "
				   + " on a.id = d.task_queue_id "
				   + " where " + sqlWhere + " and a.create_date between ? and ?  "
				   + " order by a.create_date desc ";		
		return jdbcTemplate.query(sql, 
				new Object[]{ fromDate,toDate },
				new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id",rs.getString("a.id"));
				map.put("openid",rs.getString("a.open_id"));
				map.put("service_id",rs.getString("a.service_id"));
				map.put("task_status",rs.getInt("a.task_status"));
				map.put("service_score",rs.getInt("a.service_score"));
				map.put("custom_asses_desc",rs.getString("a.custom_asses_desc"));
				map.put("custom_score",rs.getInt("a.custom_score"));
				map.put("rid",rs.getString("a.rid"));
				map.put("type",rs.getString("a.type"));
				map.put("cause",rs.getString("a.cause"));
				map.put("create_user",rs.getString("a.create_user"));
				map.put("create_date",rs.getString("a.create_date"));
				map.put("modify_user",rs.getString("a.modify_user"));
				map.put("modify_date",rs.getString("a.modify_date"));
				map.put("account",rs.getString("b.account"));
				map.put("name",rs.getString("b.name"));
				map.put("key_account",rs.getString("c.key_account"));
				map.put("custom_name",rs.getString("c.name"));
				map.put("custom_cellphone",rs.getString("c.cellphone"));
				map.put("custom_email",rs.getString("c.email"));
				map.put("first_date",rs.getString("d.create_date"));
				return map;
			}
		});	
	}
}
