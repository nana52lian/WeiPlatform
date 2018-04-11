package com.yidatec.weixin.dao.sysmgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.yidatec.weixin.dao.DataBase;
import com.yidatec.weixin.entity.ParamEntity;
import com.yidatec.weixin.entity.ResourcesEntity;

public class ResourceDao extends DataBase {

	public int addResource(ResourcesEntity resourcesEntity) throws Exception {
		String sql = "insert into "
				+ getTableName("resources")
				+ " (id, resource_name, resource_type, resource_priority, resource_string, resource_desc, issys, enabled, pid, create_user, create_date) "
				+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
		return jdbcTemplate.update(
				sql,
				new Object[] { getGUID(), resourcesEntity.getResource_name(),
						resourcesEntity.getResource_type(),
						resourcesEntity.getResource_priority(),
						resourcesEntity.getResource_string(),
						resourcesEntity.getResource_desc(),
						resourcesEntity.getIssys(),
						resourcesEntity.getEnabled(), resourcesEntity.getPid(),
						resourcesEntity.getCreate_user(),
						resourcesEntity.getCreate_date() });
	}

	public List<ResourcesEntity> loadResources(boolean isLoadParent)
			throws Exception {
		return loadResources(isLoadParent, 0, 1000);
	}

	public List<ResourcesEntity> loadResources(boolean isLoadParent,
			int offset, int rows) throws Exception {
		String sql = " select * from " + getTableName("resources")
				+ " where enabled = 1 ";
		if (isLoadParent) {
			sql = sql + " and resource_type in ('root', 'module', 'url') ";
		}
		sql = sql + " order by pid, resource_priority limit ?, ?";
		@SuppressWarnings("unchecked")
		List<ResourcesEntity> resourceParent = jdbcTemplate.query(sql,
				new Object[] { offset, rows }, new RowMapper() {
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						ResourcesEntity resource = new ResourcesEntity();
						resource.setId(rs.getString("id"));
						resource.setResource_name(rs.getString("resource_name"));
						resource.setResource_type(rs.getString("resource_type"));
						resource.setResource_priority(rs
								.getInt("resource_priority"));
						resource.setResource_string(rs
								.getString("resource_string"));
						resource.setResource_desc(rs.getString("resource_desc"));
						resource.setIssys(rs.getInt("issys"));
						resource.setEnabled(rs.getInt("enabled"));
						resource.setPid(rs.getString("pid"));
						resource.setCreate_user(rs.getString("create_user"));
						resource.setCreate_date(rs.getString("create_date"));
						resource.setModify_user(rs.getString("modify_user"));
						resource.setModify_date(rs.getString("modify_date"));
						return resource;
					}
				});
		return resourceParent;
	}

	public int[] delResources(final String[] ids) throws Exception {
		String sql = "delete from " + getTableName("resources")
				+ " where id=? ";
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

	@SuppressWarnings("rawtypes")
	public int getResourcesCount() throws Exception {
		String sql = "select count(id) 'count' from "
				+ getTableName("resources");
		List countList = jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt("count");
			}
		});
		return Integer.parseInt(countList.get(0).toString());
	}

	/**
	 * 加载参数信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ParamEntity> loadParams() throws Exception {
		String sql = " select * from " + getTableName("params")
				+ " order by create_date ";

		@SuppressWarnings("unchecked")
		List<ParamEntity> params = jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				ParamEntity param = new ParamEntity();
				param.setId(rs.getString("id"));
				param.setType(rs.getString("type"));
				param.setParam_name(rs.getString("param_name"));
				param.setParam_description(rs.getString("param_description"));
				param.setParam_value(rs.getString("param_value"));
				param.setCreate_user(rs.getString("create_user"));
				param.setCreate_date(rs.getString("create_date"));
				param.setModify_user(rs.getString("modify_user"));
				param.setModify_date(rs.getString("modify_date"));
				return param;
			}
		});
		return params;
	}

	/**
	 * 更新参数
	 * 
	 * @param id
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public int updateParams(String id, String message) throws Exception {
		String sql = "update " + getTableName("params")
				+ " set param_value=?, modify_user = ?, modify_date = ? "
				+ " where id=?";
		return jdbcTemplate.update(sql, new Object[] { message,
				getCurrentUser().getUserName(), getCurrentDate(), id

		});
	}

	/**
	 * 获取请求类型列表
	 * 
	 * @param offset
	 * @param rows
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<HashMap<String, String>> loadRequestType(int offset, int rows) {
//		String sql = "select * from " + getTableName("request_type")
//				+ " order by parent_id" + " limit ?,?";
		String sql = "select b.id id,b.description description,b.parent_id parentId,a.description parentDescription"
				+ " from wei_request_type a"
		+ " right join wei_request_type b"
		+ " on a.id = b.parent_id"
		+ " order by parentId" + " limit ?,?";
		List<HashMap<String, String>> list = jdbcTemplate.query(sql,
				new Object[] { offset, rows }, new RowMapper() {

					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("id", rs.getString("id"));
						map.put("description", rs.getString("description"));
						map.put("parentId", rs.getString("parentId"));
						map.put("parentDescription", rs.getString("parentDescription"));
						return map;
					}

				});
		return list;
	}

	/**
	 * 请求类型总数
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int getRequestTypeCount() {
		String sql = "select count(0) from " + getTableName("request_type");
		List<Integer> list = jdbcTemplate.query(sql, new RowMapper() {

			@Override
			public Integer mapRow(ResultSet rs, int arg1) throws SQLException {
				return rs.getInt(1);
			}

		});
		return list.get(0);
	}

	/**
	 * 创建请求类型
	 */
	public void createRequestType(String requestTypeDescription) {
		String sql = "insert into " + getTableName("request_type")
					+ " (id,description,create_user,create_date)"
					+ " values (?,?,?,?)";
		jdbcTemplate.update(sql, new Object[]{getGUID(),requestTypeDescription,getCurrentUser().getUserName(),getCurrentDate()});
	}
	/**
	 * 更新请求类型
	 */
	public void updateRequestType(String requestTypeId,String requestTypeDescription) {
		String sql = "update " + getTableName("request_type")
					+ " set description=?,modify_user=?,modify_date=? where id=?";
		jdbcTemplate.update(sql, new Object[]{requestTypeDescription,getCurrentUser().getUserName(),getCurrentDate(),requestTypeId});
		
	}

	/**
	 * 删除请求类型
	 * @param requestTypeId
	 */
	public void delRequestType(String requestTypeId) {
		String sql = "delete from " + getTableName("request_type")
					+ " where id=?";
		jdbcTemplate.update(sql, new Object[]{requestTypeId});
	}

	/**
	 * 添加子请求类型
	 * @param requestTypeId
	 * @param requestTypeDescription
	 */
	public void saveSubRequestType(String requestTypeId,
			String requestTypeDescription) {
		String sql = "insert into " + getTableName("request_type")
				+ " (id,description,parent_id,create_user,create_date)"
				+ " values (?,?,?,?,?)";
	jdbcTemplate.update(sql, new Object[]{getGUID(),requestTypeDescription,requestTypeId,getCurrentUser().getUserName(),getCurrentDate()});
	}
}
