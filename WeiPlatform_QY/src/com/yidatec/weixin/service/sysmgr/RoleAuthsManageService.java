package com.yidatec.weixin.service.sysmgr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;

import com.yidatec.weixin.common.AuthNode;
import com.yidatec.weixin.common.EnumRes;
import com.yidatec.weixin.dao.security.SecurityDao;
import com.yidatec.weixin.dao.sysmgr.RoleAuthsDao;
import com.yidatec.weixin.entity.AuthTreeEntity;
import com.yidatec.weixin.entity.AuthorityEntity;
import com.yidatec.weixin.entity.PlatformUserEntity;
import com.yidatec.weixin.entity.ResourcesEntity;
import com.yidatec.weixin.entity.RoleAuthEntity;
import com.yidatec.weixin.security.CustomInvocationSecurityMetadataSourceService;
import com.yidatec.weixin.util.CommonMethod;

public class RoleAuthsManageService {

	private RoleAuthsDao roleAuthsDao = null;

	private SecurityDao securityDao = null;
	
	public void setRoleAuthsDao(RoleAuthsDao roleAuthsDao) {
		this.roleAuthsDao = roleAuthsDao;
	}
	
	public void setSecurityDao(SecurityDao securityDao) {
		this.securityDao = securityDao;
	}

	/**
	 * 加载系统中的所有角色
	 * @return
	 * @throws Exception
	 */
	public List<AuthorityEntity> loadRoles() throws Exception {
		return roleAuthsDao.loadRoles();
	}
	
	/**
	 * 加载角色关联的用户
	 * @param role_id
	 * @return
	 * @throws Exception
	 */
	public List<PlatformUserEntity> loadRoleUsers(String role_id) throws Exception {
		return roleAuthsDao.loadRoleUsers(role_id);
	}
	
	/**
	 * 添加、修改、删除角色
	 * @param authorityEntity
	 * @return
	 * @throws Exception
	 */
	public String roleAction(AuthorityEntity authorityEntity) throws Exception {
		int res = 0;
		switch (authorityEntity.getAction_no()) {
			case 1: // 新增
				res = roleAuthsDao.addRole(authorityEntity);
				break;
			case 2: // 修改
				res = roleAuthsDao.updateRole(authorityEntity);
				break;
			case 3: // 删除
				roleAuthsDao.deleteRole(authorityEntity.getId().split(","));
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
	public String assignUser(AuthorityEntity authorityEntity) throws Exception {
		// 1. 先删除该角色关联的平台账户
		roleAuthsDao.deleteRolePlatformUser(authorityEntity.getId());
		if (StringUtils.isNotEmpty(authorityEntity.getPlatfrom_user_ids())) {
			roleAuthsDao.addPlatformUserToRole(authorityEntity);
		}
		return EnumRes.SUCCESS.getDescription();
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AuthTreeEntity loadAuthTree() throws Exception {
		List<ResourcesEntity> resources = roleAuthsDao.loadResources(false, 0,	1000);
		Map<String, AuthTreeEntity> authMap = new HashMap<String, AuthTreeEntity>();
		String root_id = null;
		AuthTreeEntity tmpAuth = null;
		ResourcesEntity resource = null;
		for (int i = 0; i < resources.size(); i++) {
			tmpAuth = new AuthTreeEntity();
			resource = resources.get(i);
			tmpAuth.setId(resource.getId());
			tmpAuth.setText(resource.getResource_name());
			tmpAuth.setPid(resource.getPid());
			if ("0".equals(resource.getPid())) {
				root_id = resource.getId();
			}
			if (AuthNode.ROOT.equals(resource.getResource_type())) {
				tmpAuth.setState(AuthNode.OPEN);
				tmpAuth.setIconCls(AuthNode.ROOT_CSS);
			} else if (AuthNode.MODULE.equals(resource.getResource_type())) {
				tmpAuth.setIconCls(AuthNode.MODULE_CSS);
				tmpAuth.setState(AuthNode.CLOSED);
			} else if (AuthNode.URL.equals(resource.getResource_type())) {
				tmpAuth.setIconCls(AuthNode.URL_CSS);
				tmpAuth.setState(AuthNode.CLOSED);
			} else if (AuthNode.BUTTON.equals(resource.getResource_type())) {
				tmpAuth.setIconCls(AuthNode.BUTTON_CSS);
			}
			authMap.put(tmpAuth.getId(), tmpAuth);
		}		
		Iterator iter = authMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, AuthTreeEntity> entry = (Map.Entry<String, AuthTreeEntity>) iter.next();			
			tmpAuth = authMap.get(entry.getValue().getPid());
			if (tmpAuth != null) {				
				tmpAuth.getChildren().add(entry.getValue());
			}			
		}		
		return authMap.get(root_id);
	}
	
	/**
	 * 
	 * @param role_id
	 * @return
	 * @throws Exception
	 */
	public AuthTreeEntity loadRoleAuthTree(String role_id) throws Exception {
		// 获取系统中的所有权限资源
		AuthTreeEntity baseAuthTree = loadAuthTree();//loadAuthTree();
		baseAuthTree.setId("0");
		// 获取该角色关联的权限资源
		List<ResourcesEntity> roleAuthTree = loadRoleAuth(role_id);
		// 根据角色的权限资源check权限资源，让其变成选中状态
		for (int i = 0; i < roleAuthTree.size(); i++) {
			checkAuth(baseAuthTree, roleAuthTree.get(i).getId());
		}
		return baseAuthTree;
	}
	
	/**
	 * 用递归的形式去遍历树并检查是否有权限 有就选中
	 * 
	 * @param baseAuthTree
	 * @param res_id
	 */
	private void checkAuth(AuthTreeEntity baseAuthTree, String res_id) {

		if (res_id.equals(baseAuthTree.getId())) {
			if (!baseAuthTree.hasChildren()) {
				baseAuthTree.setChecked("true");
			}
			return;
		}

		if (baseAuthTree.hasChildren()) {
			for (int i = 0; i < baseAuthTree.getChildren().size(); i++) {
				checkAuth(baseAuthTree.getChildren().get(i), res_id);
			}
		}

	}
	
	public List<ResourcesEntity> loadRoleAuth(String role_id) throws Exception {
		return roleAuthsDao.loadRoleAuth(role_id);
	}
	
	/**
	 * 
	 * @param roleAuthEntities
	 * @return
	 * @throws Exception
	 */
	public int[] syncSaveRoleAuth(List<RoleAuthEntity> roleAuthEntities)
			throws Exception {
		// 先删除以前关联的权限资源
		roleAuthsDao.deleteRoleAuth(roleAuthEntities.get(0).getAuthority_id());
		// 有的时候要删除某个角色下所有的资源，所以这里判断一下
		if (roleAuthEntities.get(0).getResource_id().length() == 0) {
			/**
			 * 重新加载权限资源
			 * 就是CustomInvocationSecurityMetadataSourceService类的loadResourceDefine方法 
			 */
			loadResourceDefine();
			return new int[] { 0 };
		}
		// 再重新保存关联的权限资源
		int[] res = roleAuthsDao.saveRoleAuth(roleAuthEntities);
		if (CommonMethod.updateSuccess(res)) {			
			/**
			 * 重新加载权限资源
			 * 就是CustomInvocationSecurityMetadataSourceService类的loadResourceDefine方法 
			 */
			loadResourceDefine();
		}
		return res;
		
	}
	
	/**
	 * 中心加载系统中的权限资源
	 */
	private void loadResourceDefine() throws Exception {
		try {			
			List<String> authorities = securityDao.loadAuthorities();
			/*
			 * 应当是资源为key， 权限为value。 资源通常为URL， 权限就是那些以ROLE_为前缀的角色。 一个资源可以由多个权限来访问。
			 */
			Map<String, Collection<ConfigAttribute>> resourceMap = new HashMap<String, Collection<ConfigAttribute>>();
			// 根据权限获取资源
			for (String auth_name : authorities) {
				ConfigAttribute ca = new SecurityConfig(auth_name);			
				List<String> resources = securityDao.loadResourcesByAuthorityName(auth_name);				
				for (String res : resources) {
					String url = res;				
					/*
					 * 判断资源文件和权限的对应关系，如果已经存在相关的资源url，则要通过该url为key提取出权限集合，将权限增加到权限集合中。
					 */
					if (resourceMap.containsKey(url)) {
						Collection<ConfigAttribute> value = resourceMap.get(url);						
						value.add(ca);
						resourceMap.put(url, value);
					} else {
						Collection<ConfigAttribute> atts = new ArrayList<ConfigAttribute>();
						atts.add(ca);
						resourceMap.put(url, atts);
					}
				}
			}
			CustomInvocationSecurityMetadataSourceService.setResourceMap(resourceMap);
		} catch (Exception ex) {
			throw ex;
		}
	}

}
