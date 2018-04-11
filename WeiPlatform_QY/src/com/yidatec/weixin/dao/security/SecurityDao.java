package com.yidatec.weixin.dao.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.yidatec.weixin.dao.DataBase;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ResourceTreeEntity;

public class SecurityDao extends DataBase {
	
	/**
	 * 提取系统中的所有权限
	 * @return List<String>
	 * @throws Exception
	 */
	public List<String> loadAuthorities() throws Exception {
		String sql = "select auth_name from " + getTableName("authorities");
		@SuppressWarnings("unchecked")
		List<String> authorities = jdbcTemplate.query(sql, new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {				
				return rs.getString("auth_name");
			}
		});
		return authorities;
	}

	/**
	 * 根据权限获取资源
	 * @param auth_name
	 * @return
	 * @throws Exception
	 */
	public List<String> loadResourcesByAuthorityName(String auth_name)
			throws Exception {		
		String sql = " select b.resource_string from " + getTableName("authorities_resources") 
				   + " a, " + getTableName("resources") + " b, " + getTableName("authorities") + " c "
			   	   + " where a.resource_id = b.id and a.authority_id = c.id and c.auth_name = ? ";		
		@SuppressWarnings("unchecked")
		List<String> resources = jdbcTemplate.query(sql, new Object[]{ auth_name }, new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {				
				return rs.getString("resource_string");
			}
		});
		return resources;
	}

	/**
	 * 根据帐号获取权限
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public List<GrantedAuthority> loadUserAuthoritiesByName(String username)
			throws Exception {
		/*
		String sql = " select a.auth_name "
			   	   + " from " + getTableName("authorities") + " a, " + getTableName("roles_authorities") 
			   	   + " b, " + getTableName("platform_users_roles") + " c, " + getTableName("platform_users") + " d "
			   	   + " where a.id = b.authority_id and b.role_id = c.role_id and c.user_id = d.id "
			   	   + " and d.account = ? ";
		*/		
		String sql = " select a.auth_name "
				   + " from " + getTableName("authorities") + " a, " + getTableName("platform_users_auths") + " b, " + getTableName("platform_users") + " c "
				   + " where a.id = b.auth_id and b.user_id = c.id and c.account = ? ";		
		SimpleGrantedAuthority authority = null;
		@SuppressWarnings("unchecked")
		List<GrantedAuthority> authorities = jdbcTemplate.query(sql, new Object[]{ username }, new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(rs.getString("auth_name"));
				return authority;
			}
		});
		return authorities;
	}

	/**
	 * 获取用户实体
	 * @param account
	 * @return
	 * @throws Exception
	 */
	public PlatformUserEntity loadUserByAccount(String account) throws Exception {
		String sql = "select * from " + getTableName("platform_users") + " where account = ?";
		@SuppressWarnings("unchecked")
		List<PlatformUserEntity> users = jdbcTemplate.query(sql, new Object[]{ account }, new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				PlatformUserEntity userEntity = new PlatformUserEntity();
				userEntity.setId(rs.getString("id"));
				userEntity.setUser_id(rs.getString("user_id"));
				userEntity.setAccount(rs.getString("account"));
				userEntity.setPassword(rs.getString("password"));
				userEntity.setName(rs.getString("name"));
				userEntity.setMail(rs.getString("mail"));
				userEntity.setMobile_phone(rs.getString("mobile_phone"));
				userEntity.setIssys(rs.getInt("issys"));
				userEntity.setEnabled(rs.getInt("enabled"));
				userEntity.setType(rs.getInt("type"));
				userEntity.setAccountNonLocked(rs.getInt("locked") == 0);
				userEntity.setAccountNonExpired(rs.getInt("expired") == 0);
				userEntity.setCredentialsNonExpired(rs.getInt("credentialsexpired") == 0);			
				userEntity.setCreate_user(rs.getString("create_user"));
				userEntity.setCreate_date(rs.getString("create_date"));
				userEntity.setModify_user(rs.getString("modify_user"));
				userEntity.setModify_date(rs.getString("modify_date"));
				userEntity.setWeichat_type(rs.getString("weichat_type"));
				if(null != rs.getObject("sex")){
					userEntity.setSex(rs.getInt("sex"));
				}
		
				return userEntity;
			}
		});
		
		if (null != users && users.size() > 0)
			return users.get(0);
		
		return null;
	}
	
	/**
	 * 获取资源树
	 * @return
	 * @throws Exception
	 */
	public List<ResourceTreeEntity> loadResourceTree() throws Exception {
		String sql = " select b.parentId, b.parentName, b.parentPriority, "
		   	   	   + " id 'childId', resource_name 'childName', resource_priority 'childPriority' "
		   	   	   + " from " + getTableName("resources") + " a, "
		   	   	   + " (select id 'parentId', resource_name 'parentName', resource_priority 'parentPriority' from o2o_resources where pid IS NULL) b "
		   	   	   + " WHERE a.pid = b.parentId order by parentPriority, childPriority ";
	
		@SuppressWarnings("unchecked")
		List<ResourceTreeEntity> resourceTree = jdbcTemplate.query(sql, new RowMapper() {			
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				ResourceTreeEntity resource = new ResourceTreeEntity();
				resource.setParentId(rs.getString("parentId"));
				resource.setParentName(rs.getString("parentName"));
				resource.setParentPriority(rs.getInt("parentPriority"));
				resource.setChildId(rs.getString("childId"));
				resource.setChildName(rs.getString("childName"));
				resource.setChildPriority(rs.getInt("childPriority"));				
				return resource;
			}
		});
		
		return resourceTree;
	}

}
