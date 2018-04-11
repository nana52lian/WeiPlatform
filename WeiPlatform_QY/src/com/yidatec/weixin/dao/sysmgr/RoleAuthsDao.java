package com.yidatec.weixin.dao.sysmgr;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.yidatec.weixin.dao.DataBase;
import com.yidatec.weixin.entity.AuthorityEntity;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ResourcesEntity;
import com.yidatec.weixin.entity.RoleAuthEntity;

/**
 * 权限、角色数据库操作
 * @author Lance
 *
 */
public class RoleAuthsDao extends DataBase {

	/**
	 * 加载系统中所有角色信息
	 * @return
	 * @throws Exception
	 */
	public List<AuthorityEntity> loadRoles() throws Exception {
		String sql = " select * from " + getTableName("authorities")
				   + " order by auth_name ";
		@SuppressWarnings("unchecked")
		List<AuthorityEntity> authorities = jdbcTemplate.query(sql,
			new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					AuthorityEntity authority = new AuthorityEntity();
					authority.setId(rs.getString("id"));
					authority.setAuth_name(rs.getString("auth_name"));
					authority.setAuth_desc(rs.getString("auth_desc"));
					authority.setIssys(rs.getInt("issys"));
					authority.setEnabled(rs.getInt("enabled"));
					authority.setPid(rs.getString("pid"));						
					authority.setCreate_user(rs.getString("create_user"));
					authority.setCreate_date(rs.getString("create_date"));
					authority.setModify_user(rs.getString("modify_user"));
					authority.setModify_date(rs.getString("modify_date"));
					return authority;
				}
			});
		return authorities;
	}
	
	/**
	 * 加载某个角色下面关联的用户
	 * @param role_id
	 * @return
	 * @throws Exception
	 */
	public List<PlatformUserEntity> loadRoleUsers(String role_id) throws Exception {
		String sql = " select b.* from " + getTableName("platform_users_auths")
		  		   + " a left join  " + getTableName("platform_users") + " b " 
		  		   + " on a.user_id = b.id where a.auth_id = ? ";		
		@SuppressWarnings("unchecked")
		List<PlatformUserEntity> entitys = jdbcTemplate.query(sql, new Object[]{ role_id }, new RowMapper() {
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
	 * 新增角色信息
	 * @param authorityEntity
	 * @return
	 * @throws Exception
	 */
	public int addRole(AuthorityEntity authorityEntity) throws Exception {
		String sql = "insert into "	+ getTableName("authorities")
				   + " (id, auth_name, auth_desc, issys, enabled, pid, create_user, create_date) "
				   + " values(?,?,?,?,?, ?,?,?) ";
		return jdbcTemplate.update(sql, 
			new Object[] { 
				getGUID(),
				authorityEntity.getAuth_name(), 
				authorityEntity.getAuth_desc(),
				authorityEntity.getIssys(), 
				authorityEntity.getEnabled(),
				authorityEntity.getPid(), 
				getCurrentUser().getUserName(),
				getCurrentDate()
			});
	}
	
	/**
	 * 修改角色信息
	 * @param authorityEntity
	 * @return
	 * @throws Exception
	 */
	public int updateRole(AuthorityEntity authorityEntity) throws Exception {
		String sql = " update " + getTableName("authorities")
				   + " set auth_name = ?, auth_desc = ?, modify_user = ?, modify_date = ? where id = ? ";
		return jdbcTemplate.update(sql,
					new Object[] { 
						authorityEntity.getAuth_name(),
						authorityEntity.getAuth_desc(),
						getCurrentUser().getUserName(),
						getCurrentDate(),
						authorityEntity.getId() 
					});
	}
	
	/**
	 * 删除角色信息
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public int[] deleteRole(final String[] ids) throws Exception {
		String sql = "delete from " + getTableName("authorities")
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
	 * 删除某个角色关联的用户
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public int deleteRolePlatformUser(String id) throws Exception {
		String sql = "delete from " + getTableName("platform_users_auths")
				   + " where auth_id = ? ";
		return jdbcTemplate.update(sql, new Object[] { id });
	}
	
	/**
	 * 用户关联到角色
	 * @param authorityEntity
	 * @return
	 * @throws Exception
	 */
	public int[] addPlatformUserToRole(final AuthorityEntity authorityEntity) throws Exception {
		String sql = "insert into " + getTableName("platform_users_auths")
				   + " (id, user_id, auth_id, create_user, create_date) "
				   + " values(?, ?, ?, ?, ?)";
		final String[] platfrom_user_ids = authorityEntity.getPlatfrom_user_ids().split(",");
		int[] res = jdbcTemplate.batchUpdate(sql,
			new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int index)
						throws SQLException {
					ps.setString(1, getGUID());
					ps.setString(2, platfrom_user_ids[index]);
					ps.setString(3, authorityEntity.getId());
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
	 * 
	 * @param isLoadParent
	 * @param offset
	 * @param rows
	 * @return
	 * @throws Exception
	 */
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
						resource.setResource_priority(rs.getInt("resource_priority"));
						resource.setResource_string(rs.getString("resource_string"));
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
	
	public List<ResourcesEntity> loadRoleAuth(String role_id) throws Exception {
		String sql = " select * from " + getTableName("resources") + " a , "
				+ getTableName("authorities_resources") + " b "
				+ " where a.id = b.resource_id and b.authority_id = ? ";
		@SuppressWarnings("unchecked")
		List<ResourcesEntity> resourceParent = jdbcTemplate.query(sql,
				new Object[] { role_id }, new RowMapper() {
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
	
	public void deleteRoleAuth(String role_id) throws Exception {
		String sql = "delete from " + getTableName("authorities_resources")
				+ " where authority_id = ? ";
		jdbcTemplate.update(sql, new Object[] { role_id });
	}
	
	public int[] saveRoleAuth(final List<RoleAuthEntity> roleAuthEntities)
			throws Exception {
		String sql = " insert into " + getTableName("authorities_resources")
				+ " (id, authority_id, resource_id, create_user, create_date) "
				+ " values(?, ?, ?, ?, ?) ";

		int[] res = jdbcTemplate.batchUpdate(sql,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int index)
							throws SQLException {
						ps.setString(1, getGUID());
						ps.setString(2, roleAuthEntities.get(index)
								.getAuthority_id());
						ps.setString(3, roleAuthEntities.get(index)
								.getResource_id());
						ps.setString(4, roleAuthEntities.get(index)
								.getCreate_user());
						ps.setString(5, roleAuthEntities.get(index)
								.getCreate_date());
					}

					public int getBatchSize() {
						return roleAuthEntities.size();
					}
				});

		return res;
	}
}
